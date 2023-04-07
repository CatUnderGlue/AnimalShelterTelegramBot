package ru.codehunters.zaepestelegrambot.service;

import ru.codehunters.zaepestelegrambot.model.Volunteer;

import java.util.List;

public interface VolunteerService {
    Volunteer create(Volunteer volunteer);
    List<Volunteer> getAll();
    void delete(Volunteer volunteer);
}
