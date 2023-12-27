package akletini.life.vaadin.view.product;

import akletini.life.core.product.dto.AttributeDto;
import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.core.product.repository.entity.BasicType;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.vaadin.service.product.ExposedProductService;
import akletini.life.vaadin.service.product.ExposedProductTypeService;
import akletini.life.vaadin.view.MainView;
import akletini.life.vaadin.view.components.ConfirmDeleteDialog;
import akletini.life.vaadin.view.components.ErrorModal;
import akletini.life.vaadin.view.components.PagedGridView;
import akletini.life.vaadin.view.components.Pagination;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.listbox.ListBox;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.splitlayout.SplitLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static akletini.life.core.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.core.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Route(value = "/products", layout = MainView.class)
@PageTitle("Products")
public class ProductView extends VerticalLayout implements PagedGridView {

    private final ExposedProductService productService;
    private final ExposedProductTypeService productTypeService;
    private final Grid<ProductDto> grid;
    private final TreeGrid<ProductTypeDto> productTypeGrid;
    private final Pagination pagination;
    private Page<ProductDto> products;
    private HorizontalLayout gridContentLayout;
    private TextField searchInput;
    private Button executeSearchButton;
    private Button newProductButton;
    private ProductEditor productEditor;

    public ProductView(ExposedProductService productService,
                       ExposedProductTypeService productTypeService) throws EntityNotFoundException {
        this.productService = productService;
        this.productTypeService = productTypeService;
        H1 title = new H1("Products page");
        grid = new Grid<>();
        initializeGrid();
        productTypeGrid = new TreeGrid<>();
        initializeTreeGrid();
        Component searchLayout = createSearchLayout();
        pagination = new Pagination(10, 1);
        pagination.addQueryListener(event -> query());
        initializeMainContentLayout();
        configureEditor();
        add(getEditorLayout(new VerticalLayout(title, searchLayout, gridContentLayout,
                pagination)));
    }

