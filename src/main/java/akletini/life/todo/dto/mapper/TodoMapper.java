package akletini.life.todo.dto.mapper;

import akletini.life.todo.dto.TagDto;
import akletini.life.todo.dto.TodoDto;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import akletini.life.user.dto.UserDto;
import akletini.life.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static akletini.life.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DATE_FORMAT)
    Todo dtoToTodo(TodoDto todoDto);

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DATE_FORMAT)
    TodoDto todoToDto(Todo todo);

    Tag dtoToTag(TagDto tagDto);

    TagDto tagToDto(Tag tag);

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);
}
