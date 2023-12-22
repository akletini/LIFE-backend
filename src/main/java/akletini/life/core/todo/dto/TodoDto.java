package akletini.life.core.todo.dto;

import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.user.dto.UserDto;
import lombok.Data;

import java.io.File;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class TodoDto {

    private Long id;
    private String title;
    private LocalDateTime createdAt;
    private LocalDate dueAt;
    private String description;
    private TagDto tag;
    private File attachedFile;
    private Todo.State state;
    private UserDto assignedUser;
}
