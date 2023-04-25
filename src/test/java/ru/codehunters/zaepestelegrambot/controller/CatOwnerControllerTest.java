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
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.service.CatOwnerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatOwnerController.class)
@ExtendWith(MockitoExtension.class)
class CatOwnerControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    CatOwnerService catOwnerService;
    private final CatOwner firstValidCatOwner = new CatOwner(1L, "Petr", "Ivanov", "256318", null, null);
    private final CatOwner secondValidCatOwner = new CatOwner(2L, "Petr", "Petrov", "365845", null, null);
    private final List<CatOwner> catOwnerList = List.of(firstValidCatOwner, secondValidCatOwner);

    @Test
    @DisplayName("Должен создать и вернуть владельца кота с нужными параметрами")
    void shouldCreateAndReturnCatOwner() throws Exception {
        when(catOwnerService.create(firstValidCatOwner, TrialPeriod.AnimalType.CAT, 1L)).thenReturn(firstValidCatOwner);
        mockMvc.perform(post("/catOwners")
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
        verify(catOwnerService, times(1)).create(firstValidCatOwner, TrialPeriod.AnimalType.CAT, 1L);
    }

    @Test
    @DisplayName("Должен создать из пользователя по id и вернуть владельца кота с нужными параметрами")
    void shouldCreateFromUserAndReturnCatOwner() throws Exception {
        when(catOwnerService.create(1L, TrialPeriod.AnimalType.CAT, 1L)).thenReturn(firstValidCatOwner);
        mockMvc.perform(post("/catOwners/user")
                        .param("id", "1")
                        .param("animalId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"))
                .andExpect(jsonPath("phone").value("256318"));
        verify(catOwnerService, times(1)).create(1L, TrialPeriod.AnimalType.CAT, 1L);
    }

    @Test
    @DisplayName("Должен вернуть список с владельцами котов")
    void shouldReturnListOfCatOwners() throws Exception {
        when(catOwnerService.getAll()).thenReturn(catOwnerList);
        mockMvc.perform(get("/catOwners"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].telegramId").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("Petr"))
                .andExpect(jsonPath("$.[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$.[0].phone").value("256318"))
                .andExpect(jsonPath("$.[1].telegramId").value(2L))
                .andExpect(jsonPath("$.[1].firstName").value("Petr"))
                .andExpect(jsonPath("$.[1].lastName").value("Petrov"))
                .andExpect(jsonPath("$.[1].phone").value("365845"));
        verify(catOwnerService, times(1)).getAll();
    }

    @Test
    @DisplayName("Должен вернуть владельца кота по ID")
    void shouldReturnCatOwnerFoundById() throws Exception {
        when(catOwnerService.getById(firstValidCatOwner.getTelegramId())).thenReturn(firstValidCatOwner);
        mockMvc.perform(get("/catOwners/id")
                        .param("catOwnerId", String.valueOf(firstValidCatOwner.getTelegramId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(catOwnerService, times(1)).getById(firstValidCatOwner.getTelegramId());
    }

    @Test
    @DisplayName("Должен обновить и вернуть владельца кота с новыми параметрами")
    void shouldUpdateAndReturnCatOwner() throws Exception {
        when(catOwnerService.update(any(CatOwner.class))).thenReturn(firstValidCatOwner);
        mockMvc.perform(put("/catOwners")
                        .param("telegramId", "1")
                        .param("firstName", "Petr")
                        .param("lastName", "Ivanov")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(catOwnerService, times(1)).update(any(CatOwner.class));
    }

    @Test
    @DisplayName("При удалении владельца кота, выдаёт сообщение о том что владелец кота удалён")
    void shouldReturnMessageWhenCatOwnerDeleted() throws Exception {
        doNothing().when(catOwnerService).deleteById(1L);
        mockMvc.perform(delete("/catOwners/id")
                        .param("catOwnerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Владелец кота успешно удалён"));
        verify(catOwnerService, times(1)).deleteById(1L);
    }
}