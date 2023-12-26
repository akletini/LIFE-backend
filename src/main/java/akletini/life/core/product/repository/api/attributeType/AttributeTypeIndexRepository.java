package akletini.life.core.product.repository.api.attributeType;

import akletini.life.core.product.repository.entity.AttributeType;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface AttributeTypeIndexRepository extends ElasticsearchRepository<AttributeType, Long> {
}
