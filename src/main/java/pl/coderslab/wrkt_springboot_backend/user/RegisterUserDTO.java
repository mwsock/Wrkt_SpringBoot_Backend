package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class RegisterUserDTO {
    @Size(max=60)
    @NotNull
    @NotBlank
    private String name;
    @Size(max=255)
    @NotNull
    @NotBlank
    private String password;
}
