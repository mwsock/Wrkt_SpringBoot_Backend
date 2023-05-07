package pl.coderslab.wrkt_springboot_backend.workout;

import jakarta.persistence.Column;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExercise;
import pl.coderslab.wrkt_springboot_backend.template.TemplateExerciseDTO;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserDTO;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RequestWorkoutDTO {

    private Long id;
    @NotNull
    @Valid
    private UserDTO user;
    @NotNull
    @Valid
    private TemplateExerciseDTO templateExercise;
    @Range(min=1,max=100)
    @NotNull
    @NotBlank
    private Integer workSet;
    @Range(min=1,max=100)
    @NotNull
    @NotBlank
    private Integer numberOfRepetitions;
    @Range(min=1,max=600)
    @NotNull
    @NotBlank
    private Double weight;
    private LocalDate createDate;

}
