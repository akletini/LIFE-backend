package akletini.life.core.product.structure;

import akletini.life.core.product.repository.entity.Attribute;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.repository.entity.Quantity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static akletini.life.core.product.repository.entity.Quantity.QuantityDescription.WEIGHT;

public class TestProducts {
    private TestProducts() {

    }

    public static Product getEmptyProduct() {
        Product product = new Product();
        product.setName("TestProduct");
        product.setDescription("Some description...");
        product.setQuantity(new Quantity(3L, WEIGHT, "kg"));
        product.setCreatedAt(LocalDateTime.now());
        return product;
    }

    public static Product getProductWithAttributes() {
        Product product = getEmptyProduct();
        List<Attribute> attributes = new ArrayList<>();
        for (int i = 0; i < 5; i++) {
//            attributes.add(new Attribute("value_" + i, BasicType.String));
        }
        product.setAttributes(attributes);
        return product;
    }
}
