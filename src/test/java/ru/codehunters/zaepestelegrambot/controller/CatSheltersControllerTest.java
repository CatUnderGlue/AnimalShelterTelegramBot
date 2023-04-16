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
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatSheltersController.class)
@ExtendWith(MockitoExtension.class)
class CatSheltersControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CatShelterServiceImpl catShelterService;
    private final CatShelter currentCatShelter = new CatShelter
            (1L, "name", "loc", "tt", "am", "sec", "sa");
    Cat cat1 = new Cat(1L, "Барсик", 1, true, true, null, 1L);
    private final List<Cat> animalList = List.of(cat1);
    private final List<CatShelter> shelterList = List.of(currentCatShelter);

    @Test
    @DisplayName("Должен создать и вернуть приют с нужными параметрами")
    void create() throws Exception {
        when(catShelterService.addShelter(any(CatShelter.class))).thenReturn(currentCatShelter);
        mockMvc.perform(post("/cats/shelters/")
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
        verify(catShelterService,times(1)).addShelter(any(CatShelter.class));

    }

    @Test
    @DisplayName("Должен обновить и вернуть приют с новыми параметрами")
    void update() throws Exception {
        when(catShelterService.updateShelter(any(CatShelter.class))).thenReturn(currentCatShelter);
        mockMvc.perform(put("/cats/shelters/")
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
        verify(catShelterService,times(1)).updateShelter(any(CatShelter.class));


    }

    @Test
    @DisplayName("Должен вернуть список с приютами")
    void getAll() throws Exception {
        when(catShelterService.getShelter()).thenReturn(shelterList);
        mockMvc.perform(get("/cats/shelters/"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("name"))
                .andExpect(jsonPath("$.[0].location").value("loc"))
                .andExpect(jsonPath("$.[0].timetable").value("tt"))
                .andExpect(jsonPath("$.[0].aboutMe").value("am"))
                .andExpect(jsonPath("$.[0].security").value("sec"))
                .andExpect(jsonPath("$.[0].safetyAdvice").value("sa"));
        verify(catShelterService, times(1)).getShelter();
    }

    @Test
    @DisplayName("Должен вернуть приют по id")
    void getShelterId() throws Exception {
        when(catShelterService.getSheltersId(currentCatShelter.getId())).thenReturn(currentCatShelter);
        mockMvc.perform(get("/cats/shelters/id"+currentCatShelter.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("name"))
                .andExpect(jsonPath("location").value("loc"))
                .andExpect(jsonPath("timetable").value("tt"))
                .andExpect(jsonPath("aboutMe").value("am"))
                .andExpect(jsonPath("security").value("sec"))
                .andExpect(jsonPath("safetyAdvice").value("sa"));
        verify(catShelterService, times(1)).getSheltersId(1L);
    }

    @Test
    @DisplayName("Должен вернуть список с животными приюта")
    void getAnimal() throws Exception {
        when(catShelterService.getAnimal(currentCatShelter.getId())).thenReturn(animalList);
        mockMvc.perform(get("/cats/shelters/list"+currentCatShelter.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("Барсик"))
                .andExpect(jsonPath("$.[0].age").value(1))
                .andExpect(jsonPath("$.[0].isHealthy").value(true))
                .andExpect(jsonPath("$.[0].vaccinated").value(true));
        verify(catShelterService, times(1)).getAnimal(currentCatShelter.getId());
    }


    @Test
    @DisplayName("При удалении приюта, выдаёт сообщение о том что приют удалён")
    void delete() throws Exception {
        long shelterId = 1L;
        doReturn("Приют удален").when(catShelterService).delShelter(shelterId);

        mockMvc.perform(MockMvcRequestBuilders.delete("/cats/shelters/{id}", shelterId))
                .andExpect(status().isOk())
                .andExpect(content().string("Приют удален"));

        verify(catShelterService, times(1)).delShelter(shelterId);
    }


}