    private Component createSearchLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        searchInput = new TextField("Search");
        searchInput.setPlaceholder("Search term...");
        executeSearchButton = new Button("Search");
        executeSearchButton.addClickListener(event -> query());
        newProductButton = new Button("New Product");
        newProductButton.addClickListener(event -> {
            ProductTypeSelectionPanel productTypeSelectionPanel =
                    new ProductTypeSelectionPanel(productTypeService);
            productTypeSelectionPanel.addNextListener(e -> {
                try {
                    productEditor.setEditorVisible(new ProductDto(), e.getProductTypeDto(), true);
                } catch (EntityNotFoundException ex) {
                    add(new ErrorModal(ex.getMessage()));
                }
                productTypeSelectionPanel.getDialog().close();
            });
            productTypeSelectionPanel.addCancelListener(e -> {
                productTypeSelectionPanel.getDialog().close();
            });
            add(productTypeSelectionPanel);
        });
        layout.setVerticalComponentAlignment(Alignment.END, executeSearchButton, newProductButton);
        layout.add(searchInput, executeSearchButton, newProductButton);
        return layout;
    }

    private SplitLayout getEditorLayout(VerticalLayout mainContent) {
        SplitLayout content = new SplitLayout(mainContent, productEditor);
        content.setSplitterPosition(65.0);
        content.setSizeFull();
        return content;
    }

    private void initializeMainContentLayout() {
        gridContentLayout = new HorizontalLayout(productTypeGrid, grid);
        gridContentLayout.setFlexGrow(5, grid);
        gridContentLayout.setFlexGrow(1, productTypeGrid);
        gridContentLayout.setSizeFull();
    }

    private void initializeTreeGrid() {
        List<ProductTypeDto> productTypeHierarchy =
                productTypeService.constructHierarchy();
        ProductTypeDto rootProductType =
                productTypeHierarchy.stream().filter(productTypeDto -> productTypeDto.getParentProductType() == null).findFirst().orElseThrow();
        productTypeGrid.setItems(new ArrayList<>(List.of(rootProductType)),
                ProductTypeDto::getChildProductTypes);
        productTypeGrid.addHierarchyColumn(ProductTypeDto::getName)
                .setHeader("Product types")
                .setAutoWidth(true)
                .setTooltipGenerator(ProductTypeDto::getName);
        GridContextMenu<ProductTypeDto> menu = productTypeGrid.addContextMenu();
        menu.addItem("Add", event -> {
        });
        menu.addItem("Edit", event -> {
        });
        menu.addItem("Delete", event -> {
        });
        productTypeGrid.setSelectionMode(Grid.SelectionMode.SINGLE);
    }

    private void initializeGrid() {
        grid.addColumn(ProductDto::getName)
                .setHeader("Name")
                .setSortable(true);
        grid.addColumn(productDto -> productDto.getProductType().getName())
                .setHeader("Product type")
                .setSortable(true);
        grid.addComponentColumn(productDto -> {
            ListBox<String> listBox = new ListBox<>();
            listBox.setReadOnly(true);
            List<String> attributes = new ArrayList<>();
            for (AttributeDto attribute : productDto.getAttributes()) {
                String value = attribute.getValue();
                if (BasicType.Date.equals(attribute.getAttributeType().getBasicType())) {
                    value = LocalDate.parse(value).format(DateTimeFormatter.ofPattern(DATE_FORMAT));
                } else if (BasicType.DateTime.equals(attribute.getAttributeType().getBasicType())) {
                    value = LocalDateTime.parse(value).format(DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
                }
                attributes.add(attribute.getAttributeType().getName() + ": " + value);
            }
            listBox.setItems(attributes);
            return listBox;
        }).setHeader("Attributes");
        grid.addComponentColumn(productDto -> {
            HorizontalLayout layout = new HorizontalLayout();
            Button edit = new Button();
            edit.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_TERTIARY);
            edit.setIcon(new Icon(VaadinIcon.EDIT));
            edit.addClickListener(event -> {
                try {
                    productEditor.setEditorVisible(productDto, productDto.getProductType(), false);
                } catch (EntityNotFoundException e) {
                    add(new ErrorModal(e.getMessage()));
                }
            });
            Button delete = new Button();
            delete.addThemeVariants(ButtonVariant.LUMO_ICON,
                    ButtonVariant.LUMO_ERROR,
                    ButtonVariant.LUMO_TERTIARY);
            delete.setIcon(new Icon(VaadinIcon.TRASH));
            delete.addClickListener(event -> {
                ConfirmDeleteDialog deleteDialog = new ConfirmDeleteDialog("Do you really " +
                        "want to delete product " + productDto.getName() + "?");
                deleteDialog.addDeleteListener(e -> {
                    deleteProduct(productDto);
                    e.getSource().getDialog().close();
                    query();
                });
                add(deleteDialog);
            });
            layout.add(edit, delete);
            return layout;
        });
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }


    private void configureEditor() throws EntityNotFoundException {
        productEditor = new ProductEditor(productTypeService, null);
        productEditor.setWidth("25em");
        productEditor.setVisible(false);
        productEditor.addSaveListener(this::saveProduct);
        productEditor.addDeleteListener(event -> deleteProduct(event.getProduct()));
    }

    private void deleteProduct(ProductDto productDto) {
        productEditor.setVisible(false);
        productService.delete(productDto);
    }

    @Override
    public void query() {
        try {
            Optional<ProductTypeDto> optionalProductType =
                    productTypeGrid.getSelectedItems().stream().findFirst();
            Long productTypeId = null;
            if (optionalProductType.isPresent()) {
                productTypeId = optionalProductType.get().getId();
            }
            this.products = productService.getProducts(pagination.currentPage,
                    pagination.pageSize,
                    productTypeId,
                    true,
                    searchInput.getValue());
            pagination.pageSize = products.getSize();
            pagination.totalPages = products.getTotalPages();
            grid.setItems(this.products.toList());
        } catch (IOException | EntityNotFoundException e) {
            add(new ErrorModal(e.getMessage()));
        }
    }

    private void saveProduct(ProductEditor.SaveEvent saveEvent) {
        try {
            productService.store(saveEvent.getProduct());
            productEditor.setVisible(false);
            productEditor.removeAll();
        } catch (BusinessException e) {
            add(new ErrorModal(e.getMessage()));
        }
    }
}
