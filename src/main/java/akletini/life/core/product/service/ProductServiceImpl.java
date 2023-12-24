package akletini.life.core.product.service;

import akletini.life.core.product.repository.api.ProductRepository;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import akletini.life.core.shared.validation.exception.InvalidDataException;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl extends ProductService {

    private ProductRepository productRepository;

    @Override
    public Product store(Product product) throws InvalidDataException {
        return super.store(product);
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
