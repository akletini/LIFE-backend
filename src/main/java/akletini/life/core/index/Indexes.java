package akletini.life.core.index;

public enum Indexes {

    ATTRIBUTE_TYPES("attribute_types"),
    PRODUCTS("products"),
    PRODUCT_TYPES("product_types");

    private final String indexName;

    Indexes(String indexName) {
        this.indexName = indexName;
    }

    public final String getIndexName() {
        return indexName;
    }
}
