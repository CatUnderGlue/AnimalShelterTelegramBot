package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.TrialPeriod;

import java.util.List;

public interface TrialPeriodService {
    /**
     * Сохранение испытательного периода в бд (Он же отвечает за обновление уже существующего отчёта)
     * @param trialPeriod Испытательный срок для сохранения в бд
     * @return id испытательного срока
     */
    Long create(TrialPeriod trialPeriod);

    /**
     * Получение испытательного срока из бд по id
     * @param id id испытательного срока
     * @return Испытательный срок
     */
    TrialPeriod getById(Long id);

    /**
     * @return Список всех отчётов
     */
    List<TrialPeriod> getAll();

    /**
     * @param ownerId id хозяина, по которому будет идти поиск
     * @return Список испытательных сроков по хозяину
     */
    List<TrialPeriod> getAllByOwnerId(Long ownerId);

    /**
     * Изменение существующего испытательного срока
     * @param trialPeriod Испытательный срок
     * @return Обновлённый испытательный срок
     */
    TrialPeriod update(TrialPeriod trialPeriod);

    /**
     * Удаление полученного из бд испытательного срока
     * @param trialPeriod Испытательный срок, который уже есть в бд
     */
    void delete(TrialPeriod trialPeriod);

    /**
     * Удаление испытательного срока по id
     * @param id id испытательного срока
     */
    void deleteById(Long id);
}
