package ru.codehunters.zaepestelegrambot.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.service.impl.CatServiceImpl;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CatController.class)
@ExtendWith(MockitoExtension.class)
class CatControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    CatServiceImpl catService;
    static final Cat VALID_CAT = new Cat(1L, "Cat", 12, true, true, 1L, 1L);
    static final Cat NEW_CAT = new Cat(1L, "Mittens", 3, false, false, 1L, 1L);
    static final Cat BORIS = new Cat(2L, "Boris", 3, false, false, 1L, 1L);

    @Test
    @DisplayName("Возвращает кота по id")
    void getByIdShouldReturnCorrectCat() throws Exception {
        when(catService.getById(VALID_CAT.getId())).thenReturn(VALID_CAT);
        mockMvc.perform(get("/cats/id?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Cat"))
                .andExpect(jsonPath("age").value(12))
                .andExpect(jsonPath("isHealthy").value(true))
                .andExpect(jsonPath("vaccinated").value(true))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("shelterId").value(1L));

        verify(catService, times(1)).getById(VALID_CAT.getId());
    }

    @Test
    @DisplayName("Создаёт и возвращает кота со всеми полями")
    void createShouldCreateNewCat() throws Exception {
        when(catService.create(any(Cat.class))).thenReturn(VALID_CAT);
        mockMvc.perform(post("/cats")
                        .param("name", "Cat")
                        .param("age", "12")
                        .param("isHealthy", "true")
                        .param("vaccinated", "true")
                        .param("shelterId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("Cat"))
                .andExpect(jsonPath("age").value(12))
                .andExpect(jsonPath("isHealthy").value(true))
                .andExpect(jsonPath("vaccinated").value(true))
                .andExpect(jsonPath("shelterId").value(1L));
        verify(catService, times(1)).create(any(Cat.class));
    }

    @Test
    @DisplayName("Получает список всех котов")
    void getAllShouldGetAllCats() throws Exception {
        List<Cat> list = List.of(VALID_CAT, BORIS);
        when(catService.getAll()).thenReturn(list);
        mockMvc.perform(get("/cats"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Cat"))
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

        verify(catService, times(1)).getAll();
    }

    @Test
    @DisplayName("Возвращает список котов по id хозяина")
    void getOwnerByIdShouldReturnCatList() throws Exception {
        when(catService.getAllByUserId(VALID_CAT.getOwnerId())).thenReturn(List.of(VALID_CAT));
        mockMvc.perform(get("/cats/ownerID?id=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1))
                .andExpect(jsonPath("$.[0].name").value("Cat"))
                .andExpect(jsonPath("$.[0].age").value(12))
                .andExpect(jsonPath("$.[0].isHealthy").value(true))
                .andExpect(jsonPath("$.[0].vaccinated").value(true))
                .andExpect(jsonPath("$.[0].ownerId").value(1L))
                .andExpect(jsonPath("$.[0].shelterId").value(1L));
        verify(catService, times(1)).getAllByUserId(VALID_CAT.getOwnerId());

    }

    @Test
    @DisplayName("Обновляет и возвращает кота по новому объекту")
    void updateShouldUpdateCat() throws Exception {
        when(catService.update(any(Cat.class))).thenReturn(NEW_CAT);
        mockMvc.perform(put("/cats")
                        .param("id", "1")
                        .param("name", "Mittens")
                        .param("age", "3")
                        .param("isHealthy", "false")
                        .param("vaccinated", "false")
                        .param("ownerId", "1")
                        .param("shelterId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1))
                .andExpect(jsonPath("name").value("Mittens"))
                .andExpect(jsonPath("age").value(3))
                .andExpect(jsonPath("isHealthy").value(false))
                .andExpect(jsonPath("vaccinated").value(false))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("shelterId").value(1L));
        verify(catService, times(1)).update(any(Cat.class));


    }

    @Test
    @DisplayName("Удаляет кота по id")
    void deleteByIdShouldDeleteCat() throws Exception {
        mockMvc.perform(delete("/cats/id?id=1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Кота выбросили на улицу"));
        verify(catService, times(1)).remove(1L);
    }
}