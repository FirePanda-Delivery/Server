package ru.diplom.FirePandaDelivery.Service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.User;
import ru.diplom.FirePandaDelivery.repositories.UserRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;

import java.util.List;
import java.util.Optional;

@Service
public final class UserService {


    private final UserRepositories userRepositories;

    @Autowired
    public UserService(UserRepositories userRepositories) {
        this.userRepositories = userRepositories;
    }


    /**
     * get all users except deleted ones
     * @return list of users without deleted
     */
    public List<User> getUserList() {

        return userRepositories.findByIsDeletedFalse();
    }

    /**
     * get all users
     * @return list of users together with deleted ones
     */
    public List<User> getAll() {
        return userRepositories.findAll();
    }

    public User getByPhone(String phone) {
        if (phone.isEmpty()) { throw new NullPointerException("phone not set"); }

        // тут будет проверка телефона(может быть)

        return userRepositories.findFirstByPhone(phone);
    }

    public User get(long id) {
        if (id == 0) { throw new NullPointerException("id not set"); }
        Optional<User> user = userRepositories.findById(id);
        if (user.isEmpty()) { throw new EntityNotFoundException("User is not found"); }
        return user.get();
    }

    public List<User> getDeletedList() {

        return userRepositories.findByIsDeletedTrue();
    }

    public User add(User user) {
        if (user == null) { throw new NullPointerException("user not set"); }
        return userRepositories.save(user);
    }

    public List<User> addUserList(List<User> users) {
        if (users == null || users.isEmpty()) {  throw new NullPointerException("users not set"); }
        return userRepositories.saveAll(users);
    }

    public User update(User user) {
        if (!userRepositories.existsById(user.getId())) {
            throw new EntityNotFoundException("user not found!");
        }
        return userRepositories.save(user);
    }

    public List<User> updateUserList(List<User> users) {
        if (users == null) {
            throw new NullPointerException("users not set");
        }

        List<User> list = new ArrayList<User>();

        for (User user: users) {
            list.add(update(user));
        }

        return list;
    }

    public User userRecovery(long id) {
        if (id == 0) { throw new NullPointerException("id not set"); }
        Optional<User> userOptional = userRepositories.findById(id);

        if (userOptional.isEmpty()) { throw new EntityNotFoundException("user not found!"); }

        User user = userOptional.get();
        user.setDeleted(false);
        return userRepositories.save(user);
    }

    public void delete(long id) {

        if (id == 0) { throw new NullPointerException("id not set"); }
        Optional<User> userOptional = userRepositories.findById(id);

        if (userOptional.isEmpty()) { throw new EntityNotFoundException("user not found!"); }

        User user = userOptional.get();
        user.setDeleted(true);
    }

}
