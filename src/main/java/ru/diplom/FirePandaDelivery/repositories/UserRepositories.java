package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.FirePandaDelivery.model.User;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepositories extends JpaRepository<User, Long> {

    List<User> findByIsDeletedFalse();

    User findFirstByPhone(String phone);

    Optional<User> findByUserName(String name);

    List<User> findByIsDeletedTrue();
}
