package ru.codehunters.zaepestelegrambot.model.owners;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
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
    @Column
    private Long telegramId;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column
    private String phone;

    @OneToMany(mappedBy = "ownerId")
    private List<Dog> dogList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerId")
    private List<TrialPeriod> trialPeriodList;

    public DogOwner(User user){
        this.telegramId = user.getTelegramId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
    }
}
