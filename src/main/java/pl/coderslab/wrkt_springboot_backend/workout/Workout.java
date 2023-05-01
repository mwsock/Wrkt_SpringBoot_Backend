package pl.coderslab.wrkt_springboot_backend.workout;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;
import pl.coderslab.wrkt_springboot_backend.template.Template;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExercise;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "workouts")
public class Workout {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "template_exercises_id")
    private TemplateExercise templateExercise;
    @Column(name = "work_set")
    private Integer workSet;
    @Column(name = "number_of_repetitions")
    private Integer numberOfRepetitions;
    private Double weight;

    @Column(name = "create_date")
    private LocalDate createDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDate.now();
    }



}
