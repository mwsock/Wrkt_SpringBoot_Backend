package pl.coderslab.wrkt_springboot_backend.template;

import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
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
}
