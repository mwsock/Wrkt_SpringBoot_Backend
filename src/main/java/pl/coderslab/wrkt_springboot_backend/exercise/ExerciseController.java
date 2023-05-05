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

    private final ExerciseService exerciseService;

    @Autowired
    public ExerciseController(ExerciseService exerciseService) {
        this.exerciseService = exerciseService;
    }


    @GetMapping
    public List<Exercise> getExercises(){
        return exerciseService.getExercises();
    }

    @PostMapping("/add")
    public String addExercise(@RequestBody Exercise exercise){
        log.info("New Exercise: " + exercise.toString());
        return exerciseService.addExercise(exercise);
    }

    @DeleteMapping("/delete/{id}")
    public void removeExcercise(@PathVariable Long id){
        log.info("Id ćwiczenia do usunięcia: " + id);
        exerciseService.removeExcercise(id);
    }


}
