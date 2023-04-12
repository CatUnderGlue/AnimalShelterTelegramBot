package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.repository.ReportRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    final Long ID = 1234567890L;
    final Long SECOND_ID = 1234567890L + 1;
    final String TEXT = "abc";
    final LocalDate DATE = LocalDate.now();

    final Report VALID_REPORT = new Report(ID, TEXT, TEXT, TEXT, TEXT,
            DATE, ID);
    final Report SECOND_VALID_REPORT = new Report(ID, null, null, null, null,
            null, SECOND_ID);
    final Report THIRD_VALID_REPORT = new Report(ID, TEXT, TEXT, TEXT, TEXT,
            DATE, SECOND_ID);
    final Report INVALID_REPORT = new Report(ID, TEXT, null, TEXT, TEXT,
            DATE, ID);
    final List<Report> REPORTS_LIST = List.of(VALID_REPORT);
    final List<Report> EMPTY_LIST = new ArrayList<>();

    @Mock
    ReportRepo reportRepoMock;

    @InjectMocks
    ReportServiceImpl reportService;

    @Test
    @DisplayName("Создаёт и возвращает отчёт со всеми полями")
    void shouldCreateAndReturnReportWithAllArgs() {
        when(reportRepoMock.save(VALID_REPORT)).thenReturn(VALID_REPORT);
        Report actual = reportService.create(VALID_REPORT);
        assertEquals(VALID_REPORT, actual);
        verify(reportRepoMock, times(1)).save(VALID_REPORT);
    }

    @Test
    @DisplayName("Выбрасывает ошибку о пустом или равном null поле, при некорректном параметре")
    void shouldThrowIllegalArgExWhenCreateReport() {
        assertThrows(IllegalArgumentException.class, () -> reportService.create(INVALID_REPORT));
    }

    @Test
    @DisplayName("Возвращает отчёт при поиске по id")
    void shouldReturnReportFoundById() {
        when(reportRepoMock.findById(ID)).thenReturn(Optional.of(VALID_REPORT));
        Report actual = reportService.getById(ID);
        assertEquals(VALID_REPORT, actual);
        verify(reportRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанном id отчёт не найден")
    void shouldThrowNotFoundExWhenFindReportById() {
        when(reportRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.getById(ID));
        verify(reportRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Возвращает отчёт при поиске по дате и id испытательного срока")
    void shouldReturnReportFoundByDateAndTrialId() {
        when(reportRepoMock.findByReceiveDateAndTrialPeriodId(DATE, ID)).thenReturn(Optional.of(VALID_REPORT));
        Report actual = reportService.getByDateAndTrialId(DATE, ID);
        assertEquals(VALID_REPORT, actual);
        verify(reportRepoMock, times(1)).findByReceiveDateAndTrialPeriodId(DATE, ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанным дате и id испытательного срока отчёт не найден")
    void shouldThrowNotFoundExWhenFindReportByDateAndTrialId() {
        when(reportRepoMock.findByReceiveDateAndTrialPeriodId(DATE, ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.getByDateAndTrialId(DATE, ID));
        verify(reportRepoMock, times(1)).findByReceiveDateAndTrialPeriodId(DATE, ID);
    }

    @Test
    @DisplayName("Возвращает список с отчётами")
    void shouldReturnListOfReportsWhenGetAllReports() {
        when(reportRepoMock.findAll()).thenReturn(REPORTS_LIST);
        List<Report> actual = reportService.getAll();
        assertEquals(REPORTS_LIST, actual);
        verify(reportRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с отчётами пуст")
    void shouldThrowNotFoundExWhenListOfReportsIsEmpty() {
        when(reportRepoMock.findAll()).thenReturn(EMPTY_LIST);
        assertThrows(NotFoundException.class, () -> reportService.getAll());
        verify(reportRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Возвращает список с отчётами по id испытательного срока")
    void shouldReturnListOfReportsWhenGetAllReportsByOTrialId() {
        when(reportRepoMock.findAllByTrialPeriodId(ID)).thenReturn(REPORTS_LIST);
        List<Report> actual = reportService.gelAllByTrialPeriodId(ID);
        assertEquals(REPORTS_LIST, actual);
        verify(reportRepoMock, times(1)).findAllByTrialPeriodId(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с отчётами по id испытательного срока пуст")
    void shouldThrowNotFoundExWhenListOfReportsByTrialIdIsEmpty() {
        when(reportRepoMock.findAllByTrialPeriodId(ID)).thenReturn(EMPTY_LIST);
        assertThrows(NotFoundException.class, () -> reportService.gelAllByTrialPeriodId(ID));
        verify(reportRepoMock, times(1)).findAllByTrialPeriodId(ID);
    }

    @Test
    @DisplayName("Изменяет и возвращает отчёт по новому объекту, не затрагивая null поля")
    void shouldUpdateVolunteerWithoutNullFields() {
        when(reportRepoMock.findById(ID)).thenReturn(Optional.of(VALID_REPORT));
        when(reportRepoMock.save(THIRD_VALID_REPORT)).thenReturn(THIRD_VALID_REPORT);
        Report actual = reportService.update(SECOND_VALID_REPORT);
        assertEquals(THIRD_VALID_REPORT, actual);
        verify(reportRepoMock, times( 1)).findById(ID);
        verify(reportRepoMock, times(1)).save(THIRD_VALID_REPORT);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному объекту отчёт не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingReport() {
        when(reportRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.update(VALID_REPORT));
        verify(reportRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному id отчёт не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingReportById() {
        when(reportRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.deleteById(ID));
        verify(reportRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по объекту отчёт не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingReport() {
        when(reportRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.delete(VALID_REPORT));
        verify(reportRepoMock, times(1)).findById(ID);
    }
}