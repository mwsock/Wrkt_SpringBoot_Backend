package pl.coderslab.wrkt_springboot_backend.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.plan.Plan;
import pl.coderslab.wrkt_springboot_backend.plan.PlanRepository;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/template")
@Slf4j
public class TemplateController {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final PlanRepository planRepository;


    @Autowired
    public TemplateController(TemplateRepository templateRepository, UserRepository userRepository, ExerciseRepository exerciseRepository, PlanRepository planRepository) {
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.planRepository = planRepository;
    }

    @GetMapping("/{planId}")
    @ResponseBody
    public List<Template> getTemplateByPlanId(@PathVariable Long planId){
        return templateRepository.findByPlanId(planId);
    }

    @GetMapping("/{planId}/{day}")
    @ResponseBody
    public List<Exercise> getExercicesForTemplateByPlanIdAndDay(@PathVariable Long planId, @PathVariable Integer day){
        return templateRepository.findByPlanIdAndDay(planId,day).getExercises();
    }

    @PostMapping("/add")
    public void addTemplate(@RequestBody Template template){
        log.info("New template: " + template.toString());

        Plan plan = planRepository.findById(template.getPlan().getId()).get();
        List<Exercise> exerciseList = template.getExercises().stream()
                .map(exercise -> exerciseRepository.findById(exercise.getId()).get())
                .toList();
        User user = userRepository.findByName(template.getUser().getName());

        template.setPlan(plan);
        template.setExercises(exerciseList);
        template.setUser(user);

        log.info("New template: " + templateRepository.save(template));
    }
}
