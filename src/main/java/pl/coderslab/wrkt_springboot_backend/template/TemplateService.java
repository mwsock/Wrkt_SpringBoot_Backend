package pl.coderslab.wrkt_springboot_backend.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.plan.Plan;
import pl.coderslab.wrkt_springboot_backend.plan.PlanRepository;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.List;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final PlanRepository planRepository;

    public TemplateService(TemplateRepository templateRepository, UserRepository userRepository, ExerciseRepository exerciseRepository, PlanRepository planRepository) {
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.planRepository = planRepository;
    }

    public List<Template> getTemplateByPlanId(Long planId){
        return templateRepository.findByPlanId(planId);
    }

    public List<Exercise> getExercisesForTemplateByPlanIdAndDay(Long planId, Integer day){
        return templateRepository.findByPlanIdAndDay(planId,day).getExercises();
    }

    public void addTemplate(Template template){
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
