package akletini.life.core.todo.dto.mapper;

import akletini.life.core.todo.dto.TodoDto;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.user.dto.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static akletini.life.core.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.core.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = {UserMapper.class,
        TagMapper.class})
public interface TodoMapper {

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DATE_FORMAT)
    Todo dtoToTodo(TodoDto todoDto);

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DATE_FORMAT)
    TodoDto todoToDto(Todo todo);

}
