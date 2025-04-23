package ru.diplom.fpd.repositories;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.City;
import ru.diplom.fpd.model.Courier;

public interface CourierRepositories extends JpaRepository<Courier, Long> {

    List<Courier> findByIsDeletedFalse();

    Courier findFirstByPhone(String phone);

    List<Courier> findByIsDeletedTrue();

    List<Courier> findAllByCity(City city);

    //  List<Courier> findAllByCity_Citi(String citi);

    List<Courier> findAllByCityCityIgnoreCase(String citi);


}
