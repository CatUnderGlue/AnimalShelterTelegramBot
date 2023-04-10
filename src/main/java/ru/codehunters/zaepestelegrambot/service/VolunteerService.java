package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.repository.VolunteerRepo;
import ru.codehunters.zaepestelegrambot.service.impl.VolunteerServiceImpl;

import java.util.List;

public interface VolunteerService {
    /**
     * Создание и сохранение волонтёра в бд<br>
     * Используется метод репозитория {@link VolunteerRepo#save(Object)}
     * @param volunteer Волонтёр для сохранения в бд, не может быть null
     * @return Сохранённый волонтёр
     */
    Volunteer create(Volunteer volunteer);

    /**
     * Получение волонтёра по id<br>
     * Используется метод репозитория {@link VolunteerRepo#findById(Object)}
     * @param id Id волонтёра, не может быть null
     * @return Полученный из бд волонтёр
     * @exception NotFoundException Если в базе нет волонтёра с указанным id
     */
    Volunteer getById(Long id);

    /**
     * Получение волонтёра по id<br>
     * Используется метод репозитория {@link VolunteerRepo#findAll()}
     * @return Список всех волонтёров
     * @exception NotFoundException Если база с волонтёрами пустая
     */
    List<Volunteer> getAll();

    /**
     * Изменение волонтёра<br>
     * Используется метод этого же сервиса {@link VolunteerServiceImpl#getById(Long)}
     * @param volunteer Волонтёр, не может быть null
     * @return Изменённый волонтёр
     * @exception NotFoundException Если у передаваемого волонтёра нет id или в базе нет волонтёра с указанным id
     */
    Volunteer update(Volunteer volunteer);

    /**
     * Удаление волонтёра через объект<br>
     * Используется метод этого же сервиса {@link VolunteerServiceImpl#getById(Long)}
     * @param volunteer Волонтёр, который уже есть в бд
     * @exception NotFoundException Если в базе нет волонтёра с указанным id
     */
    void delete(Volunteer volunteer);

    /**
     * Удаление волонтёра по id<br>
     * Используется метод этого же сервиса {@link VolunteerServiceImpl#getById(Long)}
     * @param id Id волонтёра
     * @exception NotFoundException Если в базе нет волонтёра с указанным id
     */
    void deleteById(Long id);
}
