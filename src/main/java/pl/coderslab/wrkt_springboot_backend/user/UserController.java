package pl.coderslab.wrkt_springboot_backend.user;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Register user", description = "Adds new user and checks if any with the same name is already stored in DB",
            tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = String.class)))})
    @PostMapping("/register")
    @ResponseBody
    public ResponseEntity<String> registerUser(@Valid @RequestBody RegisterUserDTO userDTO){
        userService.registerUser(userDTO);
        return ResponseEntity.ok("User added!");
    }

    @Operation(summary = "Login user", description = "Logs in user to the applicationa and returns cookie with sessionId",
            tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseDTO.class)))})
    @PostMapping("/login")
    @ResponseBody
    public ResponseEntity<ResponseDTO> login(@Valid @RequestBody UserDTO userDTO, HttpServletResponse response) {
        return ResponseEntity.ok(userService.login(userDTO,response));
    }

    @Operation(summary = "Logout user", description = "Logs out user from the applicationa and destroys cookie with sessionId",
            tags = { "user" })
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "successful operation",
                    content = @Content(mediaType = "application/json",schema = @Schema(implementation = ResponseDTO.class)))})
    @PostMapping("/logout")
    @ResponseBody
    public ResponseEntity<String> logout(@RequestHeader(value="sessionId") String sessionId,HttpServletRequest request, HttpServletResponse response){
        return ResponseEntity.ok(userService.logout(request,response));
    }

}
