package pl.coderslab.wrkt_springboot_backend.workout;

import lombok.*;

import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseLogDTO {

    private int numberOfRepetitions;
    private int workSet;
    private double weight;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutExerciseLogDTO that = (WorkoutExerciseLogDTO) o;
        return numberOfRepetitions == that.numberOfRepetitions && workSet == that.workSet && Double.compare(that.weight, weight) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(numberOfRepetitions, workSet, weight);
    }
}
