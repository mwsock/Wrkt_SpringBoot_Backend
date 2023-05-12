package pl.coderslab.wrkt_springboot_backend.workout;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseRepository;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.template.*;
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
    private final RequestWorkoutDTOMapper requestWorkoutDTOMapper;
    private final WorkoutExerciseLogDTOMapper workoutExerciseLogDTOMapper;
    private final TemplateService templateService;

    @Autowired
    public WorkoutService(WorkoutRepository workoutRepository,
                          TemplateExerciseRepository templateExerciseRepository,
                          UserRepository userRepository,
                          TemplateRepository templateRepository,
                          ExerciseRepository exerciseRepository,
                          InMemorySessionRegistry registry,
                          RequestWorkoutDTOMapper requestWorkoutDTOMapper, WorkoutExerciseLogDTOMapper workoutExerciseLogDTOMapper, TemplateService templateService) {
        this.workoutRepository = workoutRepository;
        this.templateExerciseRepository = templateExerciseRepository;
        this.userRepository = userRepository;
        this.templateRepository = templateRepository;
        this.exerciseRepository = exerciseRepository;
        this.registry = registry;
        this.requestWorkoutDTOMapper = requestWorkoutDTOMapper;
        this.workoutExerciseLogDTOMapper = workoutExerciseLogDTOMapper;
        this.templateService = templateService;
    }

    public Set<ResponseWorkoutDTO> getUserWorkouts(String sessionId){
        String userName = registry.getUserNameForSession(sessionId);
        User user = userRepository.findByName(userName);
        return workoutRepository.findByUser(user).stream()
                .map(this::getResponseWorkoutDTO)
                .collect(Collectors.toSet());
    }

    private ResponseWorkoutDTO getResponseWorkoutDTO(Workout workout) {
        return ResponseWorkoutDTO.builder()
                .templateId(workout.getTemplateExercise().getTemplateId())
                .planId(getTemplateForWorkoutLog(workout).getPlan().getId())
                .planName(getTemplateForWorkoutLog(workout).getPlan().getName())
                .day(getTemplateForWorkoutLog(workout).getDay())
                .createDate(workout.getCreateDate())
                .build();
    }

    private Template getTemplateForWorkoutLog(Workout workout) {
        return templateRepository.findById(workout.getTemplateExercise().getTemplateId()).orElse(null);
    }

    public Set<ResponseWorkoutDTO> getLastUserWorkout(String sessionId){
        String userName = registry.getUserNameForSession(sessionId);
        User user = userRepository.findByName(userName);
        List<Workout> workout = workoutRepository.findByUser(user);

        LocalDate workoutCreatDate = workout.stream()
                .map(Workout::getCreateDate)
                .max(Comparator.naturalOrder()).orElse(null);
        List<TemplateExercise> templateExercises = workout.stream()
                .filter(log -> log.getCreateDate().equals(workoutCreatDate))
                .map(Workout::getTemplateExercise)
                .collect(Collectors.toList());

        return getResponseWorkoutDTOs(workoutCreatDate,templateExercises);
    }

    public Set<ResponseWorkoutDTO> getUserWorkoutByPlanIdAndCreateDate(Long planId,LocalDate createDate){
        List<Long> templateIds = templateRepository.findByPlanId(planId)
                .stream()
                .map(Template::getId)
                .collect(Collectors.toList());

        List<TemplateExercise> templateExercises = new ArrayList<>();
        templateIds.forEach(id -> templateExercises.addAll(templateExerciseRepository.findByTemplateId(id)));

        return getResponseWorkoutDTOs(createDate, templateExercises);
    }

    public void addWorkout(RequestWorkoutDTO[] requestWorkoutDTO){
        List<Workout> workout = Arrays.stream(requestWorkoutDTO).collect(Collectors.toList())
                .stream()
                .map(requestWorkoutDTOMapper::mapToWorkout)
                .collect(Collectors.toList());
        for (Workout value : workout) {
            long templateId = value.getTemplateExercise().getTemplateId();
            long exerciseId = value.getTemplateExercise().getExerciseId();
            value.setTemplateExercise(templateExerciseRepository.findByTemplateIdAndExerciseId(templateId, exerciseId));
            value.setUser(userRepository.findByName(value.getUser().getName()));
        }
        workoutRepository.saveAll(workout);
    }

    public void updateWorkout(RequestWorkoutDTO[] requestWorkoutDTO){
        List<Workout> workouts = getWorkoutFromRequestWorkoutDTO(requestWorkoutDTO);
        HashSet<Long> templateExercisesIds = getTemplateExercisesIds(workouts);
        List<Workout> workoutsToDelete = getWorkoutsToDelete(workouts, templateExercisesIds);

        removeDeletedLogs(workouts, workoutsToDelete);

        workoutRepository.saveAll(workouts);
        log.info(workouts.toString());
        log.info("Workout updated!");
    }

    private void removeDeletedLogs(List<Workout> workouts, List<Workout> workoutsToDelete) {
        workoutsToDelete.forEach(workoutToDelete -> {
            List<Long> idListToUpdate = workouts.stream()
                    .map(Workout::getId)
                    .collect(Collectors.toList());
            if(!idListToUpdate.contains(workoutToDelete.getId())) workoutRepository.delete(workoutToDelete);
        });
    }

    private List<Workout> getWorkoutsToDelete(List<Workout> workouts, HashSet<Long> templateExercisesIds) {
        List<Workout> workoutsToDelete = new ArrayList<>();
        templateExercisesIds.forEach(id -> workoutsToDelete.addAll(workoutRepository.findByTemplateExerciseIdAndCreateDate(
                id,
                workouts.get(0).getCreateDate())
        ));
        return workoutsToDelete;
    }

    private HashSet<Long> getTemplateExercisesIds(List<Workout> workouts) {
        HashSet<Long> templateExercisesIds = new HashSet<>();

        workouts.forEach(workout ->
                templateExercisesIds.addAll(templateService.getByTemplateExerciseList(workout).stream()
                        .map(TemplateExercise::getId)
                        .collect(Collectors.toList())
                )
        );
        return templateExercisesIds;
    }

    private List<Workout> getWorkoutFromRequestWorkoutDTO(RequestWorkoutDTO[] requestWorkoutDTO) {
        List<Workout> workouts = new ArrayList<>();

        for (RequestWorkoutDTO value : requestWorkoutDTO) {
            Workout workoutEntity = requestWorkoutDTOMapper.mapToWorkout(value);

            long templateId = value.getTemplateExercise().getTemplateId();
            long exerciseId = value.getTemplateExercise().getExerciseId();
            TemplateExercise templateExercise = templateExerciseRepository.findByTemplateIdAndExerciseId(templateId,exerciseId);

            Long id = getWorkoutId(workoutEntity, exerciseId, templateExercise);

            workoutEntity.setTemplateExercise(templateExerciseRepository.findByTemplateIdAndExerciseId(templateId, exerciseId));
            workoutEntity.setUser(userRepository.findByName(value.getUser().getName()));
            workoutEntity.setId(id);

            workouts.add(workoutEntity);
        }
        return workouts;
    }

    private Long getWorkoutId(Workout workoutEntity, long exerciseId, TemplateExercise templateExercise) {
        return workoutRepository.findByTemplateExerciseIdAndCreateDate(templateExercise.getId(), workoutEntity.getCreateDate())
                .stream()
                .filter(workout -> workout.getWorkSet().equals(workoutEntity.getWorkSet()) && workout.getTemplateExercise().getExerciseId().equals(exerciseId))
                .findFirst()
                .orElse(new Workout())
                .getId();
    }


    @Transactional
    public void deleteWorkout(Long templateId, LocalDate createDate){
        List<TemplateExercise> templateExercises = templateExerciseRepository.findByTemplateId(templateId);

        templateExercises.forEach(templateExercise -> workoutRepository
                .deleteByTemplateExerciseAndCreateDate(templateExercise,createDate));
        log.info("Usunięto szablon!");
    }


    private Set<ResponseWorkoutDTO> getResponseWorkoutDTOs(LocalDate createDate, List<TemplateExercise> templateExercises) {
        List<Workout> workouts = new ArrayList<>();
        templateExercises.forEach(templateExercise ->
                workouts.addAll(getWorkoutByCreateDateAndTemplateExercise(createDate, templateExercise)));

        HashSet<WorkoutExerciseDTO> workoutExerciseDTOList = getWorkoutExerciseDTOList(createDate, templateExercises);

        return workouts.stream()
                .map(log -> getResponseWorkoutDTO(workoutExerciseDTOList, log))
                .collect(Collectors.toSet());
    }

    private ResponseWorkoutDTO getResponseWorkoutDTO(HashSet<WorkoutExerciseDTO> workoutExerciseDTOList, Workout workout) {
        return ResponseWorkoutDTO
                .builder()
                .templateId(workout.getTemplateExercise().getTemplateId())
                .planId(getTemplateForWorkoutLogNyTemplateId(workout).getPlan().getId())
                .planName(getTemplateForWorkoutLogNyTemplateId(workout).getPlan().getName())
                .day(getTemplateForWorkoutLogNyTemplateId(workout).getDay())
                .createDate(workout.getCreateDate())
                .exercises(new ArrayList<>(workoutExerciseDTOList))
                .build();
    }

    private List<Workout> getWorkoutByCreateDateAndTemplateExercise(LocalDate createDate, TemplateExercise templateExercise) {
        return workoutRepository.findWorkoutByCreateDateAndTemplateExercise(createDate, templateExercise);
    }

    private Template getTemplateForWorkoutLogNyTemplateId(Workout log) {
        return Objects.requireNonNull(templateRepository.findById(log.getTemplateExercise().getTemplateId())
                .orElse(null));
    }

    private HashSet<WorkoutExerciseDTO> getWorkoutExerciseDTOList(LocalDate createDate, List<TemplateExercise> templateExercises) {
        HashSet<WorkoutExerciseDTO> workoutExerciseDTOList = new HashSet<>();
        templateExercises.forEach(element -> workoutExerciseDTOList.add(
                getWorkoutExerciseDTO(createDate, element)));
        return workoutExerciseDTOList;
    }

    private WorkoutExerciseDTO getWorkoutExerciseDTO(LocalDate createDate, TemplateExercise templateExercise) {
        return WorkoutExerciseDTO
                .builder()
                .exerciseId(getExercise(templateExercise).getId())
                .exerciseName(getExercise(templateExercise).getName())
                .exerciseLogList(getExerciseLogList(createDate, templateExercise))
                .build();
    }

    private Exercise getExercise(TemplateExercise element) {
        return Objects.requireNonNull(exerciseRepository.findById(element.getExerciseId()).orElse(null));
    }

    private List<WorkoutExerciseLogDTO> getExerciseLogList(LocalDate createDate, TemplateExercise templateExercise) {
        return workoutRepository.findByTemplateExerciseIdAndCreateDate(templateExercise.getId(), createDate)
                .stream()
                .map(workoutExerciseLogDTOMapper::mapToWorkoutExerciseLogDTO)
                .collect(Collectors.toList());
    }
}
