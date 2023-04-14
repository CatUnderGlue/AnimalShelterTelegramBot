package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
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
                                           @RequestParam @Parameter(description = "Телефон") String phone,
                                           @RequestParam @Parameter(description = "Id кота") Long animalId) {
        return ResponseEntity.ok(catOwnerService.create(new CatOwner(telegramId, firstName, lastName, phone,
                null, null), TrialPeriod.AnimalType.CAT, animalId));
    }

    @PostMapping("/user")
    @Operation(
            summary = "Создать хозяина кота в бд из пользователя"
    )
    public ResponseEntity<CatOwner> create(@RequestParam @Parameter(description = "Пользователь") Long id,
                                           @RequestParam @Parameter(description = "Id кота") Long animalId) {
        return ResponseEntity.ok(catOwnerService.create(id, TrialPeriod.AnimalType.CAT, animalId));
    }

    @GetMapping()
    @Operation(
            summary = "Получение списка всех владельцев котов"
    )
    public ResponseEntity<Object> getAll() {
        return ResponseEntity.ok(catOwnerService.getAll());
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение владельца кота по id"
    )

    public ResponseEntity<Object> getById(@RequestParam @Parameter(description = "Id владельца кота") Long catOwnerId) {
        CatOwner catOwner = catOwnerService.getById(catOwnerId);
        return ResponseEntity.ok().body(catOwner);
    }

    @PutMapping
    @Operation(
            summary = "Изменить владельца кота"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id владельца кота") Long telegramId,
                                         @RequestParam @Parameter(description = "Имя") String firstName,
                                         @RequestParam @Parameter(description = "Фамилия") String lastName,
                                         @RequestParam @Parameter(description = "Телефон") String phone) {
        return ResponseEntity.ok(catOwnerService.update(new CatOwner(telegramId, firstName, lastName, phone,
                null, null)));
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление владельца кота по id"
    )

    public ResponseEntity<String> deleteById(@RequestParam @Parameter(description = "Id владельца кота") Long catOwnerId) {
        catOwnerService.deleteById(catOwnerId);
        return ResponseEntity.ok().body("Владелец кота успешно удалён");
    }
}
