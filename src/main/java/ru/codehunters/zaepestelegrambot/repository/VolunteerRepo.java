package ru.codehunters.zaepestelegrambot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.Volunteer;

@Repository
public interface VolunteerRepo extends JpaRepository<Volunteer, Long> {

}
