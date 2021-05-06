package ru.diplom.FirePandaDelivery;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import ru.diplom.FirePandaDelivery.dto.Coordinates;
import ru.diplom.FirePandaDelivery.service.CitiesServices;
import ru.diplom.FirePandaDelivery.service.CourierService;
import ru.diplom.FirePandaDelivery.service.RestaurantService;
import ru.diplom.FirePandaDelivery.service.UserService;
import ru.diplom.FirePandaDelivery.model.*;
import ru.diplom.FirePandaDelivery.processing.AddressProcessing;

import java.io.File;
import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

@Component
public class DatabaseLoder {

    @Autowired
    UserService userService;
    @Autowired
    RestaurantService restaurantService;
    @Autowired
    AddressProcessing validateAddress;
    @Autowired
    CitiesServices citiesServices;
    @Autowired
    CourierService courierService;

    @Value("${image.upload.path}")
    private String imagesDirectory;

    @Bean
    CommandLineRunner initDatabase(){
        return args -> {

            System.out.println(imagesDirectory);

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

            List<CitiesCoordinates> coordinates = new LinkedList<>();

            CitiesCoordinates cord1 = new CitiesCoordinates();
            cord1.setX(39.180002);
            cord1.setY(51.735140);
            coordinates.add(cord1);

            CitiesCoordinates cord2 = new CitiesCoordinates();
            cord2.setX(39.171696);
            cord2.setY(51.734682);
            coordinates.add(cord2);

            CitiesCoordinates cord3 = new CitiesCoordinates();
            cord3.setX(39.161963);
            cord3.setY(51.725849);
            coordinates.add(cord3);

            CitiesCoordinates cord4 = new CitiesCoordinates();
            cord4.setX(39.154761);
            cord4.setY(51.723338);
            coordinates.add(cord4);

            CitiesCoordinates cord5 = new CitiesCoordinates();
            cord5.setX(39.141404);
            cord5.setY(51.713918);
            coordinates.add(cord5);

            CitiesCoordinates cord6 = new CitiesCoordinates();
            cord6.setX(39.113016);
            cord6.setY(51.677332);
            coordinates.add(cord6);

            CitiesCoordinates cord7 = new CitiesCoordinates();
            cord7.setX(39.127596);
            cord7.setY(51.664600);
            coordinates.add(cord7);

            CitiesCoordinates cord8 = new CitiesCoordinates();
            cord8.setX(39.103887);
            cord8.setY(51.660705);
            coordinates.add(cord8);

            CitiesCoordinates cord9 = new CitiesCoordinates();
            cord9.setX(39.180002);
            cord9.setY(51.735140);
            coordinates.add(cord9);

            CitiesCoordinates cord10 = new CitiesCoordinates();
            cord10.setX(39.100940);
            cord10.setY(51.655563);
            coordinates.add(cord10);

            CitiesCoordinates cord11 = new CitiesCoordinates();
            cord11.setX(39.098260);
            cord11.setY(51.644920);
            coordinates.add(cord11);

            CitiesCoordinates cord12 = new CitiesCoordinates();
            cord12.setX(39.145429);
            cord12.setY(51.637393);
            coordinates.add(cord12);

            CitiesCoordinates cord13 = new CitiesCoordinates();
            cord13.setX(39.163519);
            cord13.setY(51.628499);
            coordinates.add(cord13);

            CitiesCoordinates cord14 = new CitiesCoordinates();
            cord14.setX(39.172172);
            cord14.setY(51.624150);
            coordinates.add(cord14);

            CitiesCoordinates cord15 = new CitiesCoordinates();
            cord15.setX(39.173047);
            cord15.setY(51.617528);
            coordinates.add(cord15);

            CitiesCoordinates cord16 = new CitiesCoordinates();
            cord16.setX(39.196544);
            cord16.setY(51.623877);
            coordinates.add(cord16);

            CitiesCoordinates cord17 = new CitiesCoordinates();
            cord17.setX(39.215865);
            cord17.setY(51.642566);
            coordinates.add(cord17);

            CitiesCoordinates cord18 = new CitiesCoordinates();
            cord18.setX(39.241414);
            cord18.setY(51.710398);
            coordinates.add(cord18);


            CitiesCoordinates cord19 = new CitiesCoordinates();
            cord19.setX(39.232683);
            cord19.setY(51.716396);
            coordinates.add(cord19);

            CitiesCoordinates cord20 = new CitiesCoordinates();
            cord20.setX(39.204851);
            cord20.setY(51.734857);
            coordinates.add(cord20);


            Cities cities = new Cities();


            cities.setCiti("Воронеж");
            cities.setCords(coordinates);
            citiesServices.add(cities);

            Cities cities1 = new Cities();


            cities1.setCiti("Курск");
            citiesServices.add(cities1);

            for (int i = 0; i <= 50; i++) {

                Restaurant restaurant1 = new Restaurant();
                restaurant1.setName("mak"+i);
                restaurant1.setWorkingHoursStart(new Time(5000));
                restaurant1.setWorkingHoursEnd(new Time(60000));
                restaurant1.setCitiesAddress(new LinkedList<>());

                RestaurantAddress restaurantAddress = new RestaurantAddress();
                restaurantAddress.setCity(cities);
                restaurantAddress.setAddress("пупкино " + i);
                restaurant1.getCitiesAddress().add(restaurantAddress);

                if (i%3 == 0 ) {
                    RestaurantAddress restaurantAddress1 = new RestaurantAddress();
                    restaurantAddress1.setCity(cities1);
                    restaurantAddress1.setAddress("ленина " + i);
                    restaurant1.getCitiesAddress().add(restaurantAddress1);

                }

                if (i%9 == 0) {
                    restaurant1.setPublished(true);
                }

                List<Categories> list = new LinkedList<>();

                Categories categories1 = new Categories();
                categories1.setName("Бургеры");

                {
                    List<Product> products = new LinkedList<>();

                    {
                        Product product = new Product();
                        product.setName("Чизбургер");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Чикенбургер");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Биг Мак");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Биг Тейсти");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Филе-О-Фиш");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
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
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Мексиканская");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Палермо");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("пицка");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Иркина пицца");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
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
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Капучино");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
                        products.add(product);
                    }


                    {
                        Product product = new Product();
                        product.setName("Латте");
                        product.setPrice(200.01);
                        product.setWeight(5.234);
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

//            List<Restaurant> restaurantList = restaurantService.getRestaurantsByProductName("Капучино");

            Courier courier = new Courier();
            courier.setCity(citiesServices.getByName("воронеж"));
            courier.setFirstName("Ирина");
            courier.setLastName("Хмырова");
            courier.setPhone("892038965");


            CitiesCoordinates citiesCoordinates = new CitiesCoordinates();
            citiesCoordinates.setX(39.216253);
            citiesCoordinates.setY(51.684610);

            courierService.add(courier);

            //CourierService.Storage.addCourier(courier, new Coordinates(39.216253, 51.684610));
            //courierService.courierCompletedOrder(courier);
//            boolean dd = CourierService.Storage.existActiveCourier(courier);
//            CourierService.Storage.getActiveCourier(courier);






//
//            boolean tt = validateAddress.isValid("Воронеж, Южно-Моравская улица, 74", "Воронеж");
//

           // validateAddress.courierNearestToAddress(new LinkedList<>(), "");


//            Object ob = new Object();



        };


    }


}

