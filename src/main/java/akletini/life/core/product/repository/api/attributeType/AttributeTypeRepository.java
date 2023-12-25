package akletini.life.core.product.repository.api.attributeType;

import akletini.life.core.product.repository.entity.AttributeType;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.ListPagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AttributeTypeRepository extends ListCrudRepository<AttributeType, Long>,
        ListPagingAndSortingRepository<AttributeType, Long> {
    Optional<AttributeType> findByName(String name);
}
