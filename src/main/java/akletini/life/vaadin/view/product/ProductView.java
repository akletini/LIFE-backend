package akletini.life.vaadin.view.product;

import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.vaadin.service.product.ExposedProductService;
import akletini.life.vaadin.service.product.ExposedProductTypeService;
import akletini.life.vaadin.view.MainView;
import akletini.life.vaadin.view.components.ErrorModal;
import akletini.life.vaadin.view.components.PagedGridView;
import akletini.life.vaadin.view.components.Pagination;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.contextmenu.GridContextMenu;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    public ProductView(ExposedProductService productService,
                       ExposedProductTypeService productTypeService) {
        this.productService = productService;
        this.productTypeService = productTypeService;
        H1 title = new H1("Products page");
        grid = new Grid<>();
        initializeGrid();
        productTypeGrid = new TreeGrid<>();
        initializeTreeGrid();
        Component searchLayout = createSearchLayout();
        pagination = new Pagination(10, 0);
        pagination.addQueryListener(event -> query());
        initializeMainContentLayout();
        add(title, searchLayout, gridContentLayout, pagination);
    }

    private Component createSearchLayout() {
        HorizontalLayout layout = new HorizontalLayout();
        searchInput = new TextField("Search");
        searchInput.setPlaceholder("Search term...");
        executeSearchButton = new Button("Search");
        executeSearchButton.addClickListener(event -> query());
        newProductButton = new Button("New Product");
        layout.setVerticalComponentAlignment(Alignment.END, executeSearchButton);
        layout.add(searchInput, executeSearchButton, newProductButton);
        return layout;
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
        grid.getColumns().forEach(column -> column.setAutoWidth(true));

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
}
