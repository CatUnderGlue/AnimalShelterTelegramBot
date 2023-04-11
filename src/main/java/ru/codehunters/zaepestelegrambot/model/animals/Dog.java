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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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


    public Dog(String name, int age, boolean isHealthy, boolean vaccinated,Long shelterId, Long ownerId) {
        this.name = name;
        this.age = age;
        this.isHealthy = isHealthy;
        this.vaccinated = vaccinated;
        this.shelterId = shelterId;
        this.ownerId = ownerId;
    }

    public Dog(String name, int age, boolean isHealthy, boolean vaccinated, Long shelterId) {
        this.name = name;
        this.age = age;
        this.isHealthy = isHealthy;
        this.vaccinated = vaccinated;
        this.shelterId = shelterId;
    }
}
