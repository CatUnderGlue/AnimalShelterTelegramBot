package ru.codehunters.zaepestelegrambot.model.owners;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "catowner")
public class CatOwner {

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
    private List<Cat> catList;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "ownerId")
    private List<TrialPeriod> trialPeriodList;

    public CatOwner(User user) {
        this.telegramId = user.getTelegramId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.phone = user.getPhone();
    }
}
