package ru.diplom.fpd.service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.repositories.CitiesRepositories;

@Service
public class CitiesServices {


    private final CitiesRepositories citiesRepositories;

    public CitiesServices(CitiesRepositories citiesRepositories) {
        this.citiesRepositories = citiesRepositories;
    }

    @Transactional()
    public City get(long id) {
        Optional<City> citiesOptional = citiesRepositories.findById(id);
        if (citiesOptional.isEmpty()) {
            throw new EntityNotFoundException("City not found");
        }

        return citiesOptional.get();
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public City getByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("city name not set");
        }
        return citiesRepositories.findByCityIgnoreCase(name)
                .orElseThrow(() -> new EntityNotFoundException("City not found by name: " + name));
    }

    public List<City> getAll() {
        return citiesRepositories.findAll();
    }

    public City add(City city) {

        if (city == null) {
            throw new NullPointerException("City not set");
        }

        return citiesRepositories.save(city);
    }

    public City update(City city) {
        if (!citiesRepositories.existsById(city.getId())) {
            throw new EntityNotFoundException("City not found");
        }
        return citiesRepositories.save(city);
    }
}
