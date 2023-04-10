package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.repository.CatOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.CatOwnerService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class CatOwnerServiceImpl implements CatOwnerService {

    private final CatOwnerRepo catOwnerRepo;
    private final UserService userService;
    private final TrialPeriodService trialPeriodService;

    @Override
    public CatOwner create(CatOwner catOwner)  {
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now(), new ArrayList<>(), TrialPeriod.Result.IN_PROGRESS, catOwner.getTelegramId()));
        return catOwnerRepo.save(catOwner);
    }

    @Override
    public CatOwner create(Long id) {
        CatOwner catOwner = new CatOwner(userService.getById(id));
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now(), new ArrayList<>(), TrialPeriod.Result.IN_PROGRESS, id));
        return catOwnerRepo.save(catOwner);
    }

    @Override
    public CatOwner getById(Long id) {
        Optional<CatOwner> optionalCatOwner = catOwnerRepo.findById(id);
        if (optionalCatOwner.isEmpty()) {
            throw new NotFoundException("Хозяин кота не найден!");
        }
        return optionalCatOwner.get();
    }

    @Override
    public List<CatOwner> getAll() {
        List<CatOwner> all = catOwnerRepo.findAll();
        if (all.isEmpty()) {
            throw new NotFoundException("Хозяин кота не найден!");
        }
        return all;
    }

    @Override
    public CatOwner update(CatOwner catOwner) {
        if (catOwner.getTelegramId() == null || getById(catOwner.getTelegramId()) == null) {
            throw new NotFoundException("Хозяин кота не найден!");
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
