package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.CatOwnerNotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.repository.CatOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.CatOwnerService;

import java.util.List;
import java.util.Optional;

@Service
public class CatOwnerServiceImpl implements CatOwnerService {

    private final CatOwnerRepo catOwnerRepo;

    public CatOwnerServiceImpl(CatOwnerRepo catOwnerRepo) {
        this.catOwnerRepo = catOwnerRepo;
    }

    @Override
    public CatOwner create(CatOwner catOwner)  {
        return catOwnerRepo.save(catOwner);
    }

    @Override
    public CatOwner create(User user) {
        CatOwner catOwner = new CatOwner(user);
        return catOwnerRepo.save(catOwner);
    }

    @Override
    public CatOwner getById(Long id) {
        Optional<CatOwner> optionalCatOwner = catOwnerRepo.findById(id);
        if (optionalCatOwner.isEmpty()) {
            throw new CatOwnerNotFoundException();
        }
        return optionalCatOwner.get();
    }

    @Override
    public List<CatOwner> getAll() {
        List<CatOwner> all = catOwnerRepo.findAll();
        if (all.isEmpty()) {
            throw new CatOwnerNotFoundException();
        }
        return all;
    }

    @Override
    public CatOwner update(CatOwner catOwner) {
        if (catOwner.getTelegramId() == null || getById(catOwner.getTelegramId()) == null) {
            throw new CatOwnerNotFoundException();
        }
        return catOwnerRepo.save(catOwner);
    }

    @Override
    public void delete(CatOwner catOwner) {
        catOwnerRepo.delete(getById(catOwner.getTelegramId()));
    }

    @Override
    public void deleteById(Long id) {
        catOwnerRepo.deleteById(getById(id).getTelegramId());
    }
}
