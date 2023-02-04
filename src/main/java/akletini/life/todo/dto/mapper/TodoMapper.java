package akletini.life.todo.dto.mapper;

import akletini.life.todo.dto.TagDto;
import akletini.life.todo.dto.TodoDto;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.user.dto.UserDto;
import akletini.life.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {
    Todo dtoToTodo(TodoDto todoDto);

    TodoDto todoToDto(Todo todo);

    Tag dtoToTag(TagDto tagDto);

    TagDto tagToDto(Tag tag);

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);
}
