package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.AlreadyExistsException;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;
import ru.codehunters.zaepestelegrambot.repository.DogOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.DogOwnerService;
import ru.codehunters.zaepestelegrambot.service.DogService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@RequiredArgsConstructor
public class DogOwnerServiceImpl implements DogOwnerService {

    private final DogOwnerRepo dogOwnerRepo;
    private final UserService userService;
    private final DogService dogService;
    private final TrialPeriodService trialPeriodService;

    @Override
    public DogOwner create(DogOwner dogOwner, TrialPeriod.AnimalType animalType, Long animalId) {
        if (dogService.getById(animalId).getOwnerId() != null) {
            throw new AlreadyExistsException("У этой собаки уже есть хозяин!");
        }
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now().minusDays(1), new ArrayList<>(),
                TrialPeriod.Result.IN_PROGRESS, dogOwner.getTelegramId(), animalType, animalId), animalType);
        dogService.getById(animalId).setOwnerId(dogOwner.getTelegramId());
        return dogOwnerRepo.save(dogOwner);
    }

    @Override
    public DogOwner create(Long id, TrialPeriod.AnimalType animalType, Long animalId) {
        if (dogService.getById(animalId).getOwnerId() != null) {
            throw new AlreadyExistsException("У этой собаки уже есть хозяин!");
        }
        DogOwner dogOwner = new DogOwner(userService.getById(id));
        trialPeriodService.create(new TrialPeriod(LocalDate.now(), LocalDate.now().plusDays(30),
                LocalDate.now().minusDays(1), new ArrayList<>(),
                TrialPeriod.Result.IN_PROGRESS, id, animalType, animalId), animalType);
        dogService.getById(animalId).setOwnerId(id);
        return dogOwnerRepo.save(dogOwner);
    }

    @Override
    public DogOwner getById(Long id) {
        Optional<DogOwner> optionalDogOwner = dogOwnerRepo.findById(id);
        if (optionalDogOwner.isEmpty()) {
            throw new NotFoundException("Владелец собаки не найден!");
        }
        return optionalDogOwner.get();
    }

    @Override
    public List<DogOwner> getAll() {
        List<DogOwner> all = dogOwnerRepo.findAll();
        if (all.isEmpty()) {
            throw new NotFoundException("Владелец собаки не найден!");
        }
        return all;
    }

    @Override
    public DogOwner update(DogOwner dogOwner) {
        Optional<DogOwner> optionalDogOwner = dogOwnerRepo.findById(dogOwner.getTelegramId());
        if (optionalDogOwner.isEmpty()) {
            throw new NotFoundException("Владелец собаки не найден!");
        }
        DogOwner currentDogOwner = optionalDogOwner.get();
        EntityUtils.copyNonNullFields(dogOwner, currentDogOwner);
        return dogOwnerRepo.save(currentDogOwner);
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
