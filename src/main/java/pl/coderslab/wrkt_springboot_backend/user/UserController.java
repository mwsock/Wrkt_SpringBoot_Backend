package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.wrkt_springboot_backend.session.ResponseDTO;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @ResponseBody
    public String registerUser(@Valid @RequestBody RegisterUserDTO userDTO){
        return userService.registerUser(userDTO);
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserDTO userDTO, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(userDTO,response));
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout(HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(userService.logout(request,response));
    }

}
