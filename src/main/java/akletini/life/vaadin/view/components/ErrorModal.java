package akletini.life.vaadin.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;

public class ErrorModal extends VerticalLayout {

    public ErrorModal(String errorMessage) {
        Dialog dialog = new Dialog();

        dialog.setHeaderTitle("Error!");
        dialog.getHeader().add(new Icon(VaadinIcon.EXCLAMATION_CIRCLE));
        dialog.add(errorMessage);

        Button okButton = new Button("OK", (e) -> dialog.close());
        okButton.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
        dialog.getFooter().add(okButton);
        add(dialog);
        dialog.open();
    }
}
