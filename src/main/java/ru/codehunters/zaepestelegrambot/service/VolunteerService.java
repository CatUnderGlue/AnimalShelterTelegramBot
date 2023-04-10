package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;

import java.util.List;

public interface VolunteerService {
    /**
     * Создание и сохранение волонтёра в бд
     * @param volunteer Волонтёр для сохранения в бд
     * @return Сохранённый волонтёр
     */
    Volunteer create(Volunteer volunteer);

    /**
     * Получение волонтёра по id
     * @param id Id волонтёра
     * @return Полученный из бд волонтёр
     * @exception NotFoundException Если в базе нет волонтёра с указанным id
     */
    Volunteer getById(Long id);

    /**
     * Получение волонтёра по id
     * @return Список всех волонтёров
     * @exception NotFoundException Если база с волонтёрами пустая
     */
    List<Volunteer> getAll();

    /**
     * Изменение волонтёра
     * @param volunteer Волонтёр
     * @return Изменённый волонтёр
     * @exception NotFoundException Если у передаваемого волонтёра нет id или в базе нет волонтёра с указанным id
     */
    Volunteer update(Volunteer volunteer);

    /**
     * Удаление волонтёра через объект
     * @param volunteer Волонтёр, который уже есть в бд
     * @exception NotFoundException Если в базе нет волонтёра с указанным id
     */
    void delete(Volunteer volunteer);

    /**
     * Удаление волонтёра по id
     * @param id Id волонтёра
     * @exception NotFoundException Если в базе нет волонтёра с указанным id
     */
    void deleteById(Long id);
}
