package akletini.life.core.product.service.impl;

import akletini.life.core.index.Indexes;
import akletini.life.core.index.service.impl.ProductTypeIndexer;
import akletini.life.core.product.repository.api.attributeType.AttributeTypeIndexRepository;
import akletini.life.core.product.repository.api.attributeType.AttributeTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.Query;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryStringQuery;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.elasticsearch.core.search.TrackHits;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class AttributeTypeServiceImpl extends AttributeTypeService {

    private final AttributeTypeRepository attributeTypeRepository;
    private final AttributeTypeIndexRepository attributeTypeIndexRepository;
    private final ProductTypeIndexer productTypeIndexer;
    private final ElasticsearchClient esClient;
    private final ProductTypeService productTypeService;


    @Override
    public AttributeType store(AttributeType attributeType) throws BusinessException {
        AttributeType stored = super.store(attributeType);
        attributeTypeIndexRepository.save(attributeType);
        productTypeIndexer.indexByProductTypes(getProductTypesByAttributeType(attributeType));
        return stored;
    }

    @Override
    public AttributeType getByName(String typeName) throws EntityNotFoundException {
        return attributeTypeRepository.findByName(typeName).orElseThrow(() -> {
            EntityNotFoundException exception =
                    new EntityNotFoundException(Errors.getError(Errors.ENTITY_NOT_FOUND,
                            AttributeType.class.getSimpleName(), typeName));
            log.error(exception);
            return exception;
        });
    }

    @Override
    public List<ProductType> getProductTypesByAttributeType(AttributeType attributeType) {
        List<ProductType> productTypes = productTypeService.getAllByAttributeType(attributeType);
        List<ProductType> childProductTypes = new ArrayList<>();
        productTypes.forEach(productType -> childProductTypes.addAll(productTypeService.getAllChildProductTypes(productType)));
        productTypes.addAll(childProductTypes);
        return productTypes;
    }

    @Override
    public Page<AttributeType> getAttributeTypes(int page,
                                                 int pageSize,
                                                 String searchTerm) throws IOException {

        SearchRequest.Builder builder = new SearchRequest.Builder()
                .from(page * pageSize)
                .size(pageSize)
                .index(Indexes.ATTRIBUTE_TYPES.getIndexName())
                .trackTotalHits(TrackHits.of(track -> track.enabled(true)));
        searchTerm = StringUtils.isEmpty(searchTerm) ? "*" : searchTerm;
        Query query = new Query.Builder().queryString(
                        new QueryStringQuery.Builder()
                                .query(searchTerm)
                                .build())
                .build();
        builder.query(query);
        SearchResponse<AttributeType> search = esClient.search(builder.build(),
                AttributeType.class);

        List<Hit<AttributeType>> hits = search.hits().hits();
        List<AttributeType> attributeTypes = hits.stream().map(Hit::source).toList();
        Pageable pageable = PageRequest.of(page, pageSize);
        return new PageImpl<>(attributeTypes, pageable, search.hits().total() != null ?
                search.hits().total().value() : pageSize);
    }
}
