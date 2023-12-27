package akletini.life.core.todo.dto.mapper;

import akletini.life.core.todo.dto.TodoDto;
import akletini.life.core.todo.repository.entity.Todo;
import akletini.life.core.user.dto.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = {UserMapper.class,
        TagMapper.class})
public interface TodoMapper {


    Todo dtoToTodo(TodoDto todoDto);


    TodoDto todoToDto(Todo todo);

}
