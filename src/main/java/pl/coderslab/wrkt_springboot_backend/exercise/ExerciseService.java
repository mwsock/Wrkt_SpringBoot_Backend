package pl.coderslab.wrkt_springboot_backend.exercise;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;

import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final UserRepository userRepository;
    private final ExerciseMapper exerciseMapper;
    private static final List<String> EXERCISE_TYPE = Arrays.asList(
            "olympic_weightlifting",
            "plyometrics",
            "powerlifting",
            "strength",
            "strongman"
    );
    private static final String X_API_KEY = "fDMVberKJomXhf6dnyNMUA==qVa6w7rFwW7lEXkE";
    private static final String EXERCISE_API_URL = "https://api.api-ninjas.com/v1/exercises?difficulty=intermediate&type=";
    @Autowired
    public ExerciseService(ExerciseRepository exerciseRepository, UserRepository userRepository, ExerciseMapper exerciseMapper) {
        this.exerciseRepository = exerciseRepository;
        this.userRepository = userRepository;
        this.exerciseMapper = exerciseMapper;
    }

    public List<ExerciseDTO> getExercises(String sessionId){
        User user = userRepository.findByName("skarpeta");
        return exerciseRepository.findByUser(user).stream()
                .filter(exercise -> !exercise.isDeleted())
                .map(exerciseMapper::mapToExerciseDTO)
                .collect(Collectors.toList());
    }

    public String addExercise(ExerciseDTO exerciseDTO){
        Exercise exercise = exerciseMapper.mapToExercise(exerciseDTO);
        User user = userRepository.findByName(exercise.getUser().getName());
        exercise.setUser(user);
        return "New Exercise: " + exerciseRepository.save(exercise).getName();
    }

    public void removeExercise(Long id){
        Optional<Exercise> exercise = exerciseRepository.findById(id);
        if(exercise.isPresent()){
            Exercise exerciseToDelete = exercise.get();
            exerciseToDelete.setDeleted(true);
            exerciseRepository.save(exerciseToDelete);
            log.info("Usunięto: " + id);
        }
    }

    public List<ExerciseDTO> getSampleExercises(String sessionId) {
        User user = userRepository.findByName("skarpeta");

        ResponseEntity<SampleExerciseDTO[]> sampleExerciseDTOS = getSampleExerciseDTOS();
        deletePreviousSampleExercisesForUser(user);
        saveSampleExercises(user, sampleExerciseDTOS);

        return exerciseRepository.findByUser(user).stream()
                .filter(exercise -> !exercise.isDeleted())
                .map(exerciseMapper::mapToExerciseDTO)
                .collect(Collectors.toList());
    }

    private void deletePreviousSampleExercisesForUser(User user) {
        List<Exercise> samplesToDeleteList = exerciseRepository.findByUser(user).stream()
                .filter(Exercise::isSample)
                .collect(Collectors.toList());
        samplesToDeleteList.forEach(sample -> sample.setDeleted(true));
        exerciseRepository.saveAll(samplesToDeleteList);
    }

    private void saveSampleExercises(User user, ResponseEntity<SampleExerciseDTO[]> sampleExerciseDTOS) {
        List<Exercise> exerciseList = Arrays.stream(Objects.requireNonNull(sampleExerciseDTOS.getBody()))
                .map(exerciseMapper::mapSampleToExercise)
                .collect(Collectors.toList());
        exerciseList.forEach(exercise -> {
            exercise.setUser(user);
            exercise.setSample(true);
        });
        exerciseRepository.saveAll(exerciseList);
    }

    public String getRandomListElement(List<String> exerciseType) {
        Random rand = new Random();
        return exerciseType.get(rand.nextInt(exerciseType.size()));
    }
    private ResponseEntity<SampleExerciseDTO[]> getSampleExerciseDTOS() {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.set("x-api-key", X_API_KEY);
        HttpEntity<String> entity = new HttpEntity<>(headers);
        return restTemplate.exchange(EXERCISE_API_URL + getRandomListElement(EXERCISE_TYPE), HttpMethod.GET, entity, SampleExerciseDTO[].class);
    }
}
