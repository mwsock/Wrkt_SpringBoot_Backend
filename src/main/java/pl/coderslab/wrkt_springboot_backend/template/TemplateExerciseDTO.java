package pl.coderslab.wrkt_springboot_backend.template;

import jakarta.persistence.Column;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class TemplateExerciseDTO {

    @NotNull
    @Min(1)
    private Long templateId;
    @NotNull
    @Min(1)
    private Long exerciseId;

}
