package pl.coderslab.wrkt_springboot_backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.coderslab.wrkt_springboot_backend.session.SessionFilter;

@Configuration
public class SecurityFilterChain {


}
