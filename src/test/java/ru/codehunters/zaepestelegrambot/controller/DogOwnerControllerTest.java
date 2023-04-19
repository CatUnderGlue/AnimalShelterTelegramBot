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
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;
import ru.codehunters.zaepestelegrambot.service.DogOwnerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(DogOwnerController.class)
@ExtendWith(MockitoExtension.class)
class DogOwnerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    DogOwnerService dogOwnerService;
    private final DogOwner firstValidDogOwner = new DogOwner(1L, "Petr", "Ivanov", "256318", null, null);
    private final DogOwner secondValidDogOwner = new DogOwner(2L, "Petr", "Petrov", "365845", null, null);
    private final List<DogOwner> dogOwnerList = List.of(firstValidDogOwner, secondValidDogOwner);

    @Test
    @DisplayName("Должен создать и вернуть владельца собаки с нужными параметрами")
    void shouldCreateAndReturnDogOwner() throws Exception {
        when(dogOwnerService.create(firstValidDogOwner, TrialPeriod.AnimalType.DOG, 1L)).thenReturn(firstValidDogOwner);
        mockMvc.perform(post("/dogOwners")
                        .param("telegramId", "1")
                        .param("firstName", "Petr")
                        .param("lastName", "Ivanov")
                        .param("phone", "256318")
                        .param("animalId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"))
                .andExpect(jsonPath("phone").value("256318"));
        verify(dogOwnerService, times(1)).create(firstValidDogOwner, TrialPeriod.AnimalType.DOG, 1L);
    }

    @Test
    @DisplayName("Должен создать из пользователя по id и вернуть владельца собаки с нужными параметрами")
    void shouldCreateFromUserAndReturnDogOwner() throws Exception {
        when(dogOwnerService.create(1L, TrialPeriod.AnimalType.DOG, 1L)).thenReturn(firstValidDogOwner);
        mockMvc.perform(post("/dogOwners/user")
                        .param("id", "1")
                        .param("animalId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"))
                .andExpect(jsonPath("phone").value("256318"));
        verify(dogOwnerService, times(1)).create(1L, TrialPeriod.AnimalType.DOG, 1L);
    }

    @Test
    @DisplayName("Должен вернуть список с владельцами собак")
    void shouldReturnListOfDogOwners() throws Exception {
        when(dogOwnerService.getAll()).thenReturn(dogOwnerList);
        mockMvc.perform(get("/dogOwners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].telegramId").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("Petr"))
                .andExpect(jsonPath("$.[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$.[0].phone").value("256318"))
                .andExpect(jsonPath("$.[1].telegramId").value(2L))
                .andExpect(jsonPath("$.[1].firstName").value("Petr"))
                .andExpect(jsonPath("$.[1].lastName").value("Petrov"))
                .andExpect(jsonPath("$.[1].phone").value("365845"));
        verify(dogOwnerService, times(1)).getAll();
    }

    @Test
    @DisplayName("Должен вернуть владельца собаки по ID")
    void shouldReturnDogOwnerFoundById() throws Exception {
        when(dogOwnerService.getById(firstValidDogOwner.getTelegramId())).thenReturn(firstValidDogOwner);
        mockMvc.perform(get("/dogOwners/id")
                        .param("dogOwnerId", String.valueOf(firstValidDogOwner.getTelegramId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(dogOwnerService, times(1)).getById(firstValidDogOwner.getTelegramId());
    }

    @Test
    @DisplayName("Должен обновить и вернуть владельца собаки с новыми параметрами")
    void shouldUpdateAndReturnDogOwner() throws Exception {
        when(dogOwnerService.update(any(DogOwner.class))).thenReturn(firstValidDogOwner);
        mockMvc.perform(put("/dogOwners")
                        .param("telegramId", "1")
                        .param("firstName", "Petr")
                        .param("lastName", "Ivanov")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(dogOwnerService, times(1)).update(any(DogOwner.class));
    }

    @Test
    @DisplayName("При удалении владельца собаки, выдаёт сообщение о том что владелец собаки удалён")
    void shouldReturnMessageWhenDogOwnerDeleted() throws Exception {
        doNothing().when(dogOwnerService).deleteById(1L);
        mockMvc.perform(delete("/dogOwners/id")
                        .param("dogOwnerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Владелец собаки успешно удалён"));
        verify(dogOwnerService, times(1)).deleteById(1L);
    }

}