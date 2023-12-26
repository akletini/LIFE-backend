package akletini.life.core.product.structure;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;

import java.time.LocalDateTime;
import java.util.List;

public class TestProductTypes {
    private TestProductTypes() {

    }

    public static ProductType getRootProductType() {
        ProductType rootProductType = new ProductType();
        rootProductType.setName("Root");
        rootProductType.setCreatedAt(LocalDateTime.now());
        return rootProductType;
    }

    public static ProductType getFrozenFoodProductType(AttributeType boughtFromAttributeType,
                                                       ProductType foodProductType) {
        ProductType frozenFoodProductType = new ProductType();
        frozenFoodProductType.setName("Frozen Food");
        frozenFoodProductType.setCreatedAt(LocalDateTime.now());
        frozenFoodProductType.setParentProductType(foodProductType.getId());
        frozenFoodProductType.setAttributeTypes(List.of(boughtFromAttributeType));
        return frozenFoodProductType;
    }

    public static ProductType getNonFoodProductType(AttributeType boughtFromAttributeType,
                                                    ProductType rootProductType) {
        ProductType nonFoodProductType = new ProductType();
        nonFoodProductType.setName("Non-Food");
        nonFoodProductType.setCreatedAt(LocalDateTime.now());
        nonFoodProductType.setParentProductType(rootProductType.getId());
        nonFoodProductType.setAttributeTypes(List.of(boughtFromAttributeType));
        return nonFoodProductType;
    }

    public static ProductType getFoodProductType(AttributeType priceAttributeType,
                                                 AttributeType boughtFromAttributeType,
                                                 ProductType rootProductType) {
        ProductType foodProductType = new ProductType();
        foodProductType.setName("Food");
        foodProductType.setCreatedAt(LocalDateTime.now());
        foodProductType.setParentProductType(rootProductType.getId());
        foodProductType.setAttributeTypes(List.of(priceAttributeType, boughtFromAttributeType));
        return foodProductType;
    }
}
