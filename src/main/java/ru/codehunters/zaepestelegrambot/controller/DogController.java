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
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.service.DogService;

@RestController
@RequestMapping("/dogs/")
@Tag(name = "Собаки", description = "CRUD-методы для работы с собаками")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class DogController {

    private final DogService dogService;

    @GetMapping("/id")
    @Operation(summary = "Получение собаки по ID")
    public ResponseEntity<Object> getById(@RequestParam @Parameter(description = "ID собаки") Long id) {
        try {
            Dog dog = dogService.getById(id);
            return ResponseEntity.ok().body(dog);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PostMapping
    @Operation(summary = "Добавить собаку в приют")
    public ResponseEntity<Object> create(
            @RequestParam @Parameter(description = "Имя собаки") String name,
            @RequestParam @Parameter(description = "Возраст") int age,
            @RequestParam @Parameter(description = "Здоров?") boolean isHealthy,
            @RequestParam @Parameter(description = "Привит?") boolean vaccinated,
            @RequestParam @Parameter(description = "ID приюта") Long shelterId) {
        try {
            return ResponseEntity.ok(dogService.create(new Dog(name, age, isHealthy, vaccinated, shelterId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(summary = "Получение всех собак")
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(dogService.getAll());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("/ownerID")
    @Operation(summary = "Получение собаки по ID хозяина")
    public ResponseEntity<Object> getOwnerById(@RequestParam @Parameter(description = "ID хозяина собаки") Long id) {
        try {
            return ResponseEntity.ok(dogService.getByUserId(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }


    @PutMapping
    @Operation(summary = "Изменить информацию о собаке")
    public ResponseEntity<Object> update(
            @RequestParam @Parameter(description = "ID кота") Long id,
            @RequestParam(required = false) @Parameter(description = "Имя кота") String name,
            @RequestParam(required = false) @Parameter(description = "Возраст кота") Integer age,
            @RequestParam(required = false) @Parameter(description = "Здоров?") Boolean isHealthy,
            @RequestParam(required = false) @Parameter(description = "Привит?") Boolean vaccinated,
            @RequestParam(required = false) @Parameter(description = "ID кошачьего приюта") Long shelterId,
            @RequestParam(required = false) @Parameter(description = "ID хозяина") Long ownerId) {
        try {
            return ResponseEntity.ok(dogService.update(new Dog(id, name, age, isHealthy, vaccinated, shelterId, ownerId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("/id")
    @Operation(summary = "Удаление собаки")
    public ResponseEntity<Object> deleteById(@RequestParam Long id) {
        try {
            dogService.remove(id);
            return ResponseEntity.ok().body("Собаку выбросили на улицу");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
