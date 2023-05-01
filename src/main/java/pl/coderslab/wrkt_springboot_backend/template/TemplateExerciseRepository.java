package pl.coderslab.wrkt_springboot_backend.template;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TemplateExerciseRepository extends JpaRepository<TemplateExercise,Long> {
    TemplateExercise findByTemplateIdAndExerciseId(Long templateId, Long exerciseId);
    List<TemplateExercise> findByTemplateId(Long id);
}
