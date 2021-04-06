package pl.utp.scrumban.mapper;

import org.mapstruct.Mapper;
import pl.utp.scrumban.dto.UserDto;
import pl.utp.scrumban.model.User;

@Mapper(componentModel = "spring")
public interface UserMapper {

    User mapToUser(UserDto userDto);

    UserDto mapToUserDto(User user);
}
