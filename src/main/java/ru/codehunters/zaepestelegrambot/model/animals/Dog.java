package ru.codehunters.zaepestelegrambot.model.animals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dog")

public class Dog {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    private String name;

    @Column
    private int age;

    @Column
    private boolean isHealthy;

    @Column
    private boolean vaccinated;

    @Column
    private Long ownerId;

    @Column
    private Long shelterId;


}
