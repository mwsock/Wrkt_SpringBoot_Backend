package pl.coderslab.wrkt_springboot_backend.template;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.exercise.ExerciseDTO;
import pl.coderslab.wrkt_springboot_backend.plan.Plan;
import pl.coderslab.wrkt_springboot_backend.plan.PlanDTO;
import pl.coderslab.wrkt_springboot_backend.user.UserDTO;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TemplateDTO {

    private Long id;
    @Range(min=1,max=5)
    @NotNull
    private Integer day;
    @NotNull
    @JsonProperty("plan")
    private PlanDTO planDTO;
    @NotNull
    private List<ExerciseDTO> exercises;
    @NotNull
    @Valid
    @JsonProperty("user")
    private UserDTO userDTO;

}
