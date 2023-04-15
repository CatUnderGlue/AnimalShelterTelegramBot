package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.AlreadyExistsException;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.repository.CatOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.CatOwnerService;
import ru.codehunters.zaepestelegrambot.service.CatService;
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
    private final CatService catService;
    private final TrialPeriodService trialPeriodService;

    @Override
    public CatOwner create(CatOwner catOwner, TrialPeriod.AnimalType animalType, Long animalId)  {
        if (catService.getById(animalId).getOwnerId() != null) {
            throw new AlreadyExistsException("У этого кота уже есть хозяин!");
        }
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now().minusDays(1), new ArrayList<>(),
                TrialPeriod.Result.IN_PROGRESS, catOwner.getTelegramId(), animalType, animalId), animalType);
        catService.getById(animalId).setOwnerId(catOwner.getTelegramId());
        return catOwnerRepo.save(catOwner);
    }

    @Override
    public CatOwner create(Long id, TrialPeriod.AnimalType animalType, Long animalId) {
        if (catService.getById(animalId).getOwnerId() != null) {
            throw new AlreadyExistsException("У этого кота уже есть хозяин!");
        }
        CatOwner catOwner = new CatOwner(userService.getById(id));
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now().minusDays(1), new ArrayList<>(),
                TrialPeriod.Result.IN_PROGRESS, id, animalType, animalId), animalType);
        catService.getById(animalId).setOwnerId(id);
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
            throw new NotFoundException("Владельцев котов нет!");
        }
        return all;
    }

    @Override
    public CatOwner update(CatOwner catOwner) {
        Optional<CatOwner> optionalCatOwner = catOwnerRepo.findById(catOwner.getTelegramId());
        if (optionalCatOwner.isEmpty()) {
            throw new NotFoundException("Владелец кота не найден!");
        }
        CatOwner currentCatOwner = optionalCatOwner.get();
        EntityUtils.copyNonNullFields(catOwner, currentCatOwner);
        return catOwnerRepo.save(currentCatOwner);
    }

    @Override
    public void delete(CatOwner catOwner) {
        if (catOwner.getCatList() != null) {
            for (Cat cat : catOwner.getCatList()) {
                cat.setOwnerId(null);
                catService.update(cat);
            }
        }
        catOwnerRepo.delete(getById(catOwner.getTelegramId()));
    }

    @Override
    public void deleteById(Long id) {
        CatOwner catOwner = getById(id);
        if (catOwner.getCatList() != null) {
            for (Cat cat : catOwner.getCatList()) {
                cat.setOwnerId(null);
                catService.update(cat);
            }
        }
        catOwnerRepo.deleteById(id);
    }
}
