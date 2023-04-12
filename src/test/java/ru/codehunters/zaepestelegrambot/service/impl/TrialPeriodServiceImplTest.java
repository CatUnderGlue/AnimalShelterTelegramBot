package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.repository.TrialPeriodRepo;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrialPeriodServiceImplTest {
    final Long ID = 1241421L;
    final Long SECOND_ID = ID + 1;
    final LocalDate DATE = LocalDate.now();
    final List<Report> REPORTS_LIST = new ArrayList<>();

    final TrialPeriod VALID_TRIAL_PERIOD = new TrialPeriod(ID, DATE, DATE, DATE, REPORTS_LIST,
            TrialPeriod.Result.IN_PROGRESS, ID, TrialPeriod.AnimalType.CAT, ID);
    final TrialPeriod SECOND_VALID_TRIAL_PERIOD = new TrialPeriod(ID, null, null, null, null,
            null, null, null, SECOND_ID);
    final TrialPeriod THIRD_VALID_TRIAL_PERIOD = new TrialPeriod(ID, DATE, DATE, DATE, REPORTS_LIST,
            TrialPeriod.Result.IN_PROGRESS, ID, TrialPeriod.AnimalType.CAT, SECOND_ID);
    final TrialPeriod INVALID_TRIAL_PERIOD = new TrialPeriod(SECOND_ID, null, DATE, DATE, REPORTS_LIST,
            TrialPeriod.Result.IN_PROGRESS, ID, TrialPeriod.AnimalType.CAT, ID);
    final List<TrialPeriod> TRIAL_PERIOD_LIST = List.of(VALID_TRIAL_PERIOD);
    final List<TrialPeriod> EMPTY_LIST = new ArrayList<>();

    @Mock
    TrialPeriodRepo trialPeriodRepoMock;

    @InjectMocks
    TrialPeriodServiceImpl trialPeriodService;

    @Test
    @DisplayName("Создаёт и возвращает испытательный срок со всеми полями")
    void shouldCreateAndReturnTrialPeriodWithAllArgs() {
        when(trialPeriodRepoMock.save(VALID_TRIAL_PERIOD)).thenReturn(VALID_TRIAL_PERIOD);
        TrialPeriod actual = trialPeriodService.create(VALID_TRIAL_PERIOD);
        assertEquals(VALID_TRIAL_PERIOD, actual);
        verify(trialPeriodRepoMock, times(1)).save(VALID_TRIAL_PERIOD);
    }

    @Test
    @DisplayName("Выбрасывает ошибку о пустом или равном null поле, при некорректном параметре")
    void shouldThrowIllegalArgExWhenCreateTrialPeriod() {
        assertThrows(IllegalArgumentException.class, () -> trialPeriodService.create(INVALID_TRIAL_PERIOD));
    }

    @Test
    @DisplayName("Возвращает испытательный срок при поиске по id")
    void shouldReturnTrialPeriodFoundById() {
        when(trialPeriodRepoMock.findById(ID)).thenReturn(Optional.of(VALID_TRIAL_PERIOD));
        TrialPeriod actual = trialPeriodService.getById(ID);
        assertEquals(VALID_TRIAL_PERIOD, actual);
        verify(trialPeriodRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанном id испытательный срок не найден")
    void shouldThrowNotFoundExWhenFindTrialPeriodById() {
        when(trialPeriodRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.getById(ID));
        verify(trialPeriodRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Возвращает список с испытательными сроками")
    void shouldReturnListOfTrialPeriodsWhenGetAllTrialPeriods() {
        when(trialPeriodRepoMock.findAll()).thenReturn(TRIAL_PERIOD_LIST);
        List<TrialPeriod> actual = trialPeriodService.getAll();
        assertEquals(TRIAL_PERIOD_LIST, actual);
        verify(trialPeriodRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с испытательными сроками пуст")
    void shouldThrowNotFoundExWhenListOfTrialPeriodIsEmpty() {
        when(trialPeriodRepoMock.findAll()).thenReturn(EMPTY_LIST);
        assertThrows(NotFoundException.class, () -> trialPeriodService.getAll());
        verify(trialPeriodRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Возвращает список с испытательными сроками по id владельца животного")
    void shouldReturnListOfTrialPeriodsWhenGetAllTrialPeriodsByOwnerId() {
        when(trialPeriodRepoMock.findAllByOwnerId(ID)).thenReturn(TRIAL_PERIOD_LIST);
        List<TrialPeriod> actual = trialPeriodService.getAllByOwnerId(ID);
        assertEquals(TRIAL_PERIOD_LIST, actual);
        verify(trialPeriodRepoMock, times(1)).findAllByOwnerId(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с испытательными сроками по id владельца животного пуст")
    void shouldThrowNotFoundExWhenListOfTrialPeriodByOwnerIdIsEmpty() {
        when(trialPeriodRepoMock.findAllByOwnerId(ID)).thenReturn(EMPTY_LIST);
        assertThrows(NotFoundException.class, () -> trialPeriodService.getAllByOwnerId(ID));
        verify(trialPeriodRepoMock, times(1)).findAllByOwnerId(ID);
    }

    @Test
    @DisplayName("Изменяет и возвращает испытательный срок по новому объекту, не затрагивая null поля")
    void shouldUpdateVolunteerWithoutNullFields() {
        when(trialPeriodRepoMock.findById(ID)).thenReturn(Optional.of(VALID_TRIAL_PERIOD));
        when(trialPeriodRepoMock.save(THIRD_VALID_TRIAL_PERIOD)).thenReturn(THIRD_VALID_TRIAL_PERIOD);
        TrialPeriod actual = trialPeriodService.update(SECOND_VALID_TRIAL_PERIOD);
        assertEquals(THIRD_VALID_TRIAL_PERIOD, actual);
        verify(trialPeriodRepoMock, times( 1)).findById(ID);
        verify(trialPeriodRepoMock, times(1)).save(THIRD_VALID_TRIAL_PERIOD);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному объекту испытательный срок не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingTrialPeriod() {
        when(trialPeriodRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.update(VALID_TRIAL_PERIOD));
        verify(trialPeriodRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному id испытательный срок не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteerById() {
        when(trialPeriodRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.deleteById(ID));
        verify(trialPeriodRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по объекту испытательный срок не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteer() {
        when(trialPeriodRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.delete(VALID_TRIAL_PERIOD));
        verify(trialPeriodRepoMock, times(1)).findById(ID);
    }
}