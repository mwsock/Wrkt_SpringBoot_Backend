package pl.coderslab.wrkt_springboot_backend.workout;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;

@RestController
@RequestMapping("/workout")
@Slf4j
public class WorkoutController {
    private final WorkoutService workoutService;

    @Autowired
    public WorkoutController(WorkoutService workoutService) {
        this.workoutService = workoutService;
    }

    @GetMapping
    @ResponseBody
    public Set<ResponseWorkoutDTO> getUserWorkouts(HttpServletRequest request){
        return workoutService.getUserWorkouts(request);
    }

    @GetMapping("/last")
    @ResponseBody
    public Set<ResponseWorkoutDTO> getLastUserWorkout(HttpServletRequest request){
        return workoutService.getLastUserWorkout(request);
    }

    @GetMapping("/{planId}/{createDate}")
    @ResponseBody
    public Set<ResponseWorkoutDTO> getUserWorkoutByPlanIdAndCreateDate(@PathVariable Long planId, @PathVariable LocalDate createDate){
        return workoutService.getUserWorkoutByPlanIdAndCreateDate(planId,createDate);
    }

    @PostMapping("/add")
    public void addWorkout(@RequestBody Workout[] workout){
        log.info("New workout: " + Arrays.toString(workout));
        workoutService.addWorkout(workout);
    }

    @PutMapping
    @ResponseBody
    public void updateWorkout(@RequestBody RequestWorkoutDTO[] requestWorkoutDTO){
        log.info("Trening do aktualizacji: " + Arrays.toString(requestWorkoutDTO));
        workoutService.updateWorkout(requestWorkoutDTO);
    }


    @DeleteMapping("/{templateId}/{createDate}")
    @ResponseBody
    public void deleteWorkout(@PathVariable Long templateId, @PathVariable LocalDate createDate){
        workoutService.deleteWorkout(templateId,createDate);
    }
}
