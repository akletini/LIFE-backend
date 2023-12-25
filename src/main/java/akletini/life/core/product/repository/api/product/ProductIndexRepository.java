package akletini.life.core.product.repository.api.product;

import akletini.life.core.product.repository.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProductIndexRepository extends ElasticsearchRepository<Product, Long>,
        PagingAndSortingRepository<Product, Long> {
}
