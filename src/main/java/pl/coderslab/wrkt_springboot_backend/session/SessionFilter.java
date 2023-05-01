package pl.coderslab.wrkt_springboot_backend.session;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.coderslab.wrkt_springboot_backend.user.CurrentUser;
import pl.coderslab.wrkt_springboot_backend.user.SpringDataUserDetailsService;

import java.io.IOException;

@Component
public class SessionFilter extends OncePerRequestFilter {

    private final InMemorySessionRegistry sessionRegistry;
    private final SpringDataUserDetailsService userDetailsService;

    @Autowired
    public SessionFilter(InMemorySessionRegistry sessionRegistry, SpringDataUserDetailsService userDetailsService) {
        this.sessionRegistry = sessionRegistry;
        this.userDetailsService = userDetailsService;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String sessionId = request.getHeader(HttpHeaders.AUTHORIZATION);

        if(sessionId == null || sessionId.length() == 0){
            filterChain.doFilter(request,response);
            return;
        }

        String username = sessionRegistry.getUserNameForSession(sessionId);
        if(username == null){
            filterChain.doFilter(request,response);
            return;
        }

        final CurrentUser currentUser = userDetailsService.loadUserByUsername(username);

        final UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
                currentUser,
                null,
                currentUser.getAuthorities()
        );

        auth.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        SecurityContextHolder.getContext().setAuthentication(auth);

        filterChain.doFilter(request,response);
    }
}
