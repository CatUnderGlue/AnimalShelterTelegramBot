package ru.codehunters.zaepestelegrambot.service;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;

import java.util.Collection;

public interface CatService {

     /**
      * @param id
      * @return
      */
     Cat getById(Long id);

     Cat getByUserId(Long id);

     Cat create(Cat cat);

     /**
      * @param cat
      * @return
      */
     Cat update(Cat cat);

     /**
      * @return
      */
     Collection<Cat> getAll();

     /**
      * @param id
      */
     void remove(Long id);
}
