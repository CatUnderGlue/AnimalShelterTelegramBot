package ru.codehunters.zaepestelegrambot.service;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;


import java.util.Collection;

public interface CatService {

     /**
      * Получение кота по id
      * @return Полученный из бд кот
      */
     Cat getById(Long id);

     /**
      * @param id Id хозяина
      * @return Полученный из бд кот по id хозяина
      */
     Cat getByUserId(Long id);

     /**
      * Создание кота
      * @param cat объект кот
      * @return Созданный кот
      */
     Cat create(Cat cat);

     /**
      * Обновление данных кота
      * @param cat объект кот
      * @return обновленный кот в бд
      */
     Cat update(Cat cat);

     /**
      * Получение всех котов
      * @return Коллекция всех котов
      */
     Collection<Cat> getAll();

     /**
      * Удаление кота по ID
      * @param id кота
      */
     void remove(Long id);
}
