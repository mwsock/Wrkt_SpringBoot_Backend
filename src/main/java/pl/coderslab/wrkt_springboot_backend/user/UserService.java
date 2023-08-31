package pl.coderslab.wrkt_springboot_backend.user;

import jakarta.servlet.http.Cookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private static final int COOKIE_MAX_AGE = 900;
    private final RegisterUserMapper registerUserMapper;

    @Autowired
    public UserService(UserRepository userRepository, UserMapper userMapper, RegisterUserMapper registerUserMapper) {
        this.userRepository = userRepository;
        this.registerUserMapper = registerUserMapper;
    }


    public String registerUser(RegisterUserDTO userDTO){
        User user = registerUserMapper.mapToUser(userDTO);
        saveUser(user);
        return "User added!";
    }

    public void saveUser(User user) {
        user.setPassword(user.getPassword());
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
