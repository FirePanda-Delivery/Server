package ru.diplom.fpd.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.diplom.fpd.model.Cities;
import ru.diplom.fpd.model.Courier;

import java.util.List;

public interface CourierRepositories extends JpaRepository<Courier, Long> {

    List<Courier> findByIsDeletedFalse();

    Courier findFirstByPhone(String phone);

    List<Courier> findByIsDeletedTrue();

    List<Courier> findAllByCity(Cities city);

  //  List<Courier> findAllByCity_Citi(String citi);

    List<Courier> findAllByCity_NormalizedCiti(String citi);







}
