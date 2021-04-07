package ru.diplom.FirePandaDelivery.Service;

import org.hibernate.Criteria;
import org.hibernate.FetchMode;
import org.hibernate.Hibernate;
import org.springframework.orm.hibernate5.HibernateCallback;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
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
        Optional<Cities> citiesOptional = citiesRepositories.findByCiti(name);
        if (citiesOptional.isEmpty()) {
            throw new EntityNotFoundException("Cities not found");
        }

        Cities cities = citiesOptional.get();
        Hibernate.initialize(cities.getCords());
        return cities;
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
