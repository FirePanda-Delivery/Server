package ru.diplom.fpd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.fpd.dto.UserDto;
import ru.diplom.fpd.model.User;
import ru.diplom.fpd.service.UserService;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;

    @Operation(summary = "Получить данные пользователя", parameters = {
            @Parameter(name = "id", description = "Идетификатор пользователя", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == authentication.principal.id)")
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {
        return ResponseEntity.ok(userService.getNotDeletedUser(id));
    }

    @Operation(summary = "Получить данные пользователя", parameters = {
            @Parameter(name = "id", description = "Идетификатор пользователя", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #username == authentication.principal.userName)")
    @GetMapping()
    public ResponseEntity<UserDto> getUser(@RequestParam String username) {
        return ResponseEntity.ok(userService.getUserByUsername(username));
    }

    @Operation(summary = "Изменить данные пользователя")
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #user.id == authentication.principal.id)")
    @PutMapping("/")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        return ResponseEntity.ok(userService.update(user));
    }

    @Operation(summary = "Удалить пользователя", parameters = {
            @Parameter(name = "id", description = "Идетификатор пользователя", in = ParameterIn.PATH, required = true)
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or (hasRole('ROLE_USER') and #id == authentication.principal.id)")
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
