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
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.repository.TrialPeriodRepo;
import ru.codehunters.zaepestelegrambot.service.CatService;
import ru.codehunters.zaepestelegrambot.service.DogService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TrialPeriodServiceImplTest {
    private final Long id = 1241421L;
    private final Long secondId = id + 1;
    private final LocalDate date = LocalDate.now();
    private final List<Report> reportList = new ArrayList<>();
    private final Cat cat = new Cat("Name", 2, true, true, id);
    private final Dog dog = new Dog("Name", 2, true, true, id);
    private final TrialPeriod validTrialPeriodCat = new TrialPeriod(id, date, date, date, reportList,
            TrialPeriod.Result.IN_PROGRESS, id, TrialPeriod.AnimalType.CAT, id);
    private final TrialPeriod validTrialPeriodDog = new TrialPeriod(id, date, date, date, reportList,
            TrialPeriod.Result.IN_PROGRESS, id, TrialPeriod.AnimalType.DOG, id);
    private final TrialPeriod secondValidTrialPeriod = new TrialPeriod(id, null, null, null, null,
            null, null, null, secondId);
    private final TrialPeriod thirdValidTrialPeriod = new TrialPeriod(id, date, date, date, reportList,
            TrialPeriod.Result.IN_PROGRESS, id, TrialPeriod.AnimalType.CAT, secondId);
    private final List<TrialPeriod> trialPeriodList = List.of(validTrialPeriodCat);
    private final List<TrialPeriod> emptyList = new ArrayList<>();

    @Mock
    TrialPeriodRepo trialPeriodRepoMock;

    @Mock
    CatService catService;

    @Mock
    DogService dogService;

    @InjectMocks
    TrialPeriodServiceImpl trialPeriodService;

    @Test
    @DisplayName("Создаёт и возвращает испытательный срок со всеми полями по коту")
    void shouldCreateAndReturnTrialPeriodWithAllArgsByCAT() {
        when(trialPeriodRepoMock.save(validTrialPeriodCat)).thenReturn(validTrialPeriodCat);
        when(catService.getById(id)).thenReturn(cat);
        TrialPeriod actual = trialPeriodService.create(validTrialPeriodCat, TrialPeriod.AnimalType.CAT);
        assertEquals(validTrialPeriodCat, actual);
        verify(trialPeriodRepoMock, times(1)).save(validTrialPeriodCat);
        verify(catService, times(1)).getById(id);
    }

    @Test
    @DisplayName("Создаёт и возвращает испытательный срок со всеми полями по собаке")
    void shouldCreateAndReturnTrialPeriodWithAllArgsByDOG() {
        when(trialPeriodRepoMock.save(validTrialPeriodDog)).thenReturn(validTrialPeriodDog);
        when(dogService.getById(id)).thenReturn(dog);
        TrialPeriod actual = trialPeriodService.create(validTrialPeriodDog, TrialPeriod.AnimalType.DOG);
        assertEquals(validTrialPeriodDog, actual);
        verify(trialPeriodRepoMock, times(1)).save(validTrialPeriodDog);
        verify(dogService, times(1)).getById(id);
    }

    @Test
    @DisplayName("Создаёт и возвращает испытательный срок со всеми полями")
    void shouldCreateAndReturnTrialPeriodWithAllArgs() {
        when(trialPeriodRepoMock.save(validTrialPeriodCat)).thenReturn(validTrialPeriodCat);
        TrialPeriod actual = trialPeriodService.create(validTrialPeriodCat);
        assertEquals(validTrialPeriodCat, actual);
        verify(trialPeriodRepoMock, times(1)).save(validTrialPeriodCat);
    }

    @Test
    @DisplayName("Возвращает испытательный срок при поиске по id")
    void shouldReturnTrialPeriodFoundById() {
        when(trialPeriodRepoMock.findById(id)).thenReturn(Optional.of(validTrialPeriodCat));
        TrialPeriod actual = trialPeriodService.getById(id);
        assertEquals(validTrialPeriodCat, actual);
        verify(trialPeriodRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанном id испытательный срок не найден")
    void shouldThrowNotFoundExWhenFindTrialPeriodById() {
        when(trialPeriodRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.getById(id));
        verify(trialPeriodRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Возвращает список с испытательными сроками")
    void shouldReturnListOfTrialPeriodsWhenGetAllTrialPeriods() {
        when(trialPeriodRepoMock.findAll()).thenReturn(trialPeriodList);
        List<TrialPeriod> actual = trialPeriodService.getAll();
        assertEquals(trialPeriodList, actual);
        verify(trialPeriodRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с испытательными сроками пуст")
    void shouldThrowNotFoundExWhenListOfTrialPeriodIsEmpty() {
        when(trialPeriodRepoMock.findAll()).thenReturn(emptyList);
        assertThrows(NotFoundException.class, () -> trialPeriodService.getAll());
        verify(trialPeriodRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Возвращает список с испытательными сроками по id владельца животного")
    void shouldReturnListOfTrialPeriodsWhenGetAllTrialPeriodsByOwnerId() {
        when(trialPeriodRepoMock.findAllByOwnerId(id)).thenReturn(trialPeriodList);
        List<TrialPeriod> actual = trialPeriodService.getAllByOwnerId(id);
        assertEquals(trialPeriodList, actual);
        verify(trialPeriodRepoMock, times(1)).findAllByOwnerId(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с испытательными сроками по id владельца животного пуст")
    void shouldThrowNotFoundExWhenListOfTrialPeriodByOwnerIdIsEmpty() {
        when(trialPeriodRepoMock.findAllByOwnerId(id)).thenReturn(emptyList);
        assertThrows(NotFoundException.class, () -> trialPeriodService.getAllByOwnerId(id));
        verify(trialPeriodRepoMock, times(1)).findAllByOwnerId(id);
    }

    @Test
    @DisplayName("Изменяет и возвращает испытательный срок по новому объекту, не затрагивая null поля")
    void shouldUpdateVolunteerWithoutNullFields() {
        when(trialPeriodRepoMock.findById(id)).thenReturn(Optional.of(validTrialPeriodCat));
        when(trialPeriodRepoMock.save(thirdValidTrialPeriod)).thenReturn(thirdValidTrialPeriod);
        TrialPeriod actual = trialPeriodService.update(secondValidTrialPeriod);
        assertEquals(thirdValidTrialPeriod, actual);
        verify(trialPeriodRepoMock, times(1)).findById(id);
        verify(trialPeriodRepoMock, times(1)).save(thirdValidTrialPeriod);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному объекту испытательный срок не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingTrialPeriod() {
        when(trialPeriodRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.update(validTrialPeriodCat));
        verify(trialPeriodRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному id испытательный срок не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteerById() {
        when(trialPeriodRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.deleteById(id));
        verify(trialPeriodRepoMock, times(1)).findById(id);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по объекту испытательный срок не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteer() {
        when(trialPeriodRepoMock.findById(id)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> trialPeriodService.delete(validTrialPeriodCat));
        verify(trialPeriodRepoMock, times(1)).findById(id);
    }
}