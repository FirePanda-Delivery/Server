package ru.diplom.FirePandaDelivery.Controllers;

import io.swagger.annotations.Api;
import org.hibernate.PropertyValueException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.diplom.FirePandaDelivery.service.UserService;
import ru.diplom.FirePandaDelivery.model.User;

import javax.persistence.EntityNotFoundException;


@RestController
@RequestMapping("/user")
@Api(value = "Работа с пользователем", tags = {"Пользователи"})
public class UserController {

    private UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable long id) {

        return ResponseEntity.ok(userService.get(id));
    }

    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user) {
//        if (user == null) { throw new NullPointerException("User not set"); }
        user.setId(0);
        return ResponseEntity.ok(userService.add(user));
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }

    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    /////////////////////// Exception /////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////

    @ExceptionHandler(value = {EntityNotFoundException.class})
    public String notFoundError() {
    return "User not found";
    }

    @ExceptionHandler(value = {NullPointerException.class})
    public String nullError(Exception exception) {
        return  exception.getMessage();
    }

    @ExceptionHandler(value = {PropertyValueException.class})
    public String propertyNullError(Exception exception) {
        return  "user not set";
    }
}
