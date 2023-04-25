package ru.codehunters.zaepestelegrambot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DogSheltersController.class)
@ExtendWith(MockitoExtension.class)
class DogSheltersControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    DogShelterServiceImpl dogShelterService;
    private final DogShelter currentDogShelter = new DogShelter
            (1L, "name", "loc", "tt", "am", "sec", "sa");
    Dog dog1 = new Dog(1L, "Жучка", 1, true, true, null, 1L);
    private final List<Dog> animalList = List.of(dog1);
    private final List<DogShelter> shelterList = List.of(currentDogShelter);

    @Test
    @DisplayName("Должен создать и вернуть приют с нужными параметрами")
    void create() throws Exception {
        when(dogShelterService.addShelter(any(DogShelter.class))).thenReturn(currentDogShelter);
        mockMvc.perform(post("/dogs/shelters/")
                        .param("name", "name")
                        .param("location", "loc")
                        .param("timetable", "tt")
                        .param("aboutMe", "am")
                        .param("security", "sec")
                        .param("safetyAdvice", "sa")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("loc"))
                .andExpect(jsonPath("timetable").value("tt"))
                .andExpect(jsonPath("aboutMe").value("am"))
                .andExpect(jsonPath("security").value("sec"))
                .andExpect(jsonPath("safetyAdvice").value("sa"));
        verify(dogShelterService, times(1)).addShelter(any(DogShelter.class));

    }

    @Test
    @DisplayName("Должен обновить и вернуть приют с новыми параметрами")
    void update() throws Exception {
        when(dogShelterService.updateShelter(any(DogShelter.class))).thenReturn(currentDogShelter);
        mockMvc.perform(put("/dogs/shelters/")
                        .param("id", "1")
                        .param("name", "name")
                        .param("location", "loc")
                        .param("timetable", "tt")
                        .param("aboutMe", "am")
                        .param("security", "sec")
                        .param("safetyAdvice", "sa")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("loc"))
                .andExpect(jsonPath("timetable").value("tt"))
                .andExpect(jsonPath("aboutMe").value("am"))
                .andExpect(jsonPath("security").value("sec"))
                .andExpect(jsonPath("safetyAdvice").value("sa"));
        verify(dogShelterService, times(1)).updateShelter(any(DogShelter.class));


    }

    @Test
    @DisplayName("Должен вернуть список с приютами")
    void getAll() throws Exception {
        when(dogShelterService.getShelter()).thenReturn(shelterList);
        mockMvc.perform(get("/dogs/shelters/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[0].location").value("loc"))
                .andExpect(jsonPath("$.[0].timetable").value("tt"))
                .andExpect(jsonPath("$.[0].aboutMe").value("am"))
                .andExpect(jsonPath("$.[0].security").value("sec"))
                .andExpect(jsonPath("$.[0].safetyAdvice").value("sa"));
        verify(dogShelterService, times(1)).getShelter();
    }

    @Test
    @DisplayName("Должен вернуть приют по id")
    void getShelterId() throws Exception {
        when(dogShelterService.getSheltersId(currentDogShelter.getId())).thenReturn(currentDogShelter);
        mockMvc.perform(get("/dogs/shelters/id" + currentDogShelter.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("loc"))
                .andExpect(jsonPath("timetable").value("tt"))
                .andExpect(jsonPath("aboutMe").value("am"))
                .andExpect(jsonPath("security").value("sec"))
                .andExpect(jsonPath("safetyAdvice").value("sa"));
        verify(dogShelterService, times(1)).getSheltersId(1L);
    }

    @Test
    @DisplayName("Должен вернуть список с животными приюта")
    void getAnimal() throws Exception {
        when(dogShelterService.getAnimal(currentDogShelter.getId())).thenReturn(animalList);
        mockMvc.perform(get("/dogs/shelters/list" + currentDogShelter.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("Жучка"))
                .andExpect(jsonPath("$.[0].age").value(1))
                .andExpect(jsonPath("$.[0].isHealthy").value(true))
                .andExpect(jsonPath("$.[0].vaccinated").value(true));
        verify(dogShelterService, times(1)).getAnimal(currentDogShelter.getId());
    }


    @Test
    @DisplayName("При удалении приюта, выдаёт сообщение о том что приют удалён")
    void delete() throws Exception {
        long shelterId = 1L;
        doReturn("Приют удален").when(dogShelterService).delShelter(shelterId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/dogs/shelters/{id}", shelterId))
                .andExpect(status().isOk())
                .andExpect(content().string("Приют удален"));

        verify(dogShelterService, times(1)).delShelter(shelterId);
    }


}