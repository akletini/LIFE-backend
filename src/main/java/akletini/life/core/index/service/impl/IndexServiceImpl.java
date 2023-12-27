package akletini.life.core.index.service.impl;

import akletini.life.core.index.Indexes;
import akletini.life.core.index.service.IndexService;
import akletini.life.core.product.repository.api.attributeType.AttributeTypeIndexRepository;
import akletini.life.core.product.repository.api.attributeType.AttributeTypeRepository;
import akletini.life.core.product.repository.api.product.ProductIndexRepository;
import akletini.life.core.product.repository.api.product.ProductRepository;
import akletini.life.core.product.repository.api.productType.ProductTypeIndexRepository;
import akletini.life.core.product.repository.api.productType.ProductTypeRepository;
import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.Product;
import akletini.life.core.product.repository.entity.ProductType;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
@AllArgsConstructor
@Log4j2
public class IndexServiceImpl implements IndexService {

    private ProductRepository productRepository;
    private ProductIndexRepository productIndexRepository;
    private AttributeTypeIndexRepository attributeTypeIndexRepository;
    private AttributeTypeRepository attributeTypeRepository;
    private ProductTypeIndexRepository productTypeIndexRepository;
    private ProductTypeRepository productTypeRepository;

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
            List<Product> all = productRepository.findAll();
            productIndexRepository.deleteAll();
            productIndexRepository.saveAll(all);
        } else if (Indexes.PRODUCT_TYPES.getIndexName().equals(indexName)) {
            List<ProductType> all = productTypeRepository.findAll();
            productTypeIndexRepository.deleteAll();
            productTypeIndexRepository.saveAll(all);
        } else if (Indexes.ATTRIBUTE_TYPES.getIndexName().equals(indexName)) {
            List<AttributeType> all = attributeTypeRepository.findAll();
            attributeTypeIndexRepository.deleteAll();
            attributeTypeIndexRepository.saveAll(all);
        } else {
            throw new IOException("No such index: " + indexName);
        }
    }


}
