package akletini.life.core.shared.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = {"createdAt"})
public class BaseDto {
    protected Long id;

    protected LocalDateTime createdAt;
}
