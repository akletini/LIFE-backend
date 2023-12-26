package akletini.life.core.product.repository.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Quantity {
    private Long quantityValue;
    @Enumerated(EnumType.STRING)
    private QuantityDescription quantityUnit;

    private String quantityUnitKey;

    public static List<String> getAllowedValuesForUnit(QuantityDescription unit) {
        if (QuantityDescription.PIECE.equals(unit)) {
            return List.of("Piece");
        } else if (QuantityDescription.VOLUME.equals(unit)) {
            return List.of("ml", "l");
        } else if (QuantityDescription.WEIGHT.equals(unit)) {
            return List.of("mg", "g", "kg");
        }
        return new ArrayList<>();
    }

    public enum QuantityDescription {
        PIECE,
        WEIGHT,
        VOLUME;

    }

}
