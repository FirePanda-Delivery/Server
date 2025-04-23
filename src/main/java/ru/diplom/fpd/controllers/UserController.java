package ru.diplom.fpd.controllers;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.diplom.fpd.configuration.security.CustomUserDetails;
import ru.diplom.fpd.dto.UserDto;
import ru.diplom.fpd.model.User;
import ru.diplom.fpd.service.UserService;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
public class UserController {

    private final UserService userService;


    @ModelAttribute
    public void validateRequest(@PathVariable long id) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (((CustomUserDetails) auth.getPrincipal()).getId() != id) {
            throw new AuthorizationServiceException("access is denied");
        }
    }

    @Operation(summary = "Получить данные пользователя", parameters = {
            @Parameter(name = "id", description = "Идетификатор пользователя", in = ParameterIn.PATH, required = true)
    })
    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUser(@PathVariable long id) {

        return ResponseEntity.ok(userService.getNotDeletedUser(id));
    }

    @Operation(summary = "Изменить данные пользователя")
    @PutMapping("/")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto user) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (user.getId() != ((CustomUserDetails) auth.getPrincipal()).getId()) {
            throw new AuthorizationServiceException("access is denied");
        }
        return ResponseEntity.ok(userService.update(user));
    }

    @Operation(summary = "Удалить пользователя", parameters = {
            @Parameter(name = "id", description = "Идетификатор пользователя", in = ParameterIn.PATH, required = true)
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<User> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.ok().build();
    }
}
