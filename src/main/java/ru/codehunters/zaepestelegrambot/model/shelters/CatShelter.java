package ru.codehunters.zaepestelegrambot.model.shelters;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "cat_Shelter")
public class CatShelter {
   /**
    * id для приюта
    */
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
   private Long id;
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
    * Расписание работы приюта
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
   @OneToMany(mappedBy = "shelterId", cascade = CascadeType.ALL)
   private List<Cat> list;
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
