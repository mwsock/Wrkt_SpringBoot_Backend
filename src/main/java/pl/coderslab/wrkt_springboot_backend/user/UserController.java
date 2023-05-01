package pl.coderslab.wrkt_springboot_backend.user;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.session.InMemorySessionRegistry;
import pl.coderslab.wrkt_springboot_backend.session.ResponseDTO;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {

    private final AuthenticationManager manager;
    private final InMemorySessionRegistry sessionRegistry;
    private final UserRepository userRepository;
    private final UserService userService;

    @Autowired
    public UserController(AuthenticationManager manager, InMemorySessionRegistry sessionRegistry, UserRepository userRepository, UserService userService) {
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

//    @PostMapping("/login")
//    @ResponseBody
//    public String logInUser(@RequestBody User user, HttpServletResponse response){
//        User storedUser = userRepository.findByName(user.getName());
//        String userName = storedUser.getName();
//        if(storedUser.getPassword().equals(user.getPassword())){
//            Cookie userCookie = new Cookie("user",userName);
//            userCookie.setHttpOnly(true);
//            response.addCookie(userCookie);
//           return "LoggedIn";
//       }
//       return null;
//    }

//    @PostMapping("/login")
//    @ResponseBody
//    public ResponseEntity<ResponseDTO> login(@RequestBody UserDTO user) {
//        manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));
//
//        ResponseDTO response = new ResponseDTO();
//        final String sessionId = sessionRegistry.registerSession(user.getUsername());
//        response.setSessionId(sessionId);
//
//        return ResponseEntity.ok(response);
//    }

    @PostMapping("/login")
    @ResponseBody
    public String login(@RequestBody UserDTO user) {
        manager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(),user.getPassword()));

        ResponseDTO response = new ResponseDTO();
        final String sessionId = sessionRegistry.registerSession(user.getUsername());
        response.setSessionId(sessionId);

        return sessionId;
    }
}
