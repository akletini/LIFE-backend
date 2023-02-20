package akletini.life.chore.dto.mapper;

import akletini.life.chore.dto.ChoreDto;
import akletini.life.chore.repository.entity.Chore;
import akletini.life.user.dto.UserDto;
import akletini.life.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ChoreMapper {

    Chore dtoToChore(ChoreDto choreDto);

    ChoreDto choreToDto(Chore chore);

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);
}
