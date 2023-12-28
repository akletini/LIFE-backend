package akletini.life.vaadin.view.productType;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.core.product.repository.entity.BasicType;
import akletini.life.core.shared.dto.NamedDto;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.vaadin.service.product.ExposedAttributeTypeService;
import akletini.life.vaadin.service.product.ExposedProductTypeService;
import akletini.life.vaadin.view.components.ErrorModal;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import org.springframework.lang.NonNull;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Getter
public class ProductTypeView extends VerticalLayout {

    private final ProductTypeDto productTypeDto;
    private final TextField name;
    private List<AttributeTypeDto> attributeTypes;
    private List<AttributeTypeDto> addedAttributeTypes = new ArrayList<>();
    private ProductTypeDto parentProductType;
    private final Dialog editorDialog;
    private final Button addAttributeButton;
    private final ExposedProductTypeService productTypeService;
    private final ExposedAttributeTypeService attributeTypeService;

    public ProductTypeView(ExposedProductTypeService productTypeService,
                           ExposedAttributeTypeService attributeTypeService,
                           ProductTypeDto productTypeDto, boolean create) {
        this.productTypeService = productTypeService;
        this.attributeTypeService = attributeTypeService;
        if (create) {
            this.parentProductType = productTypeDto;
            this.productTypeDto = new ProductTypeDto();
        } else {
            this.productTypeDto = productTypeDto;
        }
        editorDialog = new Dialog(productTypeDto.getName());
        name = new TextField("Name");
        name.setValue(parentProductType == null ? productTypeDto.getName() : "");
        name.setRequired(true);
        editorDialog.add(name);

        attributeTypes = productTypeDto.getAttributeTypes();
        try {
            attributeTypes = productTypeService.getAttributeTypesForProductType(productTypeDto);
        } catch (EntityNotFoundException e) {
            add(new ErrorModal(e.getMessage()));
        }
        Collections.reverse(attributeTypes);
        for (AttributeTypeDto attributeType : attributeTypes) {
            HorizontalLayout attributeTypeLayout = getAttributeTypeLayout(attributeType, false);
            editorDialog.add(attributeTypeLayout);
        }
        addAttributeButton = new Button("Add attribute");
        addAttributeButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_SUCCESS,
                ButtonVariant.LUMO_PRIMARY);
        addAttributeButton.setWidthFull();
        addAttributeButton.addClickListener(event -> {
            editorDialog.remove(addAttributeButton);
            editorDialog.add(getAttributeTypeLayout(new AttributeTypeDto(), true));
            editorDialog.add(addAttributeButton);
        });
        editorDialog.add(addAttributeButton);

        setFooter();
        add(editorDialog);
        editorDialog.open();
    }

    @NonNull
    private HorizontalLayout getAttributeTypeLayout(AttributeTypeDto attributeType,
                                                    boolean newAttribute) {
        HorizontalLayout attributeTypeLayout = new HorizontalLayout();
        TextField nameTextField = new TextField();
        ComboBox<AttributeTypeDto> comboBox = new ComboBox<>();
        Select<BasicType> basicTypeSelect = new Select<>();
        basicTypeSelect.setLabel("Type");
        basicTypeSelect.setItems(BasicType.values());
        basicTypeSelect.setValue(attributeType.getBasicType() != null ?
                attributeType.getBasicType() : BasicType.String);
        basicTypeSelect.setEnabled(newAttribute);
        if (!newAttribute) {
            nameTextField = new TextField("Name");
            nameTextField.setValue(attributeType.getName() != null ? attributeType.getName() : "");
            nameTextField.setRequired(true);
            nameTextField.setEnabled(false);
        } else {
            comboBox = new ComboBox<>("Name");
            comboBox.setItemLabelGenerator(NamedDto::getName);
            comboBox.setItemsWithFilterConverter(query -> {
                try {
                    return attributeTypeService.getAttributeTypes(query.getPage(),
                            query.getPageSize(), query.getFilter().orElse("")).get();
                } catch (IOException | EntityNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }, searchTerm -> searchTerm + "*");
            comboBox.addValueChangeListener(event -> {
                AttributeTypeDto value = event.getValue();
                basicTypeSelect.setValue(value.getBasicType());
                basicTypeSelect.setEnabled(false);
                addedAttributeTypes.add(value);
            });
        }
        Icon icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
        icon.setColor("red");
        Button removeAttribute = new Button(icon);
        removeAttribute.addClickListener(event -> {
            addedAttributeTypes.remove(((ComboBox) ((HorizontalLayout) event.getSource().getParent().get()).getComponentAt(0)).getValue());
            editorDialog.remove(attributeTypeLayout);
        });
        if (!newAttribute) {
            attributeTypeLayout.add(nameTextField, basicTypeSelect);
        } else {
            attributeTypeLayout.add(comboBox, basicTypeSelect);
        }
        // if the attribute type is not inherited, make it removable
        if (newAttribute || (attributeType.getInheritedBy() == null && parentProductType == null)) {
            attributeTypeLayout.add(removeAttribute);
        }

        attributeTypeLayout.setVerticalComponentAlignment(Alignment.END, removeAttribute);
        return attributeTypeLayout;
    }

    private void setFooter() {
        HorizontalLayout layout = new HorizontalLayout();
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_SUCCESS,
                ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> store());
        saveButton.getStyle().set("margin-right", "auto");
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> editorDialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        layout.add(saveButton, cancelButton);
        layout.setWidthFull();
        editorDialog.getFooter().add(layout);
    }

    private void store() {
        productTypeDto.setName(name.getValue());
        productTypeDto.setParentProductType(parentProductType.getId());
        productTypeDto.setAttributeTypes(addedAttributeTypes);
        fireEvent(new ProductTypeView.SaveEvent(this,
                productTypeDto));
    }

    public static class SaveEvent extends ComponentEvent<ProductTypeView> {

        private ProductTypeDto productTypeDto;

        public SaveEvent(ProductTypeView source, ProductTypeDto productTypeDto) {
            super(source, false);
            this.productTypeDto = productTypeDto;
        }

        public ProductTypeDto getProductTypeDto() {
            return productTypeDto;
        }
    }

    public Registration addSaveListener(ComponentEventListener<ProductTypeView.SaveEvent> listener) {
        return addListener(ProductTypeView.SaveEvent.class, listener);
    }
}
