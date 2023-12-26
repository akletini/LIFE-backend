package akletini.life.vaadin.service.product;

import akletini.life.core.product.dto.mapper.ProductMapper;
import akletini.life.core.product.service.ProductService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ExposedProductService {

    private final ProductService productService;
    private final ProductMapper productMapper;
}
