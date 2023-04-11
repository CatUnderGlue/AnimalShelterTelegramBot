package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.repository.VolunteerRepo;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.*;

@Service
@RequiredArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepo volunteerRepo;

    @Override
    public Volunteer create(Volunteer volunteer) {
        String nullField = EntityUtils.findNullOrBlankField(volunteer);
        if (nullField != null) {
            throw new IllegalArgumentException("Поле " + nullField + " не может быть пустым!");
        }
        return volunteerRepo.save(volunteer);
    }

    @Override
    public Volunteer getById(Long id) {
        Optional<Volunteer> optionalVolunteer = volunteerRepo.findById(id);
        if (optionalVolunteer.isEmpty()) {
            throw new NotFoundException("По указанному id волонтёр не найден!");
        }
        return optionalVolunteer.get();
    }

    @Override
    public List<Volunteer> getAll() {
        List<Volunteer> all = volunteerRepo.findAll();
        if (all.isEmpty()) {
            throw new NotFoundException("Волонтёры не найдены!");
        }
        return all;
    }

    @Override
    public Volunteer update(Volunteer volunteer) {
        Volunteer currentVolunteer = getById(volunteer.getTelegramId());
        EntityUtils.copyNonNullFields(volunteer, currentVolunteer);
        return volunteerRepo.save(currentVolunteer);
    }

    @Override
    public void delete(Volunteer volunteer) {
        volunteerRepo.delete(getById(volunteer.getTelegramId()));
    }

    @Override
    public void deleteById(Long id) {
        volunteerRepo.deleteById(getById(id).getTelegramId());
    }
}
