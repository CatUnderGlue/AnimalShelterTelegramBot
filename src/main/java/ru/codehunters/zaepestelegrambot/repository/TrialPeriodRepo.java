package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;

import java.util.List;

public interface TrialPeriodRepo extends JpaRepository<TrialPeriod, Long> {
    List<TrialPeriod> findAllByOwnerId(Long ownerId);
}
