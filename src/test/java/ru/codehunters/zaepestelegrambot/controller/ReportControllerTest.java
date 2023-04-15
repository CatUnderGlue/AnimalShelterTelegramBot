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
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.service.ReportService;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ReportController.class)
@ExtendWith(MockitoExtension.class)
class ReportControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ReportService reportService;

    private final Report firstValidReport = new Report(1L, "photoid", "ration", "health",
            "behavior", LocalDate.now(), 1L);
    private final Report secondValidReport = new Report(2L, "photoid2", "ration2", "health2",
            "behavior2", LocalDate.now(), 2L);

    private final List<Report> reportList = List.of(firstValidReport, secondValidReport);

    @Test
    @DisplayName("Должен создать и вернуть отчёт с нужными параметрами")
    void shouldCreateAndReturnReport() throws Exception {
        when(reportService.create(any(Report.class))).thenReturn(firstValidReport);
        mockMvc.perform(post("/reports")
                        .param("photoId", "photoid")
                        .param("foodRation", "ration")
                        .param("generalHealth", "health")
                        .param("behaviorChanges", "behavior")
                        .param("trialPeriodId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("photoId").value("photoid"))
                .andExpect(jsonPath("foodRation").value("ration"))
                .andExpect(jsonPath("generalHealth").value("health"))
                .andExpect(jsonPath("behaviorChanges").value("behavior"))
                .andExpect(jsonPath("trialPeriodId").value(1L));
        verify(reportService, times(1)).create(any(Report.class));
    }

    @Test
    @DisplayName("Должен вернуть список со всеми отчётами")
    void shouldReturnListOfAllReports() throws Exception {
        when(reportService.getAll()).thenReturn(reportList);
        mockMvc.perform(get("/reports"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].photoId").value("photoid"))
                .andExpect(jsonPath("$.[0].foodRation").value("ration"))
                .andExpect(jsonPath("$.[0].generalHealth").value("health"))
                .andExpect(jsonPath("$.[0].behaviorChanges").value("behavior"))
                .andExpect(jsonPath("$.[0].trialPeriodId").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].photoId").value("photoid2"))
                .andExpect(jsonPath("$.[1].foodRation").value("ration2"))
                .andExpect(jsonPath("$.[1].generalHealth").value("health2"))
                .andExpect(jsonPath("$.[1].behaviorChanges").value("behavior2"))
                .andExpect(jsonPath("$.[1].trialPeriodId").value(2L));
        verify(reportService, times(1)).getAll();
    }

    @Test
    @DisplayName("Должен вернуть список с отчётами конкретного испытательного срока")
    void shouldReturnListOfReportsByTrialId() throws Exception {
        when(reportService.gelAllByTrialPeriodId(1L)).thenReturn(reportList);
        mockMvc.perform(get("/reports/trial-id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].photoId").value("photoid"))
                .andExpect(jsonPath("$.[0].foodRation").value("ration"))
                .andExpect(jsonPath("$.[0].generalHealth").value("health"))
                .andExpect(jsonPath("$.[0].behaviorChanges").value("behavior"))
                .andExpect(jsonPath("$.[0].trialPeriodId").value(1L))
                .andExpect(jsonPath("$.[1].id").value(2L))
                .andExpect(jsonPath("$.[1].photoId").value("photoid2"))
                .andExpect(jsonPath("$.[1].foodRation").value("ration2"))
                .andExpect(jsonPath("$.[1].generalHealth").value("health2"))
                .andExpect(jsonPath("$.[1].behaviorChanges").value("behavior2"))
                .andExpect(jsonPath("$.[1].trialPeriodId").value(2L));
        verify(reportService, times(1)).gelAllByTrialPeriodId(1L);
    }

    @Test
    @DisplayName("Должен вернуть отчёт конкретного испытательного срока по дате")
    void shouldReturnReportByTrialIdAndDate() throws Exception {
        when(reportService.getByDateAndTrialId(LocalDate.now(), 1L)).thenReturn(firstValidReport);
        mockMvc.perform(get("/reports/date-and-trial")
                        .param("id", String.valueOf(1L))
                        .param("date", String.valueOf(LocalDate.now())))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("photoId").value("photoid"))
                .andExpect(jsonPath("foodRation").value("ration"))
                .andExpect(jsonPath("generalHealth").value("health"))
                .andExpect(jsonPath("behaviorChanges").value("behavior"))
                .andExpect(jsonPath("trialPeriodId").value(1L));
        verify(reportService, times(1)).getByDateAndTrialId(LocalDate.now(), 1L);
    }

    @Test
    @DisplayName("Должен вернуть отчёт по ID")
    void shouldReturnReportFoundById() throws Exception {
        when(reportService.getById(firstValidReport.getId())).thenReturn(firstValidReport);
        mockMvc.perform(get("/reports/id")
                        .param("reportId", String.valueOf(firstValidReport.getId()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("photoId").value("photoid"))
                .andExpect(jsonPath("foodRation").value("ration"))
                .andExpect(jsonPath("generalHealth").value("health"))
                .andExpect(jsonPath("behaviorChanges").value("behavior"))
                .andExpect(jsonPath("trialPeriodId").value(1L));
        verify(reportService, times(1)).getById(firstValidReport.getId());
    }

    @Test
    @DisplayName("Должен обновить и вернуть отчёт с новыми параметрами")
    void shouldUpdateAndReturnReport() throws Exception {
        when(reportService.update(any(Report.class))).thenReturn(firstValidReport);
        mockMvc.perform(put("/reports")
                        .param("id", "1")
                        .param("photoId", "photoid")
                        .param("foodRation", "ration")
                        .param("generalHealth", "health")
                        .param("behaviorChanges", "behavior")
                        .param("trialPeriodId", "1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("photoId").value("photoid"))
                .andExpect(jsonPath("foodRation").value("ration"))
                .andExpect(jsonPath("generalHealth").value("health"))
                .andExpect(jsonPath("behaviorChanges").value("behavior"))
                .andExpect(jsonPath("trialPeriodId").value(1L));
        verify(reportService, times(1)).update(any(Report.class));
    }

    @Test
    @DisplayName("При удалении отчёта, выдаёт сообщение о том что отчёт удалён")
    void shouldReturnMessageWhenReportDeleted() throws Exception {
        doNothing().when(reportService).deleteById(1L);
        mockMvc.perform(delete("/reports/id")
                        .param("id", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Отчёт успешно удалён"));
        verify(reportService, times(1)).deleteById(1L);
    }
}