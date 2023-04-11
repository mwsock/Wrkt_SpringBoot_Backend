package pl.coderslab.wrkt_springboot_backend.template;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.wrkt_springboot_backend.exercise.Exercise;
import pl.coderslab.wrkt_springboot_backend.plan.Plan;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "templates")
public class Template {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name="plan_id")
    private Plan plan;

    private Integer day;

    @ManyToMany
    @JoinTable(name = "template_exercises",
            joinColumns = @JoinColumn(name = "template_id"),
            inverseJoinColumns = @JoinColumn(name = "exercise_id"))
    private List<Exercise> exercise;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @Column(name = "create_date")
    private LocalDate createDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDate.now();
    }
}
