package ru.diplom.FirePandaDelivery.Service;

import org.springframework.stereotype.Service;
import ru.diplom.FirePandaDelivery.model.Cities;
import ru.diplom.FirePandaDelivery.repositories.CitiesRepositories;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CitiesServices {


    private CitiesRepositories citiesRepositories;

    public CitiesServices(CitiesRepositories citiesRepositories) {
        this.citiesRepositories = citiesRepositories;
    }

    public Cities get(long id) {
        Optional<Cities> citiesOptional = citiesRepositories.findById(id);
        if (citiesOptional.isEmpty()) {
            throw new EntityNotFoundException("Cities not found");
        }

        return citiesOptional.get();
    }

    public List<Cities> getAll() {
        return citiesRepositories.findAll();
    }

    public Cities set(Cities cities) {

        if (cities == null) {
            throw new NullPointerException("Cities not set");
        }

        return citiesRepositories.save(cities);
    }
}
