package pl.coderslab.wrkt_springboot_backend.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pl.coderslab.wrkt_springboot_backend.session.SessionFilter;
import pl.coderslab.wrkt_springboot_backend.user.SpringDataUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {


    @Bean
    protected SecurityFilterChain configure(HttpSecurity http,SessionFilter sessionFilter) throws Exception {
        http.csrf()
                .disable()
                .authorizeHttpRequests(requests -> {
                    try {
                        requests.requestMatchers("/login").permitAll()
                                .requestMatchers("/register").permitAll()
                                .requestMatchers("/*").authenticated()
                                .and()
                                .httpBasic()
                                .and()
                                .sessionManagement()
                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

        http.addFilterBefore(
                sessionFilter,
                UsernamePasswordAuthenticationFilter.class
        );
        return http.build();
    }
    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, BCryptPasswordEncoder bCryptPasswordEncoder, SpringDataUserDetailsService userDetailsService)
            throws Exception {
        return http.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(bCryptPasswordEncoder)
                .and()
                .build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}