package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.TrialPeriodRepo;
import ru.codehunters.zaepestelegrambot.service.impl.TrialPeriodServiceImpl;

import java.util.List;

public interface TrialPeriodService {
    /**
     * Сохранение испытательного периода в бд <br>
     * Используется метод репозитория {@link TrialPeriodRepo#save(Object)}
     * @param trialPeriod Испытательный срок для сохранения в бд
     * @return Созданный испытательный срок
     */
    TrialPeriod create(TrialPeriod trialPeriod);

    /**
     * Сохранение испытательного периода в бд при создании владельца<br>
     * Используется метод репозитория {@link TrialPeriodRepo#save(Object)}
     * @param trialPeriod Испытательный срок для сохранения в бд
     * @param animalType Тип животного
     * @return Созданный испытательный срок
     */
    TrialPeriod create(TrialPeriod trialPeriod, TrialPeriod.AnimalType animalType);

    /**
     * Получение испытательного срока из бд по id<br>
     * Используется метод репозитория {@link TrialPeriodRepo#findById(Object)}
     * @param id id испытательного срока
     * @return Испытательный срок
     * @exception NotFoundException Если в базе нет испытательного срока с указанным id
     */
    TrialPeriod getById(Long id);

    /**
     * Получение всех отчётов<br>
     * Используется метод репозитория {@link TrialPeriodRepo#findAll()}
     * @return Список всех отчётов
     * @exception NotFoundException Если база с испытательными сроками пустая
     */
    List<TrialPeriod> getAll();

    /**
     * Получение всех отчётов конкретного пользователя<br>
     * Используется метод репозитория {@link TrialPeriodRepo#findAllByOwnerId(Long)}
     * @param ownerId id хозяина, по которому будет идти поиск
     * @return Список испытательных сроков по хозяину
     * @exception NotFoundException Если у пользователя отсутствуют испытательные сроки
     */
    List<TrialPeriod> getAllByOwnerId(Long ownerId);

    /**
     * Изменение существующего испытательного срока<br>
     * Используется метод этого же сервиса {@link TrialPeriodServiceImpl#getById(Long)}
     * @param trialPeriod Испытательный срок
     * @return Обновлённый испытательный срок
     * @exception NotFoundException Если у передаваемого испытательного срока нет id или в базе нет испытательного срока с указанным id
     */
    TrialPeriod update(TrialPeriod trialPeriod);

    /**
     * Удаление полученного из бд испытательного срока<br>
     * Используется метод этого же сервиса {@link TrialPeriodServiceImpl#getById(Long)}
     * @param trialPeriod Испытательный срок, который уже есть в бд
     * @exception NotFoundException Если в базе нет испытательного срока с указанным id
     */
    void delete(TrialPeriod trialPeriod);

    /**
     * Удаление испытательного срока по id<br>
     * Используется метод этого же сервиса {@link TrialPeriodServiceImpl#getById(Long)}
     * @param id id испытательного срока
     * @exception NotFoundException Если в базе нет испытательного срока с указанным id
     */
    void deleteById(Long id);
}
