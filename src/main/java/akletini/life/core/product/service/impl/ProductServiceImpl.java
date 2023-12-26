package akletini.life.core.product.service.impl;

import akletini.life.core.index.Indexes;
import akletini.life.core.product.repository.api.product.ProductIndexRepository;
import akletini.life.core.product.repository.api.product.ProductRepository;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.product.service.ProductTypeService;
import akletini.life.core.shared.BaseEntity;
import akletini.life.core.shared.validation.Errors;
import akletini.life.core.shared.validation.exception.BusinessException;
import akletini.life.core.shared.validation.exception.EntityNotFoundException;
import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
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
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Log4j2
public class ProductServiceImpl extends ProductService {
    private final ProductRepository productRepository;
    protected ProductIndexRepository entityIndexRepository;
    private final ProductTypeService productTypeService;
    protected ElasticsearchClient esClient;

    @Override
    public Product store(Product product) throws BusinessException {
        Product stored = super.store(product);
        entityIndexRepository.save(product);
        return stored;
    }

    @Override
    public Page<Product> getProducts(int page,
                                     int pageSize,
                                     Long productTypeId,
                                     boolean searchProductTypeRecursive,
                                     String searchTerm) throws IOException,
            EntityNotFoundException {
        SearchRequest.Builder builder = new SearchRequest.Builder()
                .from(page * pageSize)
                .size(pageSize)
                .index(Indexes.PRODUCTS.getIndexName())
                .trackTotalHits(TrackHits.of(track -> track.enabled(true)));
        searchTerm = StringUtils.isEmpty(searchTerm) ? "*" : searchTerm;
        BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder();
        if (productTypeId != null) {
            if (searchProductTypeRecursive) {
                addRecursiveProductTypesToQuery(productTypeId, boolQueryBuilder);
            } else {
                boolQueryBuilder
                        .must(new Query.Builder()
                                .match(new MatchQuery.Builder()
                                        .query(productTypeId)
                                        .field("productType.id")
                                        .build()).build());
            }
        }
        boolQueryBuilder
                .must(new Query.Builder()
                        .queryString(new QueryStringQuery.Builder()
                                .query(searchTerm)
                                .build())
                        .build());

        Query query = new Query.Builder().bool(boolQueryBuilder.build()).build();
        builder.query(query);

        SearchResponse<Product> search = esClient.search(builder.build(), Product.class);

        List<Hit<Product>> hits = search.hits().hits();
        List<Product> products = hits.stream().map(Hit::source).toList();
        Pageable pageable = PageRequest.of(page, pageSize);

        return new PageImpl<>(products, pageable,
                search.hits().total() != null ? search.hits().total().value() : pageSize);
    }

    @Override
    public List<Product> storeBulk(List<Product> products) throws BusinessException {
        List<Product> storedProducts = new ArrayList<>();
        for (Product product : products) {
            storedProducts.add(super.store(product));
        }
        entityIndexRepository.saveAll(products);
        return storedProducts;
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

    private void addRecursiveProductTypesToQuery(Long productTypeId,
                                                 BoolQuery.Builder boolQueryBuilder) throws EntityNotFoundException {
        List<Long> childProductTypes =
                productTypeService
                        .getAllChildProductTypes(productTypeService.getById
                                (productTypeId))
                        .stream().map(BaseEntity::getId).collect(Collectors.toList());
        childProductTypes.add(productTypeId);
        List<Query> productTypeQueries = new ArrayList<>();
        childProductTypes.forEach(id -> {
            Query q = new Query.Builder()
                    .match(new MatchQuery.Builder()
                            .field("productType.id")
                            .query(id)
                            .build()).build();
            productTypeQueries.add(q);
        });
        boolQueryBuilder.must(new Query.Builder()
                .bool(new BoolQuery.Builder().should(productTypeQueries)
                        .build())
                .build());
    }
}
