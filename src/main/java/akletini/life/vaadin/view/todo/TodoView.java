package akletini.life.vaadin.view.todo;

import akletini.life.core.shared.constants.SortingConstants;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.todo.dto.TagDto;
import akletini.life.core.todo.dto.TodoDto;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.vaadin.service.todo.ExposedTagService;
import akletini.life.vaadin.service.todo.ExposedTodoService;
import akletini.life.vaadin.view.MainView;
import akletini.life.vaadin.view.components.ErrorModal;
import akletini.life.vaadin.view.components.PagedGridView;
import akletini.life.vaadin.view.components.Pagination;
import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dependency.CssImport;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.data.renderer.LocalDateRenderer;
import com.vaadin.flow.data.renderer.LocalDateTimeRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

import static akletini.life.core.shared.constants.FilterConstants.OPEN;
import static akletini.life.core.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.core.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Route(value = "/todos", layout = MainView.class)
@PageTitle("Todos")
@CssImport(value = "./styles/grid-cell.css")
public class TodoView extends VerticalLayout implements PagedGridView {

    private final ExposedTodoService todoService;
    private final ExposedTagService tagService;
    private final Pagination pagination;
    private TagEditor tagEditor;
    private TodoEditor todoEditor;
    private final Select<Todo.State> stateSelect;
    private final MultiSelectComboBox<TagDto> tagComboBox;
    private final Select<String> sortSelect;
    private final Grid<TodoDto> todoGrid;
    private List<TodoDto> todos;
    private DatePicker datePicker;
    private TextField input;
    private int currentPage = 0;
    private int pageSize = 10;
    private Component mainContent;

    private HorizontalLayout content;
    private Page<TodoDto> todoPage;

    @Autowired
    public TodoView(ExposedTodoService todoService, ExposedTagService tagService) {
        this.todoService = todoService;
        this.tagService = tagService;

        H1 title = new H1("Todo page");
        todoGrid = new Grid<>();
        initializeGrid(todoGrid);
        HorizontalLayout createTodoLayout = new HorizontalLayout();
        input = new TextField("Title");
        input.setPlaceholder("Enter todo title...");
        input.setWidthFull();
        input.setRequired(true);
        input.addKeyPressListener(Key.ENTER, event -> createNewTodo());
        input.setRequiredIndicatorVisible(true);
        datePicker = new DatePicker("Due date");
        datePicker.setLocale(Locale.GERMAN);
        Button addTodoButton = new Button("Add");
        addTodoButton.addClickListener(onCreateTodo());
        createTodoLayout.setWidthFull();
        createTodoLayout.setVerticalComponentAlignment(Alignment.END, addTodoButton);
        createTodoLayout.add(input, datePicker, addTodoButton);

        HorizontalLayout filterLayout = new HorizontalLayout();
        stateSelect = new Select<>();
        stateSelect.setLabel("State: ");
        stateSelect.setItems(List.of(Todo.State.values()));
        stateSelect.setValue(Todo.State.OPEN);
        stateSelect.addValueChangeListener(event -> query());

        tagComboBox = new MultiSelectComboBox<>();
        tagComboBox.setLabel("Tags: ");
        tagComboBox.addValueChangeListener(event -> query());
        List<TagDto> allTags = tagService.getAll();
        allTags.sort(Comparator.comparing(TagDto::getName));
        tagComboBox.setItems(allTags);
        tagComboBox.setItemLabelGenerator(TagDto::getName);
        sortSelect = new Select<>();
        sortSelect.setLabel("Sort: ");
        sortSelect.setItems(SortingConstants.getNames());
        sortSelect.setEmptySelectionAllowed(true);
        sortSelect.addValueChangeListener(event -> query());
        Button newTagButton = new Button("New Tag");
        newTagButton.addClickListener(event -> {
            if (todoEditor.isVisible()) {
                todoEditor.setVisible(false);
            }
            content.remove(todoEditor);
            content.add(tagEditor);
            content.setFlexGrow(1, tagEditor);
            tagEditor.setCreateVisible(!tagEditor.isVisible());
        });
        filterLayout.add(newTagButton, stateSelect, tagComboBox, sortSelect);
        filterLayout.setVerticalComponentAlignment(Alignment.END, newTagButton);
        filterLayout.setWidthFull();
        filterLayout.setJustifyContentMode(JustifyContentMode.END);

        configureTodoEditor();
        configureTagEditor();

        mainContent = new VerticalLayout(title, createTodoLayout, filterLayout, todoGrid);
        content = getContent();
        add(content);

        pagination = new Pagination(pageSize, todoPage.getTotalPages());
        pagination.addQueryListener(event -> query());
        add(pagination);

        setAlignItems(Alignment.CENTER);
    }

