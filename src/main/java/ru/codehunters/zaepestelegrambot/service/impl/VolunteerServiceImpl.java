package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.VolunteerNotFoundException;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.repository.VolunteerRepo;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.List;
import java.util.Optional;

@Service
public class VolunteerServiceImpl implements VolunteerService {

    private final VolunteerRepo volunteerRepo;

    public VolunteerServiceImpl(VolunteerRepo volunteerRepo) {
        this.volunteerRepo = volunteerRepo;
    }

    @Override
    public Volunteer create(Volunteer volunteer) {
        return volunteerRepo.save(volunteer);
    }

    @Override
    public Volunteer getById(Long id) {
        Optional<Volunteer> optionalVolunteer = volunteerRepo.findById(id);
        if (optionalVolunteer.isEmpty()) {
            throw new VolunteerNotFoundException();
        }
        return optionalVolunteer.get();
    }

    @Override
    public List<Volunteer> getAll() {
        List<Volunteer> all = volunteerRepo.findAll();
        if (all.isEmpty()) {
            throw new VolunteerNotFoundException();
        }
        return all;
    }

    @Override
    public Volunteer update(Volunteer volunteer) {
        if (volunteer.getTelegramId() == null || getById(volunteer.getTelegramId()) == null) {
            throw new VolunteerNotFoundException();
        }
        return volunteerRepo.save(volunteer);
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
