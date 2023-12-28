package akletini.life.core.product.repository.api.product;

import akletini.life.core.product.repository.entity.Product;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends ListCrudRepository<Product, Long>,
        ListPagingAndSortingRepository<Product, Long> {
    Optional<Product> findByName(String name);

    void deleteByProductType_Id(Long id);

}
