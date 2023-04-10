package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;



public interface ShelterValidationService {
    /**
     * Проверка записи приютов на null и ""
     * Сейчас методы одинаковые, но
     * есть возможность предъявлять разные требования приютам
     *
     * @param shelter приют
     * @return boolean
     */
    boolean isCompleteRecord(CatShelter shelter);

    /**
     * Проверка записи приютов на null и ""
     * Сейчас методы одинаковые, но
     * есть возможность предъявлять разные требования
     *
     * @param shelter приют
     * @return boolean
     */
    boolean isCompleteRecord(DogShelter shelter);

    CatShelter updateCompleteRecord(CatShelter shelter, CatShelter shelterId);



    DogShelter updateCompleteRecord(DogShelter shelter, DogShelter shelterId);
}
