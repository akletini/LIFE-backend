package akletini.life.core.chore.dto.mapper;

import akletini.life.core.chore.dto.ChoreDto;
import akletini.life.core.chore.repository.entity.Chore;
import akletini.life.core.shared.utils.DateUtils;
import akletini.life.core.user.dto.mapper.UserMapper;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, injectionStrategy =
        InjectionStrategy.CONSTRUCTOR, componentModel = "spring", uses = {UserMapper.class})
public interface ChoreMapper {

    @Mapping(target = "createdAt", dateFormat = DateUtils.DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DateUtils.DATE_FORMAT)
    @Mapping(target = "startDate", dateFormat = DateUtils.DATE_FORMAT)
    @Mapping(target = "lastCompleted", dateFormat = DateUtils.DATE_FORMAT)
    Chore dtoToChore(ChoreDto choreDto);

    @Mapping(target = "createdAt", dateFormat = DateUtils.DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DateUtils.DATE_FORMAT)
    @Mapping(target = "startDate", dateFormat = DateUtils.DATE_FORMAT)
    @Mapping(target = "lastCompleted", dateFormat = DateUtils.DATE_FORMAT)
    ChoreDto choreToDto(Chore chore);

}
