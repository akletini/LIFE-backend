package akletini.life.vaadin.view.product;


import akletini.life.core.product.dto.AttributeDto;
import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.core.product.repository.entity.BasicType;
import akletini.life.core.product.repository.entity.Quantity;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.vaadin.service.product.ExposedProductTypeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.datetimepicker.DateTimePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;
import lombok.Setter;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Getter
@Setter
public class ProductEditor extends FormLayout {
    Binder<ProductDto> binder = new BeanValidationBinder<>(ProductDto.class);
    H1 heading = new H1("Create product");
    TextField name = new TextField("Name");
    TextArea description = new TextArea("Description");

    IntegerField quantityValue = new IntegerField("Quantity value");
    Select<Quantity.QuantityDescription> quantityUnit = new Select<>();
    Select<String> quantityUnitKey = new Select<>();
    private final HorizontalLayout quantityLayout;

    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button cancelButton = new Button("Cancel");
    ProductDto product = new ProductDto();
    private ProductTypeDto productType;

    private final ExposedProductTypeService productTypeService;
    private List<Component> attributeFields;
    private List<AttributeTypeDto> attributeTypesForProductType;

    public ProductEditor(ExposedProductTypeService productTypeService,
                         ProductTypeDto productType) throws EntityNotFoundException {
        this.productTypeService = productTypeService;
        this.productType = productType;
        binder.bindInstanceFields(this);
        name.setRequired(true);
        quantityValue.setRequired(true);
        quantityUnit.setItems(Quantity.QuantityDescription.values());
        quantityUnitKey.setValue(Quantity.QuantityDescription.values()[0].name());
        quantityUnit.setLabel("Unit type");
        quantityUnit.addValueChangeListener(event -> {
            List<String> values = Quantity.getAllowedValuesForUnit(event.getValue());
            quantityUnitKey.setItems(values);
            quantityUnitKey.setValue(!CollectionUtils.isEmpty(values) ? values.get(0) : "");
        });
        quantityUnitKey.setLabel("Unit");
        quantityLayout = new HorizontalLayout(quantityValue, quantityUnit,
                quantityUnitKey);
        quantityLayout.setVerticalComponentAlignment(FlexComponent.Alignment.END, quantityUnit,
                quantityUnitKey);
        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addClickListener(event -> saveProduct());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addClickListener(event -> {
            setVisible(false);
            removeAll();
        });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
    }

    private void saveProduct() {
        ProductDto product = binder.getBean();
        List<AttributeDto> attributes = new ArrayList<>();
        for (int i = 0; i < attributeTypesForProductType.size(); i++) {
            Component component = attributeFields.get(i);
            String value = "";
            if (component instanceof TextField textField) {
                value = textField.getValue();
            } else if (component instanceof IntegerField integerField) {
                value = String.valueOf(integerField.getValue());
            } else if (component instanceof NumberField numberField) {
                value = String.valueOf(numberField.getValue());
            } else if (component instanceof DatePicker datePicker) {
                value = datePicker.getValue().toString();
            } else if (component instanceof DateTimePicker dateTimePicker) {
                value = dateTimePicker.getValue().toString();
            }
            attributes.add(new AttributeDto(attributeTypesForProductType.get(i), value));
        }
        product.setAttributes(attributes);
        product.setCreatedAt(LocalDateTime.now());
        product.setProductType(productType);
        product.setQuantity(new Quantity(quantityValue.getValue().longValue(),
                quantityUnit.getValue(), quantityUnitKey.getValue()));
        fireEvent(new SaveEvent(this, product));
    }

    private void initializeComponents(ProductTypeDto productType) throws EntityNotFoundException {
        attributeFields = addAttributeFields(productType);
        add(heading, name, description, quantityLayout);
        attributeFields.forEach(this::add);
        add(saveButton, cancelButton, deleteButton);
    }


    private List<Component> addAttributeFields(ProductTypeDto productTypeDto) throws EntityNotFoundException {
        List<Component> components = new ArrayList<>();
        attributeTypesForProductType =
                productTypeService.getAttributeTypesForProductType(productTypeDto);
        for (AttributeTypeDto attributeType : attributeTypesForProductType) {
            if (BasicType.String.equals(attributeType.getBasicType())) {
                TextField textField = new TextField(attributeType.getName());
                textField.setRequired(attributeType.isRequired());
                components.add(textField);
            } else if (BasicType.Integer.equals(attributeType.getBasicType())) {
                IntegerField integerField = new IntegerField(attributeType.getName());
                integerField.setRequired(attributeType.isRequired());
                components.add(integerField);
            } else if (BasicType.Number.equals(attributeType.getBasicType())) {
                NumberField numberField = new NumberField(attributeType.getName());
                numberField.setRequired(attributeType.isRequired());
                components.add(numberField);
            } else if (BasicType.Date.equals(attributeType.getBasicType())) {
                DatePicker datePicker = new DatePicker(attributeType.getName());
                datePicker.setLocale(Locale.GERMAN);
                datePicker.setRequired(attributeType.isRequired());
                components.add(datePicker);
            } else if (BasicType.DateTime.equals(attributeType.getBasicType())) {
                DateTimePicker dateTimePicker = new DateTimePicker(attributeType.getName());
                dateTimePicker.setLocale(Locale.GERMAN);
                components.add(dateTimePicker);
            }
        }
        return components;
    }

    public void setEditorVisible(ProductDto product, ProductTypeDto productTypeDto,
                                 boolean create) throws EntityNotFoundException {
        if (create) {
            heading.setText("Create product");
            deleteButton.setVisible(false);
        } else {
            heading.setText("Edit product");
            deleteButton.setVisible(true);
        }
        this.productType = productTypeDto;
        initializeComponents(productTypeDto);
        setVisible(true);
        this.product = product;
        binder.setBean(product);
    }

    public static abstract class ProductFormEvent extends ComponentEvent<ProductEditor> {

        private ProductDto productDto;

        public ProductFormEvent(ProductEditor source, ProductDto productDto) {
            super(source, false);
            this.productDto = productDto;
        }

        public ProductDto getProduct() {
            return productDto;
        }
    }

    public static class SaveEvent extends ProductEditor.ProductFormEvent {

        public SaveEvent(ProductEditor source, ProductDto productDto) {
            super(source, productDto);
        }
    }

    public static class DeleteEvent extends ProductEditor.ProductFormEvent {

        public DeleteEvent(ProductEditor source, ProductDto productDto) {
            super(source, productDto);
        }
    }

    public Registration addSaveListener(ComponentEventListener<ProductEditor.SaveEvent> listener) {
        return addListener(ProductEditor.SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<ProductEditor.DeleteEvent> listener) {
        return addListener(ProductEditor.DeleteEvent.class, listener);
    }
}
