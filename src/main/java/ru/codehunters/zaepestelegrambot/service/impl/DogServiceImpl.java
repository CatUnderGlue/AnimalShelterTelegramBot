package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.CatNotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.repository.DogRepo;
import ru.codehunters.zaepestelegrambot.service.DogService;
import java.util.Collection;
@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    private final DogRepo dogRepo;

    @Override
    public Dog getById(Long id) {
       return dogRepo.findById(id).orElseThrow(CatNotFoundException::new);
    }

    @Override
    public Dog create(Dog dog) {
        return dogRepo.save(dog);
    }

    @Override
    public Dog update(Dog dog) {
        if (dog.getId() != null && getById(dog.getId()) != null){
            return dogRepo.save(dog);
        }
        throw new CatNotFoundException();
    }

    @Override
    public Collection<Dog> getAll() {
        return dogRepo.findAll();
    }

    @Override
    public void remove(Long id) {
        dogRepo.deleteById(id);
    }
}
