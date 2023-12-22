package akletini.life.vaadin.view.todo;

import akletini.life.core.todo.dto.TagDto;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import org.springframework.beans.factory.annotation.Autowired;
import org.vaadin.addons.tatu.ColorPicker;

public class TagEditor extends FormLayout {

    Binder<TagDto> binder = new BeanValidationBinder<>(TagDto.class);
    TextField name = new TextField("Tag name");
    ColorPicker color = new ColorPicker();

    H1 heading;

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    Button deleteButton = new Button("Delete");


    private TagDto tag;

    @Autowired
    public TagEditor() {

        binder.bindInstanceFields(this);
        heading = new H1();
        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addClickListener(event -> onSave());
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addClickListener(event -> setVisible(false));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);


        add(
                heading,
                name,
                color,
                saveButton,
                deleteButton,
                cancelButton
        );
    }

    public void setCreateVisible(boolean visible) {
        setVisible(visible);
        heading.setText("Create Tag");
        tag = new TagDto();
        binder.setBean(tag);
    }

    public void setEditVisible(TagDto tag) {
        setVisible(true);
        heading.setText("Edit Tag");
        this.tag = tag;
        binder.setBean(tag);
    }

    public void onSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public static abstract class TagFormEvent extends ComponentEvent<TagEditor> {
        private TagDto tagDto;

        protected TagFormEvent(TagEditor source, TagDto tagDto) {
            super(source, false);
            this.tagDto = tagDto;
        }

        public TagDto getTag() {
            return tagDto;
        }
    }

    public static class SaveEvent extends TagFormEvent {
        SaveEvent(TagEditor source, TagDto tag) {
            super(source, tag);
        }
    }

    public static class DeleteEvent extends TagFormEvent {
        DeleteEvent(TagEditor source, TagDto tag) {
            super(source, tag);
        }

    }

    public static class CloseEvent extends TagFormEvent {
        CloseEvent(TagEditor source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
