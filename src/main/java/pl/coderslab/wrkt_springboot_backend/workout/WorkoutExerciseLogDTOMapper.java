package pl.coderslab.wrkt_springboot_backend.workout;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface WorkoutExerciseLogDTOMapper {

    WorkoutExerciseLogDTOMapper INSTANCE = Mappers.getMapper(WorkoutExerciseLogDTOMapper.class);

    WorkoutExerciseLogDTO mapToWorkoutExerciseLogDTO(Workout workout);


}
