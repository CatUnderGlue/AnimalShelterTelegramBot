package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
    @Operation(summary = "Создать пользователя")
    public ResponseEntity<User> create(@RequestParam @Parameter(description = "Телеграм id пользователя") Long telegramId,
                                       @RequestParam @Parameter(description = "Имя") String firstName,
                                       @RequestParam @Parameter(description = "Фамилия") String lastName,
                                       @RequestParam @Parameter(description = "Телефон") String phone) {
        return ResponseEntity.ok(userService.create(new User(telegramId, firstName, lastName, phone)));
    }

    @GetMapping()
    @Operation(summary = "Получение списка всех пользователей")
    public ResponseEntity<Object> getAll() {
            return ResponseEntity.ok(userService.getAll());
    }

    @GetMapping("id")
    @Operation(summary = "Получение пользователя по id")
    public ResponseEntity<Object> getById(@RequestParam @Parameter(description = "Id пользователя") Long userId) {
            User user = userService.getById(userId);
            return ResponseEntity.ok().body(user);
    }

    @PutMapping
    @Operation(summary = "Изменить пользователя")
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id пользователя") Long telegramId,
                                         @RequestParam @Parameter(description = "Имя") String firstName,
                                         @RequestParam @Parameter(description = "Фамилия") String lastName,
                                         @RequestParam @Parameter(description = "Телефон") String phone) {
            return ResponseEntity.ok(userService.create(new User(telegramId, firstName, lastName, phone)));
    }

    @DeleteMapping("id")
    @Operation(summary = "Удаление пользователя по id")
    public ResponseEntity<String> deleteById(@RequestParam @Parameter(description = "Id пользователя") Long userId) {
            userService.deleteById(userId);
            return ResponseEntity.ok().body("Пользователь успешно удалён");
    }
}
