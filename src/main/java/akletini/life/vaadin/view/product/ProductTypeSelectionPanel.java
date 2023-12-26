package akletini.life.vaadin.view.product;

import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.vaadin.service.product.ExposedProductTypeService;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.shared.Registration;
import lombok.Getter;

@Getter
public class ProductTypeSelectionPanel extends VerticalLayout {
    private final Dialog dialog;
    private Select<ProductTypeDto> productTypeSelect;

    public ProductTypeSelectionPanel(ExposedProductTypeService productTypeService) {
        productTypeSelect = new Select<>();
        productTypeSelect.setItems(productTypeService.getAll());
        productTypeSelect.setValue(productTypeSelect.getListDataView().getItems().findAny().get());
        productTypeSelect.setTextRenderer(ProductTypeDto::getName);
        dialog = new Dialog();
        dialog.setHeaderTitle("Select product type");
        dialog.add(productTypeSelect);
        Button cancelButton = new Button("Cancel");
        cancelButton.addClickListener(event -> {
            fireEvent(new CancelEvent(this, null));
        });
        Button okButton = new Button("OK");
        okButton.addClickListener(event -> {
            fireEvent(new NextEvent(this, productTypeSelect.getValue()));
        });
        dialog.getFooter().add(new HorizontalLayout(cancelButton, okButton));
        add(dialog);
        dialog.open();
    }

    public static abstract class ProductTypeSelectionPanelEvent extends
            ComponentEvent<ProductTypeSelectionPanel> {

        private ProductTypeDto productTypeDto;

        public ProductTypeSelectionPanelEvent(ProductTypeSelectionPanel source,
                                              ProductTypeDto productTypeDto) {
            super(source, false);
            this.productTypeDto = productTypeDto;
        }

        public ProductTypeDto getProductTypeDto() {
            return productTypeDto;
        }
    }

    public static class NextEvent extends ProductTypeSelectionPanel.ProductTypeSelectionPanelEvent {
        public NextEvent(ProductTypeSelectionPanel source, ProductTypeDto productTypeDto) {
            super(source, productTypeDto);
        }
    }

    public static class CancelEvent extends
            ProductTypeSelectionPanel.ProductTypeSelectionPanelEvent {
        public CancelEvent(ProductTypeSelectionPanel source, ProductTypeDto productTypeDto) {
            super(source, productTypeDto);
        }
    }

    public Registration addNextListener(ComponentEventListener<NextEvent> listener) {
        return addListener(NextEvent.class, listener);
    }

    public Registration addCancelListener(ComponentEventListener<CancelEvent> listener) {
        return addListener(CancelEvent.class, listener);
    }
}
