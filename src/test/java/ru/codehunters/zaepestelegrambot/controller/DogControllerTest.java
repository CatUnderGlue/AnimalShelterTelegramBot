package ru.codehunters.zaepestelegrambot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.service.impl.DogServiceImpl;
import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DogController.class)
@ExtendWith(MockitoExtension.class)
class DogControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    DogServiceImpl dogService;
    static final Dog VALID_DOG = new Dog(1L, "Dog", 12, true, true, 1L, 1L);
    static final Dog NEW_DOG = new Dog(1L, "Mittens", 3, false, false, 1L, 1L);
    static final Dog BORIS = new Dog(2L, "Boris", 3, false, false, 1L, 1L);

    @Test
    @DisplayName("Возвращает собаку со всеми полями")
    void getByIdShouldReturnCorrectDog() throws Exception {
        when(dogService.getById(VALID_DOG.getId())).thenReturn(VALID_DOG);
        mockMvc.perform(get("/dogs/id?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Dog"))
                .andExpect(jsonPath("age").value(12))
                .andExpect(jsonPath("isHealthy").value(true))
                .andExpect(jsonPath("vaccinated").value(true))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("shelterId").value(1L));

        verify(dogService,times(1)).getById(VALID_DOG.getId());
    }

    @Test
    @DisplayName("Создаёт и возвращает собаку со всеми полями")
    void createShouldCreateNewDog() throws Exception {
        when(dogService.create(any(Dog.class))).thenReturn(VALID_DOG);
        mockMvc.perform(post("/dogs")
                        .param("name","Dog")
                        .param("age","12")
                        .param("isHealthy","true")
                        .param("vaccinated","true")
                        .param("shelterId","1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Dog"))
                .andExpect(jsonPath("age").value(12))
                .andExpect(jsonPath("isHealthy").value(true))
                .andExpect(jsonPath("vaccinated").value(true))
                .andExpect(jsonPath("shelterId").value(1L));
        verify(dogService, times(1)).create(any(Dog.class));
    }

    @Test
    @DisplayName("Получает список всех собак")
    void getAllShouldGetAllDogs() throws Exception {
        List<Dog> list = List.of(VALID_DOG,BORIS);
        when(dogService.getAll()).thenReturn(list);
        mockMvc.perform(get("/dogs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Dog"))
                .andExpect(jsonPath("$[0].age").value(12))
                .andExpect(jsonPath("$[0].isHealthy").value(true))
                .andExpect(jsonPath("$[0].vaccinated").value(true))
                .andExpect(jsonPath("$[0].ownerId").value(1L))
                .andExpect(jsonPath("$[0].shelterId").value(1L))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Boris"))
                .andExpect(jsonPath("$[1].age").value(3))
                .andExpect(jsonPath("$[1].isHealthy").value(false))
                .andExpect(jsonPath("$[1].vaccinated").value(false))
                .andExpect(jsonPath("$[1].ownerId").value(1L))
                .andExpect(jsonPath("$[1].shelterId").value(1L));

        verify(dogService,times(1)).getAll();
    }

    @Test
    @DisplayName("Возвращает собаку по id хозяина")
    void getOwnerByIdShouldReturnCorrectDog() throws Exception {
    when(dogService.getByUserId(VALID_DOG.getOwnerId())).thenReturn(VALID_DOG);
        mockMvc.perform(get("/dogs/ownerID?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Dog"))
                .andExpect(jsonPath("age").value(12))
                .andExpect(jsonPath("isHealthy").value(true))
                .andExpect(jsonPath("vaccinated").value(true))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("shelterId").value(1L));

        verify(dogService,times(1)).getByUserId(VALID_DOG.getOwnerId());

    }

    @Test
    @DisplayName("Обновляет и возвращает собаку по новому объекту")
    void updateShouldUpdateDog() throws Exception {
        when(dogService.update(any(Dog.class))).thenReturn(NEW_DOG);
        mockMvc.perform(put("/dogs")
                .param("id", "1")
                .param("name", "Mittens")
                .param("age", "3")
                .param("isHealthy", "false")
                .param("vaccinated", "false")
                .param("shelterId", "1")
                .param("ownerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Mittens"))
                .andExpect(jsonPath("age").value(3))
                .andExpect(jsonPath("isHealthy").value(false))
                .andExpect(jsonPath("vaccinated").value(false))
                .andExpect(jsonPath("shelterId").value(1L))
                .andExpect(jsonPath("ownerId").value(1L));
        verify(dogService, times(1)).update(any(Dog.class));


    }

    @Test
    @DisplayName("Удаление собаки")
    void deleteByIdShouldDeleteDog() throws Exception {
        mockMvc.perform(delete("/dogs/id?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Собаку выбросили на улицу"));
        verify(dogService, times(1)).remove(1L);
    }
}