    @NonNull
    private ComponentEventListener<ClickEvent<Button>> onCreateTodo() {
        return event -> {
            createNewTodo();
        };
    }

    private void createNewTodo() {
        TodoDto todo = new TodoDto();
        if (!StringUtils.isEmpty(input.getValue())) {
            todo.setTitle(input.getValue());
            todo.setState(Todo.State.OPEN);
            todo.setCreatedAt(LocalDateTime.now());
            todo.setDueAt(datePicker.getOptionalValue().orElse(LocalDate.now()));
            try {
                todoService.store(todo);
                query();
                datePicker.clear();
                input.clear();
            } catch (BusinessException e) {
                add(new ErrorModal(e.getMessage()));
            }
        }
    }

    private void initializeGrid(Grid<TodoDto> todoGrid) {
        todoPage = todoService.getTodos(currentPage, pageSize, Optional.empty(),
                Optional.of(OPEN),
                Optional.empty());
        todos = new ArrayList<>(todoPage.toList());
        todoGrid.setSelectionMode(Grid.SelectionMode.MULTI);
        todoGrid.setItems(todos.stream().toList());
        todoGrid.addColumn(TodoDto::getTitle)
                .setHeader("Title")
                .setSortable(true);
        todoGrid.addColumn(new LocalDateTimeRenderer<>(TodoDto::getCreatedAt, DATE_TIME_FORMAT))
                .setHeader("Created At")
                .setSortable(true);
        todoGrid.addComponentColumn(todoDto -> {
            LocalDateRenderer<TodoDto> todoDtoLocalDateRenderer =
                    new LocalDateRenderer<>(TodoDto::getDueAt, DATE_FORMAT);
            Component component = todoDtoLocalDateRenderer.createComponent(todoDto);
            Icon icon = new Icon(VaadinIcon.HOURGLASS);
            String color;
            if (todoDto.getDueAt().isBefore(LocalDate.now())) {
                color = "red";
            } else if (todoDto.getDueAt().minusWeeks(1).isBefore(LocalDate.now())) {
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
        }).setHeader(
                "Due At").setSortable(true);
        todoGrid.addComponentColumn(todoDto -> {
                    if (todoDto.getTag() != null) {
                        TagDto tag = todoDto.getTag();
                        Button button = new Button(tag.getName());
                        button.getStyle().set("background-color", tag.getColor());
                        button.setClassName("tag");
                        button.addClickListener(event -> {
                            if (todoEditor.isVisible()) {
                                todoEditor.setVisible(false);
                            }
                            content.remove(todoEditor);
                            content.add(tagEditor);
                            content.setFlexGrow(1, tagEditor);
                            tagEditor.setEditVisible(tag);
                        });
                        return button;
                    }
                    return new Span();
                })
                .setHeader("Tag")
                .setSortable(true)
                .setComparator((o1, o2) -> {
                    if (o1.getTag() == null || o2.getTag() == null) {
                        return -1;
                    } else {
                        return o1.getTag().getName().compareTo(o2.getTag().getName());
                    }
                });
        todoGrid.addColumn(TodoDto::getState).setHeader("State").setSortable(true);
        todoGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, todoDto) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_SUCCESS,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        try {
                            todoDto.setState(Todo.State.DONE);
                            TodoDto storedTodo = todoService.store(todoDto);
                            todos.set(todos.indexOf(todoDto), storedTodo);
                            todoGrid.setItems(todos.stream().toList());
                        } catch (BusinessException ex) {
                            add(new ErrorModal(ex.getMessage()));
                        }
                    });
                    button.setIcon(new Icon(VaadinIcon.CHECK));
                })
        );
        todoGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, todoDto) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        if (tagEditor.isVisible()) {
                            tagEditor.setVisible(false);
                        }
                        content.remove(tagEditor);
                        content.add(todoEditor);
                        content.setFlexGrow(1, todoEditor);
                        todoEditor.setEditorVisible(todoDto);
                    });
                    button.setIcon(new Icon(VaadinIcon.EDIT));
                })
        );
        todoGrid.addColumn(
                new ComponentRenderer<>(Button::new, (button, todoDto) -> {
                    button.addThemeVariants(ButtonVariant.LUMO_ICON,
                            ButtonVariant.LUMO_ERROR,
                            ButtonVariant.LUMO_TERTIARY);
                    button.addClickListener(e -> {
                        todoService.delete(todoDto);
                        todos.remove(todoDto);
                        todoGrid.setItems(todos.stream().toList());
                    });
                    button.setIcon(new Icon(VaadinIcon.TRASH));
                })
        );
        todoGrid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    @Override
    public void query() {
        todos.clear();
        SortingConstants sortingConstant = SortingConstants.getByValue(sortSelect.getValue());
        Page<TodoDto> todoPage = todoService.getTodos(pagination.currentPage, pagination.pageSize,
                Optional.ofNullable(sortingConstant != null ? sortingConstant.name() : null),
                stateSelect.getOptionalValue().map(Todo.State::toString),
                Optional.of(tagComboBox.getSelectedItems()
                        .stream().map(TagDto::getName).collect(Collectors.toList())));
        todos.addAll(todoPage.get().toList());
        this.todoPage = todoPage;
        todoGrid.setItems(todos);
        pagination.pageSize = todoPage.getSize();
        pagination.totalPages = todoPage.getTotalPages();
    }

    private void configureTagEditor() {
        tagEditor = new TagEditor();
        tagEditor.setWidth("25em");
        tagEditor.setVisible(false);
        tagEditor.addSaveListener(this::saveTag);
        tagEditor.addDeleteListener(this::deleteTag);
    }

    private void configureTodoEditor() {
        todoEditor = new TodoEditor(tagService);
        todoEditor.setWidth("25em");
        todoEditor.setVisible(false);
        todoEditor.addSaveListener(this::saveTodo);
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(mainContent, tagEditor);
        content.setFlexGrow(2, mainContent);
        content.setFlexGrow(1, tagEditor);
        content.setSizeFull();
        return content;
    }

    private void saveTag(TagEditor.SaveEvent saveEvent) {
        try {
            tagService.store(saveEvent.getTag());
            List<TagDto> allTags = tagService.getAll();
            tagComboBox.setItems(allTags);
            todoEditor.tag.setItems(allTags);
            todos.forEach(todo -> {
                if (todo.getTag() != null && saveEvent.getTag().getId().equals(todo.getTag().getId())) {
                    todo.setTag(saveEvent.getTag());
                }
            });
            todoGrid.setItems(todos);
            tagEditor.setCreateVisible(false);
        } catch (BusinessException e) {
            add(new ErrorModal(e.getMessage()));
        }
    }

    private void deleteTag(TagEditor.DeleteEvent deleteEvent) {
        tagService.delete(deleteEvent.getTag());
        List<TagDto> allTags = tagService.getAll();
        tagComboBox.setItems(allTags);
        todoEditor.tag.setItems(allTags);
        todos.forEach(todo -> {
            if (todo.getTag() != null && deleteEvent.getTag().getId().equals(todo.getTag().getId())) {
                todo.setTag(null);
            }
        });
        todoGrid.setItems(todos);
        tagEditor.setCreateVisible(false);
    }

    private void saveTodo(TodoEditor.SaveEvent saveEvent) {
        TodoDto todoDto = saveEvent.getTodo();
        TodoDto storedTodo = null;
        try {
            storedTodo = todoService.store(todoDto);
            todos.set(todos.indexOf(todoDto), storedTodo);
            todoGrid.setItems(todos.stream().toList());
            todoEditor.setVisible(false);
        } catch (BusinessException e) {
            add(new ErrorModal(e.getMessage()));
        }
    }
}
