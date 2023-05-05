package pl.coderslab.wrkt_springboot_backend.workout;

import lombok.*;

import java.util.List;
import java.util.Objects;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class WorkoutExerciseDTO {

    private long exerciseId;
    private String exerciseName;
    private List<WorkoutExerciseLogDTO> exerciseLogList;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkoutExerciseDTO that = (WorkoutExerciseDTO) o;
        return exerciseId == that.exerciseId && Objects.equals(exerciseName, that.exerciseName) && Objects.equals(exerciseLogList, that.exerciseLogList);
    }

    @Override
    public int hashCode() {
        return Objects.hash(exerciseId, exerciseName, exerciseLogList);
    }
}
