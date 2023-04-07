package ru.codehunters.zaepestelegrambot.model.animals;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import lombok.NoArgsConstructor;



@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cat")
public class Cat  {
    @Id
    @GeneratedValue
    private Long id;

    @Column
    String nameCat;

    public Cat(String nameCat) {
        this.nameCat = nameCat;
    }
}
