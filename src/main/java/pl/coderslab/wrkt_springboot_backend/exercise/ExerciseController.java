package pl.coderslab.wrkt_springboot_backend.exercise;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

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
    public List<ExerciseDTO> getExercises(HttpServletRequest request){
        return exerciseService.getExercises(request);
    }

    @PostMapping("/add")
    public String addExercise(@Valid @RequestBody ExerciseDTO exerciseDTO){
        log.info("New Exercise: " + exerciseDTO.toString());
        return exerciseService.addExercise(exerciseDTO);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeExcercise(@PathVariable Long id){
        log.info("Id ćwiczenia do usunięcia: " + id);
        exerciseService.removeExcercise(id);
        return ResponseEntity.ok("Exercise Deleted!");
    }




}
