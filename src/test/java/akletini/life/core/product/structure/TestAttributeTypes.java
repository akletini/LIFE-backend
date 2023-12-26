package akletini.life.core.product.structure;

import akletini.life.core.product.repository.entity.AttributeType;
import akletini.life.core.product.repository.entity.BasicType;
import org.springframework.lang.NonNull;

import java.time.LocalDateTime;

public class TestAttributeTypes {
    private TestAttributeTypes() {

    }

    @NonNull
    public static AttributeType getPriceAttributeType() {
        AttributeType priceAttributeType = new AttributeType();
        priceAttributeType.setName("Price");
        priceAttributeType.setBasicType(BasicType.Number);
        priceAttributeType.setRequired(false);
        priceAttributeType.setCreatedAt(LocalDateTime.now());
        return priceAttributeType;
    }

    @NonNull
    public static AttributeType getBoughtOnAttributeType() {
        AttributeType boughtOnAttributeType = new AttributeType();
        boughtOnAttributeType.setName("Bought on");
        boughtOnAttributeType.setBasicType(BasicType.Date);
        boughtOnAttributeType.setRequired(false);
        boughtOnAttributeType.setCreatedAt(LocalDateTime.now());
        return boughtOnAttributeType;
    }

    @NonNull
    public static AttributeType getBoughtFromAttributeType() {
        AttributeType boughtFromAttributeType = new AttributeType();
        boughtFromAttributeType.setName("Bought from");
        boughtFromAttributeType.setBasicType(BasicType.String);
        boughtFromAttributeType.setRequired(false);
        boughtFromAttributeType.setCreatedAt(LocalDateTime.now());
        return boughtFromAttributeType;
    }
}
