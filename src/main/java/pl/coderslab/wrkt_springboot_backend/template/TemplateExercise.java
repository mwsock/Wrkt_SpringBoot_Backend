package pl.coderslab.wrkt_springboot_backend.template;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "template_exercises")
public class TemplateExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "template_id")
    private Long templateId;
    @Column(name = "exercise_id")
    private Long exerciseId;
    @Column(name = "create_date")
    private LocalDate createDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDate.now();
    }
}
