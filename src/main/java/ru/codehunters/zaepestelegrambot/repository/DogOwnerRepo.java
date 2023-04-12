package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;

@Repository
public interface DogOwnerRepo extends JpaRepository<DogOwner,Long> {

}