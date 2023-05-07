package pl.coderslab.wrkt_springboot_backend.exercise;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final InMemorySessionRegistry registry;
    private final ExerciseMapper exerciseMapper;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserRepository userRepository, InMemorySessionRegistry registry, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.registry = registry;
        this.exerciseMapper = exerciseMapper;
    }

    public List<ExerciseDTO> getExercises(HttpServletRequest request){
        String userName = registry.getUserNameForSession(request.getHeader("Authorization"));
        User user = userRepository.findByName(userName);
        return exerciseRepository.findByUser(user).stream()
                .filter(exercise -> !exercise.isDeleted())
                .map(exerciseMapper::mapToExerciseDTO)
                .collect(Collectors.toList());
    }

    public String addExercise(ExerciseDTO exerciseDTO){
        Exercise exercise = exerciseMapper.mapToExercise(exerciseDTO);
        User user = userRepository.findByName(exercise.getUser().getName());
        exercise.setUser(user);
        userRepository.save(exercise.getUser());
        return "New Exercise: " + exerciseRepository.save(exercise).getName();
    }

    public void removeExcercise(Long id){
        Exercise exercise = exerciseRepository.findById(id).orElse(null);
        if(exercise != null){
            exercise.setDeleted(true);
            exerciseRepository.save(exercise);
            log.info("Usunięto: " + id);
        }
    }
}
