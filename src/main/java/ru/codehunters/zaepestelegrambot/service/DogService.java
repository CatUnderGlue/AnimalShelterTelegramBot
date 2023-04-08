package ru.codehunters.zaepestelegrambot.service;


import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.Collection;

public interface DogService {
    Dog getById(Long id);
     Dog create(Dog Dog);
     Dog update(Dog Dog);
     Collection<Dog> getAll();
     void remove(Long id);
}
