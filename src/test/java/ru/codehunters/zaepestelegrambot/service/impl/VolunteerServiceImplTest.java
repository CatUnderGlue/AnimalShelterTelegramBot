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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VolunteerServiceImplTest {
    private final Long telegramId = 1234567890L;
    private final String correctFirstName = "Bob";
    private final String correctLastName = "Bee";
    private final Volunteer validVolunteer = new Volunteer(telegramId, correctFirstName, correctLastName);
    private final Volunteer secondValidVolunteer = new Volunteer(telegramId, correctLastName, null);
    private final Volunteer thirdValidVolunteer = new Volunteer(telegramId, correctLastName, correctLastName);
    private final List<Volunteer> listOfVolunteer = List.of(validVolunteer);
    private final List<Volunteer> emptyList = new ArrayList<>();

    @Mock
    VolunteerRepo volunteerRepoMock;

    @InjectMocks
    VolunteerServiceImpl volunteerService;

    @Test
    @DisplayName("Создаёт и возвращает волонтёра со всеми полями")
    void shouldCreateAndReturnVolunteerWithAllArgs() {
        when(volunteerRepoMock.save(validVolunteer)).thenReturn(validVolunteer);
        Volunteer actual = volunteerService.create(validVolunteer);
        assertEquals(validVolunteer, actual);
        verify(volunteerRepoMock, times(1)).save(validVolunteer);
    }

    @Test
    @DisplayName("Возвращает волонтёра при поиске по id")
    void shouldReturnVolunteerFoundById() {
        when(volunteerRepoMock.findById(telegramId)).thenReturn(Optional.of(validVolunteer));
        Volunteer actual = volunteerService.getById(telegramId);
        assertEquals(validVolunteer, actual);
        verify(volunteerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанном id волонтёр не найден")
    void shouldThrowNotFoundExWhenFindVolunteerById() {
        when(volunteerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.getById(telegramId));
        verify(volunteerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Возвращает список с волонтёрами")
    void shouldReturnListOfVolunteersWhenGetAllVolunteers() {
        when(volunteerRepoMock.findAll()).thenReturn(listOfVolunteer);
        List<Volunteer> actual = volunteerService.getAll();
        assertEquals(listOfVolunteer, actual);
        verify(volunteerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что список с волонтёрами пуст")
    void shouldThrowNotFoundExWhenFindAllVolunteers() {
        when(volunteerRepoMock.findAll()).thenReturn(emptyList);
        assertThrows(NotFoundException.class, () -> volunteerService.getAll());
        verify(volunteerRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Изменяет и возвращает волонтёра по новому объекту, не затрагивая null поля")
    void shouldUpdateVolunteerWithoutNullFields() {
        when(volunteerRepoMock.findById(telegramId)).thenReturn(Optional.of(validVolunteer));
        when(volunteerRepoMock.save(thirdValidVolunteer)).thenReturn(thirdValidVolunteer);
        Volunteer actual = volunteerService.update(secondValidVolunteer);
        assertEquals(thirdValidVolunteer, actual);
        verify(volunteerRepoMock, times(1)).findById(telegramId);
        verify(volunteerRepoMock, times(1)).save(thirdValidVolunteer);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному объекту волонтёр не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingVolunteer() {
        when(volunteerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.update(validVolunteer));
        verify(volunteerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по указанному id волонтёр не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteerById() {
        when(volunteerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.deleteById(telegramId));
        verify(volunteerRepoMock, times(1)).findById(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, что по объекту волонтёр не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingVolunteer() {
        when(volunteerRepoMock.findById(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> volunteerService.delete(validVolunteer));
        verify(volunteerRepoMock, times(1)).findById(telegramId);
    }
}