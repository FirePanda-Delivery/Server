package ru.diplom.FirePandaDelivery;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.diplom.FirePandaDelivery.Service.RestaurantService;
import ru.diplom.FirePandaDelivery.Service.UserService;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Restaurant;
import ru.diplom.FirePandaDelivery.model.User;

import java.sql.Time;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Component
public class DatabaseLoder {

    @Autowired
    UserService userService;
    @Autowired
    RestaurantService restaurantService;

    @Bean
    CommandLineRunner initDatabase(){
        return args -> {

            User user = new User();
            user.setFirstName("Вадим");
            user.setLastName("Белый");
            user.setPhone("83247893245767");
            user.setDeleted(true);

            userService.add(user);

            for (int i = 2; i <= 20; i++) {

                User users = new User();
                users.setFirstName("name" + i);
                users.setLastName("name" + i);
                users.setPhone("83247893245767" + i);

                if (i % 2 == 0) { users.setDeleted(true); }

                userService.add(users);
            }
            List<User> userList = userService.getDeletedList();

            Restaurant restaurant = new Restaurant();
            restaurant.setName("mak");
            restaurant.setWorkingHoursStart(new Time(5000));
            restaurant.setWorkingHoursEnd(new Time(60000));

            long id = restaurantService.add(restaurant).getId();

            restaurantService.addCategory(id, new Categories());
            System.out.println("kjbn,");
        };


    }


}

