package akletini.life.chore.repository.entity;

import jakarta.persistence.Embeddable;
import lombok.Data;

@Data
@Embeddable
public class Interval {
    
    private int value;
    private DateUnit unit;

    public enum DateUnit {
        DAYS,
        WEEKS,
        MONTHS,
    }
}
