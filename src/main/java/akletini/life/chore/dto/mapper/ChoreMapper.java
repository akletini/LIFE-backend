package akletini.life.chore.dto.mapper;

import akletini.life.chore.dto.ChoreDto;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.user.dto.UserDto;
import akletini.life.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

import static akletini.life.shared.utils.DateUtils.DATE_FORMAT;
import static akletini.life.shared.utils.DateUtils.DATE_TIME_FORMAT;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChoreMapper {

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DATE_FORMAT)
    @Mapping(target = "startDate", dateFormat = DATE_FORMAT)
    Chore dtoToChore(ChoreDto choreDto);

    @Mapping(target = "createdAt", dateFormat = DATE_TIME_FORMAT)
    @Mapping(target = "dueAt", dateFormat = DATE_FORMAT)
    @Mapping(target = "startDate", dateFormat = DATE_FORMAT)
    ChoreDto choreToDto(Chore chore);

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);
}
