package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.service.ShelterValidationService;
@Service
public class ShelterValidationServiceImpl implements ShelterValidationService {


    @Override
    public boolean isCompleteRecord(CatShelter shelter) {
        return shelter.getName().isEmpty()
                || shelter.getAboutMe().isEmpty()
                || shelter.getLocation().isEmpty()
                || shelter.getSafetyAdvice().isEmpty()
                || shelter.getTimetable().isEmpty()
                || shelter.getSecurity().isEmpty();
    }

    @Override
    public boolean isCompleteRecord(DogShelter shelter) {
        return shelter.getName().isEmpty()
                || shelter.getAboutMe().isEmpty()
                || shelter.getLocation().isEmpty()
                || shelter.getSafetyAdvice().isEmpty()
                || shelter.getTimetable().isEmpty()
                || shelter.getSecurity().isEmpty();
    }
}
