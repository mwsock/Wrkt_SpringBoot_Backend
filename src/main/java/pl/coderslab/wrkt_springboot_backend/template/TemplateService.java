package pl.coderslab.wrkt_springboot_backend.template;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseDTO;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseMapper;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.plan.Plan;
import pl.coderslab.wrkt_springboot_backend.plan.PlanRepository;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;
import pl.coderslab.wrkt_springboot_backend.workout.Workout;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TemplateService {

    private final TemplateRepository templateRepository;
    private final UserRepository userRepository;
    private final ExerciseRepository exerciseRepository;
    private final PlanRepository planRepository;
    private final TemplateMapper templateMapper;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final ExerciseMapper exerciseMapper;

    @Autowired
    public TemplateService(TemplateRepository templateRepository, UserRepository userRepository, ExerciseRepository exerciseRepository, PlanRepository planRepository, TemplateMapper templateMapper, TemplateExerciseRepository templateExerciseRepository, ExerciseMapper exerciseMapper) {
        this.templateRepository = templateRepository;
        this.userRepository = userRepository;
        this.exerciseRepository = exerciseRepository;
        this.planRepository = planRepository;
        this.templateMapper = templateMapper;
        this.templateExerciseRepository = templateExerciseRepository;
        this.exerciseMapper = exerciseMapper;
    }

    public List<TemplateDTO> getTemplateByPlanId(Long planId){
        return templateRepository.findByPlanId(planId)
                .stream()
                .map(templateMapper::mapToTemplateDTO)
                .collect(Collectors.toList());
    }

    public List<ExerciseDTO> getExercisesForTemplateByPlanIdAndDay(Long planId, Integer day){
        return templateRepository.findByPlanIdAndDay(planId,day).getExercises()
                .stream()
                .map(exerciseMapper::mapToExerciseDTO)
                .collect(Collectors.toList());
    }

    public void addTemplate(TemplateDTO templateDTO){
        Template template = templateMapper.mapToTemplate(templateDTO);
        Plan plan = planRepository.findById(template.getPlan().getId()).orElse(null);
        List<Exercise> exerciseList = template.getExercises().stream()
                .map(exercise -> exerciseRepository.findById(exercise.getId()).orElse(null))
                .collect(Collectors.toList());
        User user = userRepository.findByName(template.getUser().getName());

        template.setPlan(plan);
        template.setExercises(exerciseList);
        template.setUser(user);

        log.info("New template: " + templateRepository.save(template));
    }

    public List<TemplateExercise> getByTemplateExerciseList(Workout workout) {
        return templateExerciseRepository.findByTemplateId(workout.getTemplateExercise().getTemplateId());
    }
}
