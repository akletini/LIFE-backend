package akletini.life.vaadin.view.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

@Getter
public class ConfirmDeleteDialog extends VerticalLayout {

    private final Dialog dialog;

    public ConfirmDeleteDialog(String confirmationText) {
        dialog = new Dialog("Confirm deletion");
        dialog.add(confirmationText);
        HorizontalLayout layout = new HorizontalLayout();
        Button deleteButton = new Button("Delete");
        deleteButton.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);
        deleteButton.addClickListener(event -> fireEvent(new DeleteEvent(this)));
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> dialog.close());
        layout.add(deleteButton, cancelButton);
        dialog.getFooter().add(layout);
        add(dialog);
        dialog.open();
    }

    public static class DeleteEvent extends ComponentEvent<ConfirmDeleteDialog> {

        public DeleteEvent(ConfirmDeleteDialog source) {
            super(source, false);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }
}
