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
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static ru.codehunters.zaepestelegrambot.model.TrialPeriod.AnimalType.CAT;
import static ru.codehunters.zaepestelegrambot.model.TrialPeriod.Result.IN_PROGRESS;

@WebMvcTest(TrialPeriodController.class)
@ExtendWith(MockitoExtension.class)
class TrialPeriodControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    TrialPeriodService trialPeriodService;

    private final TrialPeriod firstTrialPeriod = new TrialPeriod(1L, LocalDate.now(), LocalDate.now().plusDays(30),
            LocalDate.now().minusDays(1), new ArrayList<>(), IN_PROGRESS, 1L,
            CAT, 1L);

    private final TrialPeriod secondTrialPeriod = new TrialPeriod(2L, LocalDate.now(), LocalDate.now().plusDays(30),
            LocalDate.now().minusDays(1), new ArrayList<>(), IN_PROGRESS, 2L,
            CAT, 2L);

    private final List<TrialPeriod> trialPeriodList = List.of(firstTrialPeriod, secondTrialPeriod);

    @Test
    @DisplayName("Должен создать и вернуть испытательный срок с нужными параметрами")
    void shouldCreateAndReturnTrialPeriod() throws Exception {
        when(trialPeriodService.create(any(TrialPeriod.class))).thenReturn(firstTrialPeriod);
        mockMvc.perform(post("/trial-periods")
                        .param("startDate", String.valueOf(LocalDate.now()))
                        .param("result", String.valueOf(IN_PROGRESS))
                        .param("ownerId", String.valueOf(1L))
                        .param("animalType", String.valueOf(CAT))
                        .param("animalId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("reports").value(new ArrayList<>()))
                .andExpect(jsonPath("result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("animalId").value(1L));
        verify(trialPeriodService, times(1)).create(any(TrialPeriod.class));
    }

    @Test
    @DisplayName("Должен вернуть список со всеми испытательными сроками")
    void shouldReturnListOfAllTrials() throws Exception {
        when(trialPeriodService.getAll()).thenReturn(trialPeriodList);
        mockMvc.perform(get("/trial-periods"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("$.[0].endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("$.[0].lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("$.[0].reports").value(new ArrayList<>()))
                .andExpect(jsonPath("$.[0].result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("$.[0].ownerId").value(1L))
                .andExpect(jsonPath("$.[0].animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("$.[0].animalId").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("$.[1].endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("$.[1].lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("$.[1].reports").value(new ArrayList<>()))
                .andExpect(jsonPath("$.[1].result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("$.[1].ownerId").value(2L))
                .andExpect(jsonPath("$.[1].animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("$.[1].animalId").value(2L));
        verify(trialPeriodService, times(1)).getAll();
    }

    @Test
    @DisplayName("Должен вернуть список со всеми испытательными сроками по id владельца")
    void shouldReturnListOfAllTrialsByOwnerId() throws Exception {
        when(trialPeriodService.getAllByOwnerId(1L)).thenReturn(trialPeriodList);
        mockMvc.perform(get("/trial-periods/owner")
                        .param("ownerId", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("$.[0].endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("$.[0].lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("$.[0].reports").value(new ArrayList<>()))
                .andExpect(jsonPath("$.[0].result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("$.[0].ownerId").value(1L))
                .andExpect(jsonPath("$.[0].animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("$.[0].animalId").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("$.[1].endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("$.[1].lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("$.[1].reports").value(new ArrayList<>()))
                .andExpect(jsonPath("$.[1].result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("$.[1].ownerId").value(2L))
                .andExpect(jsonPath("$.[1].animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("$.[1].animalId").value(2L));
        verify(trialPeriodService, times(1)).getAllByOwnerId(1L);
    }

    @Test
    @DisplayName("Должен вернуть испытательный срок по id")
    void shouldReturnTrialsById() throws Exception {
        when(trialPeriodService.getById(1L)).thenReturn(firstTrialPeriod);
        mockMvc.perform(get("/trial-periods/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("reports").value(new ArrayList<>()))
                .andExpect(jsonPath("result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("animalId").value(1L));
        verify(trialPeriodService, times(1)).getById(1L);
    }

    @Test
    @DisplayName("Должен обновить и вернуть испытательный срок с новыми параметрами")
    void shouldUpdateAndReturnTrial() throws Exception {
        when(trialPeriodService.update(any(TrialPeriod.class))).thenReturn(firstTrialPeriod);
        mockMvc.perform(put("/trial-periods")
                        .param("id", String.valueOf(1L))
                        .param("startDate", String.valueOf(LocalDate.now()))
                        .param("result", String.valueOf(IN_PROGRESS))
                        .param("ownerId", String.valueOf(1L))
                        .param("animalType", String.valueOf(CAT))
                        .param("animalId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("startDate").value(String.valueOf(LocalDate.now())))
                .andExpect(jsonPath("endDate").value(String.valueOf(LocalDate.now().plusDays(30))))
                .andExpect(jsonPath("lastReportDate").value(String.valueOf(LocalDate.now().minusDays(1))))
                .andExpect(jsonPath("reports").value(new ArrayList<>()))
                .andExpect(jsonPath("result").value(String.valueOf(IN_PROGRESS)))
                .andExpect(jsonPath("ownerId").value(1L))
                .andExpect(jsonPath("animalType").value(String.valueOf(CAT)))
                .andExpect(jsonPath("animalId").value(1L));
        verify(trialPeriodService, times(1)).update(any(TrialPeriod.class));
    }

    @Test
    @DisplayName("При удалении испытательного срока, выдаёт сообщение о том что испытательный срок удалён")
    void shouldReturnMessageWhenTrialDeleted() throws Exception {
        doNothing().when(trialPeriodService).deleteById(1L);
        mockMvc.perform(delete("/trial-periods/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(content().string("Испытательный срок успешно удалён"));
        verify(trialPeriodService, times(1)).deleteById(1L);
    }
}