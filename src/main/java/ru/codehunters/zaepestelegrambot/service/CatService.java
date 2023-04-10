package ru.codehunters.zaepestelegrambot.service;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;

import java.util.Collection;

public interface CatService {

     Cat getById(Long id);

     Cat getByUserId(Long id);

     Cat create(Cat cat);
     Cat update(Cat cat);
     Collection<Cat> getAll();
     void remove(Long id);
}
