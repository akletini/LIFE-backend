package akletini.life.core.user.dto.mapper;

import akletini.life.core.user.dto.UserDto;
import akletini.life.core.user.repository.entity.Token;
import akletini.life.core.user.repository.entity.User;
import org.mapstruct.*;

import java.util.List;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface UserMapper {

    @Mapping(target = "role", ignore = true)
    User dtoToUser(UserDto userDto);

    @Mapping(source = "tokens", target = "jwtToken", qualifiedByName = "getToken")
    @Mapping(target = "password", ignore = true)
    UserDto userToDto(User user);

    @Named("getToken")
    static String getTokenFromList(List<Token> tokens) {
        return tokens == null || tokens.isEmpty() ? null : tokens.get(0).getToken();
    }
}
