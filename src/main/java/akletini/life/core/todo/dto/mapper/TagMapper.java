package akletini.life.core.todo.dto.mapper;

import akletini.life.core.todo.dto.TagDto;
import akletini.life.core.todo.repository.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface TagMapper {

    Tag dtoToTag(TagDto tagDto);

    TagDto tagToDto(Tag tag);
}
