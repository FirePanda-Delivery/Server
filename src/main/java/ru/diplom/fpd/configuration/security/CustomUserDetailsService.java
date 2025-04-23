package ru.diplom.fpd.configuration.security;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import ru.diplom.fpd.model.User;
import ru.diplom.fpd.service.UserService;

@Component
public class CustomUserDetailsService implements UserDetailsService {

    private final UserService userService;

    @Autowired
    public CustomUserDetailsService(UserService userService) {
        this.userService = userService;
    }

    @Override
    public CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User userEntity = userService.getByUserName(username);
        return CustomUserDetails.fromUserEntityToCustomUserDetails(userEntity);
    }
}
