package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.repository.DogRepo;
import ru.codehunters.zaepestelegrambot.service.DogService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DogServiceImpl implements DogService {
    private final DogRepo dogRepo;

    @Override
    public Dog create(Dog dog) {
        return dogRepo.save(dog);
    }

    @Override
    public Dog getById(Long id) {
        Optional<Dog> optionalDog = dogRepo.findById(id);
        if (optionalDog.isEmpty()) {
            throw new NotFoundException("Пёс не найден!");
        }
        return optionalDog.get();

    }

    @Override
    public List<Dog> getAllByUserId(Long id) {
        List<Dog> dogList = dogRepo.findAllByOwnerId(id);
        if (dogList.isEmpty()) {
            throw new NotFoundException("У хозяина нет собак!");
        }
        return dogList;
    }

    @Override
    public Dog update(Dog dog) {
        Optional<Dog> dogId = dogRepo.findById(dog.getId());
        if (dogId.isEmpty()) {
            throw new NotFoundException("Пса нет");
        }
        Dog currentDog = dogId.get();
        EntityUtils.copyNonNullFields(dog, currentDog);
        return dogRepo.save(currentDog);
    }

    @Override
    public List<Dog> getAll() {
        return dogRepo.findAll();
    }

    @Override
    public void remove(Long id) {
        dogRepo.deleteById(getById(id).getId());
    }
}
