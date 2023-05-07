package pl.coderslab.wrkt_springboot_backend.plan;

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
public class PlanDTO {

    private Long id;
    @Size(max=255)
    @NotNull
    @NotBlank
    @Pattern(regexp = "^[a-zA-Z0-9/ ]+$")
    private String name;
    @Valid
    @NotNull
    @JsonProperty("user")
    private UserDTO userDTO;

}
