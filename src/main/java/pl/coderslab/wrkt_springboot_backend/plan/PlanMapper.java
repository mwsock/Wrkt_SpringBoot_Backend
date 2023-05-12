package pl.coderslab.wrkt_springboot_backend.plan;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface PlanMapper {

    PlanMapper INSTANCE = Mappers.getMapper(PlanMapper.class);
    @Mapping(target = "userDTO", ignore = true)
    PlanDTO mapToPlanDTO(Plan plan);
    @Mapping(source="userDTO.name",target = "user.name")
    Plan mapToPlan(PlanDTO planDTO);
}
