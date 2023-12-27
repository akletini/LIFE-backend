package akletini.life.application;

import akletini.life.core.product.repository.entity.*;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.validation.exception.BusinessException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Component
@Log4j2
@AllArgsConstructor
public class InitialDataLoader implements ApplicationRunner {

    private final ProductService productService;
    private final ProductTypeService productTypeService;
    private final AttributeTypeService attributeTypeService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        try {
            log.info("Preparing initial data import");
            AttributeType priceAttributeType = new AttributeType();
            priceAttributeType.setName("Price");
            priceAttributeType.setBasicType(BasicType.Number);
            priceAttributeType.setRequired(false);
            attributeTypeService.store(priceAttributeType);

            AttributeType boughtOnAttributeType = new AttributeType();
            boughtOnAttributeType.setName("Bought on");
            boughtOnAttributeType.setBasicType(BasicType.Date);
            boughtOnAttributeType.setRequired(false);
            attributeTypeService.store(boughtOnAttributeType);

            AttributeType boughtFromAttributeType = new AttributeType();
            boughtFromAttributeType.setName("Bought from");
            boughtFromAttributeType.setBasicType(BasicType.String);
            boughtFromAttributeType.setRequired(false);
            attributeTypeService.store(boughtFromAttributeType);

            ProductType rootProductType = new ProductType();
            rootProductType.setName("Root");
            rootProductType = productTypeService.store(rootProductType);

            ProductType foodProductType = new ProductType();
            foodProductType.setName("Food");
            foodProductType.setParentProductType(rootProductType.getId());
            foodProductType.setAttributeTypes(List.of(priceAttributeType, boughtOnAttributeType));
            foodProductType = productTypeService.store(foodProductType);

            ProductType nonFoodProductType = new ProductType();
            nonFoodProductType.setName("Non-Food");
            nonFoodProductType.setParentProductType(rootProductType.getId());
            nonFoodProductType.setAttributeTypes(List.of(boughtOnAttributeType));
            nonFoodProductType = productTypeService.store(nonFoodProductType);

            ProductType frozenFoodProductType = new ProductType();
            frozenFoodProductType.setName("Frozen Food");
            frozenFoodProductType.setParentProductType(foodProductType.getId());
            frozenFoodProductType.setAttributeTypes(List.of(boughtFromAttributeType));
            frozenFoodProductType = productTypeService.store(frozenFoodProductType);


            Product product = new Product();
            product.setName("Beer");
            product.setQuantity(new Quantity(10L, Quantity.QuantityDescription.VOLUME, "l"));
            product.setProductType(foodProductType);
            List<AttributeType> attributeTypesForProductType =
                    productTypeService.getAttributeTypesForProductType(foodProductType);
            List<Attribute> attributes = new ArrayList<>();
            attributes.add(new Attribute(attributeTypesForProductType.get(0), "10"));
            attributes.add(new Attribute(attributeTypesForProductType.get(1),
                    LocalDate.now().toString()));
            product.setAttributes(attributes);
            productService.store(product);
            log.info("Finished initial data import. ");
        } catch (BusinessException e) {
            log.info("Entities already exist.");

        }
    }
}
