package akletini.life.todo.dto.mapper;

import akletini.life.todo.dto.TagDto;
import akletini.life.todo.repository.entity.Tag;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring")
public interface TagMapper {

    Tag dtoToTag(TagDto tagDto);

    TagDto tagToDto(Tag tag);
}
