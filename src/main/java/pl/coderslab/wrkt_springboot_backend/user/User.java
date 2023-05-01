package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import pl.coderslab.wrkt_springboot_backend.role.Role;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users")
@AllArgsConstructor
public class User {

    public User(String name, LocalDateTime createDate) {
        this.name = name;
        this.createDate = createDate;
    }

    public User(String name) {
        this.name = name;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false, unique = true, length = 60)
    private String name;
    private String password;
    private int enabled;
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinTable(name = "user_role"
            , joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;

    @Column(name = "create_date")
    private LocalDateTime createDate;

//    public User(String name, String password, Collection<GrantedAuthority> authorities) {
//        this.name = name;
//        this.password = password;
//        this.roles = authorities.stream()
//                .map(grantedAuthority -> new Role(null,grantedAuthority.getAuthority()))
//                .collect(Collectors.toSet());
//    }


    @PrePersist
    public void prePersist() {
        createDate = LocalDateTime.now();
    }

}
