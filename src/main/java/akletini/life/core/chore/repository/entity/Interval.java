package akletini.life.core.chore.repository.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@Embeddable
@NoArgsConstructor
public class Interval {

    private Integer value;
    @Enumerated(EnumType.STRING)
    private DateUnit unit;

    public enum DateUnit {
        DAYS("Days"),
        WEEKS("Weeks"),
        MONTHS("Months");

        private String value;

        DateUnit(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    @Override
    public String toString() {
        return value + " " + unit.getValue();
    }
}
