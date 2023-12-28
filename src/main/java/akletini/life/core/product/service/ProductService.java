package akletini.life.core.product.service;

import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import org.springframework.data.domain.Page;

import java.io.IOException;

public abstract class ProductService extends EntityService<Product> {

    public abstract void deleteByProductType(ProductType productType);

    public abstract Page<Product> getProducts(int page,
                                              int pageSize,
                                              Long productTypeId,
                                              boolean searchProductTypeRecursive,
                                              String searchTerm) throws IOException,
            EntityNotFoundException;

    protected abstract Product getByName(String name) throws EntityNotFoundException;
}
