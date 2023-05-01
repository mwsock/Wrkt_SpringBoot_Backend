package pl.coderslab.wrkt_springboot_backend;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.session.ResponseDTO;
import pl.coderslab.wrkt_springboot_backend.user.User;
import pl.coderslab.wrkt_springboot_backend.user.UserDTO;
import pl.coderslab.wrkt_springboot_backend.user.UserRepository;
import pl.coderslab.wrkt_springboot_backend.user.UserService;

@RestController
@Slf4j
public class LoginController {

    private final AuthenticationManager manager;
    private final InMemorySessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public LoginController(AuthenticationManager manager, InMemorySessionRegistry sessionRegistry, UserRepository userRepository, UserService userService) {
        this.manager = manager;
        this.sessionRegistry = sessionRegistry;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseBody
    public String registerUser(@RequestBody UserDTO user){
        User newUser = new User();
        newUser.setName(user.getUsername());
        newUser.setPassword(user.getPassword());
        userService.saveUser(newUser);
        return "User added!";
    }
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@RequestBody UserDTO user) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        ResponseDTO response = new ResponseDTO();
        final String sessionId = sessionRegistry.registerSession(user.getUsername());
        response.setSessionId(sessionId);

        return ResponseEntity.ok(response);
    }
}
