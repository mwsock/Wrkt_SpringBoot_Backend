package pl.coderslab.wrkt_springboot_backend.config;

import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private static final AntPathRequestMatcher NEW_DEFAULT_ANT_PATH_REQUEST_MATCHER = new AntPathRequestMatcher("/user/login","POST");

    public CustomUsernamePasswordAuthenticationFilter() {
        super.setRequiresAuthenticationRequestMatcher(NEW_DEFAULT_ANT_PATH_REQUEST_MATCHER);
    }

}
