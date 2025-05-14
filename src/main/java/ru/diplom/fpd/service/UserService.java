package ru.diplom.fpd.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.diplom.fpd.dto.UserDto;
import ru.diplom.fpd.mapper.UserMapper;
import ru.diplom.fpd.model.User;
import ru.diplom.fpd.repositories.UserRepositories;

@AllArgsConstructor
@Service
public final class UserService {


    private final UserRepositories userRepositories;
    private final UserMapper userMapper;


    public User get(long id) {
        if (id == 0) {
            throw new NullPointerException("id not set");
        }
        Optional<User> user = userRepositories.findById(id);
        if (user.isEmpty()) {
            throw new EntityNotFoundException("User is not found");
        }
        return user.get();
    }

    public UserDto getNotDeletedUser(long id) {
        return userRepositories.findById(id)
                .filter(Predicate.not(User::isDeleted))
                .map(userMapper::toDto)
                .orElseThrow(EntityNotFoundException::new);

    }

    public User getByUserName(String name) {
        Optional<User> userOptional = userRepositories.findByUserName(name);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("user is not found");
        }

        return userOptional.get();
    }

    public List<User> getDeletedList() {

        return userRepositories.findByIsDeletedTrue();
    }

    public User add(User user) {
        if (user == null) {
            throw new NullPointerException("user not set");
        }
        return userRepositories.save(user);
    }

    public List<User> addUserList(List<User> users) {
        if (users == null || users.isEmpty()) {
            throw new NullPointerException("users not set");
        }
        return userRepositories.saveAll(users);
    }


    public UserDto update(UserDto userDto) {

        User user = get(userDto.getId());
        userMapper.update(userDto, user);
        return userMapper.toDto(userRepositories.save(user));
    }

    public void delete(long id) {

        if (id == 0) {
            throw new NullPointerException("id not set");
        }
        Optional<User> userOptional = userRepositories.findById(id);

        if (userOptional.isEmpty()) {
            throw new EntityNotFoundException("user not found!");
        }

        User user = userOptional.get();
        user.setDeleted(true);
        userRepositories.save(user);
    }

    public UserDto getUserByUsername(String username) {
        return userMapper.toDto(getByUserName(username));
    }
}
