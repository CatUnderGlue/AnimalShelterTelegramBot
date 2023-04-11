package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.repository.VolunteerRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {
    final Long TELEGRAM_ID = 1234567890L;
    final Long SECOND_TELEGRAM_ID = TELEGRAM_ID + 1;
    final String CORRECT_FIRST_NAME = "Bob";
    final String CORRECT_LAST_NAME = "Bee";
    final Volunteer VALID_VOLUNTEER = new Volunteer(TELEGRAM_ID, CORRECT_FIRST_NAME, CORRECT_LAST_NAME);
    final Volunteer SECOND_VALID_VOLUNTEER = new Volunteer(TELEGRAM_ID, CORRECT_LAST_NAME, null);
    final Volunteer THIRD_VALID_VOLUNTEER = new Volunteer(TELEGRAM_ID, CORRECT_LAST_NAME, CORRECT_LAST_NAME);
    final Volunteer INVALID_VOLUNTEER = new Volunteer(SECOND_TELEGRAM_ID, null, CORRECT_LAST_NAME);
    final List<Volunteer> LIST_OF_VOLUNTEER = List.of(VALID_VOLUNTEER);
    final List<Volunteer> EMPTY_LIST = new ArrayList<>();

    @Mock
    VolunteerRepo volunteerRepoMock;

    @InjectMocks
    VolunteerServiceImpl volunteerService;

    @Test
    @DisplayName("Создаёт и возвращает волонтёра со всеми полями")
    void shouldCreateAndReturnVolunteerWithAllArgs() {
        when(volunteerRepoMock.save(VALID_VOLUNTEER)).thenReturn(VALID_VOLUNTEER);
        Volunteer actual = volunteerService.create(VALID_VOLUNTEER);
        assertEquals(VALID_VOLUNTEER, actual);
        verify(volunteerRepoMock, times(1)).save(VALID_VOLUNTEER);
    }

    @Test
    @DisplayName("Выбрасывает ошибку о пустом или равном null поле, при некорректном параметре")
    void shouldThrowIllegalArgExWhenCreateVolunteer() {
        assertThrows(IllegalArgumentException.class, () -> volunteerService.create(INVALID_VOLUNTEER));
    }

    @Test
    @DisplayName("Возвращает волонтёра при поиске по id")
    void shouldReturnVolunteerFoundById() {
        when(volunteerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_VOLUNTEER));
        Volunteer actual = volunteerService.getById(TELEGRAM_ID);
        assertEquals(VALID_VOLUNTEER, actual);
        verify(volunteerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанном id волонтёр не найден")
    void shouldThrowNotFoundExWhenFindVolunteerById() {
        when(volunteerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.getById(TELEGRAM_ID));
        verify(volunteerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Возвращает список с волонтёрами")
    void shouldReturnListOfVolunteersWhenGetAllVolunteers() {
        when(volunteerRepoMock.findAll()).thenReturn(LIST_OF_VOLUNTEER);
        List<Volunteer> actual = volunteerService.getAll();
        assertEquals(LIST_OF_VOLUNTEER, actual);
        verify(volunteerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с волонтёрами пуст")
    void shouldThrowNotFoundExWhenFindAllVolunteers() {
        when(volunteerRepoMock.findAll()).thenReturn(EMPTY_LIST);
        assertThrows(NotFoundException.class, () -> volunteerService.getAll());
        verify(volunteerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Изменяет и возвращает волонтёра по новому объекту, не затрагивая null поля")
    void shouldUpdateVolunteerWithoutNullFields() {
        when(volunteerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_VOLUNTEER));
        when(volunteerRepoMock.save(THIRD_VALID_VOLUNTEER)).thenReturn(THIRD_VALID_VOLUNTEER);
        Volunteer actual = volunteerService.update(SECOND_VALID_VOLUNTEER);
        assertEquals(THIRD_VALID_VOLUNTEER, actual);
        verify(volunteerRepoMock, times( 1)).findById(TELEGRAM_ID);
        verify(volunteerRepoMock, times(1)).save(THIRD_VALID_VOLUNTEER);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному объекту волонтёр не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingVolunteer() {
        when(volunteerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.update(VALID_VOLUNTEER));
        verify(volunteerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному id волонтёр не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteerById() {
        when(volunteerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.deleteById(TELEGRAM_ID));
        verify(volunteerRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по объекту волонтёр не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteer() {
        when(volunteerRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.delete(VALID_VOLUNTEER));
        verify(volunteerRepoMock, times(1)).findById(TELEGRAM_ID);
    }
}