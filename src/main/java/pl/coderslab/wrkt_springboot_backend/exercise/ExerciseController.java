package pl.coderslab.wrkt_springboot_backend.exercise;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Find sample exercises for user", description = "Returns a list of sample exercises saved for user",
            tags = { "exercise" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExerciseDTO.class)))})
    @GetMapping("/samples")
    public List<ExerciseDTO> getSampleExercises(@RequestHeader(value="sessionId") String sessionId,HttpServletRequest request){
        return exerciseService.getSampleExercises(request.getHeader("sessionId"));
    }

    @Operation(summary = "Find exercises for user", description = "Returns a list of exercises defined by user",
            tags = { "exercise" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExerciseDTO.class)))})
    @GetMapping
    public List<ExerciseDTO> getExercises(@RequestHeader(value="sessionId") String sessionId,HttpServletRequest request){
        return exerciseService.getExercises(request.getHeader("sessionId"));
    }

    @Operation(summary = "Add exercise for specific user", description = "Adds a new exercise defined by user",
            tags = { "exercise" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @PostMapping("/add")
    public ResponseEntity<String> addExercise(@Valid @RequestBody ExerciseDTO exerciseDTO){
        log.info("New Exercise: " + exerciseDTO.toString());
        exerciseService.addExercise(exerciseDTO);
        return ResponseEntity.ok("Exercise added!");
    }

    @Operation(summary = "Delete exercise for specific user", description = "Deletes specific exercise by setting 'DELETED' flag",
            tags = { "exercise" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removeExcercise(@PathVariable Long id){
        log.info("Id ćwiczenia do usunięcia: " + id);
        exerciseService.removeExcercise(id);
        return ResponseEntity.ok("Exercise Deleted!");
    }




}
