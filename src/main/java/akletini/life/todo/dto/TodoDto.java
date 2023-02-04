package akletini.life.todo.dto;

import akletini.life.todo.repository.entity.Todo;
import akletini.life.user.dto.UserDto;
import lombok.Data;

import java.io.File;

@Data
public class TodoDto {

    private Long id;
    private String title;
    private String createdAt;
    private String dueAt;
    private String description;
    private TagDto tag;
    private File attachedFile;
    private Todo.State state;
    private UserDto assignedUser;
}
