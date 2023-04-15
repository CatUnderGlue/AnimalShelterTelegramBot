package ru.codehunters.zaepestelegrambot.controller;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.request.SendMessage;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(VolunteerController.class)
@ExtendWith(MockitoExtension.class)
class VolunteerControllerTest {
    final Volunteer FIRST_VALID_VOLUNTEER = new Volunteer(1L, "Ivan", "Ivanov");
    final Volunteer SECOND_VALID_VOLUNTEER = new Volunteer(2L, "Petr", "Petrov");
    final List<Volunteer> VOLUNTEER_LIST = List.of(FIRST_VALID_VOLUNTEER, SECOND_VALID_VOLUNTEER);
    @Autowired
    MockMvc mockMvc;
    @MockBean
    VolunteerService volunteerService;
    @MockBean
    TelegramBot telegramBot;
    @Test
    @DisplayName("Должен создать и вернуть волонтёра с нужными параметрами")
    void shouldCreateAndReturnVolunteer() throws Exception {
        when(volunteerService.create(any(Volunteer.class))).thenReturn(FIRST_VALID_VOLUNTEER);
        mockMvc.perform(post("/volunteers")
                        .param("telegramId", "1")
                        .param("firstName", "Ivan")
                        .param("lastName", "Ivanov")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Ivan"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(volunteerService, times(1)).create(any(Volunteer.class));
    }

    @Test
    @DisplayName("Должен вернуть список с волонтёрами")
    void shouldReturnListOfVolunteers() throws Exception {
        when(volunteerService.getAll()).thenReturn(VOLUNTEER_LIST);
        mockMvc.perform(get("/volunteers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].telegramId").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("Ivan"))
                .andExpect(jsonPath("$.[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$.[1].telegramId").value(2L))
                .andExpect(jsonPath("$.[1].firstName").value("Petr"))
                .andExpect(jsonPath("$.[1].lastName").value("Petrov"));
        verify(volunteerService, times(1)).getAll();
    }

    @Test
    @DisplayName("Должен вернуть волонтёра по ID")
    void shouldReturnVolunteerFoundById() throws Exception {
        when(volunteerService.getById(FIRST_VALID_VOLUNTEER.getTelegramId())).thenReturn(FIRST_VALID_VOLUNTEER);
        mockMvc.perform(get("/volunteers/id")
                        .param("volunteerId", String.valueOf(FIRST_VALID_VOLUNTEER.getTelegramId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Ivan"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(volunteerService, times(1)).getById(FIRST_VALID_VOLUNTEER.getTelegramId());
    }

    @Test
    @DisplayName("Должен обновить и вернуть волонтёра с новыми параметрами")
    void shouldUpdateAndReturnVolunteer() throws Exception {
        when(volunteerService.update(any(Volunteer.class))).thenReturn(FIRST_VALID_VOLUNTEER);
        mockMvc.perform(put("/volunteers")
                        .param("telegramId", "1")
                        .param("firstName", "Ivan")
                        .param("lastName", "Ivanov")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Ivan"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(volunteerService, times(1)).update(any(Volunteer.class));
    }

    @Test
    @DisplayName("При удалении волонтёра, выдаёт сообщение о том что волонтёр удалён")
    void shouldReturnMessageWhenVolunteerDeleted() throws Exception {
        doNothing().when(volunteerService).deleteById(1L);
        mockMvc.perform(delete("/volunteers/id")
                        .param("volunteerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Volunteer successfully removed"));
        verify(volunteerService, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("Отправляет сообщения волонтёрам")
    public void shouldSendMessageToVolunteers() throws Exception {
        mockMvc.perform(post("/volunteers/warning_message")
                        .param("ownerId", FIRST_VALID_VOLUNTEER.getTelegramId().toString()))
                .andExpect(status().isOk());
        verify(telegramBot, times(1)).execute(any(SendMessage.class));
    }
}