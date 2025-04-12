package ru.diplom.fpd.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.diplom.fpd.configuration.security.CustomUserDetails;
import ru.diplom.fpd.service.UserService;
import ru.diplom.fpd.model.User;


@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @ModelAttribute
    public void validateRequest(@PathVariable long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (((CustomUserDetails) auth.getPrincipal()).getId() != id) {
            throw new AuthorizationServiceException("access is denied");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {

        return ResponseEntity.ok(userService.getNotDeletedUser(id));
    }

//    @PostMapping
//    public ResponseEntity<User> addUser(@RequestBody User user) {
//        user.setId(0);
//        return ResponseEntity.ok(userService.add(user));
//    }

    @PutMapping("/{id}")
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (user.getId() != ((CustomUserDetails) auth.getPrincipal()).getId()) {
            throw new AuthorizationServiceException("access is denied");
        }
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
