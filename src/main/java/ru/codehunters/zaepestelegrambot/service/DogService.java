package ru.codehunters.zaepestelegrambot.service;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.Collection;

public interface DogService {
    /**
     * @param id
     * @return
     */
    Dog getById(Long id);

    /**
     * @param id
     * @return
     */
    Dog getByUserId(Long id);

    /**
     * @param Dog
     * @return
     */
     Dog create(Dog Dog);




    /**
     * @param Dog
     * @return
     */
     Dog update(Dog Dog);

    /**
     * @return
     */
     Collection<Dog> getAll();

    /**
     * @param id
     */
     void remove(Long id);
}
