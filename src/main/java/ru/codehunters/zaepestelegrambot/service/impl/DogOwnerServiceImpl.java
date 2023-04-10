package ru.codehunters.zaepestelegrambot.service.impl;


import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.DogOwnerNotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;
import ru.codehunters.zaepestelegrambot.repository.DogOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.DogOwnerService;

import java.util.List;
import java.util.Optional;

@Service
public class DogOwnerServiceImpl implements DogOwnerService {

    private final DogOwnerRepo dogOwnerRepo;

    public DogOwnerServiceImpl(DogOwnerRepo dogOwnerRepo) {
        this.dogOwnerRepo = dogOwnerRepo;
    }

    @Override
    public DogOwner create(DogOwner dogOwner)  {
        return dogOwnerRepo.save(dogOwner);
    }

    @Override
    public DogOwner create(User user) {
        DogOwner dogOwner = new DogOwner(user);
        return dogOwnerRepo.save(dogOwner);
    }

    @Override
    public DogOwner getById(Long id) {
        Optional<DogOwner> optionalDogOwner = dogOwnerRepo.findById(id);
        if (optionalDogOwner.isEmpty()) {
            throw new DogOwnerNotFoundException();
        }
        return optionalDogOwner.get();
    }

    @Override
    public List<DogOwner> getAll() {
        List<DogOwner> all = dogOwnerRepo.findAll();
        if (all.isEmpty()) {
            throw new DogOwnerNotFoundException();
        }
        return all;
    }

    @Override
    public DogOwner update(DogOwner dogOwner) {
        if (dogOwner.getTelegramId() == null || getById(dogOwner.getTelegramId()) == null) {
            throw new DogOwnerNotFoundException();
        }
        return dogOwnerRepo.save(dogOwner);
    }

    @Override
    public void delete(DogOwner dogOwner) {
        dogOwnerRepo.delete(getById(dogOwner.getTelegramId()));
    }

    @Override
    public void deleteById(Long id) {
        dogOwnerRepo.deleteById(getById(id).getTelegramId());
    }
}
