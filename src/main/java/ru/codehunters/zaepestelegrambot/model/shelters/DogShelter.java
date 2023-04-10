package ru.codehunters.zaepestelegrambot.model.shelters;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.List;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dog_Shelter")
public class DogShelter {
    /**
     * id для приюта
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idShelter;
    /**
     * Название приюта
     */
    @Column
    private String name;
    /**
     * Адрес и схема проезда
     */
    @Column
    private String location;
    /**
     * Расписание
     */
    @Column
    private String timetable;
    /**
     * О приюте
     */
    @Column(name = "about_me")
    private String aboutMe;
    /**
     * Список животных в приюте
     */
    @Hidden
    @OneToMany(mappedBy = "shelterId", cascade = CascadeType.ALL)
    private List<Dog> list; // Список животных в приюте
    /**
     * Способ связи с охраной
     */
    @Column
    private String security;
    /**
     * Рекомендации о технике безопасности на территории приюта
     */
    @Column(name = "safety_advice")
    private String safetyAdvice;
}
