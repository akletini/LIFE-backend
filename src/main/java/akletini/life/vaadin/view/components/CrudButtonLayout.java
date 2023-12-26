package akletini.life.vaadin.view.components;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import lombok.Getter;

@Getter
public class CrudButtonLayout extends HorizontalLayout {

    private final Button complete;
    private final Button edit;
    private final Button delete;

    public CrudButtonLayout() {
        complete = new Button();
        complete.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_SUCCESS,
                ButtonVariant.LUMO_TERTIARY);
        complete.setIcon(new Icon(VaadinIcon.CHECK));
        edit = new Button();
        edit.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_TERTIARY);
        edit.setIcon(new Icon(VaadinIcon.EDIT));
        delete = new Button();
        delete.addThemeVariants(ButtonVariant.LUMO_ICON,
                ButtonVariant.LUMO_ERROR,
                ButtonVariant.LUMO_TERTIARY);
        delete.setIcon(new Icon(VaadinIcon.TRASH));
        add(complete, edit, delete);
    }


}
