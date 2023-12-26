package akletini.life.core.product.service.impl;

import akletini.life.core.product.repository.api.product.ProductIndexRepository;
import akletini.life.core.product.repository.api.product.ProductRepository;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl extends ProductService {
    private final ProductRepository productRepository;
    protected ProductIndexRepository entityIndexRepository;

    @Override
    public Product store(Product product) throws BusinessException {
        Product stored = super.store(product);
        entityIndexRepository.save(product);
        return stored;
    }

    @Override
    public List<Product> storeBulk(List<Product> products) throws BusinessException {
        List<Product> storedProducts = new ArrayList<>();
        for (Product product : products) {
            storedProducts.add(super.store(product));
        }
        entityIndexRepository.saveAll(products);
        return storedProducts;
    }

    @Override
    public Product getByName(String name) throws EntityNotFoundException {
        return productRepository.findByName(name).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            Product.class.getSimpleName(), name));
            log.error(exception);
            return exception;
        });
    }
}
