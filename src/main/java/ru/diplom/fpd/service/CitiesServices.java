package ru.diplom.fpd.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import ru.diplom.fpd.model.Cities;
import ru.diplom.fpd.repositories.CitiesRepositories;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Optional;

@Service
public class CitiesServices {


    private CitiesRepositories citiesRepositories;

    public CitiesServices(CitiesRepositories citiesRepositories) {
        this.citiesRepositories = citiesRepositories;
    }

    @Transactional()
    public Cities get(long id) {
        Optional<Cities> citiesOptional = citiesRepositories.findById(id);
        if (citiesOptional.isEmpty()) {
            throw new EntityNotFoundException("Cities not found");
        }

        return citiesOptional.get();
    }

    @Transactional(propagation= Propagation.REQUIRED)
    public Cities getByName(String name) {
        if (name == null || name.isEmpty()) {
            throw new NullPointerException("city name not set");
        }
        return citiesRepositories.findByCityIgnoreCase(name).orElseThrow(EntityNotFoundException::new);
    }

    public List<Cities> getAll() {
        return citiesRepositories.findAll();
    }

    public Cities add(Cities cities) {

        if (cities == null) {
            throw new NullPointerException("Cities not set");
        }

        return citiesRepositories.save(cities);
    }

    public Cities update(Cities cities) {
        if (!citiesRepositories.existsById(cities.getId())) {
            throw new EntityNotFoundException("City not found");
        }
        return citiesRepositories.save(cities);
    }
}
