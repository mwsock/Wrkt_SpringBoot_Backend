package pl.coderslab.wrkt_springboot_backend.plan;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseDTO;

import java.util.List;

@RestController
@RequestMapping("/plan")
@Slf4j
public class PlanController {

    private final PlanService planService;

    @Autowired
    public PlanController(PlanService planService) {
        this.planService = planService;
    }

    @Operation(summary = "Find plans for user", description = "Returns a list of plans defined by user",
            tags = { "exercise" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = PlanDTO.class)))})
    @GetMapping
    public List<PlanDTO> getPlans(@RequestHeader(value="sessionId") String sessionId,HttpServletRequest request){
        return planService.getPlans(request.getHeader("sessionId"));
    }

    @Operation(summary = "Add plan for specific user", description = "Adds a new plan defined by user",
            tags = { "plan" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @PostMapping("/add")
    public ResponseEntity<String> addPlan(@Valid @RequestBody PlanDTO planDTO){
        log.info("New plan: " + planDTO.toString());
        planService.addPlan(planDTO);
        return ResponseEntity.ok("Plan added!");
    }

    @Operation(summary = "Delete plan for specific user", description = "Deletes specific plan by setting 'DELETED' flag",
            tags = { "plan" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> removePlan(@PathVariable Long id){
        log.info("Id planu do usunięcia: " + id);
        planService.removePlan(id);
        return ResponseEntity.ok("Plan deleted!");
    }

}
