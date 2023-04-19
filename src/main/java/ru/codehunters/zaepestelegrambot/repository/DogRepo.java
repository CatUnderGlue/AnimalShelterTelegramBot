package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.List;

@Repository
public interface DogRepo extends JpaRepository<Dog, Long> {
    List<Dog> findAllByOwnerId(Long id); // Поиск псов по telegramId пользователя
}
