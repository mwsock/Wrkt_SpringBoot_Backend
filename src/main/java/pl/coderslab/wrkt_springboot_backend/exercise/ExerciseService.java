package pl.coderslab.wrkt_springboot_backend.exercise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;

@Service
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;

    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }

    public List<Exercise> getExercises(){
        return exerciseRepository.findAll().stream()
                .filter(exercise -> !exercise.isDeleted())
                .toList();
    }

    public String addExercise(Exercise exercise){
        User user = userRepository.findByName(exercise.getUser().getName());
        exercise.setUser(user);
        userRepository.save(exercise.getUser());
        return "New Exercise: " + exerciseRepository.save(exercise);
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
