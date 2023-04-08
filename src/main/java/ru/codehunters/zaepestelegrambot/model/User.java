package ru.codehunters.zaepestelegrambot.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

//    private Long id;

    @Id
    @Column
    private String telegramId;

    @Column
    private String name;

    @Column
    private String lastName;

}
