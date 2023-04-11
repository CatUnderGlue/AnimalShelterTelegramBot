package ru.codehunters.zaepestelegrambot.service;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.Collection;

public interface DogService {
    /**
      * Получение собаки по id
      * @return Полученный из бд собаки
      */
    Dog getById(Long id);

/**
      * @param id Id хозяина
      * @return Полученный из бд собака по id хозяина
      */
    Dog getByUserId(Long id);

     /**
      * Создание собаки
      * @param dog объект собака
      * @return Созданная собака
      */
     Dog create(Dog dog);

     /**
      * Обновление данных собак
      * @param dog объект собак
      * @return обновленный собак в бд
      */
     Dog update(Dog dog);

     /**
      * Получение всех собак
      * @return Коллекция всех собак
      */
     Collection<Dog> getAll();

     /**
      * Удаление собаки по ID
      * @param id собаки
      */
     void remove(Long id);
}
