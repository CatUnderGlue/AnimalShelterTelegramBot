package ru.codehunters.zaepestelegrambot.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;

import java.util.List;

@Repository
public interface CatRepo extends JpaRepository<Cat, Long> {

    List<Cat> findAllByOwnerId(Long id); // Поиск котов по telegramId пользователя

}
