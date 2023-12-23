package akletini.life.vaadin.view.components;

import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.IntegerField;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class Pagination extends HorizontalLayout {

    private final Button firstPage;
    private final Button lastPage;
    private final Button previous;
    private final Button next;
    public int currentPage;
    public int totalPages;
    public int pageSize;
    final Button refresh;
    final Span label1;
    final Span label2;
    final Span label3;
    Select<Integer> pageSizeSelect;
    IntegerField pageInput;

    public Pagination(int pageSize, int totalPages) {
        currentPage = 0;
        this.pageSize = pageSize;
        this.totalPages = totalPages;

        firstPage = new Button();
        firstPage.setIcon(new Icon(VaadinIcon.ANGLE_DOUBLE_LEFT));
        firstPage.setEnabled(false);
        firstPage.addClickListener(event -> firstPage());

        lastPage = new Button();
        lastPage.setEnabled(currentPage < totalPages - 1);
        lastPage.setIcon(new Icon(VaadinIcon.ANGLE_DOUBLE_RIGHT));
        lastPage.addClickListener(event -> lastPage());

        previous = new Button();
        previous.setEnabled(false);
        previous.setIcon(new Icon(VaadinIcon.ANGLE_LEFT));
        previous.addClickListener(event -> previousPage());

        next = new Button();
        next.setEnabled(currentPage < totalPages - 1);
        next.setIcon(new Icon(VaadinIcon.ANGLE_RIGHT));
        next.addClickListener(event -> nextPage());

        refresh = new Button();
        refresh.setIcon(new Icon(VaadinIcon.REFRESH));
        refresh.addClickListener(event -> refresh());

        pageSizeSelect = new Select<>();
        pageSizeSelect.setItems(List.of(10, 20, 30, 50));
        pageSizeSelect.setValue(10);
        pageSizeSelect.setWidth(80, Unit.PIXELS);
        pageSizeSelect.addValueChangeListener(event -> {
            this.pageSize = event.getValue();
            refresh();
        });

        pageInput = new IntegerField();
        pageInput.setValue(currentPage + 1);
        pageInput.setWidth(30, Unit.PIXELS);
        pageInput.addValueChangeListener(event -> {
            Integer value = event.getValue();
            if (value > 0 && value <= totalPages) {
                currentPage = value - 1;
                refresh();
            } else {
                pageInput.setValue(event.getOldValue());
            }
        });

        label1 = new Span("Showing ");
        label2 = new Span(" items on page ");
        label3 = new Span(" out of " + this.totalPages);

        getStyle().set("margin-left", "auto").set("padding", "15px");
        setAlignItems(Alignment.END);
        setVerticalComponentAlignment(Alignment.CENTER, label1, label2, pageInput, label3);
        setJustifyContentMode(JustifyContentMode.END);
        add(firstPage, previous, next, lastPage, label1, pageSizeSelect, label2,
                pageInput, label3, refresh);
    }

    public void previousPage() {
        currentPage--;
        setNavigationEnabled();
        fireEvent(new PaginationEvent(this, currentPage, pageSize));
    }

    public void nextPage() {
        currentPage++;
        setNavigationEnabled();
        fireEvent(new PaginationEvent(this, currentPage, pageSize));
    }

    public void lastPage() {
        currentPage = totalPages - 1;
        setNavigationEnabled();
        fireEvent(new PaginationEvent(this, currentPage, pageSize));
    }

    public void firstPage() {
        currentPage = 0;
        setNavigationEnabled();
        fireEvent(new PaginationEvent(this, currentPage, pageSize));
    }

    private void setNavigationEnabled() {
        previous.setEnabled(currentPage > 0);
        firstPage.setEnabled(currentPage > 0);
        next.setEnabled(currentPage < totalPages - 1);
        lastPage.setEnabled(currentPage < totalPages - 1);
        pageInput.setValue(currentPage + 1);
    }

    public void refresh() {
        fireEvent(new PaginationEvent(this, currentPage, pageSize));
        setNavigationEnabled();
        label3.setText(" out of " + totalPages);
    }

    public static class PaginationEvent extends ComponentEvent<Pagination> {
        private int page;
        private int pageSize;

        protected PaginationEvent(Pagination source, int page, int pageSize) {
            super(source, false);
            this.page = page;
            this.pageSize = pageSize;
        }

        public int getPage() {
            return page;
        }

        public int getPageSize() {
            return pageSize;
        }
    }

    public Registration addQueryListener(ComponentEventListener<Pagination.PaginationEvent> listener) {
        return addListener(Pagination.PaginationEvent.class, listener);
    }
}
