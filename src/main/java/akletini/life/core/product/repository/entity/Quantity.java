package akletini.life.core.product.repository.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Quantity {
    private long quantityValue;
    @Enumerated(EnumType.STRING)
    private QuantityDescription quantityUnit;

    public enum QuantityDescription {
        PIECE,
        WEIGHT,
        VOLUME;

        @NotNull
        private String unit;
        private final Map<String, Double> quantityMapping = new HashMap<>();

        public void setUnit(String unit) {
            this.unit = unit;
            if (this.equals(PIECE)) {
                quantityMapping.put("Piece", 1.0);
            } else if (this.equals(WEIGHT)) {
                quantityMapping.put("mg", 0.001);
                quantityMapping.put("g", 1.0);
                quantityMapping.put("kg", 1000.0);
            } else if (this.equals(VOLUME)) {
                quantityMapping.put("ml", 0.001);
                quantityMapping.put("l", 1.0);
            }
        }

        public String getUnit() {
            return unit;
        }

        public Double getUnitFactor() {
            return quantityMapping.get(this.unit);
        }

    }

}
