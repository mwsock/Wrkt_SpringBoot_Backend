package pl.coderslab.wrkt_springboot_backend.template;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface TemplateExerciseMapper {

    TemplateExerciseDTO INSTANCE = Mappers.getMapper(TemplateExerciseDTO.class);

    TemplateExercise mapToTemplateExercise(TemplateExerciseDTO templateExerciseDTO);

}
