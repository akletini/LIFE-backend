package akletini.life.vaadin.service.chore;

import akletini.life.core.chore.dto.ChoreDto;
import akletini.life.core.chore.repository.entity.Interval;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.Locale;

public class ChoreEditor extends FormLayout {

    Binder<ChoreDto> binder = new BeanValidationBinder<>(ChoreDto.class);
    H1 heading = new H1("Edit Chore");
    TextField title = new TextField("Title");
    DatePicker dueAt = new DatePicker("Due at");
    TextArea description = new TextArea("Description");
    DatePicker startDate = new DatePicker("Start date");
    Checkbox active = new Checkbox("Active");
    Checkbox shiftInterval = new Checkbox("Shift Interval");
    IntegerField intervalValue = new IntegerField("");
    Select<Interval.DateUnit> intervalUnit = new Select<>();
    Button saveButton = new Button("Save");
    Button deleteButton = new Button("Delete");
    Button cancelButton = new Button("Cancel");
    ChoreDto chore = new ChoreDto();

    public ChoreEditor() {
        binder.bindInstanceFields(this);
        binder.forField(intervalValue).bind("interval.value");
        binder.forField(intervalUnit).bind("interval.unit");
        dueAt.setLocale(Locale.GERMAN);
        startDate.setLocale(Locale.GERMAN);
        intervalUnit.setItems(Interval.DateUnit.values());

        saveButton.addClickShortcut(Key.ENTER);
        saveButton.addClickListener(event -> {
            if (binder.isValid()) {
                fireEvent(new SaveEvent(this, binder.getBean()));
            }
        });
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ERROR);
        cancelButton.addClickShortcut(Key.ESCAPE);
        cancelButton.addClickListener(event -> setVisible(false));
        cancelButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        HorizontalLayout checkboxes = new HorizontalLayout(active, shiftInterval);

        HorizontalLayout interval = new HorizontalLayout();
        interval.add(intervalValue, intervalUnit);


        add(heading, title, dueAt, description, startDate, checkboxes, interval, saveButton,
                deleteButton,
                cancelButton);
    }

    public void setEditorVisible(ChoreDto chore) {
        setVisible(true);
        this.chore = chore;
        binder.setBean(chore);
    }

    public static abstract class ChoreFormEvent extends ComponentEvent<ChoreEditor> {

        private ChoreDto choreDto;

        public ChoreFormEvent(ChoreEditor source, ChoreDto choreDto) {
            super(source, false);
            this.choreDto = choreDto;
        }

        public ChoreDto getChore() {
            return choreDto;
        }
    }

    public static class SaveEvent extends ChoreEditor.ChoreFormEvent {

        public SaveEvent(ChoreEditor source, ChoreDto choreDto) {
            super(source, choreDto);
        }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }
}
