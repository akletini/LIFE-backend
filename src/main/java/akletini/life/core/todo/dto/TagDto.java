package akletini.life.core.todo.dto;

import akletini.life.core.shared.dto.NamedDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class TagDto extends NamedDto {

    private String color;
}
