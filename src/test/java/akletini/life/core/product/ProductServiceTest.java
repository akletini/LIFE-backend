package akletini.life.core.product;

import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.product.structure.DatabaseInitializer;
import akletini.life.core.shared.validation.exception.BusinessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
//@ExtendWith(SpringExtension.class)
@SpringBootTest(useMainMethod = SpringBootTest.UseMainMethod.ALWAYS)
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTest {
    @Autowired
    ProductService productService;

    @Autowired
    ProductTypeService productTypeService;

    @Autowired
    AttributeTypeService attributeTypeService;

    @Autowired
    DatabaseInitializer database;

    @BeforeEach
    public void init() throws Exception {
//        database.run(null);
    }

    @Test
    void testStoreProduct() throws BusinessException {
//        Product beerProduct = database.getBeerProduct();
//
//        assertNotNull(beerProduct);
    }

}
