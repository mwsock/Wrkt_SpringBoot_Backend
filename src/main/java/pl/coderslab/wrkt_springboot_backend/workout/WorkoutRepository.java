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
    @Query("select w from Workout w where w.createDate = (select max(createDate) from Workout where User = ?1)")
    List<Workout> findLastWorkoutForUser(User user);
    List<Workout> findWorkoutByCreateDateAndTemplateExercise(LocalDate createDate, TemplateExercise templateExercise);
    List<Workout> findByUser(User user);
    List<Workout> findByTemplateExerciseIdAndCreateDate(Long templateExerciseId,LocalDate createDate);
}
