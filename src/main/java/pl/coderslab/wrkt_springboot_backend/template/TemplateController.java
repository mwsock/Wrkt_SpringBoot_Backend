package pl.coderslab.wrkt_springboot_backend.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;

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

    @GetMapping("/{planId}")
    @ResponseBody
    public List<Template> getTemplateByPlanId(@PathVariable Long planId){
        return templateService.getTemplateByPlanId(planId);
    }

    @GetMapping("/{planId}/{day}")
    @ResponseBody
    public List<Exercise> getExercisesForTemplateByPlanIdAndDay(@PathVariable Long planId, @PathVariable Integer day){
        return templateService.getExercisesForTemplateByPlanIdAndDay(planId,day);
    }

    @PostMapping("/add")
    public void addTemplate(@RequestBody Template template){
        log.info("New template: " + template.toString());
        templateService.addTemplate(template);
    }
}
