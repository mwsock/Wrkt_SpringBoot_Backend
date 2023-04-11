package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    public User(String name, LocalDate createDate) {
        this.name = name;
        this.createDate = createDate;
    }

    public User(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String password;
    @Column(name = "create_date")
    private LocalDate createDate;

    @PrePersist
    public void prePersist() {
        createDate = LocalDate.now();
    }

}
