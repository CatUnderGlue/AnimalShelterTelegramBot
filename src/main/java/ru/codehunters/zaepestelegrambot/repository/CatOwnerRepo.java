package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;

@Repository
public interface CatOwnerRepo extends JpaRepository<CatOwner,Long> {

}