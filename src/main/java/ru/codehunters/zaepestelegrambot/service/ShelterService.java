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
    T updateShelter(T shelter);

    /**
     * Выдача списка животных находящихся в приюте
     *
     * @return List со списком животных в приюте
     */
    List<D> giveAnimal(long id);

    /**
     * Выдача списка приютов
     *
     * @return List со списком приютов
     */
    List<T> getShelter();

    /**
     * добавление объекта в список животных в приюте
     *
     * @param animal животное
     * @param id     приюта
     * @return animal
     */
    T addAnimalList(D animal, long id);

    /**
     * Удаление объекта из списка животных в приюте
     *
     * @param animal животное
     * @return animal
     */
    T delAnimalList(D animal, long id);

    /**
     * Удаление приюта
     *
     * @param index номер
     */
    void delShelter(long index);
}
