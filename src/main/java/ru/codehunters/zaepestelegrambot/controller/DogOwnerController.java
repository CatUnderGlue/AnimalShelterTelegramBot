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
import ru.codehunters.zaepestelegrambot.exception.DogOwnerNotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.owners.DogOwner;
import ru.codehunters.zaepestelegrambot.service.DogOwnerService;

@RestController
@RequestMapping("dogOwners")
@Tag(name = "Владелец собаки", description = "CRUD-методы для работы с владельцами котов")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class DogOwnerController {
    private final DogOwnerService dogOwnerService;

    public DogOwnerController(DogOwnerService dogOwnerService) {
        this.dogOwnerService = dogOwnerService;
    }

    @PostMapping
    @Operation(
            summary = "Создать владельца собаки"
    )
    public ResponseEntity<DogOwner> create(@RequestParam @Parameter(description = "Телеграм id владельца собаки") Long telegramId,
                                           @RequestParam @Parameter(description = "Имя") String firstName,
                                           @RequestParam @Parameter(description = "Фамилия") String lastName,
                                           @RequestParam @Parameter(description = "Телефон") String phone) {
        try {
            return ResponseEntity.ok(dogOwnerService.create(new DogOwner(telegramId, firstName, lastName, phone,
                    null, null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/user")
    @Operation(
            summary = "Создать хозяина собаки в бд из пользователя"
    )
    public ResponseEntity<DogOwner> create(@RequestParam @Parameter(description = "Пользователь") User user) {
        try {
            return ResponseEntity.ok(dogOwnerService.create(user));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(
            summary = "Получение списка всех владельцев котов"
    )
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(dogOwnerService.getAll());
        } catch (DogOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение владельца собаки по id"
    )
    @Parameter(
            name = "id",
            description = "Id владельца собаки",
            example = "1"
    )
    public ResponseEntity<Object> getById(@RequestParam Long dogOwnerId) {
        try {
            DogOwner dogOwner = dogOwnerService.getById(dogOwnerId);
            return ResponseEntity.ok().body(dogOwner);
        } catch (DogOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(
            summary = "Изменить владельца собаки"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id владельца собаки") Long telegramId,
                                         @RequestParam @Parameter(description = "Имя") String firstName,
                                         @RequestParam @Parameter(description = "Фамилия") String lastName,
                                         @RequestParam @Parameter(description = "Телефон") String phone) {
        try {
            return ResponseEntity.ok(dogOwnerService.create(new DogOwner(telegramId, firstName, lastName, phone,
                    null, null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (DogOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    @Operation(
            summary = "Удаление владельца собаки"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Владелец собаки в формате json",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = DogOwner.class)
                    )
            }
    )
    public ResponseEntity<String> delete(@RequestBody DogOwner dogOwner) {
        try {
            dogOwnerService.delete(dogOwner);
            return ResponseEntity.ok().body("Владелец собаки успешно удалён");
        } catch (DogOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление владельца собаки по id"
    )
    @Parameter(
            name = "id",
            description = "Id владельца собаки",
            example = "1"
    )
    public ResponseEntity<String> deleteById(@RequestParam Long dogOwnerId) {
        try {
            dogOwnerService.deleteById(dogOwnerId);
            return ResponseEntity.ok().body("Владелец собаки успешно удалён");
        } catch (DogOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
