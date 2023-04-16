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
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(UserController.class)
@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Autowired
    MockMvc mockMvc;
    @MockBean
    UserService userService;
    private final User firstValidUser = new User(1L, "Petr", "Ivanov", "256318");
    private final User secondValidUser = new User(2L, "Petr", "Petrov","365845");
    private final List<User> userList = List.of(firstValidUser, secondValidUser);
    @Test
    @DisplayName("Должен создать и вернуть пользователя с нужными параметрами")
    void shouldCreateAndReturnUser() throws Exception {
        when(userService.create(any(User.class))).thenReturn(firstValidUser);
        mockMvc.perform(post("/users")
                        .param("telegramId", "1")
                        .param("firstName", "Petr")
                        .param("lastName", "Ivanov")
                        .param("phone", "256318")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"))
                .andExpect(jsonPath("phone").value("256318"));
        verify(userService, times(1)).create(any(User.class));
    }

    @Test
    @DisplayName("Должен вернуть список с пользователями")
    void shouldReturnListOfUsers() throws Exception {
        when(userService.getAll()).thenReturn(userList);
        mockMvc.perform(get("/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].telegramId").value(1L))
                .andExpect(jsonPath("$.[0].firstName").value("Petr"))
                .andExpect(jsonPath("$.[0].lastName").value("Ivanov"))
                .andExpect(jsonPath("$.[0].phone").value("256318"))
                .andExpect(jsonPath("$.[1].telegramId").value(2L))
                .andExpect(jsonPath("$.[1].firstName").value("Petr"))
                .andExpect(jsonPath("$.[1].lastName").value("Petrov"))
                .andExpect(jsonPath("$.[1].phone").value("365845"));
        verify(userService, times(1)).getAll();
    }

    @Test
    @DisplayName("Должен вернуть пользователя по ID")
    void shouldReturnUserFoundById() throws Exception {
        when(userService.getById(firstValidUser.getTelegramId())).thenReturn(firstValidUser);
        mockMvc.perform(get("/users/id")
                        .param("userId", String.valueOf(firstValidUser.getTelegramId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(userService, times(1)).getById(firstValidUser.getTelegramId());
    }

    @Test
    @DisplayName("Должен обновить и вернуть пользователя с новыми параметрами")
    void shouldUpdateAndReturnUser() throws Exception {
        when(userService.update(any(User.class))).thenReturn(firstValidUser);
        mockMvc.perform(put("/users")
                        .param("telegramId", "1")
                        .param("firstName", "Petr")
                        .param("lastName", "Ivanov")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("telegramId").value(1L))
                .andExpect(jsonPath("firstName").value("Petr"))
                .andExpect(jsonPath("lastName").value("Ivanov"));
        verify(userService, times(1)).update(any(User.class));
    }

    @Test
    @DisplayName("При удалении пользователя, выдаёт сообщение о том что пользователь удалён")
    void shouldReturnMessageWhenUserDeleted() throws Exception {
        doNothing().when(userService).deleteById(1L);
        mockMvc.perform(delete("/users/id")
                        .param("userId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Пользователь успешно удалён"));
        verify(userService, times(1)).deleteById(1L);
    }
}