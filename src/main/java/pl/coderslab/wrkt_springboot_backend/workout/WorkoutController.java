package pl.coderslab.wrkt_springboot_backend.workout;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.annotation.RequestScope;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.plan.PlanRepository;
import pl.coderslab.wrkt_springboot_backend.template.Template;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExercise;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.template.TemplateRepository;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/workout")
@Slf4j
public class WorkoutController {

    private final WorkoutRepository workoutRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final UserRepository userRepository;
    private final PlanRepository planRepository;

    private final TemplateRepository templateRepository;

    private final ExerciseRepository exerciseRepository;

    @Autowired
    public WorkoutController(WorkoutRepository workoutRepository, TemplateExerciseRepository templateExerciseRepository, UserRepository userRepository, PlanRepository planRepository, TemplateRepository templateRepository, ExerciseRepository exerciseRepository) {
        this.workoutRepository = workoutRepository;
        this.templateExerciseRepository = templateExerciseRepository;
        this.userRepository = userRepository;
        this.planRepository = planRepository;
        this.templateRepository = templateRepository;
        this.exerciseRepository = exerciseRepository;
    }

    @GetMapping("/{userName}")
    @ResponseBody
    public Set<WorkoutDTO> getUserWorkouts(@PathVariable String userName){
        User user = userRepository.findByName(userName);
        return workoutRepository.findByUser(user).stream()
                .map(log -> WorkoutDTO.builder()
                        .planId(templateRepository.findById(log.getTemplateExercise().getTemplateId()).get().getPlan().getId())
                        .planName(templateRepository.findById(log.getTemplateExercise().getTemplateId()).get().getPlan().getName())
                        .day(templateRepository.findById(log.getTemplateExercise().getTemplateId()).get().getDay())
                        .createDate(log.getCreateDate()).build())
                .collect(Collectors.toSet());
    }

    @GetMapping("/{userName}/last")
    @ResponseBody
    public Set<WorkoutDTO> getLastUserWorkout(@PathVariable String userName){
        User user = userRepository.findByName(userName);
        List<Workout> workout = workoutRepository.findByUser(user);
        LocalDate workoutCreatDate = workout.stream()
                .map(Workout::getCreateDate)
                .max(Comparator.naturalOrder()).orElse(null);
        List<TemplateExercise> templateExercises = workout.stream()
                .filter(log -> log.getCreateDate().equals(workoutCreatDate))
                .map(Workout::getTemplateExercise)
                .toList();
        return getWorkoutDTOS(workoutCreatDate,templateExercises);
    }

    @GetMapping("/{planId}/{createDate}")
    @ResponseBody
    public Set<WorkoutDTO> getUserWorkoutByPlanIdAndCreateDate(@PathVariable Long planId, @PathVariable LocalDate createDate){
        List<Long> templateIds = templateRepository.findByPlanId(planId).stream()
                .map(Template::getId)
                .toList();

        List<TemplateExercise> templateExercises = new ArrayList<>();
        templateIds.forEach(id -> templateExercises.addAll(templateExerciseRepository.findByTemplateId(id)));

        return getWorkoutDTOS(createDate, templateExercises);
    }


    private Set<WorkoutDTO> getWorkoutDTOS(LocalDate createDate, List<TemplateExercise> templateExercises) {
        List<Workout> workouts = new ArrayList<>();
        templateExercises.forEach(templateExercise -> workouts.addAll(workoutRepository.findWorkoutByCreateDateAndTemplateExercise(createDate,templateExercise)));

        List<WorkoutExerciseDTO> workoutExerciseDTOList = getWorkoutExerciseDTOList(createDate, templateExercises);


        return workouts.stream()
                .map(log -> WorkoutDTO.builder()
                        .planId(Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId()).orElse(null)).getPlan().getId())
                        .planName(Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId()).orElse(null)).getPlan().getName())
                        .day(Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId()).orElse(null)).getDay())
                        .createDate(log.getCreateDate())
                        .exercises(workoutExerciseDTOList)
                        .build())
                .collect(Collectors.toSet());
    }

    private List<WorkoutExerciseDTO> getWorkoutExerciseDTOList(LocalDate createDate, List<TemplateExercise> templateExercises) {
        List<WorkoutExerciseDTO> workoutExerciseDTOList = new ArrayList<>();
        templateExercises.forEach(element -> workoutExerciseDTOList.add(WorkoutExerciseDTO.builder()
                        .exerciseName(Objects.requireNonNull(exerciseRepository.findById(element.getExerciseId()).orElse(null)).getName())
                        .exerciseLogList(workoutRepository.findByTemplateExerciseIdAndCreateDate(element.getId(), createDate).stream()
                                .map(workout -> WorkoutExerciseLogDTO.builder()
                                        .numberOfRepetitions(workout.getNumberOfRepetitions())
                                        .workSet(workout.getWorkSet())
                                        .weight(workout.getWeight())
                                        .build())
                                .toList())
                        .build()));
        return workoutExerciseDTOList;
    }

    @PostMapping("/add")
    public void addWorkout(@RequestBody Workout[] workout){
        log.info("New workout: " + Arrays.toString(workout));
        for (Workout value : workout) {
            long templateId = value.getTemplateExercise().getTemplateId();
            long exerciseId = value.getTemplateExercise().getExerciseId();
            value.setTemplateExercise(templateExerciseRepository.findByTemplateIdAndExerciseId(templateId, exerciseId));
            value.setUser(userRepository.findByName(value.getUser().getName()));
        }
        workoutRepository.saveAll(Arrays.asList(workout));
    }

    @PutMapping
    @ResponseBody
    public void updateWorkout(@RequestBody WorkoutDTO workoutDTO){
        log.info("Trening do aktualizacji: " + workoutDTO);
    }


    @DeleteMapping("/{id}")
    @ResponseBody
    public void deleteWorkout(@PathVariable Long id){
        log.info("Szablon do usunięcia id: " + id);
        workoutRepository.deleteById(id);
        log.info("Usunięto szablon!");
    }
}
