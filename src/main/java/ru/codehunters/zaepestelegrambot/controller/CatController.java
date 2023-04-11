package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.service.CatService;

@RestController
@RequestMapping("cats")
@Tag(name = "Кошки", description = "CRUD-методы для работы с кошками")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class CatController {

    private final CatService catService;

    @GetMapping("/id")
    @Operation(summary = "Получение кота по ID")
    public ResponseEntity<Object> getById(@RequestParam @Parameter(description = "ID кота") Long id) {
        try {
            Cat cat = catService.getById(id);
            return ResponseEntity.ok().body(cat);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Добавить кота в приют")
    public ResponseEntity<Object> create(
            @RequestParam @Parameter(description = "Имя кота") String name,
            @RequestParam @Parameter(description = "Возраст") int age,
            @RequestParam @Parameter(description = "Здоров?") boolean isHealthy,
            @RequestParam @Parameter(description = "Привит?") boolean vaccinated,
            @RequestParam @Parameter(description = "ID приюта") Long shelterId) {
        try {
            return ResponseEntity.ok(catService.create(new Cat(name, age, isHealthy, vaccinated, shelterId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(summary = "Получение всех котов")
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(catService.getAll());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ownerID")
    @Operation(summary = "Получение информации о хозяине кота по ID")
    public ResponseEntity<Object> getOwnerById(@RequestParam @Parameter(description = "ID хозяина") Long id) {
        try {
            return ResponseEntity.ok(catService.getByUserId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping
    @Operation(summary = "Изменить информацию о коте")
    public ResponseEntity<Object> update(
            @RequestParam @Parameter(description = "ID кота") Long id,
            @RequestParam @Parameter(description = "Имя кота") String name,
            @RequestParam(required = false) @Parameter(description = "Возраст кота") int age,
            @RequestParam(required = false) @Parameter(description = "Здоров?") boolean isHealthy,
            @RequestParam(required = false) @Parameter(description = "Привит?") boolean vaccinated,
            @RequestParam(required = false) @Parameter(description = "ID кошачьего приюта") Long shelterId,
            @RequestParam(required = false) @Parameter(description = "ID хозяина") Long owner) {
        try {
            return ResponseEntity.ok(catService.update(new Cat(id, name, age, isHealthy, vaccinated, shelterId, owner)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/id")
    @Operation(summary = "Удаление кота")
    public ResponseEntity<Object> deleteById(@RequestParam Long id) {
        try {
            catService.remove(id);
            return ResponseEntity.ok().body("Кота выбросили на улицу");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

}
