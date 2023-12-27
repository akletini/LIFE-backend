package akletini.life.core.index.service.impl;

import akletini.life.core.product.repository.api.productType.ProductTypeIndexRepository;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.shared.BaseEntity;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class ProductTypeIndexer {

    private final ProductTypeIndexRepository productTypeIndexRepository;

    public void indexByProductTypes(List<ProductType> productTypes) {
        productTypeIndexRepository.deleteAllById(productTypes.stream().map(BaseEntity::getId).toList());
        productTypeIndexRepository.saveAll(productTypes);
    }
}
