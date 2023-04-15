package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;

import java.util.Optional;


@Repository
public interface CatShelterRepo extends JpaRepository<CatShelter, Long> {

    Optional<CatShelter> findByName(String name);

}
