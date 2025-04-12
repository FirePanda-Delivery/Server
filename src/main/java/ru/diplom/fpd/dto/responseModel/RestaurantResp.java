package ru.diplom.fpd.dto.responseModel;
import lombok.Builder;
import lombok.Data;
import ru.diplom.fpd.model.Restaurant;
import ru.diplom.fpd.model.RestaurantAddress;


import java.sql.Time;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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

    private Map<String, String> CitiesAddress;

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

        Map<String, String> map = new HashMap<>();

        for (RestaurantAddress restaurantAddress : restaurant.getCitiesAddress()) {
            map.put(restaurantAddress.getCity().getCiti(), restaurantAddress.getAddress());
        }

        restaurantResp.setCitiesAddress(map);

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


