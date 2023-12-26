package akletini.life.core.product.structure;

import akletini.life.core.product.repository.entity.*;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.validation.exception.BusinessException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
@Getter
@Profile("test")
public class DatabaseInitializer implements ApplicationRunner {

    @Autowired
    private Environment environment;
    @Autowired
    private ProductService productService;
    @Autowired
    private ProductTypeService productTypeService;
    @Autowired
    private AttributeTypeService attributeTypeService;

    private AttributeType priceAttributeType;
    private ProductType frozenFoodProductType;
    private AttributeType boughtOnAttributeType;
    private AttributeType boughtFromAttributeType;
    private ProductType rootProductType;
    private ProductType foodProductType;
    private ProductType nonFoodProductType;
    private Product vacuumProduct;
    private Product beerProduct;
    private Product frozenPizzaProduct;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean isTest = Arrays.asList(environment.getActiveProfiles()).contains("test");
        if (isTest) {
            initializeDatabase();
        }
    }

    private void initializeDatabase() throws BusinessException {
        // create attribute types
        priceAttributeType = TestAttributeTypes.getPriceAttributeType();
        attributeTypeService.store(priceAttributeType);

        boughtOnAttributeType = TestAttributeTypes.getBoughtOnAttributeType();
        attributeTypeService.store(boughtOnAttributeType);

        boughtFromAttributeType = TestAttributeTypes.getBoughtFromAttributeType();
        attributeTypeService.store(boughtFromAttributeType);

        // create product types
        rootProductType = TestProductTypes.getRootProductType();
        rootProductType = productTypeService.store(rootProductType);

        foodProductType = TestProductTypes.getFoodProductType(priceAttributeType,
                boughtOnAttributeType,
                rootProductType);
        foodProductType = productTypeService.store(foodProductType);

        nonFoodProductType =
                TestProductTypes.getNonFoodProductType(boughtOnAttributeType, rootProductType);
        nonFoodProductType = productTypeService.store(nonFoodProductType);

        frozenFoodProductType = TestProductTypes.getFrozenFoodProductType(boughtFromAttributeType
                , foodProductType);
        frozenFoodProductType = productTypeService.store(frozenFoodProductType);

        vacuumProduct = createVacuumProduct(nonFoodProductType);
        beerProduct = createBeerProduct(foodProductType);
        frozenPizzaProduct = createFrozenPizzaProduct(frozenFoodProductType);
    }


    private Product createVacuumProduct(ProductType nonFoodProductType) throws BusinessException {
        Product product = new Product();
        product.setName("Vacuum");
        product.setCreatedAt(LocalDateTime.now());
        product.setQuantity(new Quantity(1, Quantity.QuantityDescription.PIECE, "Piece"));
        product.setProductType(nonFoodProductType);
        List<AttributeType> attributeTypesForProductType =
                productTypeService.getAttributeTypesForProductType(nonFoodProductType);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(attributeTypesForProductType.get(0),
                LocalDate.now().toString()));
        product.setAttributes(attributes);
        return productService.store(product);
    }

    private Product createBeerProduct(ProductType foodProductType) throws BusinessException {
        Product product = new Product();
        product.setName("Beer");
        product.setCreatedAt(LocalDateTime.now());
        product.setQuantity(new Quantity(10, Quantity.QuantityDescription.VOLUME, "l"));
        product.setProductType(foodProductType);
        List<AttributeType> attributeTypesForProductType =
                productTypeService.getAttributeTypesForProductType(foodProductType);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(attributeTypesForProductType.get(0), "10"));
        attributes.add(new Attribute(attributeTypesForProductType.get(1),
                LocalDate.now().toString()));
        product.setAttributes(attributes);
        return productService.store(product);
    }

    private Product createFrozenPizzaProduct(ProductType frozenFoodProductType) throws BusinessException {
        Product product = new Product();
        product.setName("Frozen Pizza");
        product.setCreatedAt(LocalDateTime.now());
        product.setQuantity(new Quantity(1, Quantity.QuantityDescription.PIECE, "Piece"));
        product.setProductType(frozenFoodProductType);
        List<AttributeType> attributeTypesForProductType =
                productTypeService.getAttributeTypesForProductType(frozenFoodProductType);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(new Attribute(attributeTypesForProductType.get(0), "REWE"));
        attributes.add(new Attribute(attributeTypesForProductType.get(1), "10"));
        attributes.add(new Attribute(attributeTypesForProductType.get(2),
                LocalDate.now().toString()));
        product.setAttributes(attributes);
        return productService.store(product);
    }
}
