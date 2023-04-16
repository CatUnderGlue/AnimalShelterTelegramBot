package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.repository.DogRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogServiceImplTest {

    static final Long ID = 1L;
    static final String VALID_NAME = "Dog";
    static final String NEW_NAME = "Bucephalus";
    static final Integer AGE = 3;
    static final boolean IS_HEALTHY = true;
    static final boolean VACCINATED = true;
    static final Long OWNER_ID = 123312123L;
    static final Long SHELTER_ID = 1L;
    static final Dog VALID_DOG = new Dog(ID, VALID_NAME, AGE, IS_HEALTHY, VACCINATED, OWNER_ID, SHELTER_ID);
    static final Dog NEW_DOG = new Dog(ID, NEW_NAME, AGE, null, null, null, null);
    static final Dog RESULTED_DOG = new Dog(ID, NEW_NAME, AGE, IS_HEALTHY, VACCINATED, OWNER_ID, SHELTER_ID);

    @Mock
    DogRepo dogRepoMock;

    @InjectMocks
    DogServiceImpl out;

    @Test
    @DisplayName("Создаёт и возвращает собаку со всеми полями")
    void shouldCreateAndReturnDogWithAllArgs() {
        when(dogRepoMock.save(VALID_DOG)).thenReturn(VALID_DOG);
        Dog saved = out.create(VALID_DOG);
        verify(dogRepoMock, times(1)).save(VALID_DOG);
        assertEquals(VALID_DOG, saved);
    }


    @Test
    @DisplayName("Возвращает собаку по id")
    void shouldReturnCatById() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.of(VALID_DOG));
        Dog findDog = out.getById(ID);
        assertEquals(VALID_DOG, findDog);
        verify(dogRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, когда по данному id собаки нет")
    void shouldThrowNotFoundExceptionWhenFindCatById() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getById(ID));
        verify(dogRepoMock, times(1)).findById(ID);
    }

    @DisplayName("Возвращает собаку по id хозяина")
    @Test
    void shouldReturnCatByOwnerId() {
        when(dogRepoMock.findByOwnerId(OWNER_ID)).thenReturn(Optional.of(VALID_DOG));
        Dog result = out.getByUserId(OWNER_ID);
        verify(dogRepoMock).findByOwnerId(OWNER_ID);
        assertEquals(VALID_DOG, result);
    }

    @DisplayName("Выбрасывает ошибку, когда нет хозяина собаки по данному ID")
    @Test
    void shouldThrowNotFoundExceptionWhenReturnCatByOwnerId() {
        when(dogRepoMock.findByOwnerId(OWNER_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getByUserId(OWNER_ID));
        verify(dogRepoMock, times(1)).findByOwnerId(OWNER_ID);
    }

    @DisplayName("Обновляет и возвращает собаку по новому объекту, не принимая null поля")
    @Test
    void shouldUpdateCatWithoutNullFields() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.of(VALID_DOG));
        when(dogRepoMock.save(RESULTED_DOG)).thenReturn(RESULTED_DOG);
        Dog result = out.update(NEW_DOG);
        assertEquals(RESULTED_DOG, result);
        verify(dogRepoMock, times(1)).findById(ID);
        verify(dogRepoMock, times(1)).save(RESULTED_DOG);
    }

    @DisplayName("Выбрасывает ошибку, когда по данному id собаки нет. Обновление не возможно")
    @Test
    void shouldThrowNotFoundExceptionWhenUpdateCatId() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.update(NEW_DOG));
        verify(dogRepoMock, times(1)).findById(ID);
    }


    @DisplayName("Получает список всех собак")
    @Test
    void shouldCollectAllCat() {
        List<Dog> list = List.of(NEW_DOG);
        when(dogRepoMock.findAll()).thenReturn(list);
        List<Dog> actual = out.getAll();
        assertEquals(list, actual);
        verify(dogRepoMock, times(1)).findAll();

    }

    @DisplayName("Удаляет собаку по id и выбрасывает ошибку если собака не найден")
    @Test
    void shouldThrowNotFoundExceptionWhenDelCatId() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.remove(ID));
        verify(dogRepoMock, times(1)).findById(ID);
    }
}