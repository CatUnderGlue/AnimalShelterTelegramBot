package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.repository.CatRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatServiceImplTest {

    static final Long ID = 1L;
    static final String VALID_NAME = "CAT";
    static final String NEW_NAME = "Ferdinande";
    static final Integer AGE = 1;
    static final boolean IS_HEALTHY = true;
    static final boolean VACCINATED = true;
    static final Long OWNER_ID = 312123412412L;
    static final Long SHELTER_ID = 1L;
    static final Cat VALID_CAT = new Cat(ID, VALID_NAME, AGE, IS_HEALTHY, VACCINATED, OWNER_ID, SHELTER_ID);
    static final Cat NEW_CAT = new Cat(ID, NEW_NAME, AGE, null, null, null, null);
    static final Cat RESULTED_CAT = new Cat(ID, NEW_NAME, AGE, IS_HEALTHY, VACCINATED, OWNER_ID, SHELTER_ID);

    @Mock
    CatRepo catRepoMock;

    @InjectMocks
    CatServiceImpl out;

    @Test
    @DisplayName("Создаёт и возвращает кота со всеми полями")
    void shouldCreateAndReturnCatWithAllArgs() {
        when(catRepoMock.save(VALID_CAT)).thenReturn(VALID_CAT);
        Cat savedCat = out.create(VALID_CAT);
        verify(catRepoMock, times(1)).save(VALID_CAT);
        assertEquals(VALID_CAT, savedCat);
    }


    @Test
    @DisplayName("Возвращает кота по id")
    void shouldReturnCatById() {
        when(catRepoMock.findById(ID)).thenReturn(Optional.of(VALID_CAT));
        Cat findCat = out.getById(ID);
        assertEquals(VALID_CAT, findCat);
        verify(catRepoMock, times(1)).findById(ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, когда по данному id кота нет")
    void shouldThrowNotFoundExceptionWhenFindCatById() {
        when(catRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getById(ID));
        verify(catRepoMock, times(1)).findById(ID);
    }

    @DisplayName("Возвращает кота по id хозяина")
    @Test
    void shouldReturnCatByOwnerId() {
        when(catRepoMock.findByOwnerId(OWNER_ID)).thenReturn(Optional.of(VALID_CAT));
        Cat result = out.getByUserId(OWNER_ID);
        verify(catRepoMock).findByOwnerId(OWNER_ID);
        assertEquals(VALID_CAT, result);
    }

    @DisplayName("Выбрасывает ошибку, когда нет хозяина кота по данному ID")
    @Test
    void shouldThrowNotFoundExceptionWhenReturnCatByOwnerId() {
        when(catRepoMock.findByOwnerId(OWNER_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.getByUserId(OWNER_ID));
        verify(catRepoMock, times(1)).findByOwnerId(OWNER_ID);
    }

    @DisplayName("Обновляет и возвращает кота по новому объекту, не принимая null поля")
    @Test
    void shouldUpdateCatWithoutNullFields() {
        when(catRepoMock.findById(ID)).thenReturn(Optional.of(VALID_CAT));
        when(catRepoMock.save(RESULTED_CAT)).thenReturn(RESULTED_CAT);
        Cat result = out.update(NEW_CAT);
        assertEquals(RESULTED_CAT, result);
        verify(catRepoMock, times(1)).findById(ID);
        verify(catRepoMock, times(1)).save(RESULTED_CAT);
    }

    @DisplayName("Выбрасывает ошибку, когда по данному id кота нет. Обновление не возможно")
    @Test
    void shouldThrowNotFoundExceptionWhenUpdateCatId() {
        when(catRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.update(NEW_CAT));
        verify(catRepoMock, times(1)).findById(ID);
    }


    @DisplayName("Получает список всех котов")
    @Test
    void shouldCollectAllCat() {
        List<Cat> list = List.of(NEW_CAT);
        when(catRepoMock.findAll()).thenReturn(list);
        List<Cat> actual = out.getAll();
        assertEquals(list, actual);
        verify(catRepoMock, times(1)).findAll();

    }

    @DisplayName("Удаляет кота по id и выбрасывает ошибку если кот не найден")
    @Test
    void shouldThrowNotFoundExceptionWhenDelCatId() {
        when(catRepoMock.findById(ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> out.remove(ID));
        verify(catRepoMock, times(1)).findById(ID);
    }

}