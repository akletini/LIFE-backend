package akletini.life.todo.dto.mapper;

import akletini.life.todo.dto.TagDto;
import akletini.life.todo.dto.TodoDto;
import akletini.life.todo.repository.entity.Tag;
import akletini.life.todo.repository.entity.Todo;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface TodoMapper {
    Todo dtoToTodo(TodoDto todoDto);

    TodoDto todoToDto(Todo todo);

    Tag dtoToTag(TagDto tagDto);

    TagDto tagToDto(Tag tag);
}
