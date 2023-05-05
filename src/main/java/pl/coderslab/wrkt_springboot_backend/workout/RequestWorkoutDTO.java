package pl.coderslab.wrkt_springboot_backend.workout;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExercise;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;

@Getter
@Setter
@Builder
public class RequestWorkoutDTO {

    private Long id;
    private User user;
    private TemplateExercise templateExercise;
    private Integer workSet;
    private Integer numberOfRepetitions;
    private Double weight;
    private LocalDate createDate;

}
