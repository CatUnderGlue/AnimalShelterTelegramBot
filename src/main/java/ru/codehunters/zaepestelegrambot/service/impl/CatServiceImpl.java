package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.CatNotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.repository.CatRepo;
import ru.codehunters.zaepestelegrambot.service.CatService;


import java.util.Collection;
@Service
@RequiredArgsConstructor
public class CatServiceImpl implements CatService {
    private final CatRepo catRepo;


    @Override
    public Cat getById(Long id) {
        return catRepo.findById(id).orElseThrow(CatNotFoundException::new);
    }

    @Override
    public Cat getByUserId(Long id) {
        return catRepo.findByOwnerId(id).orElseThrow(CatNotFoundException::new);
    }
    @Override
    public Cat create(Cat cat) {
        return catRepo.save(cat);
    }

    @Override
    public Cat update(Cat cat) {
        if (cat.getId() != null && getById(cat.getId()) != null){
            return catRepo.save(cat);
        }
        throw new CatNotFoundException();
    }

    @Override
    public Collection<Cat> getAll() {
        return catRepo.findAll();
    }

    @Override
    public void remove(Long id) {
        catRepo.deleteById(id);
    }
}
