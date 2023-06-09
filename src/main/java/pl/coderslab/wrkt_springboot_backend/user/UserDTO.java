package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UserDTO {

    @Size(max=60)
    @NotNull
    @NotBlank
    private String name;
    @Size(max=255)
    private String password;
}
