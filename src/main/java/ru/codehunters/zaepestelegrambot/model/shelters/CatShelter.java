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
   @Id
   @GeneratedValue
   private Long id; // id для приюта
   @Column
   private String name; // Название приюта
   @Column
   private String location; // Адрес и схема проезда
   @Column
   private String timetable; // Расписание
   @Column (name ="about_me")
   private String aboutMe; // О приюте
   @OneToMany(mappedBy = "shelterId",cascade = CascadeType.ALL)
   private List<Cat> list; // Список животных в приюте
   @Column
   private String security; // Способ связи с охраной
   @Column(name ="safety_advice")
   private String safetyAdvice; // Рекомендации о технике безопасности на территории приюта


}
