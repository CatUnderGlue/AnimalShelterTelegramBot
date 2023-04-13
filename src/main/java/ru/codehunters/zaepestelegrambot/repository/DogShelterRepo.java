package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;

import java.util.Optional;

@Repository
public interface DogShelterRepo extends JpaRepository<DogShelter, Long> {

    Optional<DogShelter> findByName(String name);

}
