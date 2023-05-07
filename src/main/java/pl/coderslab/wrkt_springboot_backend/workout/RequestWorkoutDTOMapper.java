package pl.coderslab.wrkt_springboot_backend.workout;

import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface RequestWorkoutDTOMapper {

    RequestWorkoutDTO INSTANCE = Mappers.getMapper(RequestWorkoutDTO.class);

    Workout mapToWorkout(RequestWorkoutDTO requestWorkoutDTO);

}
