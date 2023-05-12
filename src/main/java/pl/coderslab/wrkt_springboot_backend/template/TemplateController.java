package pl.coderslab.wrkt_springboot_backend.template;

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
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseDTO;

import java.util.List;

@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController {
    private final TemplateService templateService;

    @Autowired
    public TemplateController(TemplateService templateService) {
        this.templateService = templateService;
    }

    @Operation(summary = "Find templates for specific plan", description = "Returns a list of templates defined by user for specific plan",
            tags = { "template","plan" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExerciseDTO.class)))})
    @GetMapping("/{planId}")
    @ResponseBody
    public List<TemplateDTO> getTemplateByPlanId(@PathVariable Long planId){
        return templateService.getTemplateByPlanId(planId);
    }

    @Operation(summary = "Find exercises for specific template", description = "Returns a list of exercises defined by user for specific template",
            tags = { "exercise","template" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ExerciseDTO.class)))})
    @GetMapping("/{planId}/{day}")
    @ResponseBody
    public List<ExerciseDTO> getExercisesForTemplateByPlanIdAndDay(@PathVariable Long planId, @PathVariable Integer day){
        return templateService.getExercisesForTemplateByPlanIdAndDay(planId,day);
    }

    @Operation(summary = "Add template for specific user", description = "Adds a new template defined by user",
            tags = { "template" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @PostMapping("/add")
    public ResponseEntity<String> addTemplate(@Valid @RequestBody TemplateDTO templateDTO){
        log.info("New template: " + templateDTO.toString());
        templateService.addTemplate(templateDTO);
        return ResponseEntity.ok("Template added!");
    }
}
