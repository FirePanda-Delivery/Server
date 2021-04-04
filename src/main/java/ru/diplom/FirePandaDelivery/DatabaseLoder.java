package ru.diplom.FirePandaDelivery;


import org.apache.juli.logging.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.diplom.FirePandaDelivery.Service.RestaurantService;
import ru.diplom.FirePandaDelivery.Service.UserService;
import ru.diplom.FirePandaDelivery.model.Categories;
import ru.diplom.FirePandaDelivery.model.Product;
import ru.diplom.FirePandaDelivery.model.Restaurant;
import ru.diplom.FirePandaDelivery.model.User;

import java.sql.Time;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.LogManager;
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

            for (int i = 0; i <= 3; i++) {

                Restaurant restaurant1 = new Restaurant();
                restaurant1.setName("mak"+i);
                restaurant1.setWorkingHoursStart(new Time(5000));
                restaurant1.setWorkingHoursEnd(new Time(60000));

                List<Categories> list = new LinkedList<>();

                Categories categories1 = new Categories();
                categories1.setName("Бургеры");

                {
                    List<Product> products = new LinkedList<>();

                    {
                        Product product = new Product();
                        product.setName("Чизбургер");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Чикенбургер");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Биг Мак");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Биг Тейсти");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Филе-О-Фиш");
                        product.setPrice(200.01);
                        products.add(product);
                    }

                    categories1.setProducts(products);

                }

                list.add(categories1);


                Categories categories2 = new Categories();
                categories2.setName("Пицца");

                {
                    List<Product> products = new LinkedList<>();

                    {
                        Product product = new Product();
                        product.setName("Итальянская");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Мексиканская");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Палермо");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("пицка");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Иркина пицца");
                        product.setPrice(200.01);
                        products.add(product);
                    }

                    categories2.setProducts(products);

                }

                list.add(categories2);


                Categories categories3 = new Categories();
                categories3.setName("Кофе");

                {
                    List<Product> products = new LinkedList<>();

                    {
                        Product product = new Product();
                        product.setName("Американо");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Капучино");
                        product.setPrice(200.01);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Латте");
                        product.setPrice(200.01);
                        products.add(product);
                    }

                    categories3.setProducts(products);

                }

                list.add(categories3);


                Categories categories4 = new Categories();
                categories4.setName("Крылышки");

                list.add(categories4);

                restaurant1.setCategories(list);

                restaurantService.add(restaurant1);
            }




            long id = restaurantService.add(restaurant).getId();

            System.out.println("kjbn,");

            List<Restaurant> restaurantList = restaurantService.getRestaurantsByProductName("Капучино");
        };


    }


}

