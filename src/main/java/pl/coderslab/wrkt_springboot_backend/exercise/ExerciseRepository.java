package pl.coderslab.wrkt_springboot_backend.exercise;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.util.List;

@Repository
public interface ExerciseRepository extends JpaRepository<Exercise,Long> {
    List<Exercise> findByUser(User user);
    void deleteByUserAndSampleIsTrue(User user);
}
