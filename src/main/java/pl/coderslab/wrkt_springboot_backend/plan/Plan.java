package pl.coderslab.wrkt_springboot_backend.plan;

import com.fasterxml.jackson.annotation.JsonValue;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.wrkt_springboot_backend.user.User;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
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
    @Column(columnDefinition = "boolean default false")
    private boolean deleted;
    @Column(name = "create_date")
    private LocalDateTime createDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
    }

}
