package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.AlreadyExistsException;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.ReportRepo;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

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
    final String TEXT = "ваш текст";
    final String PHOTO_ID = "qewgsdjgsdoiypofsdffhnsu";
    final String CAPTION = """
            Рацион: ваш текст;
            Самочувствие: ваш текст;
            Поведение: ваш текст;
            """;
    final LocalDate DATE_NOW = LocalDate.now();
    final Report VALID_REPORT = new Report(ID, PHOTO_ID, TEXT, TEXT, TEXT,
            DATE_NOW, ID);
    final Report VALID_REPORT_WITHOUT_ID = new Report(PHOTO_ID, TEXT, TEXT, TEXT,
            DATE_NOW, ID);
    final Report SECOND_VALID_REPORT = new Report(ID, null, null, null, null,
            null, SECOND_ID);
    final Report THIRD_VALID_REPORT = new Report(ID, PHOTO_ID, TEXT, TEXT, TEXT,
            DATE_NOW, SECOND_ID);
    final List<Report> REPORTS_LIST = List.of(VALID_REPORT);
    final List<Report> EMPTY_LIST = new ArrayList<>();
    final TrialPeriod TRIAL_PERIOD_WITH_REPORT = new TrialPeriod(ID, DATE_NOW, DATE_NOW, DATE_NOW,
            REPORTS_LIST, TrialPeriod.Result.IN_PROGRESS, ID, TrialPeriod.AnimalType.CAT, ID);
    final TrialPeriod TRIAL_PERIOD_WITHOUT_REPORT = new TrialPeriod(ID, DATE_NOW, DATE_NOW, DATE_NOW.minusDays(1),
            new ArrayList<>(), TrialPeriod.Result.IN_PROGRESS, ID, TrialPeriod.AnimalType.CAT, ID);
    final List<TrialPeriod> TRIAL_PERIOD_LIST_WITH_REPORT = List.of(TRIAL_PERIOD_WITH_REPORT);
    final List<TrialPeriod> TRIAL_PERIOD_LIST_WITHOUT_REPORT = List.of(TRIAL_PERIOD_WITHOUT_REPORT);

    @Mock
    ReportRepo reportRepoMock;

    @Mock
    TrialPeriodService trialPeriodService;

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
        when(reportRepoMock.findByReceiveDateAndTrialPeriodId(DATE_NOW, ID)).thenReturn(Optional.of(VALID_REPORT));
        Report actual = reportService.getByDateAndTrialId(DATE_NOW, ID);
        assertEquals(VALID_REPORT, actual);
        verify(reportRepoMock, times(1)).findByReceiveDateAndTrialPeriodId(DATE_NOW, ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанным дате и id испытательного срока отчёт не найден")
    void shouldThrowNotFoundExWhenFindReportByDateAndTrialId() {
        when(reportRepoMock.findByReceiveDateAndTrialPeriodId(DATE_NOW, ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.getByDateAndTrialId(DATE_NOW, ID));
        verify(reportRepoMock, times(1)).findByReceiveDateAndTrialPeriodId(DATE_NOW, ID);
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

    @Test
    @DisplayName("Создаёт отчёт по данным из телеграма")
    void shouldCreateReportBasedOnDataFromTelegram() {
        when(trialPeriodService.getAllByOwnerId(ID)).thenReturn(TRIAL_PERIOD_LIST_WITHOUT_REPORT);
        when(trialPeriodService.update(TRIAL_PERIOD_WITHOUT_REPORT)).thenReturn(TRIAL_PERIOD_WITH_REPORT);
        when(reportRepoMock.save(VALID_REPORT_WITHOUT_ID)).thenReturn(VALID_REPORT);
        Report actual = reportService.createFromTelegram(PHOTO_ID, CAPTION, ID);
        assertEquals(VALID_REPORT, actual);
        verify(trialPeriodService, times(1)).getAllByOwnerId(ID);
        verify(trialPeriodService, times(1)).update(TRIAL_PERIOD_WITHOUT_REPORT);
        verify(reportRepoMock, times(1)).save(VALID_REPORT_WITHOUT_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку при попытке создать отчёт повторно в один день.")
    void shouldThrowAlreadyExistsExceptionWhenLastReportDateEqualsNow() {
        when(trialPeriodService.getAllByOwnerId(ID)).thenReturn(TRIAL_PERIOD_LIST_WITH_REPORT);
        assertThrows(AlreadyExistsException.class, () -> reportService.createFromTelegram(PHOTO_ID, CAPTION, ID));
        verify(trialPeriodService, times(1)).getAllByOwnerId(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку при попытке создать отчёт без описания.")
    void shouldThrowIllegalArgumentExceptionWhenCaptionIsBlankOrNull() {
        when(trialPeriodService.getAllByOwnerId(ID)).thenReturn(TRIAL_PERIOD_LIST_WITHOUT_REPORT);
        assertThrows(IllegalArgumentException.class, () -> reportService.createFromTelegram(PHOTO_ID, "", ID));
        verify(trialPeriodService, times(1)).getAllByOwnerId(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку при попытке создать отчёт с неправильным описанием.")
    void shouldThrowIllegalArgumentExceptionWhenCaptionIsIllegal() {
        when(trialPeriodService.getAllByOwnerId(ID)).thenReturn(TRIAL_PERIOD_LIST_WITHOUT_REPORT);
        assertThrows(IllegalArgumentException.class, () -> reportService.createFromTelegram(PHOTO_ID, TEXT, ID));
        verify(trialPeriodService, times(1)).getAllByOwnerId(ID);
    }
}