package ru.codehunters.zaepestelegrambot.model.owners;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dogowner")
public class DogOwner{

    @Id
    @GeneratedValue
    private Long id;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "telegramId")
//    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(cascade = CascadeType.ALL,  mappedBy = "ownerId")
//    @JoinColumn(name = "id", referencedColumnName = "dogOwner_id")
    private List<Dog> dogList;
}
