package ru.codehunters.zaepestelegrambot.repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

@Repository
public interface DogRepo extends JpaRepository<Dog,Long>{
}
