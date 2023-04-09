package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.Volunteer;

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
     */
    Volunteer getById(Long id);

    /**
     * @return Список всех волонтёров
     */
    List<Volunteer> getAll();

    /**
     * Изменение волонтёра
     * @param volunteer Волонтёр
     * @return Изменённый волонтёр
     */
    Volunteer update(Volunteer volunteer);

    /**
     * @param volunteer Волонтёр, который уже есть в бд
     */
    void delete(Volunteer volunteer);

    /**
     * Удаление волонтёра по id
     * @param id Id волонтёра
     */
    void deleteById(Long id);
}
