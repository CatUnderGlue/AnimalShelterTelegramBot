package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.repository.CatRepo;
import ru.codehunters.zaepestelegrambot.service.CatService;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatServiceImpl implements CatService {
    private final CatRepo catRepo;


    @Override
    public Cat create(Cat cat) {
        return catRepo.save(cat);
    }

    @Override
    public Cat getById(Long id) {
        Optional<Cat> optionalCat = catRepo.findById(id);
        if (optionalCat.isEmpty()) {
            throw new NotFoundException("Кот не найден!");
        }
        return optionalCat.get();
    }



    @Override
    public Cat getByUserId(Long id) {
        Optional<Cat> optionalCat = catRepo.findByOwnerId(id);
        if (optionalCat.isEmpty()) {
            throw new NotFoundException("Хозяин кота не найден!");
        }
        return optionalCat.get();
    }

    @Override
    public Cat update(Cat cat) {
        Optional<Cat> catId = catRepo.findById(cat.getId());
        if (catId.isEmpty()){
            throw new NotFoundException("Кота нет");
        }
        Cat currentCat = catId.get();
        EntityUtils.copyNonNullFields(cat, currentCat);
        return catRepo.save(currentCat);
    }

    @Override
    public List<Cat> getAll() {
        return catRepo.findAll();
    }

    @Override
    public void remove(Long id) {
        catRepo.deleteById(id);
    }
}
