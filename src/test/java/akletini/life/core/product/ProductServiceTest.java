package akletini.life.core.product;

import akletini.life.core.product.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ActiveProfiles("test")
@ExtendWith(SpringExtension.class)
@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_METHOD)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ProductServiceTest {
    @Autowired
    ProductService productService;

    @BeforeEach
    public void initDB() {

    }

//    @Test
//    void testStoreProductWithAttributes() throws BusinessException {
//        Product product = getProductWithAttributes();
//        Product stored = productService.store(product);
//        assertNotNull(stored);
//    }

}
