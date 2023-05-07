package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import pl.coderslab.wrkt_springboot_backend.role.Role;
import pl.coderslab.wrkt_springboot_backend.role.RoleRepository;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.session.ResponseDTO;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager manager;
    private final InMemorySessionRegistry sessionRegistry;
    private static final int COOKIE_MAX_AGE = 900;
    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final UserMapper userMapper;
    private final RegisterUserMapper registerUserMapper;

    @Autowired
    public UserService(UserRepository userRepository, RoleRepository roleRepository, BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager manager, InMemorySessionRegistry sessionRegistry, UserMapper userMapper, RegisterUserMapper registerUserMapper) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.manager = manager;
        this.sessionRegistry = sessionRegistry;
        this.userMapper = userMapper;
        this.registerUserMapper = registerUserMapper;
    }


    public String registerUser(RegisterUserDTO userDTO){
        User user = registerUserMapper.mapToUser(userDTO);
        saveUser(user);
        return "User added!";
    }

    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(1);

        HashSet<Role> roles = getRole();

        user.setRoles(roles);
        userRepository.save(user);
    }

    private HashSet<Role> getRole() {
        Role userRole = roleRepository.findByName(DEFAULT_ROLE);
        if(Objects.isNull(userRole)){
            userRole = new Role(null,DEFAULT_ROLE);
        }
        HashSet<Role> roles = new HashSet<>();
        roles.add(userRole);
        return roles;
    }

    public ResponseDTO login(UserDTO userDTO, HttpServletResponse response) {
        Authentication authentication = manager.authenticate(new UsernamePasswordAuthenticationToken(userDTO.getName(),userDTO.getPassword()));

        String sessionId = sessionRegistry.registerSession(userDTO.getName());

        // TODO: 06.05.2023 zmienić metodę na void
        ResponseDTO responseDTO = new ResponseDTO();
        responseDTO.setSessionId(sessionId);
        
        if(authentication.isAuthenticated()){
            Cookie sessionIdcookie = prepareCookie("sessionId",sessionId);
            response.addCookie(sessionIdcookie);

            Cookie userCookie = prepareCookie("user",userDTO.getName());
            response.addCookie(userCookie);
        }

        return responseDTO;
    }

    private Cookie prepareCookie(String cookieName ,String cookieValue) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(COOKIE_MAX_AGE);
        return cookie;
    }

    private Cookie prepareCookieToDelete(String cookieName ,String cookieValue, int cookieExpireTime) {
        Cookie cookie = new Cookie(cookieName, cookieValue);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(cookieExpireTime);
        return cookie;
    }

    public String logout(HttpServletRequest request, HttpServletResponse response){
        String user = sessionRegistry.getUserNameForSession(request.getHeader("Authorization"));
        sessionRegistry.deleteSessionForUser(request.getHeader("Authorization"),user);

        Cookie sessionIdCookieToDelete = prepareCookieToDelete("sessionId",null,0);
        response.addCookie(sessionIdCookieToDelete);

        Cookie userCookieToDelete = prepareCookieToDelete("user",null,0);
        response.addCookie(userCookieToDelete);
        // TODO: 06.05.2023 zmienić metodę na void
        return user + " logged out.";
    }
}
