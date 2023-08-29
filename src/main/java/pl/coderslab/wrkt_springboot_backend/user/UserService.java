package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final AuthenticationManager manager;
    private static final int COOKIE_MAX_AGE = 900;
    private static final String DEFAULT_ROLE = "ROLE_USER";
    private final UserMapper userMapper;
    private final RegisterUserMapper registerUserMapper;

    @Autowired
    public UserService(UserRepository userRepository,BCryptPasswordEncoder bCryptPasswordEncoder, AuthenticationManager manager, UserMapper userMapper, RegisterUserMapper registerUserMapper) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.manager = manager;
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
        userRepository.save(user);
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

}
