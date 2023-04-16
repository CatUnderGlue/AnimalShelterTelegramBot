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

    private final Long catId = 1L;
    private final Long telegramId = 1L;
    private final String correctFirstName = "Leopold";
    private final String correctLastName = "Lodkin";
    private final String correctPhone = "258934";
    private final Cat validCat = new Cat(catId, null, null, null, null, null, null);
    private final Cat validCatWithOwner = new Cat(catId, "Kot", 1, true, true, telegramId, 1L);
    private final List<Cat> listOfCatWithOwner = List.of(validCatWithOwner);
    private final User validUser = new User(telegramId, correctFirstName, correctLastName, correctPhone);
    private final CatOwner validCatOwner = new CatOwner(telegramId, correctFirstName, correctLastName,
            correctPhone, null, null);
    private final CatOwner validCatOwnerWithCat = new CatOwner(telegramId, correctFirstName, correctLastName,
            correctPhone, listOfCatWithOwner, null);
    private final CatOwner secondValidCatOwner = new CatOwner(telegramId, correctLastName, null,
            correctPhone, null, null);
    private final CatOwner thirdValidCatOwner = new CatOwner(telegramId, correctLastName, correctLastName,
            correctPhone, null, null);
    private final List<CatOwner> listOfCatOwner = List.of(validCatOwner);
    private final List<CatOwner> listOfCatOwnerEmpty = new ArrayList<>();
    private final LocalDate date = LocalDate.now();
    private final List<Report> reportList = new ArrayList<>();
    private final TrialPeriod validTrialPeriod = new TrialPeriod(date, date.plusDays(30), date.minusDays(1), reportList,
            TrialPeriod.Result.IN_PROGRESS, telegramId, TrialPeriod.AnimalType.CAT, catId);

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
        when(catServiceMock.getById(catId)).thenReturn(validCat);
        when(catOwnerRepoMock.save(validCatOwner)).thenReturn(validCatOwner);
        when(trialPeriodServiceMock.create(validTrialPeriod, TrialPeriod.AnimalType.CAT)).thenReturn(validTrialPeriod);
        CatOwner actual = catOwnerService.create(validCatOwner, TrialPeriod.AnimalType.CAT, catId);
        assertEquals(validCatOwner, actual);
        verify(catOwnerRepoMock, times(1)).save(validCatOwner);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если у кота уже есть владелец")
    void shouldThrowAlreadyExistsExceptionWhenCatHasOwner() {
        when(catServiceMock.getById(catId)).thenReturn(validCatWithOwner);
        assertThrows(AlreadyExistsException.class, () -> catOwnerService.create(validCatOwner, TrialPeriod.AnimalType.CAT, catId));
    }

    @Test
    @DisplayName("Создаёт из пользователя и возвращает владельца кота со всеми полями, кроме временных")
    void shouldCreateFromUserAndReturnCatOwnerWithAllArgs() {
        when(catServiceMock.getById(catId)).thenReturn(validCat);
        when(catOwnerRepoMock.save(validCatOwner)).thenReturn(validCatOwner);
        when(trialPeriodServiceMock.create(validTrialPeriod, TrialPeriod.AnimalType.CAT)).thenReturn(validTrialPeriod);
        when(userServiceMock.getById(telegramId)).thenReturn(validUser);
        CatOwner actual = catOwnerService.create(telegramId, TrialPeriod.AnimalType.CAT, catId);
        assertEquals(validCatOwner, actual);
        verify(catOwnerRepoMock, times(1)).save(validCatOwner);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если у кота уже есть владелец")
    void shouldThrowAlreadyExistsExceptionWhenCatHasOwner2() {
        when(catServiceMock.getById(catId)).thenReturn(validCatWithOwner);
        assertThrows(AlreadyExistsException.class, () -> catOwnerService.create(telegramId, TrialPeriod.AnimalType.CAT, catId));
    }

    @Test
    @DisplayName("Возвращает владельца кота при поиске по id")
    void shouldReturnCatOwnerFoundById() {
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.of(validCatOwner));
        CatOwner actual = catOwnerService.getById(telegramId);
        assertEquals(validCatOwner, actual);
        verify(catOwnerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id владелец кота не найден")
    void shouldThrowNotFoundExWhenFindCatOwnerById() {
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.getById(telegramId));
        verify(catOwnerRepoMock, times(1)).findById(telegramId);
    }
    @Test
    @DisplayName("Возвращает список с владельцами котов")
    void shouldReturnListOfCatOwnersWhenGetAllCatOwner() {
        when(catOwnerRepoMock.findAll()).thenReturn(listOfCatOwner);
        List<CatOwner> actual = catOwnerService.getAll();
        assertEquals(listOfCatOwner, actual);
        verify(catOwnerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если владельцев котов нет")
    void shouldThrowNotFoundExWhenCatOwnerListIsEmpty() {
        when(catOwnerRepoMock.findAll()).thenReturn(listOfCatOwnerEmpty);
        assertThrows(NotFoundException.class, () -> catOwnerService.getAll());
    }

    @Test
    @DisplayName("Изменяет и возвращает владельца кота по новому объекту, не затрагивая null поля")
    void shouldUpdateCatOwnerWithoutNullFields() {
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.of(validCatOwner));
        when(catOwnerRepoMock.save(thirdValidCatOwner)).thenReturn(thirdValidCatOwner);
        CatOwner actual = catOwnerService.update(secondValidCatOwner);
        assertEquals(thirdValidCatOwner, actual);
        verify(catOwnerRepoMock, times( 1)).findById(telegramId);
        verify(catOwnerRepoMock, times(1)).save(thirdValidCatOwner);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному объекту владелец кота не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingCatOwner() {
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.update(validCatOwner));
        verify(catOwnerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id владелец кота не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingCatOwnerById() {
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> catOwnerService.deleteById(telegramId));
        verify(catOwnerRepoMock, times(1)).findById(telegramId);
    }



    @Test
    @DisplayName("Выбрасывает ошибку, если по объекту владелец кота не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingCatOwner() {
        when(catOwnerRepoMock.findById(telegramId)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> catOwnerService.delete(validCatOwner));
        verify(catOwnerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Удаляет владельца у кота по объекту.")
    void shouldDeleteCatOwner() {
        when(catServiceMock.update(validCatWithOwner)).thenReturn(validCat);
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.of(validCatOwnerWithCat));
        doNothing().when(catOwnerRepoMock).delete(validCatOwnerWithCat);
        catOwnerService.delete(validCatOwnerWithCat);
        verify(catOwnerRepoMock, times(1)).delete(validCatOwnerWithCat);
        verify(catOwnerRepoMock, times(1)).findById(telegramId);
        verify(catServiceMock, times(1)).update(validCatWithOwner);
    }

    @Test
    @DisplayName("Удаляет владельца у кота по id.")
    void shouldDeleteCatOwnerById() {
        when(catServiceMock.update(validCatWithOwner)).thenReturn(validCat);
        when(catOwnerRepoMock.findById(telegramId)).thenReturn(Optional.of(validCatOwnerWithCat));
        doNothing().when(catOwnerRepoMock).delete(validCatOwnerWithCat);
        catOwnerService.deleteById(validCatOwnerWithCat.getTelegramId());
        verify(catOwnerRepoMock, times(1)).delete(validCatOwnerWithCat);
        verify(catOwnerRepoMock, times(2)).findById(telegramId);
        verify(catServiceMock, times(1)).update(validCatWithOwner);
    }
}