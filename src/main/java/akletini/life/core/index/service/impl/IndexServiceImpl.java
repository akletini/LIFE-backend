package akletini.life.core.index.service.impl;

import akletini.life.core.index.Indexes;
import akletini.life.core.index.service.IndexService;
import akletini.life.core.product.repository.api.attributeType.AttributeTypeIndexRepository;
import akletini.life.core.product.repository.api.product.ProductIndexRepository;
import akletini.life.core.product.repository.api.productType.ProductTypeIndexRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.product.service.AttributeTypeService;
import akletini.life.core.product.service.ProductService;
import akletini.life.core.product.service.ProductTypeService;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class IndexServiceImpl implements IndexService {

    private ProductIndexRepository productIndexRepository;
    private AttributeTypeIndexRepository attributeTypeIndexRepository;
    private ProductTypeIndexRepository productTypeIndexRepository;
    private AttributeTypeService attributeTypeService;
    private ProductService productService;

    private ProductTypeService productTypeService;

    @Override
    public void reIndexAllIndexes() throws IOException {
        for (Indexes value : Indexes.values()) {
            log.info("Reindexing index " + value.getIndexName());
            reIndexSingleIndex(value.getIndexName());
        }
    }

    @Override
    public void reIndexSingleIndex(String indexName) throws IOException {

        if (Indexes.PRODUCTS.getIndexName().equals(indexName)) {
            List<Product> all = productService.getAll();
            productIndexRepository.deleteAll();
            productIndexRepository.saveAll(all);
        } else if (Indexes.PRODUCT_TYPES.getIndexName().equals(indexName)) {
            List<ProductType> all = productTypeService.getAll();
            productTypeIndexRepository.deleteAll();
            productTypeIndexRepository.saveAll(all);
        } else if (Indexes.ATTRIBUTE_TYPES.getIndexName().equals(indexName)) {
            List<AttributeType> all = attributeTypeService.getAll();
            attributeTypeIndexRepository.deleteAll();
            attributeTypeIndexRepository.saveAll(all);
        } else {
            throw new IOException("No such index: " + indexName);
        }
        log.info("Successfully indexed index: " + indexName);
    }


}
