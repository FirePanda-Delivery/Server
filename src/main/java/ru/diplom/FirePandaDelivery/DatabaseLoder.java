package ru.diplom.FirePandaDelivery;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.diplom.FirePandaDelivery.Service.UserService;
import ru.diplom.FirePandaDelivery.model.User;

import java.util.List;

@Component
public class DatabaseLoder {

    @Autowired
    UserService userService;

    @Bean
    CommandLineRunner initDatabase(){
        return args -> {

            User user = new User();
            user.setFirstName("Вадим");
            user.setLastName("Белый");
            user.setPhone("83247893245767");
            user.setDeleted(true);

            userService.set(user);

            for (int i = 2; i <= 20; i++) {

                User users = new User();
                users.setFirstName("name" + i);
                users.setLastName("name" + i);
                users.setPhone("83247893245767" + i);

                if (i % 2 == 0) { users.setDeleted(true); }

                userService.set(users);
            }
            List<User> userList = userService.getDeletedList();

        };


    }


}

