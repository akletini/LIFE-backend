package akletini.life.user.dto.mapper;

import akletini.life.user.dto.UserDto;
import akletini.life.user.repository.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface UserMapper {

    User dtoToUser(UserDto userDto);

    UserDto userToDto(User user);
}
