package pl.coderslab.wrkt_springboot_backend.template;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TemplateMapper {

    TemplateMapper INSTANCE = Mappers.getMapper(TemplateMapper.class);
    @Mapping(source="user.name",target ="userDTO.name")
    @Mapping(source = "plan.id", target = "planDTO.id")
    @Mapping(source = "plan.name", target = "planDTO.name")
    TemplateDTO mapToTemplateDTO(Template template);
    @Mapping(source="userDTO.name",target = "user.name")
    @Mapping(source = "planDTO.id", target = "plan.id")
    @Mapping(source = "planDTO.name", target = "plan.name")
    Template mapToTemplate(TemplateDTO templateDTO);

}
