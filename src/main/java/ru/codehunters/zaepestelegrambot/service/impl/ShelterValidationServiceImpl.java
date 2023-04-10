package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.service.ShelterValidationService;

@Service
public class ShelterValidationServiceImpl implements ShelterValidationService {


    @Override
    public boolean isCompleteRecord(CatShelter shelter) {
        return !shelter.getName().isEmpty()
                && !shelter.getAboutMe().isEmpty()
                && !shelter.getLocation().isEmpty()
                && !shelter.getSafetyAdvice().isEmpty()
                && !shelter.getTimetable().isEmpty()
                && !shelter.getSecurity().isEmpty();
    }

    @Override
    public boolean isCompleteRecord(DogShelter shelter) {
        return !shelter.getName().isEmpty()
                && !shelter.getAboutMe().isEmpty()
                && !shelter.getLocation().isEmpty()
                && !shelter.getSafetyAdvice().isEmpty()
                && !shelter.getTimetable().isEmpty()
                && !shelter.getSecurity().isEmpty();
    }



    @Override
    public CatShelter updateCompleteRecord(CatShelter shelter, CatShelter shelterId) {
        if (shelter.getName().isBlank()) {
            shelter.setName(shelterId.getName());
        }
        if (shelter.getLocation().isBlank()) {
            shelter.setLocation(shelterId.getLocation());
        }
        if (shelter.getTimetable().isBlank()) {
            shelter.setTimetable(shelterId.getTimetable());
        }
        if (shelter.getAboutMe().isBlank()) {
            shelter.setAboutMe(shelterId.getAboutMe());
        }
        if (shelter.getSecurity().isBlank()) {
            shelter.setSecurity(shelterId.getSecurity());
        }
        if (shelter.getSafetyAdvice().isBlank()) {
            shelter.setSafetyAdvice(shelterId.getSafetyAdvice());
        }
        return shelter;
    }
    @Override
    public DogShelter updateCompleteRecord(DogShelter shelter, DogShelter shelterId) {
        if (shelter.getName().isBlank()) {
            shelter.setName(shelterId.getName());
        }
        if (shelter.getLocation().isBlank()) {
            shelter.setLocation(shelterId.getLocation());
        }
        if (shelter.getTimetable().isBlank()) {
            shelter.setTimetable(shelterId.getTimetable());
        }
        if (shelter.getAboutMe().isBlank()) {
            shelter.setAboutMe(shelterId.getAboutMe());
        }
        if (shelter.getSecurity().isBlank()) {
            shelter.setSecurity(shelterId.getSecurity());
        }
        if (shelter.getSafetyAdvice().isBlank()) {
            shelter.setSafetyAdvice(shelterId.getSafetyAdvice());
        }
        return shelter;
    }
}
