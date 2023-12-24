package akletini.life.core.product.repository.api;

import akletini.life.core.product.repository.entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends ListCrudRepository<Product, Long>,
        ListPagingAndSortingRepository<Product, Long> {

    @Query("from Product p where p.name=:productName")
    Optional<Product> findByName(@Param("productName") String productName);

}
