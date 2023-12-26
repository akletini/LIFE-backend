package akletini.life.vaadin.view.product;

import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.vaadin.service.product.ExposedProductService;
import akletini.life.vaadin.service.product.ExposedProductTypeService;
import akletini.life.vaadin.view.MainView;
import akletini.life.vaadin.view.components.PagedGridView;
import akletini.life.vaadin.view.components.Pagination;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.treegrid.TreeGrid;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.data.domain.Page;

import java.util.ArrayList;
import java.util.List;

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

    public ProductView(ExposedProductService productService,
                       ExposedProductTypeService productTypeService) {
        this.productService = productService;
        this.productTypeService = productTypeService;
        H1 title = new H1("Products page");
        grid = new Grid<>();
        initializeGrid(grid);
        productTypeGrid = new TreeGrid<>();
        initializeTreeGrid();
//        pagination = new Pagination(10, products.getTotalPages());
        pagination = new Pagination(10, 1);
        pagination.addQueryListener(event -> query());
        initializeMainContentLayout();
        add(title, gridContentLayout, pagination);
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
                .setAutoWidth(true);

    }

    private void initializeGrid(Grid<ProductDto> grid) {
        grid.addColumn(ProductDto::getName)
                .setHeader("Name")
                .setSortable(true);
        grid.addColumn(productDto -> productDto.getProductTypeDto().getName())
                .setHeader("Product type")
                .setSortable(true);
        grid.getColumns().forEach(column -> column.setAutoWidth(true));
    }

    @Override
    public void query() {

    }
}
