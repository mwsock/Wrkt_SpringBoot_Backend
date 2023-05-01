package pl.coderslab.wrkt_springboot_backend.exercise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;

@RestController
@RequestMapping("/exercise")
@Slf4j
public class ExerciseController {

    private ExerciseRepository exerciseRepository;
    private UserRepository userRepository;

    @Autowired
    public ExerciseController(ExerciseRepository exerciseRepository, UserRepository userRepository) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
    }


    @GetMapping
    public List<Exercise> getExercises(){
        return exerciseRepository.findAll().stream()
                .filter(exercise -> !exercise.isDeleted())
                .toList();
    }

    @PostMapping("/add")
    public String addExercise(@RequestBody Exercise exercise){
        log.info("New Exercise: " + exercise.toString());
        User user = userRepository.findByName(exercise.getUser().getName());
        exercise.setUser(user);
        userRepository.save(exercise.getUser());
        return "New Exercise: " + exerciseRepository.save(exercise);
    }

    @DeleteMapping("/delete/{id}")
    public void removeExcercise(@PathVariable Long id){
        log.info("Id ćwiczenia do usunięcia: " + id);
        Exercise exercise = exerciseRepository.findById(id).orElse(null);
        if(exercise != null){
            exercise.setDeleted(true);
            exerciseRepository.save(exercise);
            log.info("Usunięto: " + id);
        }
    }


}
