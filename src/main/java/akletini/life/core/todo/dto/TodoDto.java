package akletini.life.core.todo.dto;

import akletini.life.core.shared.dto.BaseDto;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.user.dto.UserDto;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.File;
import java.time.LocalDate;

@Data
@EqualsAndHashCode(callSuper = true)
public class TodoDto extends BaseDto {

    private String title;
    private LocalDate dueAt;
    private String description;
    private TagDto tag;
    private File attachedFile;
    private Todo.State state;
    private UserDto assignedUser;
}
