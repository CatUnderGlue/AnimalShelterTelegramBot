package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.repository.UserRepo;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    private final Long telegramId = 1L;
    private final String correctFirstName = "Leopold";
    private final String correctLastName = "Lodkin";
    private final String correctPhone = "258934";
    private final String correctShelterType = "CAT";
    private final User validUser = new User(telegramId, correctFirstName, correctLastName, correctPhone);
    private final User secondValidUser = new User(telegramId, correctLastName, null, correctPhone);
    private final User thirdValidUser = new User(telegramId, correctLastName, correctLastName, correctPhone);
    private final User validUserWithShelterType = new User(telegramId, correctLastName, correctLastName, correctPhone,
            correctShelterType, null);
    private final List<User> listOfUser = List.of(validUser);

    @Mock
    UserRepo userRepoMock;

    @InjectMocks
    UserServiceImpl userService;

    @Test
    @DisplayName("Создаёт и возвращает пользователя со всеми полями, кроме временных")
    void shouldCreateAndReturnUserWithAllArgs() {
        when(userRepoMock.save(validUser)).thenReturn(validUser);
        User actual = userService.create(validUser);
        assertEquals(validUser, actual);
        verify(userRepoMock, times(1)).save(validUser);
    }

    @Test
    @DisplayName("Возвращает пользователя при поиске по id")
    void shouldReturnUserFoundById() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validUser));
        User actual = userService.getById(telegramId);
        assertEquals(validUser, actual);
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Возвращает выбранный пользователем тип приюта при поиске по id пользователя")
    void shouldReturnShelterTypeFoundById() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validUserWithShelterType));
        String actual = userService.getShelterById(telegramId);
        assertEquals(correctShelterType, actual);
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если при поиске типа приюта по указанному id пользователь не найден")
    void shouldThrowNotFoundExWhenGetShelterById() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getShelterById(telegramId));
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id пользователь не найден")
    void shouldThrowNotFoundExWhenFindUserById() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.getById(telegramId));
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Возвращает список с пользователями")
    void shouldReturnListOfUsersWhenGetAllUser() {
        when(userRepoMock.findAll()).thenReturn(listOfUser);
        List<User> actual = userService.getAll();
        assertEquals(listOfUser, actual);
        verify(userRepoMock, times(1)).findAll();
    }

    @Test
    @DisplayName("Изменяет и возвращает пользователя по новому объекту, не затрагивая null поля")
    void shouldUpdateUserWithoutNullFields() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.of(validUser));
        when(userRepoMock.save(thirdValidUser)).thenReturn(thirdValidUser);
        User actual = userService.update(secondValidUser);
        assertEquals(thirdValidUser, actual);
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
        verify(userRepoMock, times(1)).save(thirdValidUser);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному объекту пользователь не найден. Изменение невозможно.")
    void shouldThrowNotFoundExWhenUpdatingUser() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.update(validUser));
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по указанному id пользователь не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingUserById() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.deleteById(telegramId));
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }

    @Test
    @DisplayName("Выбрасывает ошибку, если по объекту пользователь не найден. Удаление невозможно.")
    void shouldThrowNotFoundExWhenDeletingUser() {
        when(userRepoMock.findByTelegramId(telegramId)).thenReturn(Optional.empty());
        assertThrows(NotFoundException.class, () -> userService.delete(validUser));
        verify(userRepoMock, times(1)).findByTelegramId(telegramId);
    }
}