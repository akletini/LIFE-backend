package akletini.life.vaadin.view.product;

import akletini.life.core.product.dto.AttributeTypeDto;
import akletini.life.core.product.repository.entity.BasicType;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

@Getter
public class AttributeTypeEditor extends VerticalLayout {

    private final Dialog dialog;
    private final TextField name;
    private final Select<BasicType> basicTypeSelect;
    private final Checkbox required;
    private AttributeTypeDto attributeTypeDto;


    public AttributeTypeEditor() {
        dialog = new Dialog("Create new attribute type");
        attributeTypeDto = new AttributeTypeDto();

        name = new TextField("Name");
        basicTypeSelect = new Select<>();
        basicTypeSelect.setItems(BasicType.values());
        basicTypeSelect.setValue(BasicType.String);
        required = new Checkbox("Required");
        HorizontalLayout formLayout = new HorizontalLayout(name, basicTypeSelect, required);
        formLayout.setVerticalComponentAlignment(Alignment.END, basicTypeSelect, required);
        dialog.add(formLayout);
        setFooter();
        dialog.open();
    }

    private void setFooter() {
        HorizontalLayout layout = new HorizontalLayout();
        Button saveButton = new Button("Save");
        saveButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_SUCCESS,
                ButtonVariant.LUMO_PRIMARY);
        saveButton.addClickListener(event -> save());
        saveButton.getStyle().set("margin-right", "auto");
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> dialog.close());
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        layout.add(saveButton, cancelButton);
        layout.setWidthFull();
        dialog.getFooter().add(layout);
    }

    public void save() {
        attributeTypeDto.setName(name.getValue());
        attributeTypeDto.setBasicType(basicTypeSelect.getValue());
        attributeTypeDto.setRequired(required.getValue());
        fireEvent(new AttributeTypeEditor.SaveEvent(this,
                attributeTypeDto));
    }

    public static class SaveEvent extends ComponentEvent<AttributeTypeEditor> {

        private AttributeTypeDto attributeTypeDto;

        public SaveEvent(AttributeTypeEditor source, AttributeTypeDto attributeTypeDto) {
            super(source, false);
            this.attributeTypeDto = attributeTypeDto;
        }

        public AttributeTypeDto getAttributeTypeDto() {
            return attributeTypeDto;
        }
    }

    public Registration addSaveListener(ComponentEventListener<AttributeTypeEditor.SaveEvent> listener) {
        return addListener(AttributeTypeEditor.SaveEvent.class, listener);
    }
}
