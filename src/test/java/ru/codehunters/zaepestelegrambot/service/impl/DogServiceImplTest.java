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

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogServiceImplTest {

    final Long ID = 1L;
    final String VALID_NAME = "Dog";
    final String NEW_NAME = "Bucephalus";
    final Integer AGE = 3;
    final boolean IS_HEALTHY = true;
    final boolean VACCINATED = false;
    final Long OWNER_ID = 123312123L;
    final Long SHELTER_ID = 1L;
    final Dog VALID_DOG = new Dog(ID, VALID_NAME, AGE, IS_HEALTHY, VACCINATED, OWNER_ID, SHELTER_ID);
    final Dog NEW_DOG = new Dog(ID, NEW_NAME, AGE, null, true, null, null);

    @Mock
    DogRepo dogRepoMock;

    @InjectMocks
    DogServiceImpl out;

    @Test
    @DisplayName("Создаёт и возвращает кота со всеми полями")
    void shouldCreateAndReturnCatWithAllArgs() {
        when(dogRepoMock.save(VALID_DOG)).thenReturn(VALID_DOG);
        Dog dog = out.create(VALID_DOG);
        verify(dogRepoMock, times(1)).save(VALID_DOG);
        assertEquals(VALID_DOG, dog);
    }


    @Test
    @DisplayName("Возвращает кота по id")
    void shouldReturnCatById() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.of(VALID_DOG));
        Dog findDog = out.getById(ID);
        assertEquals(VALID_DOG, findDog);
        verify(dogRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, когда по данному id кота нет")
    void shouldThrowNotFoundExceptionWhenFindCatById() {
        when(dogRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getById(ID));
        verify(dogRepoMock, times(1)).findById(ID);
    }

    @DisplayName("Возвращает кота по id хозяина")
    @Test
    void shouldReturnCatByOwnerId() {
        when(dogRepoMock.findByOwnerId(OWNER_ID)).thenReturn(Optional.of(VALID_DOG));
        Dog result = out.getByUserId(OWNER_ID);
        verify(dogRepoMock).findByOwnerId(OWNER_ID);
        assertEquals(VALID_DOG, result);
    }
}