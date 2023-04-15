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
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.repository.CatOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.CatService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatOwnerServiceImplTest {

    final Long CAT_ID = 1L;
    final Long TELEGRAM_ID = 1L;
    final String CORRECT_FIRST_NAME = "Leopold";
    final String CORRECT_LAST_NAME = "Lodkin";
    final String CORRECT_PHONE = "258934";
    final Cat VALID_CAT = new Cat(CAT_ID, null, null, null, null, null, null);
    final Cat VALID_CAT_WITH_OWNER = new Cat(CAT_ID, "Kot", 1, true, true, TELEGRAM_ID, 1L);
    final List<Cat> LIST_OF_CAT_WITH_OWNER = List.of(VALID_CAT_WITH_OWNER);
    final User VALID_USER = new User(TELEGRAM_ID, CORRECT_FIRST_NAME, CORRECT_LAST_NAME, CORRECT_PHONE);
    final CatOwner VALID_CAT_OWNER = new CatOwner(TELEGRAM_ID, CORRECT_FIRST_NAME, CORRECT_LAST_NAME,
            CORRECT_PHONE, null, null);
    final CatOwner VALID_CAT_OWNER_WITH_CAT = new CatOwner(TELEGRAM_ID, CORRECT_FIRST_NAME, CORRECT_LAST_NAME,
            CORRECT_PHONE, LIST_OF_CAT_WITH_OWNER, null);
    final CatOwner VALID_CAT_OWNER_WITH_CAT2 = new CatOwner(TELEGRAM_ID, CORRECT_FIRST_NAME, CORRECT_LAST_NAME,
            CORRECT_PHONE, LIST_OF_CAT_WITH_OWNER, null);
    final CatOwner SECOND_VALID_CAT_OWNER = new CatOwner(TELEGRAM_ID, CORRECT_LAST_NAME, null,
            CORRECT_PHONE, null, null);
    final CatOwner THIRD_VALID_CAT_OWNER = new CatOwner(TELEGRAM_ID, CORRECT_LAST_NAME, CORRECT_LAST_NAME,
            CORRECT_PHONE, null, null);
    final List<CatOwner> LIST_OF_CAT_OWNER = List.of(VALID_CAT_OWNER);
    final List<CatOwner> LIST_OF_CAT_OWNER_EMPTY = new ArrayList<>();
    final Long ID = null;
    final LocalDate DATE = LocalDate.now();
    final List<Report> REPORTS_LIST = new ArrayList<>();
    final TrialPeriod VALID_TRIAL_PERIOD = new TrialPeriod(DATE, DATE.plusDays(30), DATE.minusDays(1), REPORTS_LIST,
            TrialPeriod.Result.IN_PROGRESS, TELEGRAM_ID, TrialPeriod.AnimalType.CAT, CAT_ID);

    @Mock
    CatOwnerRepo catOwnerRepoMock;

    @Mock
    CatService catServiceMock;

    @Mock
    UserService userServiceMock;

    @Mock
    TrialPeriodService trialPeriodServiceMock;

    @InjectMocks
    CatOwnerServiceImpl catOwnerService;

    @Test
    @DisplayName("Создаёт и возвращает владельца кота со всеми полями, кроме временных")
    void shouldCreateAndReturnCatOwnerWithAllArgs() {
        when(catServiceMock.getById(CAT_ID)).thenReturn(VALID_CAT);
        when(catOwnerRepoMock.save(VALID_CAT_OWNER)).thenReturn(VALID_CAT_OWNER);
        when(trialPeriodServiceMock.create(VALID_TRIAL_PERIOD, TrialPeriod.AnimalType.CAT)).thenReturn(VALID_TRIAL_PERIOD);
        CatOwner actual = catOwnerService.create(VALID_CAT_OWNER, TrialPeriod.AnimalType.CAT, CAT_ID);
        assertEquals(VALID_CAT_OWNER, actual);
        verify(catOwnerRepoMock, times(1)).save(VALID_CAT_OWNER);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если у кота уже есть владелец")
    void shouldThrowAlreadyExistsExceptionWhenCatHasOwner() {
        when(catServiceMock.getById(CAT_ID)).thenReturn(VALID_CAT_WITH_OWNER);
        assertThrows(AlreadyExistsException.class, () -> catOwnerService.create(VALID_CAT_OWNER, TrialPeriod.AnimalType.CAT, CAT_ID));
    }

    @Test
    @DisplayName("Создаёт из пользователя и возвращает владельца кота со всеми полями, кроме временных")
    void shouldCreateFromUserAndReturnCatOwnerWithAllArgs() {
        when(catServiceMock.getById(CAT_ID)).thenReturn(VALID_CAT);
        when(catOwnerRepoMock.save(VALID_CAT_OWNER)).thenReturn(VALID_CAT_OWNER);
        when(trialPeriodServiceMock.create(VALID_TRIAL_PERIOD, TrialPeriod.AnimalType.CAT)).thenReturn(VALID_TRIAL_PERIOD);
        when(userServiceMock.getById(TELEGRAM_ID)).thenReturn(VALID_USER);
        CatOwner actual = catOwnerService.create(TELEGRAM_ID, TrialPeriod.AnimalType.CAT, CAT_ID);
        assertEquals(VALID_CAT_OWNER, actual);
        verify(catOwnerRepoMock, times(1)).save(VALID_CAT_OWNER);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если у кота уже есть владелец")
    void shouldThrowAlreadyExistsExceptionWhenCatHasOwner2() {
        when(catServiceMock.getById(CAT_ID)).thenReturn(VALID_CAT_WITH_OWNER);
        assertThrows(AlreadyExistsException.class, () -> catOwnerService.create(TELEGRAM_ID, TrialPeriod.AnimalType.CAT, CAT_ID));
    }

    @Test
    @DisplayName("Возвращает владельца кота при поиске по id")
    void shouldReturnCatOwnerFoundById() {
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_CAT_OWNER));
        CatOwner actual = catOwnerService.getById(TELEGRAM_ID);
        assertEquals(VALID_CAT_OWNER, actual);
        verify(catOwnerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id владелец кота не найден")
    void shouldThrowNotFoundExWhenFindCatOwnerById() {
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.getById(TELEGRAM_ID));
        verify(catOwnerRepoMock, times(1)).findById(TELEGRAM_ID);
    }
    @Test
    @DisplayName("Возвращает список с владельцами котов")
    void shouldReturnListOfCatOwnersWhenGetAllCatOwner() {
        when(catOwnerRepoMock.findAll()).thenReturn(LIST_OF_CAT_OWNER);
        List<CatOwner> actual = catOwnerService.getAll();
        assertEquals(LIST_OF_CAT_OWNER, actual);
        verify(catOwnerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если владельцев котов нет")
    void shouldThrowNotFoundExWhenCatOwnerListIsEmpty() {
        when(catOwnerRepoMock.findAll()).thenReturn(LIST_OF_CAT_OWNER_EMPTY);
        assertThrows(NotFoundException.class, () -> catOwnerService.getAll());
    }

    @Test
    @DisplayName("Изменяет и возвращает владельца кота по новому объекту, не затрагивая null поля")
    void shouldUpdateCatOwnerWithoutNullFields() {
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_CAT_OWNER));
        when(catOwnerRepoMock.save(THIRD_VALID_CAT_OWNER)).thenReturn(THIRD_VALID_CAT_OWNER);
        CatOwner actual = catOwnerService.update(SECOND_VALID_CAT_OWNER);
        assertEquals(THIRD_VALID_CAT_OWNER, actual);
        verify(catOwnerRepoMock, times( 1)).findById(TELEGRAM_ID);
        verify(catOwnerRepoMock, times(1)).save(THIRD_VALID_CAT_OWNER);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному объекту владелец кота не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingCatOwner() {
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.update(VALID_CAT_OWNER));
        verify(catOwnerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id владелец кота не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingCatOwnerById() {
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.deleteById(TELEGRAM_ID));
        verify(catOwnerRepoMock, times(1)).findById(TELEGRAM_ID);
    }



    @Test
    @DisplayName("Выбрасывает ошибку, если по объекту владелец кота не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingCatOwner() {
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.delete(VALID_CAT_OWNER));
        verify(catOwnerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Удаляет владельца у кота по объекту.")
    void shouldDeleteCatOwner() {
        when(catServiceMock.update(VALID_CAT_WITH_OWNER)).thenReturn(VALID_CAT);
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_CAT_OWNER_WITH_CAT));
        doNothing().when(catOwnerRepoMock).delete(VALID_CAT_OWNER_WITH_CAT);
        catOwnerService.delete(VALID_CAT_OWNER_WITH_CAT);
        verify(catOwnerRepoMock, times(1)).delete(VALID_CAT_OWNER);
    }

    @Test
    @DisplayName("Удаляет владельца у кота по id.")
    void shouldDeleteCatOwnerById() {
        when(catServiceMock.update(VALID_CAT_WITH_OWNER)).thenReturn(VALID_CAT);
        when(catOwnerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_CAT_OWNER_WITH_CAT));
        doNothing().when(catOwnerRepoMock).delete(VALID_CAT_OWNER);
        catOwnerService.deleteById(TELEGRAM_ID);
        verify(catOwnerRepoMock, times(1)).delete(VALID_CAT_OWNER);
    }
}