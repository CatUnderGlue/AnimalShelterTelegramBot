package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;

@Repository
public interface DogShelterRepo extends JpaRepository<DogShelter, Long> {

}
