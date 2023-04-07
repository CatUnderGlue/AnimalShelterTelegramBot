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
    String name;

    public Dog(String name) {
        this.name = name;
    }
}
