package pl.coderslab.wrkt_springboot_backend.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TemplateExerciseRepository extends JpaRepository<TemplateExercise,Long> {
}
