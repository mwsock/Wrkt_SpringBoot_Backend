package pl.coderslab.wrkt_springboot_backend.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;
import org.springframework.stereotype.Component;

@Mapper(componentModel = "spring")
public interface UserMapper {

    UserMapper INSTANCE = Mappers.getMapper(UserMapper.class);
    @Mapping(target = "password", ignore = true)
    UserDTO mapToUserDTO(User user);
    User mapToUser(UserDTO userDTO);

}
