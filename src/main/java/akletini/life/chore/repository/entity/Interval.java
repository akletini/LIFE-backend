package akletini.life.chore.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Interval {
    
    private int value;
    private DateUnit unit;

    public enum DateUnit {
        DAYS,
        WEEKS,
        MONTHS,
    }
}
