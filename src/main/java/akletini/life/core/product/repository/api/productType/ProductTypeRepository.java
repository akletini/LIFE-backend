package akletini.life.core.product.repository.api.productType;

import akletini.life.core.product.repository.entity.ProductType;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductTypeRepository extends ListCrudRepository<ProductType, Long>,
        ListPagingAndSortingRepository<ProductType, Long> {
    Optional<ProductType> findByName(String name);

    List<ProductType> findByAttributeTypes_Id(Long id);
}
