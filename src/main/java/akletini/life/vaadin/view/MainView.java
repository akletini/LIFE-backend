package akletini.life.vaadin.view;

import akletini.life.core.index.service.IndexService;
import akletini.life.vaadin.view.chore.ChoreView;
import akletini.life.vaadin.view.components.IndexDialog;
import akletini.life.vaadin.view.product.ProductView;
import akletini.life.vaadin.view.todo.TodoView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.contextmenu.SubMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.theme.lumo.Lumo;

@Route("")
public class MainView extends AppLayout {

    public MainView(IndexService indexService) {
        setTheme(true);
        DrawerToggle toggle = new DrawerToggle();
        H1 title = new H1("LIFE Application");

        Tabs tabs = getTabs();
        Checkbox themeToggle = new Checkbox("Dark Theme", true);
        themeToggle.addValueChangeListener(e -> {
            setTheme(e.getValue());
        });
        Span settings = new Span(new Span("Settings "), new Icon(VaadinIcon.COG_O));
        MenuBar settingsMenu = new MenuBar();
        settingsMenu.getStyle().set("margin-left", "auto");
        settingsMenu.getStyle().set("padding", "15px");
        MenuItem menuItem = settingsMenu.addItem(settings);
        SubMenu subMenu = menuItem.getSubMenu();
        subMenu.addItem(themeToggle);
        MenuItem indexMenuItem = subMenu.addItem("Index");
        indexMenuItem.addClickListener(event -> addToDrawer(new IndexDialog(indexService)));

        addToDrawer(tabs);
        addToNavbar(toggle, title, settingsMenu);

        setPrimarySection(Section.DRAWER);
    }

    private Tabs getTabs() {
        Tabs tabs = new Tabs();

        tabs.add(createTab(VaadinIcon.DASHBOARD, "Dashboard", MainView.class),
                createTab(VaadinIcon.TASKS, "Chores", ChoreView.class),
                createTab(VaadinIcon.CHECK, "Todos", TodoView.class),
                createTab(VaadinIcon.PACKAGE, "Products", ProductView.class));
        tabs.setOrientation(Tabs.Orientation.VERTICAL);
        return tabs;
    }

    private Tab createTab(VaadinIcon viewIcon, String viewName,
                          Class<? extends Component> routeComponent) {
        Icon icon = viewIcon.create();
        icon.getStyle().set("box-sizing", "border-box")
                .set("margin-inline-end", "var(--lumo-space-m)")
                .set("padding", "var(--lumo-space-xs)");

        RouterLink link = new RouterLink();
        link.add(icon, new Span(viewName));
        if (routeComponent != null) {
            link.setRoute(routeComponent);
        }

        return new Tab(link);
    }

    private void setTheme(boolean dark) {
        var js = "document.documentElement.setAttribute('theme', $0)";
        getElement().executeJs(js, dark ? Lumo.DARK : Lumo.LIGHT);
    }
}
