package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.exception.UserNotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.service.UserService;

@RestController
@RequestMapping("users")
@Tag(name = "Пользователь", description = "CRUD-методы для работы с пользователями")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    @Operation(
            summary = "Создать пользователя"
    )
    public ResponseEntity<User> create(@RequestParam @Parameter(description = "Телеграм id пользователя") Long telegramId,
                                            @RequestParam @Parameter(description = "Имя") String firstName,
                                            @RequestParam @Parameter(description = "Фамилия") String lastName,
                                            @RequestParam @Parameter(description = "Телефон") String phone) {
        try {
            return ResponseEntity.ok(userService.create(new User(telegramId, firstName, lastName, phone)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(
            summary = "Получение списка всех пользователей"
    )
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(userService.getAll());
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение пользователя по id"
    )
    @Parameter(
            name = "id",
            description = "Id пользователя",
            example = "1"
    )
    public ResponseEntity<Object> getById(@RequestParam Long userId) {
        try {
            User user = userService.getById(userId);
            return ResponseEntity.ok().body(user);
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(
            summary = "Изменить пользователя"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id пользователя") Long telegramId,
                                         @RequestParam @Parameter(description = "Имя") String firstName,
                                         @RequestParam @Parameter(description = "Фамилия") String lastName,
                                         @RequestParam @Parameter(description = "Телефон") String phone) {
        try {
            return ResponseEntity.ok(userService.create(new User(telegramId, firstName, lastName, phone)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    @Operation(
            summary = "Удаление пользователя"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Пользователь в формате json",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = User.class)
                    )
            }
    )
    public ResponseEntity<String> delete(@RequestBody User user) {
        try {
            userService.delete(user);
            return ResponseEntity.ok().body("Пользователь успешно удалён");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление пользователя по id"
    )
    @Parameter(
            name = "id",
            description = "Id пользователя",
            example = "1"
    )
    public ResponseEntity<String> deleteById(@RequestParam Long userId) {
        try {
            userService.deleteById(userId);
            return ResponseEntity.ok().body("Пользователь успешно удалён");
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
