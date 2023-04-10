package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.exception.CatNotFoundException;
import ru.codehunters.zaepestelegrambot.exception.DogNotFoundException;
import ru.codehunters.zaepestelegrambot.exception.ShelterException;

import java.util.List;

public interface ShelterService<T, D> {
    /**
     * Сохранить приют в БД
     * @param shelter объект приют
     * @return сохранение приюта в БД
     * @exception ShelterException если, какое-либо поле содержит null или ""
     */

    T addShelter(T shelter);

    /**
     * Обновление данных приюта
     * @param id идентификатор приюта
     * @param shelter объект приют
     * @return shelter объект приют
     * @exception CatNotFoundException если, не удалось найти кошачий приют по id
     * @exception DogNotFoundException если, не удалось найти собачий приют по id
     * @exception ShelterException если, какое-либо поле содержит null или ""
     */
    T updateShelter(T shelter, long id);


    /**
     * Выдача списка приютов
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
     * @param index номер
     * @exception DogNotFoundException не найден по индексу собачий приют
     * @exception CatNotFoundException не найден по индексу кошачий приют
     */
    String delShelter(long index);
}
