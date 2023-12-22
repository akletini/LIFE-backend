package akletini.life.vaadin.view.todo;

import akletini.life.core.todo.dto.TagDto;
import akletini.life.core.todo.dto.TodoDto;
import akletini.life.vaadin.service.todo.ExposedTagService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.Locale;

public class TodoEditor extends FormLayout {

    Binder<TodoDto> binder = new BeanValidationBinder<>(TodoDto.class);

    TextField title = new TextField("Title");
    DatePicker dueAt = new DatePicker("Due at");
    TextArea description = new TextArea("Description");
    ComboBox<TagDto> tag = new ComboBox<>("Tag");
    Upload attachedFile = new Upload();

    H1 heading = new H1("Edit Todo");

    Button saveButton = new Button("Save");
    Button cancelButton = new Button("Cancel");
    TodoDto todo = new TodoDto();

    public TodoEditor(ExposedTagService tagService) {
        binder.bindInstanceFields(this);
        dueAt.setLocale(Locale.GERMAN);
        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addClickListener(event -> onSave());
        tag.setItems(tagService.getAll());
        tag.setItemLabelGenerator(TagDto::getName);
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addClickListener(event -> setVisible(false));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        add(heading, title, dueAt, description, tag, attachedFile, saveButton,
                cancelButton);
    }

    public void onSave() {
        if (binder.isValid()) {
            fireEvent(new TodoEditor.SaveEvent(this, binder.getBean()));
        }
    }

    public void setEditorVisible(TodoDto todoDto) {
        setVisible(!isVisible());
        todo = todoDto;
        binder.setBean(todo);
    }

    public static abstract class TodoFormEvent extends ComponentEvent<TodoEditor> {
        private TodoDto todoDto;

        protected TodoFormEvent(TodoEditor source, TodoDto todoDto) {
            super(source, false);
            this.todoDto = todoDto;
        }

        public TodoDto getTodo() {
            return todoDto;
        }
    }

    public static class SaveEvent extends TodoEditor.TodoFormEvent {
        SaveEvent(TodoEditor source, TodoDto todo) {
            super(source, todo);
        }
    }

    public static class DeleteEvent extends TodoEditor.TodoFormEvent {
        DeleteEvent(TodoEditor source, TodoDto todo) {
            super(source, todo);
        }

    }

    public Registration addDeleteListener(ComponentEventListener<TodoEditor.DeleteEvent> listener) {
        return addListener(TodoEditor.DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<TodoEditor.SaveEvent> listener) {
        return addListener(TodoEditor.SaveEvent.class, listener);
    }
}
