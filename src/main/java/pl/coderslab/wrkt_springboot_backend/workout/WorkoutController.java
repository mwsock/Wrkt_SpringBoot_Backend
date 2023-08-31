package pl.coderslab.wrkt_springboot_backend.workout;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseDTO;

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

    @Operation(summary = "Find workouts for user", description = "Returns a list of workouts (without exercises) saved by user",
            tags = { "workout" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseWorkoutDTO.class)))})
    @GetMapping
    @ResponseBody
    public Set<ResponseWorkoutDTO> getUserWorkouts(){
        return workoutService.getUserWorkouts();
    }

    @Operation(summary = "Find last workout for user", description = "Returns last workout saved by user",
            tags = { "workout" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseWorkoutDTO.class)))})
    @GetMapping("/last")
    @ResponseBody
    public Set<ResponseWorkoutDTO> getLastUserWorkout(){
        return workoutService.getLastUserWorkout();
    }

    @Operation(summary = "Find workout for user for specific plan and day", description = "Returns a workout saved by user for specific plan and day",
            tags = { "workout" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseWorkoutDTO.class)))})
    @GetMapping("/{planId}/{createDate}")
    @ResponseBody
    public Set<ResponseWorkoutDTO> getUserWorkoutByPlanIdAndCreateDate(@PathVariable Long planId, @PathVariable LocalDate createDate){
        return workoutService.getUserWorkoutByPlanIdAndCreateDate(planId,createDate);
    }

    @Operation(summary = "Add workout for specific user", description = "Adds a new workout performed by user",
            tags = { "workout" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @PostMapping("/add")
    public ResponseEntity<String> addWorkout(@Valid @RequestBody RequestWorkoutDTO[] requestWorkoutDTO){
        log.info("New workout: " + Arrays.toString(requestWorkoutDTO));
        workoutService.addWorkout(requestWorkoutDTO);
        return ResponseEntity.ok("Workout added!");
    }

    @Operation(summary = "Update workout for specific user", description = "Updates a workout performed by user with added changes",
            tags = { "workout" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @PutMapping
    @ResponseBody
    public ResponseEntity<String> updateWorkout(@RequestBody RequestWorkoutDTO[] requestWorkoutDTO){
        log.info("Trening do aktualizacji: " + Arrays.toString(requestWorkoutDTO));
        workoutService.updateWorkout(requestWorkoutDTO);
        return ResponseEntity.ok("Workout updated!");
    }

    @Operation(summary = "Delete workout for specific user", description = "Deletes specific workout for user",
            tags = { "workout" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @DeleteMapping("/{templateId}/{createDate}")
    @ResponseBody
    public ResponseEntity<String> deleteWorkout(@PathVariable Long templateId, @PathVariable LocalDate createDate){
        workoutService.deleteWorkout(templateId,createDate);
        return ResponseEntity.ok("Workout deleted!");
    }
}
