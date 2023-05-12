package pl.coderslab.wrkt_springboot_backend.exercise;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface ExerciseMapper {

    ExerciseMapper INSTANCE = Mappers.getMapper(ExerciseMapper.class);

    @Mapping(target = "userDTO", ignore = true)
    ExerciseDTO mapToExerciseDTO(Exercise exercise);

    @Mapping(source="userDTO.name",target = "user.name")
    Exercise mapToExercise(ExerciseDTO exerciseDTO);

}
