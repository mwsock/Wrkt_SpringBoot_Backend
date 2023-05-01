package pl.coderslab.wrkt_springboot_backend.workout;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
public class WorkoutDTO {

    private long planId;
    private String planName;
    private int day;
    private LocalDate createDate;
    private List<WorkoutExerciseDTO> exercises;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutDTO that = (WorkoutDTO) o;
        return planId == that.planId && day == that.day && Objects.equals(planName, that.planName) && Objects.equals(createDate, that.createDate) && Objects.equals(exercises, that.exercises);
    }

    @Override
    public int hashCode() {
        return Objects.hash(planId, planName, day, createDate, exercises);
    }
}
