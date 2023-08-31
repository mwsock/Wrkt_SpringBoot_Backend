package pl.coderslab.wrkt_springboot_backend.exercise;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserDTO;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExerciseServiceTest {

    @InjectMocks
    private ExerciseService exerciseService;
    @Mock
    private ExerciseRepository exerciseRepository;
    @Mock UserRepository userRepository;
    @Mock ExerciseMapper exerciseMapper;
    @Mock InMemorySessionRegistry registry;

    @Test
    void should_return_exercise_list_for_user_with_sessionId_in_registry() {
        //given
        String sessionId = "1234567890";
        User user = User.builder()
                .id(1L).name("Test User")
                .password(UUID.randomUUID().toString())
                .build();
        Exercise exerciseOne =  Exercise.builder()
                .id(1L)
                .name("Test Exercise")
                .user(User.builder().name("Test User").build())
                .deleted(false)
                .build();
        Exercise exerciseTwo = Exercise.builder()
                .id(2L)
                .name("Another Test Exercise")
                .user(User.builder().name("Test User").build())
                .deleted(true)
                .build();
        Exercise exerciseThree = Exercise.builder()
                .id(3L)
                .name("Third Test Exercise")
                .user(User.builder().name("Test User").build())
                .deleted(false)
                .build();
        List<Exercise> exercises = Arrays.asList(exerciseOne,exerciseTwo,exerciseThree);

        ExerciseDTO exerciseDTOOne = ExerciseDTO.builder()
                .id(1L)
                .name("Test Exercise")
                .build();
        ExerciseDTO exerciseDTOThree = ExerciseDTO.builder()
                .id(3L)
                .name("Third Test Exercise")
                .build();
        List<ExerciseDTO> exerciseDTOList = Arrays.asList(exerciseDTOOne,exerciseDTOThree);

        when(registry.getUserNameForSession(sessionId)).thenReturn("Test User");
        when(userRepository.findByName(registry.getUserNameForSession(sessionId))).thenReturn(user);
        when(exerciseRepository.findByUser(user)).thenReturn(exercises);
        when(exerciseMapper.mapToExerciseDTO(exerciseOne)).thenReturn(exerciseDTOOne);
        when(exerciseMapper.mapToExerciseDTO(exerciseThree)).thenReturn(exerciseDTOThree);
        //when
        List<ExerciseDTO> resultExerciseList = exerciseService.getExercises();
        //then
        assertEquals(resultExerciseList,exerciseDTOList);
    }

    @Test
    void should_return_name_of_newly_added_exercise() {
        //given
        ExerciseDTO exerciseDTO = ExerciseDTO.builder()
                .name("Test Exercise")
                .userDTO(UserDTO.builder().name("Test User").build())
                .build();

        Exercise exercise = Exercise.builder()
                .name("Test Exercise")
                .user(User.builder().name("Test User").build())
                .build();

        when(userRepository.findByName("Test User")).thenReturn(User.builder()
                        .id(1L).name("Test User")
                        .password(UUID.randomUUID().toString())
                        .build());
        when(exerciseRepository.save(exercise)).thenReturn(exercise);
        when(exerciseMapper.mapToExercise(exerciseDTO)).thenReturn(exercise);
        //when
        String exerciseName = exerciseService.addExercise(exerciseDTO);
        //then
        assertEquals("New Exercise: " + exerciseDTO.getName(),exerciseName);
    }

}