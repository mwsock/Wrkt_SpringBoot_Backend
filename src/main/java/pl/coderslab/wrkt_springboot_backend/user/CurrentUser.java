package pl.coderslab.wrkt_springboot_backend.user;

import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

public class CurrentUser extends User {
    private final pl.coderslab.wrkt_springboot_backend.user.User user;
    public CurrentUser(String username, String password,
                       Collection<? extends GrantedAuthority> authorities,
                       pl.coderslab.wrkt_springboot_backend.user.User user) {
        super(username, password, authorities);
        this.user = user;
    }
    public pl.coderslab.wrkt_springboot_backend.user.User getUser() {return user;}
}

