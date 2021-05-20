package ru.diplom.FirePandaDelivery.Controllers;


import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.FirePandaDelivery.configuration.security.jwt.JwtProvider;
import ru.diplom.FirePandaDelivery.dto.RegistrationUser;
import ru.diplom.FirePandaDelivery.model.User;
import ru.diplom.FirePandaDelivery.service.UserService;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


@RestController
public class AuthController {
    private final UserService userService;
    private final JwtProvider jwtProvider;
    private final BCryptPasswordEncoder passwordEncoder;

    @Autowired
    public AuthController(UserService userService, JwtProvider jwtProvider, BCryptPasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtProvider = jwtProvider;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register/user")
    public ResponseEntity<Object> registerUser(@RequestBody RegistrationUser registrationUser) {
        User user = new User();

        user.setPassword(passwordEncoder.encode(registrationUser.getPassword()));
        user.setPhone(registrationUser.getPhone());
        user.setLastName(registrationUser.getLastName());
        user.setFirstName(registrationUser.getFirstName());
        user.setEmail(registrationUser.getEmail());
        user.setUserName(registrationUser.getUserName());
        user.setRole("ROLE_user");

        user = userService.add(user);

        String token = jwtProvider.generateToken(user.getUserName());
        Map<String, String> map = new LinkedHashMap<>();
        map.put("token", token);
        map.put("id", user.getId()+"");

        return ResponseEntity.ok(map);
    }

    @PostMapping("/login/user")
    public ResponseEntity<Object> auth(@RequestBody UserLogin userLogin) {

        if (userLogin == null) {
            throw new NullPointerException("login and password not set");
        }

        User user = userService.getByUserName(userLogin.getLogin());

        if (passwordEncoder.matches(userLogin.password, user.getPassword())) {
            String token = jwtProvider.generateToken(user.getUserName());
            Map<String, String> map = new HashMap<>();
            map.put("token", token);
            map.put("id", user.getId()+"");
            return ResponseEntity.ok(map);
        }

        throw new AuthorizationServiceException("wrong password");
    }

    @Data
    static class UserLogin {

        private String login;

        private String password;
    }

}
