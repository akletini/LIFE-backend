package akletini.life.vaadin.view.chore;

import akletini.life.core.chore.dto.ChoreDto;
import akletini.life.core.shared.constants.SortingConstants;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.vaadin.service.chore.ExposedChoreService;
import akletini.life.vaadin.view.MainView;
import akletini.life.vaadin.view.components.CrudButtonLayout;
import akletini.life.vaadin.view.components.ErrorModal;
import akletini.life.vaadin.view.components.PagedGridView;
import akletini.life.vaadin.view.components.Pagination;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Input;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static akletini.life.core.shared.constants.FilterConstants.ACTIVE;
import static akletini.life.core.shared.constants.FilterConstants.DUE;
import static akletini.life.core.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.core.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Route(value = "/chores", layout = MainView.class)
@PageTitle("Chores")
@CssImport(value = "./styles/grid-cell.css")
public class ChoreView extends VerticalLayout implements PagedGridView {

    private Input titleInput;
    private DatePicker datePicker;
    private Button addChoreButton;
    private ExposedChoreService choreService;
    private Page<ChoreDto> chores;
    private final Grid<ChoreDto> choreGrid;
    private final Pagination pagination;
    private int currentPage = 0;
    private int pageSize = 10;
    private Select<String> sortSelect;
    private MultiSelectComboBox<String> filterSelect;
    private ChoreEditor choreEditor;
    private Button newChoreButton;

    public ChoreView(ExposedChoreService choreService) {
        this.choreService = choreService;

        H1 title = new H1("Chore page");
        choreGrid = new Grid<>();
        HorizontalLayout filterLayout = createFilterLayout();

        pagination = new Pagination(10, 0, 0);
        pagination.addQueryListener(event -> query());
        configureEditor();
        initializeGrid(choreGrid);
        add(getEditorLayout(new VerticalLayout(title, filterLayout, choreGrid,
                pagination)));
    }

    private HorizontalLayout getEditorLayout(VerticalLayout mainContent) {
        HorizontalLayout content = new HorizontalLayout(mainContent, choreEditor);
        content.setFlexGrow(2, mainContent);
        content.setFlexGrow(1, choreEditor);
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout createFilterLayout() {
        HorizontalLayout filterLayout = new HorizontalLayout();
        newChoreButton = new Button("New chore");
        newChoreButton.addClickListener(event -> {
            choreEditor.setEditorVisible(new ChoreDto(), true);
        });
        newChoreButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_SUCCESS);
        filterSelect = new MultiSelectComboBox<>();
        filterSelect.setLabel("Filter: ");
        filterSelect.setItems(List.of(ACTIVE, DUE));
        filterSelect.addValueChangeListener(event -> query());
        sortSelect = new Select<>();
        sortSelect.setLabel("Sort: ");
        sortSelect.setItems(SortingConstants.getNames());
        sortSelect.setEmptySelectionAllowed(true);
        sortSelect.addValueChangeListener(event -> query());
        filterLayout.setJustifyContentMode(JustifyContentMode.END);
        filterLayout.setPadding(true);
        filterLayout.setWidthFull();
        filterLayout.setVerticalComponentAlignment(Alignment.END, newChoreButton);

        filterLayout.add(newChoreButton, filterSelect, sortSelect);
        return filterLayout;
    }

    private void initializeGrid(Grid<ChoreDto> grid) {
        query();
        grid.setItems(chores.toList());
        grid.addColumn(ChoreDto::getTitle)
                .setHeader("Title")
                .setSortable(true);
        grid.addColumn(new LocalDateTimeRenderer<>(ChoreDto::getCreatedAt, DATE_TIME_FORMAT))
                .setHeader("Created At")
                .setSortable(true);
        grid.addComponentColumn(choreDto -> {
            LocalDateRenderer<ChoreDto> localDateRenderer =
                    new LocalDateRenderer<>(ChoreDto::getDueAt, DATE_FORMAT);
            Component component = localDateRenderer.createComponent(choreDto);
            Icon icon = new Icon(VaadinIcon.HOURGLASS);
            String color;
            if (choreDto.getDueAt().isBefore(LocalDate.now())) {
                color = "red";
            } else if (choreDto.getDueAt().minusWeeks(1).isBefore(LocalDate.now())) {
                color = "orange";
            } else {
                color = "green";
            }
            icon.setColor(color);
            icon.setSize("15px");
            HorizontalLayout layout = new HorizontalLayout(component, icon);
            layout.getStyle()
                    .set("color", color)
                    .set("border-color", color);
            layout.setClassName("dueBox");
            layout.setWidth(9, Unit.REM);
            layout.setVerticalComponentAlignment(Alignment.CENTER, component, icon);
            return layout;
        }).setHeader("Due At").setSortable(true);
        grid.addColumn(new LocalDateRenderer<>(ChoreDto::getLastCompleted, DATE_TIME_FORMAT))
                .setHeader("Last completed")
                .setSortable(true);
        grid.addColumn(ChoreDto::getInterval).setHeader("Repeat every");
        grid.addColumn(ChoreDto::isShiftInterval)
                .setHeader("Shift interval")
                .setSortable(true);
        grid.addComponentColumn(choreDto -> {
            CrudButtonLayout layout = new CrudButtonLayout();
            layout.getComplete().addClickListener(event -> {
                try {
                    choreService.completeChore(choreDto);
                } catch (BusinessException e) {
                    add(new ErrorModal(e.getMessage()));
                }
            });

            layout.getEdit().addClickListener(event -> choreEditor.setEditorVisible(choreDto,
                    false));
            layout.getDelete().addClickListener(event -> choreService.delete(choreDto));
            return layout;
        });
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    private void configureEditor() {
        choreEditor = new ChoreEditor();
        choreEditor.setWidth("25em");
        choreEditor.setVisible(false);
        choreEditor.addSaveListener(this::saveChore);
        choreEditor.addDeleteListener(this::deleteChore);
    }

    private void saveChore(ChoreEditor.SaveEvent saveEvent) {
        try {
            choreService.store(saveEvent.getChore());
            choreEditor.setVisible(false);
            query();
        } catch (BusinessException e) {
            add(new ErrorModal(e.getMessage()));
        }
    }

    private void deleteChore(ChoreEditor.DeleteEvent deleteEvent) {
        choreEditor.setVisible(false);
        choreService.delete(deleteEvent.getChore());
        query();
    }

    @Override
    public void query() {
        SortingConstants sortingConstants = SortingConstants.getByValue(sortSelect.getValue());
        Page<ChoreDto> chorePage = choreService.getChores(pagination.currentPage,
                pagination.pageSize,
                Optional.ofNullable(sortingConstants != null ? sortingConstants.name() : null),
                Optional.of(new ArrayList<>(filterSelect.getSelectedItems())));
        this.chores = chorePage;
        pagination.reloadLabels(chorePage.getSize(), chorePage.getTotalPages(),
                chorePage.getTotalElements());
    }
}
