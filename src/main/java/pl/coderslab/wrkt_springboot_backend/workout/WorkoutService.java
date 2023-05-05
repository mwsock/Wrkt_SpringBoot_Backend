package pl.coderslab.wrkt_springboot_backend.workout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.template.Template;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExercise;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.template.TemplateRepository;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class WorkoutService {

    private final WorkoutRepository workoutRepository;
    private final TemplateExerciseRepository templateExerciseRepository;
    private final UserRepository userRepository;

    private final TemplateRepository templateRepository;

    private final ExerciseRepository exerciseRepository;

    private final InMemorySessionRegistry registry;

    public WorkoutService(WorkoutRepository workoutRepository,
                          TemplateExerciseRepository templateExerciseRepository,
                          UserRepository userRepository,
                          TemplateRepository templateRepository,
                          ExerciseRepository exerciseRepository,
                          InMemorySessionRegistry registry) {
        this.workoutRepository = workoutRepository;
        this.templateExerciseRepository = templateExerciseRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
        this.exerciseRepository = exerciseRepository;
        this.registry = registry;
    }

    public Set<ResponseWorkoutDTO> getUserWorkouts(HttpServletRequest request){
        String userName = registry.getUserNameForSession(request.getHeader("Authorization"));
        User user = userRepository.findByName(userName);
        return workoutRepository.findByUser(user).stream()
                .map(log -> ResponseWorkoutDTO.builder()
                        .templateId(log.getTemplateExercise().getTemplateId())
                        .planId(templateRepository.findById(log.getTemplateExercise().getTemplateId()).get().getPlan().getId())
                        .planName(templateRepository.findById(log.getTemplateExercise().getTemplateId()).get().getPlan().getName())
                        .day(templateRepository.findById(log.getTemplateExercise().getTemplateId()).get().getDay())
                        .createDate(log.getCreateDate()).build())
                .collect(Collectors.toSet());
    }

    public Set<ResponseWorkoutDTO> getLastUserWorkout(HttpServletRequest request){
        String userName = registry.getUserNameForSession(request.getHeader("Authorization"));
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

    public Set<ResponseWorkoutDTO> getUserWorkoutByPlanIdAndCreateDate(Long planId,LocalDate createDate){
        List<Long> templateIds = templateRepository.findByPlanId(planId).stream()
                .map(Template::getId)
                .toList();

        List<TemplateExercise> templateExercises = new ArrayList<>();
        templateIds.forEach(id -> templateExercises.addAll(templateExerciseRepository.findByTemplateId(id)));

        return getWorkoutDTOS(createDate, templateExercises);
    }

    public void addWorkout(Workout[] workout){
        for (Workout value : workout) {
            long templateId = value.getTemplateExercise().getTemplateId();
            long exerciseId = value.getTemplateExercise().getExerciseId();
            value.setTemplateExercise(templateExerciseRepository.findByTemplateIdAndExerciseId(templateId, exerciseId));
            value.setUser(userRepository.findByName(value.getUser().getName()));
        }
        workoutRepository.saveAll(Arrays.asList(workout));
    }

    public void updateWorkout(RequestWorkoutDTO[] requestWorkoutDTO){

        List<Workout> workouts = new ArrayList<>();
        for (RequestWorkoutDTO value : requestWorkoutDTO) {
            Workout workoutEntity = new Workout();

            long templateId = value.getTemplateExercise().getTemplateId();
            long exerciseId = value.getTemplateExercise().getExerciseId();
            TemplateExercise templateExercise = templateExerciseRepository.findByTemplateIdAndExerciseId(templateId,exerciseId);

            Long id = workoutRepository.findByTemplateExerciseIdAndCreateDate(templateExercise.getId(),value.getCreateDate()).stream()
                    .filter(workout -> workout.getWorkSet().equals(value.getWorkSet()) && workout.getTemplateExercise().getExerciseId().equals(exerciseId))
                    .findFirst().orElse(new Workout()).getId();

            workoutEntity.setTemplateExercise(templateExerciseRepository.findByTemplateIdAndExerciseId(templateId, exerciseId));
            workoutEntity.setUser(userRepository.findByName(value.getUser().getName()));
            workoutEntity.setId(id);
            workoutEntity.setWeight(value.getWeight());
            workoutEntity.setNumberOfRepetitions(value.getNumberOfRepetitions());
            workoutEntity.setWorkSet(value.getWorkSet());
            workoutEntity.setCreateDate(value.getCreateDate());

            workouts.add(workoutEntity);
        }
        List<Workout> workoutsToDelete = new ArrayList<>();
        HashSet<Long> templateExercisesIds = new HashSet<>();
        workouts.forEach(workout -> templateExercisesIds.addAll(templateExerciseRepository.findByTemplateId(
                                workout.getTemplateExercise().getTemplateId())
                        .stream()
                        .map(TemplateExercise::getExerciseId)
                        .toList()
                )
        );

        templateExercisesIds.forEach(id -> workoutsToDelete.addAll(workoutRepository.findByTemplateExerciseIdAndCreateDate(
                id,
                workouts.get(0).getCreateDate())
        ));
        workoutsToDelete.forEach(workoutToDelete -> {
            List<Long> idListToUpdate = workouts.stream()
                    .map(Workout::getId)
                    .toList();
            if(!idListToUpdate.contains(workoutToDelete.getId())) workoutRepository.delete(workoutToDelete);
        });

        workoutRepository.saveAll(workouts);
        log.info(workouts.toString());
        log.info("Workout updated!");
    }

    @Transactional
    public void deleteWorkout(Long templateId, LocalDate createDate){
        List<TemplateExercise> templateExercises = templateExerciseRepository.findByTemplateId(templateId);

        templateExercises.forEach(templateExercise -> workoutRepository
                .deleteByTemplateExerciseAndCreateDate(templateExercise,createDate));
        log.info("Usunięto szablon!");
    }




    private Set<ResponseWorkoutDTO> getWorkoutDTOS(LocalDate createDate, List<TemplateExercise> templateExercises) {
        List<Workout> workouts = new ArrayList<>();
        templateExercises.forEach(templateExercise -> workouts.addAll(workoutRepository.findWorkoutByCreateDateAndTemplateExercise(createDate,templateExercise)));

        HashSet<WorkoutExerciseDTO> workoutExerciseDTOList = getWorkoutExerciseDTOList(createDate, templateExercises);


        return workouts.stream()
                .map(log -> ResponseWorkoutDTO.builder()
                        .templateId(log.getTemplateExercise().getTemplateId())
                        .planId(Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId()).orElse(null)).getPlan().getId())
                        .planName(Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId()).orElse(null)).getPlan().getName())
                        .day(Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId()).orElse(null)).getDay())
                        .createDate(log.getCreateDate())
                        .exercises(workoutExerciseDTOList.stream().toList())
                        .build())
                .collect(Collectors.toSet());
    }

    private HashSet<WorkoutExerciseDTO> getWorkoutExerciseDTOList(LocalDate createDate, List<TemplateExercise> templateExercises) {
        HashSet<WorkoutExerciseDTO> workoutExerciseDTOList = new HashSet<>();
        templateExercises.forEach(element -> workoutExerciseDTOList.add(WorkoutExerciseDTO.builder()
                .exerciseId(Objects.requireNonNull(exerciseRepository.findById(element.getExerciseId()).orElse(null)).getId())
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
}
