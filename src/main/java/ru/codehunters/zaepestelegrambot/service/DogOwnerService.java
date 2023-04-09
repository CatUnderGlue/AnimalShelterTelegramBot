package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;

import java.util.List;

public interface DogOwnerService {

    /**
     * Создание и сохранение хозяина собаки в бд
     * @param dogOwner Хозяин собаки для сохранения в бд
     * @return Сохранённый хозяин собаки
     */
    DogOwner create(DogOwner dogOwner);

    /**
     * Создание хозяина собаки в бд из пользователя
     * @param user Пользователь из бота для сохранения в бд в качестве хозяина собаки
     * @return Сохранённый хозяин собаки
     */
    DogOwner create(User user);

    /**
     * Получение хозяина собаки по id
     * @param id Id хозяина собаки
     * @return Полученный из бд хозяин собаки
     */
    DogOwner getById(Long id);

    /**
     * @return Список всех владельцев собак
     */
    List<DogOwner> getAll();

    /**
     * Изменение хозяина собаки
     * @param dogOwner хозяин собаки
     * @return Изменённый хозяин собаки
     */
    DogOwner update(DogOwner dogOwner);

    /**
     * @param dogOwner хозяин собаки, который уже есть в бд
     */
    void delete(DogOwner dogOwner);

    /**
     * Удаление хозяина собаки по id
     * @param id Id хозяина собаки
     */
    void deleteById(Long id);
}
