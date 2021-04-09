package ru.diplom.FirePandaDelivery.dto.responseModel;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.model.Restaurant;


import java.sql.Time;
import java.util.LinkedList;
import java.util.List;

@Builder
@Data
public class RestaurantResp {
    
    private long id;

    private String name;

    private String description;

    private Time workingHoursStart;

    private Time workingHoursEnd;

    private double minPrice;

    private float rating;

    private boolean ownDelivery;

    private List<String> Cities;

    private String img;
    
    public static RestaurantResp toRestaurantResponse(Restaurant restaurant) {
        
        RestaurantResp restaurantResp = RestaurantResp.builder()
                .id(restaurant.getId())
                .description(restaurant.getDescription())
                .img(restaurant.getImg())
                .minPrice(restaurant.getMinPrice())
                .name(restaurant.getName())
                .ownDelivery(restaurant.isOwnDelivery())
                .rating(restaurant.getRating())
                .workingHoursEnd(restaurant.getWorkingHoursEnd())
                .workingHoursStart(restaurant.getWorkingHoursStart())
                .build();

        List<String> list = new LinkedList<>();

        for (Cities city : restaurant.getCities()) {
            list.add(city.getCiti());
        }

        return restaurantResp;
    }

    public static List<RestaurantResp> toRestaurantResponse(List<Restaurant> restaurants) {

        List<RestaurantResp> restaurantRespList = new LinkedList<>();

        for (Restaurant restaurant : restaurants) {
            restaurantRespList.add(toRestaurantResponse(restaurant));
        }

        return restaurantRespList;
    }
    
    
    
    
}


