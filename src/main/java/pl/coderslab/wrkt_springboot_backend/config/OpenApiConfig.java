package pl.coderslab.wrkt_springboot_backend.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {
    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components())
                .info(new Info()
                        .title("Workout Backend SpringBoot Application")
                        .description("API Description")
                        .version("1.0.0"));

    }
    @Bean
    public GroupedOpenApi exerciseOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("exercise")
                .packagesToScan("pl.coderslab.wrkt_springboot_backend.exercise")
                .build();
    }
    @Bean
    public GroupedOpenApi planOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("plan")
                .packagesToScan("pl.coderslab.wrkt_springboot_backend.plan")
                .build();
    }
    @Bean
    public GroupedOpenApi templateOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("template")
                .packagesToScan("pl.coderslab.wrkt_springboot_backend.template")
                .build();
    }
    @Bean
    public GroupedOpenApi userOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("user")
                .packagesToScan("pl.coderslab.wrkt_springboot_backend.user")
                .build();
    }
    @Bean
    public GroupedOpenApi workoutOpenApi() {
        return GroupedOpenApi
                .builder()
                .group("workout")
                .packagesToScan("pl.coderslab.wrkt_springboot_backend.workout")
                .build();
    }

}
