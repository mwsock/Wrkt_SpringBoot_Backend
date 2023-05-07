package pl.coderslab.wrkt_springboot_backend.exercise;

import jakarta.persistence.*;
import lombok.*;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Builder
@Table(name = "exercises")
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
    }

}
