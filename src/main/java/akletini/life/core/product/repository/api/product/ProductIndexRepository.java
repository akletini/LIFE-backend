package akletini.life.core.product.repository.api.product;

import akletini.life.core.product.repository.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface ProductIndexRepository extends ElasticsearchRepository<Product, Long> {

    void deleteByProductType_Id(Long id);
}
