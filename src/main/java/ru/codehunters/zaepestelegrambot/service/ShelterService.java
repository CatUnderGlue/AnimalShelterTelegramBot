package ru.codehunters.zaepestelegrambot.service;

import java.util.List;

public interface ShelterService<T, D> {
    /**
     * Создать приют
     *
     * @param shelter объект приют
     * @return shelter объект приют
     */

    T addShelter(T shelter);

    /**
     * Обновление данных приюта
     *
     * @param shelter объект приют
     * @return shelter объект приют
     */
    T updateShelter(T shelter, long id);


    /**
     * Выдача списка приютов
     *
     * @return List со списком приютов
     */
    List<T> getShelter();
    /**
     * Выдача списка животных приютов
     * @param index индекс приюта
     * @return List со списком приютов
     */
    List<D> getAnimal(long index);


    /**
     * Удаление приюта
     *
     * @param index номер
     */
    String delShelter(long index);
}
