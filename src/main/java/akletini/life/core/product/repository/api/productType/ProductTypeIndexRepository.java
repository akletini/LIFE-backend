package akletini.life.core.product.repository.api.productType;

import akletini.life.core.product.repository.entity.ProductType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductTypeIndexRepository extends ElasticsearchRepository<ProductType, Long> {
}
