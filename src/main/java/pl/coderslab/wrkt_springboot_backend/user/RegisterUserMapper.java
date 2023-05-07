package pl.coderslab.wrkt_springboot_backend.user;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RegisterUserMapper {

    RegisterUserMapper INSTANCE = Mappers.getMapper(RegisterUserMapper.class);
    RegisterUserDTO mapToRegisterUserDTO(User user);
    User mapToUser(RegisterUserDTO userDTO);
}
