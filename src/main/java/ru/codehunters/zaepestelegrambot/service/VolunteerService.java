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
     * @return Список всех волонтёров
     */
    List<Volunteer> getAll();

    /**
     * @param volunteer Волонтёр, который уже есть в бд
     */
    void delete(Volunteer volunteer);
}
