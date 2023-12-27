package akletini.life.vaadin.service.product;

import akletini.life.core.product.dto.ProductDto;
import akletini.life.core.product.dto.mapper.ProductMapper;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@AllArgsConstructor
public class ExposedProductService {

    private final ProductService productService;
    private final ProductMapper productMapper;

    public Page<ProductDto> getProducts(int page,
                                        int pageSize,
                                        Long productTypeId,
                                        boolean searchProductTypeRecursive,
                                        String searchTerm) throws IOException,
            EntityNotFoundException {
        Page<Product> products = productService.getProducts(page, pageSize, productTypeId,
                searchProductTypeRecursive, searchTerm);
        return products.map(productMapper::productToDto);
    }

    public ProductDto store(ProductDto productDto) throws BusinessException {
        return productMapper.productToDto(productService.store(productMapper.dtoToProduct(productDto)));
    }

    public void delete(ProductDto productDto) {
        productService.delete(productMapper.dtoToProduct(productDto));
    }
}
