package pl.coderslab.wrkt_springboot_backend.plan;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name="plans")
public class Plan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

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
