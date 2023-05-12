package pl.coderslab.wrkt_springboot_backend.workout;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExercise;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface WorkoutRepository extends JpaRepository<Workout,Long> {
    List<Workout> findWorkoutByCreateDateAndTemplateExercise(LocalDate createDate, TemplateExercise templateExercise);
    List<Workout> findByUser(User user);
    List<Workout> findByTemplateExerciseIdAndCreateDate(Long templateExerciseId,LocalDate createDate);

    void deleteByTemplateExerciseAndCreateDate(TemplateExercise templateExercise,LocalDate createDate);
}
