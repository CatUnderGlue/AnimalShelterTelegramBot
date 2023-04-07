package ru.codehunters.zaepestelegrambot.model.shelters;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;

import java.util.List;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public class DogShelter{
        private String name; // Название приюта
        private String location; // Адрес и схема проезда
        private String timetable; // Расписание
        private String aboutMe; // О приюте
        private List<Dog> list; // Список животных в приюте
        private String security; // Способ связи с охраной
        private String safetyAdvice; // Рекомендации о технике безопасности на территории приюта
}
