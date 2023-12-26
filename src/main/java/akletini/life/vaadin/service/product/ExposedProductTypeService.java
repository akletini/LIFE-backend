package akletini.life.vaadin.service.product;

import akletini.life.core.product.dto.ProductTypeDto;
import akletini.life.core.product.dto.mapper.ProductTypeMapper;
import akletini.life.core.product.repository.entity.ProductType;
import akletini.life.core.product.service.ProductTypeService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class ExposedProductTypeService {
    private final ProductTypeMapper productTypeMapper;
    private final ProductTypeService productTypeService;

    public List<ProductTypeDto> constructHierarchy() {
        Map<ProductType, List<ProductType>> productTypeListMap =
                productTypeService.constructHierarchy();
        HashMap<ProductTypeDto, List<ProductTypeDto>> mappedHierarchyMap = new HashMap<>();
        productTypeListMap.forEach((productType, productTypes) -> {
            mappedHierarchyMap.put(productTypeMapper.productTypeToDto(productType),
                    productTypes.stream().map(productTypeMapper::productTypeToDto).collect(Collectors.toList()));
        });
        mappedHierarchyMap.forEach(ProductTypeDto::setChildProductTypes);
        return new ArrayList<>(mappedHierarchyMap.keySet());
    }
}
