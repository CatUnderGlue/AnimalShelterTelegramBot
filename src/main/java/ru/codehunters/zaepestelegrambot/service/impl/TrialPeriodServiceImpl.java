package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.TrialPeriodRepo;
import ru.codehunters.zaepestelegrambot.service.CatService;
import ru.codehunters.zaepestelegrambot.service.DogService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrialPeriodServiceImpl implements TrialPeriodService {

    private final TrialPeriodRepo trialPeriodRepo;
    private final CatService catService;
    private final DogService dogService;

    @Override
    public TrialPeriod create(TrialPeriod trialPeriod) {
        return trialPeriodRepo.save(trialPeriod);
    }

    @Override
    public TrialPeriod create(TrialPeriod trialPeriod, TrialPeriod.AnimalType animalType) {
        if (animalType.equals(TrialPeriod.AnimalType.CAT)) {
            catService.getById(trialPeriod.getAnimalId()).setOwnerId(trialPeriod.getOwnerId());
        } else if (animalType.equals(TrialPeriod.AnimalType.DOG)) {
            dogService.getById(trialPeriod.getAnimalId()).setOwnerId(trialPeriod.getOwnerId());
        }
        return trialPeriodRepo.save(trialPeriod);
    }

    @Override
    public TrialPeriod getById(Long id) {
        Optional<TrialPeriod> optionalTrialPeriod = trialPeriodRepo.findById(id);
        if (optionalTrialPeriod.isEmpty()) {
            throw new NotFoundException("Испытательный срок не найден!");
        }
        return optionalTrialPeriod.get();
    }

    @Override
    public List<TrialPeriod> getAll() {
        List<TrialPeriod> all = trialPeriodRepo.findAll();
        if (all.isEmpty()) {
            throw new NotFoundException("Испытательные сроки не найдены!");
        }
        return all;
    }

    @Override
    public List<TrialPeriod> getAllByOwnerId(Long ownerId) {
        List<TrialPeriod> allByOwnerId = trialPeriodRepo.findAllByOwnerId(ownerId);
        if (allByOwnerId.isEmpty()) {
            throw new NotFoundException("Испытательные сроки не найдены!");
        }
        return allByOwnerId;
    }

    @Override
    public TrialPeriod update(TrialPeriod trialPeriod) {
        TrialPeriod currentTrialPeriod = getById(trialPeriod.getId());
        EntityUtils.copyNonNullFields(trialPeriod, currentTrialPeriod);
        return trialPeriodRepo.save(currentTrialPeriod);
    }

    @Override
    public void delete(TrialPeriod trialPeriod) {
        trialPeriodRepo.delete(getById(trialPeriod.getId()));
    }

    @Override
    public void deleteById(Long id) {
        trialPeriodRepo.deleteById(getById(id).getId());
    }
}
