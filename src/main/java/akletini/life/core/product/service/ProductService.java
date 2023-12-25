package akletini.life.core.product.service;

import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.shared.EntityService;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;

public abstract class ProductService extends EntityService<Product> {

    protected abstract Product getByName(String name) throws EntityNotFoundException;
}
