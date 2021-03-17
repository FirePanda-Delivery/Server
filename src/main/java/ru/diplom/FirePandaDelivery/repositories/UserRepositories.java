package ru.diplom.FirePandaDelivery.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.diplom.FirePandaDelivery.model.User;

import java.util.List;

@Repository
public interface UserRepositories extends JpaRepository<User, Long> {

}
