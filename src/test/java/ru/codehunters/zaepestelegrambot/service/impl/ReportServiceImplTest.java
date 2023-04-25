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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReportServiceImplTest {

    private final Long id = 1234567890L;
    private final Long secondId = 1234567890L + 1;
    private final String text = "ваш текст";
    private final String photoId = "qewgsdjgsdoiypofsdffhnsu";
    private final String caption = """
            Рацион: ваш текст;
            Самочувствие: ваш текст;
            Поведение: ваш текст;
            """;
    private final LocalDate dateNow = LocalDate.now();
    private final Report validReport = new Report(id, photoId, text, text, text, dateNow, id);
    private final Report validReportWithoutId = new Report(photoId, text, text, text, dateNow, id);
    private final Report secondValidReport = new Report(id, photoId, null, null, null, null, secondId);
    private final Report thirdValidReport = new Report(id, photoId, text, text, text, dateNow, secondId);
    private final List<Report> reportList = List.of(validReport);
    private final List<Report> emptyList = new ArrayList<>();
    private final TrialPeriod trialPeriodWithReport = new TrialPeriod(id, dateNow, dateNow, dateNow,
            reportList, TrialPeriod.Result.IN_PROGRESS, id, TrialPeriod.AnimalType.CAT, id);
    private final TrialPeriod trialPeriodWithoutReport = new TrialPeriod(id, dateNow, dateNow, dateNow.minusDays(1),
            new ArrayList<>(), TrialPeriod.Result.IN_PROGRESS, id, TrialPeriod.AnimalType.CAT, id);
    private final List<TrialPeriod> trialPeriodListWithReport = List.of(trialPeriodWithReport);
    private final List<TrialPeriod> trialPeriodListWithoutReport = List.of(trialPeriodWithoutReport);

    @Mock
    ReportRepo reportRepoMock;

    @Mock
    TrialPeriodService trialPeriodService;

    @InjectMocks
    ReportServiceImpl reportService;

    @Test
    @DisplayName("Создаёт и возвращает отчёт со всеми полями")
    void shouldCreateAndReturnReportWithAllArgs() {
        when(reportRepoMock.save(validReport)).thenReturn(validReport);
        Report actual = reportService.create(validReport);
        assertEquals(validReport, actual);
        verify(reportRepoMock, times(1)).save(validReport);
    }

    @Test
    @DisplayName("Возвращает отчёт при поиске по id")
    void shouldReturnReportFoundById() {
        when(reportRepoMock.findById(id)).thenReturn(Optional.of(validReport));
        Report actual = reportService.getById(id);
        assertEquals(validReport, actual);
        verify(reportRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанном id отчёт не найден")
    void shouldThrowNotFoundExWhenFindReportById() {
        when(reportRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.getById(id));
        verify(reportRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Возвращает отчёт при поиске по дате и id испытательного срока")
    void shouldReturnReportFoundByDateAndTrialId() {
        when(reportRepoMock.findByReceiveDateAndTrialPeriodId(dateNow, id)).thenReturn(Optional.of(validReport));
        Report actual = reportService.getByDateAndTrialId(dateNow, id);
        assertEquals(validReport, actual);
        verify(reportRepoMock, times(1)).findByReceiveDateAndTrialPeriodId(dateNow, id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанным дате и id испытательного срока отчёт не найден")
    void shouldThrowNotFoundExWhenFindReportByDateAndTrialId() {
        when(reportRepoMock.findByReceiveDateAndTrialPeriodId(dateNow, id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.getByDateAndTrialId(dateNow, id));
        verify(reportRepoMock, times(1)).findByReceiveDateAndTrialPeriodId(dateNow, id);
    }

    @Test
    @DisplayName("Возвращает список с отчётами")
    void shouldReturnListOfReportsWhenGetAllReports() {
        when(reportRepoMock.findAll()).thenReturn(reportList);
        List<Report> actual = reportService.getAll();
        assertEquals(reportList, actual);
        verify(reportRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с отчётами пуст")
    void shouldThrowNotFoundExWhenListOfReportsIsEmpty() {
        when(reportRepoMock.findAll()).thenReturn(emptyList);
        assertThrows(NotFoundException.class, () -> reportService.getAll());
        verify(reportRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Возвращает список с отчётами по id испытательного срока")
    void shouldReturnListOfReportsWhenGetAllReportsByOTrialId() {
        when(reportRepoMock.findAllByTrialPeriodId(id)).thenReturn(reportList);
        List<Report> actual = reportService.gelAllByTrialPeriodId(id);
        assertEquals(reportList, actual);
        verify(reportRepoMock, times(1)).findAllByTrialPeriodId(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с отчётами по id испытательного срока пуст")
    void shouldThrowNotFoundExWhenListOfReportsByTrialIdIsEmpty() {
        when(reportRepoMock.findAllByTrialPeriodId(id)).thenReturn(emptyList);
        assertThrows(NotFoundException.class, () -> reportService.gelAllByTrialPeriodId(id));
        verify(reportRepoMock, times(1)).findAllByTrialPeriodId(id);
    }

    @Test
    @DisplayName("Изменяет и возвращает отчёт по новому объекту, не затрагивая null поля")
    void shouldUpdateVolunteerWithoutNullFields() {
        when(reportRepoMock.findById(id)).thenReturn(Optional.of(validReport));
        when(reportRepoMock.save(thirdValidReport)).thenReturn(thirdValidReport);
        Report actual = reportService.update(secondValidReport);
        assertEquals(thirdValidReport, actual);
        verify(reportRepoMock, times(1)).findById(id);
        verify(reportRepoMock, times(1)).save(thirdValidReport);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному объекту отчёт не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingReport() {
        when(reportRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.update(validReport));
        verify(reportRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному id отчёт не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingReportById() {
        when(reportRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.deleteById(id));
        verify(reportRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по объекту отчёт не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingReport() {
        when(reportRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> reportService.delete(validReport));
        verify(reportRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Создаёт отчёт по данным из телеграма")
    void shouldCreateReportBasedOnDataFromTelegram() {
        when(trialPeriodService.getAllByOwnerId(id)).thenReturn(trialPeriodListWithoutReport);
        when(trialPeriodService.update(trialPeriodWithoutReport)).thenReturn(trialPeriodWithReport);
        when(reportRepoMock.save(validReportWithoutId)).thenReturn(validReport);
        Report actual = reportService.createFromTelegram(photoId, caption, id);
        assertEquals(validReport, actual);
        verify(trialPeriodService, times(1)).getAllByOwnerId(id);
        verify(trialPeriodService, times(1)).update(trialPeriodWithoutReport);
        verify(reportRepoMock, times(1)).save(validReportWithoutId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку при попытке создать отчёт повторно в один день.")
    void shouldThrowAlreadyExistsExceptionWhenLastReportDateEqualsNow() {
        when(trialPeriodService.getAllByOwnerId(id)).thenReturn(trialPeriodListWithReport);
        assertThrows(AlreadyExistsException.class, () -> reportService.createFromTelegram(photoId, caption, id));
        verify(trialPeriodService, times(1)).getAllByOwnerId(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку при попытке создать отчёт без описания.")
    void shouldThrowIllegalArgumentExceptionWhenCaptionIsBlankOrNull() {
        when(trialPeriodService.getAllByOwnerId(id)).thenReturn(trialPeriodListWithoutReport);
        assertThrows(IllegalArgumentException.class, () -> reportService.createFromTelegram(photoId, "", id));
        verify(trialPeriodService, times(1)).getAllByOwnerId(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку при попытке создать отчёт с неправильным описанием.")
    void shouldThrowIllegalArgumentExceptionWhenCaptionIsIllegal() {
        when(trialPeriodService.getAllByOwnerId(id)).thenReturn(trialPeriodListWithoutReport);
        assertThrows(IllegalArgumentException.class, () -> reportService.createFromTelegram(photoId, text, id));
        verify(trialPeriodService, times(1)).getAllByOwnerId(id);
    }
}