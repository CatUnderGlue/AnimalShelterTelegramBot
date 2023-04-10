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
import ru.codehunters.zaepestelegrambot.exception.CatOwnerNotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.owners.CatOwner;
import ru.codehunters.zaepestelegrambot.service.CatOwnerService;

@RestController
@RequestMapping("catOwners")
@Tag(name = "Владелец кота", description = "CRUD-методы для работы с владельцами котов")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class CatOwnerController {
    private final CatOwnerService catOwnerService;

    public CatOwnerController(CatOwnerService catOwnerService) {
        this.catOwnerService = catOwnerService;
    }

    @PostMapping
    @Operation(
            summary = "Создать владельца кота"
    )
    public ResponseEntity<CatOwner> create(@RequestParam @Parameter(description = "Телеграм id владельца кота") Long telegramId,
                                           @RequestParam @Parameter(description = "Имя") String firstName,
                                           @RequestParam @Parameter(description = "Фамилия") String lastName,
                                           @RequestParam @Parameter(description = "Телефон") String phone) {
        try {
            return ResponseEntity.ok(catOwnerService.create(new CatOwner(telegramId, firstName, lastName, phone,
                    null, null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/user")
    @Operation(
            summary = "Создать хозяина кота в бд из пользователя"
    )
    public ResponseEntity<CatOwner> create(@RequestParam @Parameter(description = "Пользователь") User user) {
        try {
            return ResponseEntity.ok(catOwnerService.create(user));
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
            return ResponseEntity.ok(catOwnerService.getAll());
        } catch (CatOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение владельца кота по id"
    )
    @Parameter(
            name = "id",
            description = "Id владельца кота",
            example = "1"
    )
    public ResponseEntity<Object> getById(@RequestParam Long catOwnerId) {
        try {
            CatOwner catOwner = catOwnerService.getById(catOwnerId);
            return ResponseEntity.ok().body(catOwner);
        } catch (CatOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(
            summary = "Изменить владельца кота"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id владельца кота") Long telegramId,
                                         @RequestParam @Parameter(description = "Имя") String firstName,
                                         @RequestParam @Parameter(description = "Фамилия") String lastName,
                                         @RequestParam @Parameter(description = "Телефон") String phone) {
        try {
            return ResponseEntity.ok(catOwnerService.create(new CatOwner(telegramId, firstName, lastName, phone,
                    null, null)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (CatOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    @Operation(
            summary = "Удаление владельца кота"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Владелец кота в формате json",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CatOwner.class)
                    )
            }
    )
    public ResponseEntity<String> delete(@RequestBody CatOwner catOwner) {
        try {
            catOwnerService.delete(catOwner);
            return ResponseEntity.ok().body("Владелец кота успешно удалён");
        } catch (CatOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление владельца кота по id"
    )
    @Parameter(
            name = "id",
            description = "Id владельца кота",
            example = "1"
    )
    public ResponseEntity<String> deleteById(@RequestParam Long catOwnerId) {
        try {
            catOwnerService.deleteById(catOwnerId);
            return ResponseEntity.ok().body("Владелец кота успешно удалён");
        } catch (CatOwnerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
