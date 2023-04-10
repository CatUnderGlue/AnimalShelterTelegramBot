package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.TrialPeriodNotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.TrialPeriodRepo;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrialPeriodServiceImpl implements TrialPeriodService {

    private final TrialPeriodRepo trialPeriodRepo;

    @Override
    public Long create(TrialPeriod trialPeriod) {
        return trialPeriodRepo.save(trialPeriod).getId();
    }

    @Override
    public TrialPeriod getById(Long id) {
        Optional<TrialPeriod> optionalTrialPeriod = trialPeriodRepo.findById(id);
        if (optionalTrialPeriod.isEmpty()){
            throw new TrialPeriodNotFoundException();
        }
        return optionalTrialPeriod.get();
    }

    @Override
    public List<TrialPeriod> getAll() {
        List<TrialPeriod> all = trialPeriodRepo.findAll();
        if (all.isEmpty()) {
            throw new TrialPeriodNotFoundException();
        }
        return all;
    }

    @Override
    public List<TrialPeriod> getAllByOwnerId(Long ownerId) {
        List<TrialPeriod> allByOwnerId = trialPeriodRepo.findAllByOwnerId(ownerId);
        if (allByOwnerId.isEmpty()) {
            throw new TrialPeriodNotFoundException();
        }
        return allByOwnerId;
    }

    @Override
    public TrialPeriod update(TrialPeriod trialPeriod) {
        if (trialPeriod.getId() == null || getById(trialPeriod.getId()) == null) {
            throw new TrialPeriodNotFoundException();
        }
        return trialPeriodRepo.save(trialPeriod);
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
