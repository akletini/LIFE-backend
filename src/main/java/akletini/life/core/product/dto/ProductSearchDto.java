package akletini.life.core.product.dto;

import lombok.Builder;

@Builder
public class ProductSearchDto {
    private Long productTypeId;
    private boolean searchProductTypeRecursive;
    private String searchTerm;
}
