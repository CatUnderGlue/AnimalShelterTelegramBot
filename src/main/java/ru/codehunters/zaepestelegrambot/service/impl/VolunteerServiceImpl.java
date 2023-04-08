package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.repository.VolunteerRepo;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.List;

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
    public List<Volunteer> getAll() {
        return volunteerRepo.findAll();
    }

    @Override
    public void delete(Volunteer volunteer) {
        volunteerRepo.delete(volunteer);
    }
}
