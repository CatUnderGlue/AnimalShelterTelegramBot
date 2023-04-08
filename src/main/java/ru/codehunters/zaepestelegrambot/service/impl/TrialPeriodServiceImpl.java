package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.TrialPeriodRepo;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.util.List;
import java.util.Optional;

@Service
public class TrialPeriodServiceImpl implements TrialPeriodService {

    private final TrialPeriodRepo trialPeriodRepo;

    public TrialPeriodServiceImpl(TrialPeriodRepo trialPeriodRepo) {
        this.trialPeriodRepo = trialPeriodRepo;
    }

    @Override
    public Long create(TrialPeriod trialPeriod) {
        return trialPeriodRepo.save(trialPeriod).getId();
    }

    @Override
    public TrialPeriod getById(Long id) {
        Optional<TrialPeriod> optionalTrialPeriod = trialPeriodRepo.findById(id);
        if (!optionalTrialPeriod.isPresent()){
            return null;
        }
        return optionalTrialPeriod.get();
    }

    @Override
    public List<TrialPeriod> getAll() {
        return trialPeriodRepo.findAll();
    }

    @Override
    public List<TrialPeriod> getAllByOwnerId(Long ownerId) {
        return trialPeriodRepo.findAllByOwnerId(ownerId);
    }

    @Override
    public void delete(TrialPeriod trialPeriod) {
        trialPeriodRepo.delete(trialPeriod);
    }

    @Override
    public void deleteById(Long id) {
        trialPeriodRepo.deleteById(id);
    }
}
