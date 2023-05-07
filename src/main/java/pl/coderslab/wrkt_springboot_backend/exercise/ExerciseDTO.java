package pl.coderslab.wrkt_springboot_backend.exercise;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.coderslab.wrkt_springboot_backend.user.UserDTO;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class ExerciseDTO {

    private Long id;
    @Size(max=255)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z ]+$")
    private String name;
    @Valid
    @NotNull
    @JsonProperty("user")
    private UserDTO userDTO;

}
