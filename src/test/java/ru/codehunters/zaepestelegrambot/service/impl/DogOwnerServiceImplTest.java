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
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;
import ru.codehunters.zaepestelegrambot.repository.DogOwnerRepo;
import ru.codehunters.zaepestelegrambot.service.DogService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogOwnerServiceImplTest {
    private final Long dogId = 1L;
    private final Long telegramId = 1L;
    private final String correctFirstName = "Leopold";
    private final String correctLastName = "Lodin";
    private final String correctPhone = "258934";
    private final Dog validDog = new Dog(dogId, "Dog", 1, true, true, null, 1L);
    private final Dog validDogWithOwner = new Dog(dogId, "Dog", 1, true, true, telegramId, 1L);
    private final List<Dog> listOfDogWithOwner = List.of(validDogWithOwner);
    private final User validUser = new User(telegramId, correctFirstName, correctLastName, correctPhone);
    private final DogOwner validDogOwner = new DogOwner(null, telegramId, correctFirstName, correctLastName,
            correctPhone, null, null);
    private final DogOwner validDogOwnerWithDog = new DogOwner(1L, telegramId, correctFirstName, correctLastName,
            correctPhone, null, listOfDogWithOwner);
    private final DogOwner secondValidDogOwner = new DogOwner(1L, telegramId, correctLastName, correctLastName,
            correctPhone, null, null);
    private final List<DogOwner> listOfDogOwner = List.of(validDogOwner);
    private final List<DogOwner> listOfDogOwnerEmpty = new ArrayList<>();
    private final LocalDate date = LocalDate.now();
    private final List<Report> reportList = new ArrayList<>();
    private final TrialPeriod validTrialPeriod = new TrialPeriod(date, date.plusDays(30), date.minusDays(1), reportList,
            TrialPeriod.Result.IN_PROGRESS, telegramId, TrialPeriod.AnimalType.DOG, dogId);

    @Mock
    DogOwnerRepo dogOwnerRepoMock;

    @Mock
    DogService dogServiceMock;

    @Mock
    UserService userServiceMock;

    @Mock
    TrialPeriodService trialPeriodServiceMock;

    @InjectMocks
    DogOwnerServiceImpl dogOwnerService;

    @Test
    @DisplayName("Создаёт и возвращает владельца собаки со всеми полями, кроме временных")
    void shouldCreateAndReturnDogOwnerWithAllArgs() {
        when(dogServiceMock.getById(dogId)).thenReturn(validDog);
        when(dogOwnerRepoMock.save(validDogOwner)).thenReturn(validDogOwner);
        when(trialPeriodServiceMock.create(validTrialPeriod, TrialPeriod.AnimalType.DOG)).thenReturn(validTrialPeriod);
        DogOwner actual = dogOwnerService.create(validDogOwner, TrialPeriod.AnimalType.DOG, dogId);
        assertEquals(validDogOwner, actual);
        verify(dogOwnerRepoMock, times(1)).save(validDogOwner);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если у собаки уже есть владелец")
    void shouldThrowAlreadyExistsExceptionWhenDogHasOwner() {
        when(dogServiceMock.getById(dogId)).thenReturn(validDogWithOwner);
        assertThrows(AlreadyExistsException.class, () -> dogOwnerService.create(validDogOwner, TrialPeriod.AnimalType.DOG, dogId));
    }

    @Test
    @DisplayName("Создаёт из пользователя и возвращает владельца собаки со всеми полями, кроме временных")
    void shouldCreateFromUserAndReturnDogOwnerWithAllArgs() {
        when(dogServiceMock.getById(dogId)).thenReturn(validDog);
        when(dogOwnerRepoMock.save(validDogOwner)).thenReturn(secondValidDogOwner);
        when(trialPeriodServiceMock.create(validTrialPeriod, TrialPeriod.AnimalType.DOG)).thenReturn(validTrialPeriod);
        when(userServiceMock.getById(telegramId)).thenReturn(validUser);
        DogOwner actual = dogOwnerService.create(telegramId, TrialPeriod.AnimalType.DOG, dogId);
        assertEquals(secondValidDogOwner, actual);
        verify(dogOwnerRepoMock, times(1)).save(validDogOwner);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если у собаки уже есть владелец")
    void shouldThrowAlreadyExistsExceptionWhenDogHasOwner2() {
        when(dogServiceMock.getById(dogId)).thenReturn(validDogWithOwner);
        assertThrows(AlreadyExistsException.class, () -> dogOwnerService.create(telegramId, TrialPeriod.AnimalType.DOG, dogId));
    }

    @Test
    @DisplayName("Возвращает владельца собаки при поиске по id")
    void shouldReturnDogOwnerFoundById() {
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validDogOwner));
        DogOwner actual = dogOwnerService.getById(telegramId);
        assertEquals(validDogOwner, actual);
        verify(dogOwnerRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id владелец собаки не найден")
    void shouldThrowNotFoundExWhenFindDogOwnerById() {
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> dogOwnerService.getById(telegramId));
        verify(dogOwnerRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Возвращает список с владельцами собак")
    void shouldReturnListOfDogOwnersWhenGetAllDogOwner() {
        when(dogOwnerRepoMock.findAll()).thenReturn(listOfDogOwner);
        List<DogOwner> actual = dogOwnerService.getAll();
        assertEquals(listOfDogOwner, actual);
        verify(dogOwnerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если владельцев собак нет")
    void shouldThrowNotFoundExWhenDogOwnerListIsEmpty() {
        when(dogOwnerRepoMock.findAll()).thenReturn(listOfDogOwnerEmpty);
        assertThrows(NotFoundException.class, () -> dogOwnerService.getAll());
    }

    @Test
    @DisplayName("Изменяет и возвращает владельца собаки по новому объекту, не затрагивая null поля")
    void shouldUpdateDogOwnerWithoutNullFields() {
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(secondValidDogOwner));
        when(dogOwnerRepoMock.save(secondValidDogOwner)).thenReturn(secondValidDogOwner);
        DogOwner actual = dogOwnerService.update(secondValidDogOwner);
        assertEquals(secondValidDogOwner, actual);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному объекту владелец собаки не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingDogOwner() {
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> dogOwnerService.update(validDogOwner));
        verify(dogOwnerRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id владелец собаки не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingDogOwnerById() {
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> dogOwnerService.deleteById(telegramId));
        verify(dogOwnerRepoMock, times(1)).findByTelegramId(telegramId);
    }


    @Test
    @DisplayName("Выбрасывает ошибку, если по объекту владелец кота не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingDogOwner() {
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenThrow(NotFoundException.class);
        assertThrows(NotFoundException.class, () -> dogOwnerService.delete(validDogOwner));
        verify(dogOwnerRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Удаляет владельца у кота по объекту.")
    void shouldDeleteDogOwner() {
        when(dogServiceMock.update(validDogWithOwner)).thenReturn(validDog);
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validDogOwnerWithDog));
        when(dogServiceMock.getAllByUserId(telegramId)).thenReturn(List.of(validDogWithOwner));
        doNothing().when(dogOwnerRepoMock).delete(validDogOwnerWithDog);
        dogOwnerService.delete(validDogOwnerWithDog);
        verify(dogOwnerRepoMock, times(1)).delete(validDogOwnerWithDog);
        verify(dogOwnerRepoMock, times(1)).findByTelegramId(telegramId);
        verify(dogServiceMock, times(1)).update(validDogWithOwner);
    }

    @Test
    @DisplayName("Удаляет владельца у кота по id.")
    void shouldDeleteDogOwnerById() {
        when(dogServiceMock.update(validDogWithOwner)).thenReturn(validDog);
        when(dogOwnerRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validDogOwnerWithDog));
        when(dogServiceMock.getAllByUserId(telegramId)).thenReturn(List.of(validDogWithOwner));
        doNothing().when(dogOwnerRepoMock).delete(validDogOwnerWithDog);
        dogOwnerService.deleteById(validDogOwnerWithDog.getTelegramId());
        verify(dogOwnerRepoMock, times(1)).delete(validDogOwnerWithDog);
        verify(dogOwnerRepoMock, times(2)).findByTelegramId(telegramId);
        verify(dogServiceMock, times(1)).update(validDogWithOwner);
    }
}