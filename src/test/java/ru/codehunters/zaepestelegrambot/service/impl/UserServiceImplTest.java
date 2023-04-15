package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.repository.UserRepo;
import ru.codehunters.zaepestelegrambot.repository.VolunteerRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    static final Long TELEGRAM_ID = 1L;
    static final String CORRECT_FIRST_NAME = "Leopold";
    static final String CORRECT_LAST_NAME = "Lodkin";
    static final String CORRECT_PHONE = "258934";
    static final String CORRECT_SHELTER_TYPE = "CAT";
    static final User VALID_USER = new User(TELEGRAM_ID, CORRECT_FIRST_NAME, CORRECT_LAST_NAME, CORRECT_PHONE);
    static final User SECOND_VALID_USER = new User(TELEGRAM_ID, CORRECT_LAST_NAME, null, CORRECT_PHONE);
    static final User THIRD_VALID_USER = new User(TELEGRAM_ID, CORRECT_LAST_NAME, CORRECT_LAST_NAME, CORRECT_PHONE);
    static final User VALID_USER_WITH_SHELTER_TYPE = new User(TELEGRAM_ID, CORRECT_LAST_NAME, CORRECT_LAST_NAME, CORRECT_PHONE,
            CORRECT_SHELTER_TYPE, null);
    static final List<User> LIST_OF_USER = List.of(VALID_USER);

    @Mock
    UserRepo userRepoMock;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Создаёт и возвращает пользователя со всеми полями, кроме временных")
    void shouldCreateAndReturnUserWithAllArgs() {
        when(userRepoMock.save(VALID_USER)).thenReturn(VALID_USER);
        User actual = userService.create(VALID_USER);
        assertEquals(VALID_USER, actual);
        verify(userRepoMock, times(1)).save(VALID_USER);
    }
    @Test
    @DisplayName("Возвращает пользователя при поиске по id")
    void shouldReturnUserFoundById() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_USER));
        User actual = userService.getById(TELEGRAM_ID);
        assertEquals(VALID_USER, actual);
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Возвращает выбранный пользователем тип приюта при поиске по id пользователя")
    void shouldReturnShelterTypeFoundById() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_USER_WITH_SHELTER_TYPE));
        String actual = userService.getShelterById(TELEGRAM_ID);
        assertEquals(CORRECT_SHELTER_TYPE, actual);
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если при поиске типа приюта по указанному id пользователь не найден")
    void shouldThrowNotFoundExWhenGetShelterById() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getShelterById(TELEGRAM_ID));
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id пользователь не найден")
    void shouldThrowNotFoundExWhenFindUserById() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getById(TELEGRAM_ID));
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }
    @Test
    @DisplayName("Возвращает список с пользователями")
    void shouldReturnListOfUsersWhenGetAllUser() {
        when(userRepoMock.findAll()).thenReturn(LIST_OF_USER);
        List<User> actual = userService.getAll();
        assertEquals(LIST_OF_USER, actual);
        verify(userRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Изменяет и возвращает пользователя по новому объекту, не затрагивая null поля")
    void shouldUpdateUserWithoutNullFields() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.of(VALID_USER));
        when(userRepoMock.save(THIRD_VALID_USER)).thenReturn(THIRD_VALID_USER);
        User actual = userService.update(SECOND_VALID_USER);
        assertEquals(THIRD_VALID_USER, actual);
        verify(userRepoMock, times( 1)).findById(TELEGRAM_ID);
        verify(userRepoMock, times(1)).save(THIRD_VALID_USER);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному объекту пользователь не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingUser() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(VALID_USER));
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id пользователь не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingUserById() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.deleteById(TELEGRAM_ID));
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по объекту пользователь не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingUser() {
        when(userRepoMock.findById(TELEGRAM_ID)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete(VALID_USER));
        verify(userRepoMock, times(1)).findById(TELEGRAM_ID);
    }
}