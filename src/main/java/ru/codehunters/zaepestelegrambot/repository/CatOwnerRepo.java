package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;

import java.util.Optional;

@Repository
public interface CatOwnerRepo extends JpaRepository<CatOwner, Long> {

    Optional<CatOwner> findByTelegramId(Long telegramId);

}