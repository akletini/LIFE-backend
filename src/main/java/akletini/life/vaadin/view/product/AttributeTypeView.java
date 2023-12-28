package akletini.life.vaadin.view.product;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.repository.entity.BasicType;
import akletini.life.core.shared.dto.NamedDto;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.vaadin.service.product.ExposedAttributeTypeService;
import akletini.life.vaadin.view.MainView;
import akletini.life.vaadin.view.components.ErrorModal;
import akletini.life.vaadin.view.components.PagedGridView;
import akletini.life.vaadin.view.components.Pagination;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.editor.Editor;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.io.IOException;

@Route(value = "/products/attribute-types", layout = MainView.class)
@PageTitle("Attribute types")
public class AttributeTypeView extends VerticalLayout implements PagedGridView {

    private final ExposedAttributeTypeService attributeTypeService;
    private final Grid<AttributeTypeDto> grid;
    private final HorizontalLayout buttonLayout;
    private TextField searchField;
    private Button addTypeButton;
    private Button delete;
    private Pagination pagination;
    private Page<AttributeTypeDto> attributeTypes;
    private Button edit;
    private TextField name;
    private Select<BasicType> basicTypeSelect;
    private Checkbox requiredCheckbox;
    private AttributeTypeEditor attributeTypeEditor;
    private AttributeTypeDto attributeType;

    public AttributeTypeView(ExposedAttributeTypeService attributeTypeService) {
        this.attributeTypeService = attributeTypeService;
        H1 heading = new H1("Attribute types page");
        grid = new Grid<>();
        pagination = new Pagination(10, 1, 0);
        buttonLayout = createButtonLayout();
        initGrid();
        add(heading, buttonLayout, grid, pagination);
    }

    private HorizontalLayout createButtonLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        addTypeButton = new Button("New attribute type");
        addTypeButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_SUCCESS,
                ButtonVariant.LUMO_PRIMARY);
        addTypeButton.addClickListener(event -> {
            attributeTypeEditor = new AttributeTypeEditor();
            attributeTypeEditor.addSaveListener(saveEvent -> {
                saveAttributeType(saveEvent.getAttributeTypeDto());
            });
            add(attributeTypeEditor);
        });
        searchField = new TextField("Name");
        searchField.setPlaceholder("Search term...");
        searchField.setValueChangeMode(ValueChangeMode.LAZY);
        searchField.addValueChangeListener(event -> query());
        layout.add(searchField, addTypeButton);
        layout.setVerticalComponentAlignment(Alignment.END, addTypeButton);
        return layout;
    }

    private void initGrid() {
        name = new TextField();
        basicTypeSelect = new Select<>();
        requiredCheckbox = new Checkbox();
        Editor<AttributeTypeDto> editor = grid.getEditor();
        grid.addColumn(NamedDto::getName)
                .setHeader("Name")
                .setSortable(true)
                .setEditorComponent(attributeTypeDto -> {
                    name.setValue(attributeTypeDto.getName());
                    return name;
                });
        grid.addColumn(AttributeTypeDto::getBasicType)
                .setHeader("Basic type")
                .setSortable(true)
                .setEditorComponent(attributeTypeDto -> {
                    basicTypeSelect.setItems(BasicType.values());
                    basicTypeSelect.setValue(attributeTypeDto.getBasicType());
                    return basicTypeSelect;
                });
        grid.addColumn(AttributeTypeDto::isRequired)
                .setHeader("Required")
                .setSortable(true)
                .setEditorComponent(attributeTypeDto -> {
                    requiredCheckbox.setValue(attributeTypeDto.isRequired());
                    return requiredCheckbox;
                });
        Grid.Column<AttributeTypeDto> editColumn =
                grid.addComponentColumn(attributeTypeDto -> {
                    HorizontalLayout layout = new HorizontalLayout();
                    edit = new Button("Edit");
                    edit.addClickListener(event -> {
                        if (editor.isOpen()) {
                            editor.cancel();
                        }
                        editor.editItem(attributeTypeDto);
                    });
                    delete = new Button();
                    delete.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    delete.setIcon(new Icon(VaadinIcon.TRASH));
                    layout.add(edit, delete);
                    return layout;
                });
        query();
        Binder<AttributeTypeDto> binder = new Binder<>(AttributeTypeDto.class);
        editor.setBinder(binder);
        editor.setBuffered(true);

        binder.forField(name).bind("name");
        binder.forField(basicTypeSelect).bind("basicType");
        binder.forField(requiredCheckbox).bind("required");

        Button saveButton = new Button("Save", e -> {
            editor.save();
        });
        editor.addSaveListener(event -> {
            try {
                attributeType = event.getItem();
                attributeTypeService.store(attributeType);
            } catch (BusinessException ex) {
                add(new ErrorModal(ex.getMessage()));
                editor.editItem(attributeType);
            }
        });
        Button cancelButton = new Button(VaadinIcon.CLOSE.create());
        cancelButton.addClickListener(
                e -> {
                    editor.cancel();
                });
        cancelButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR);
        HorizontalLayout actions = new HorizontalLayout(saveButton,
                cancelButton);
        actions.setPadding(false);
        editColumn.setEditorComponent(actions);
    }

    public void saveAttributeType(AttributeTypeDto attributeTypeDto) {
        try {
            attributeTypeService.store(attributeTypeDto);
        } catch (BusinessException e) {
            add(new ErrorModal(e.getMessage()));
        }
        attributeTypeEditor.getDialog().close();
    }

    @Override
    public void query() {
        try {
            attributeTypes =
                    attributeTypeService.getAttributeTypes(pagination.currentPage,
                            pagination.pageSize, searchField.getValue());
            grid.setItems(attributeTypes.toList());
            pagination.reloadLabels(attributeTypes.getSize(), attributeTypes.getTotalPages(),
                    attributeTypes.getTotalElements());
        } catch (IOException e) {
            add(new ErrorModal(e.getMessage()));
        } catch (EntityNotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
