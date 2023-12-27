package akletini.life.core.chore.dto.mapper;

import akletini.life.core.chore.dto.ChoreDto;
import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.user.dto.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = {UserMapper.class})
public interface ChoreMapper {

    Chore dtoToChore(ChoreDto choreDto);

    ChoreDto choreToDto(Chore chore);

}
