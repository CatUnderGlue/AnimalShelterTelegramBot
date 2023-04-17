package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.Optional;

@Repository
public interface DogRepo extends JpaRepository<Dog, Long> {
    Optional<Dog> findByOwnerId(Long id); // Поиск пса по telegramId пользователя
}
