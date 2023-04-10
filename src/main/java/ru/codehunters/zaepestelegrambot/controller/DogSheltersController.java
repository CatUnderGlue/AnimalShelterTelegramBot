package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.util.List;

@RestController
@RequestMapping("dogs/shelters")
@Tag(name = "Собачий приют", description = "CRUD-методы для работы с приютом")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, собачки довольны."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class DogSheltersController {

    private final DogShelterServiceImpl dogShelterService;

    @PostMapping("/")
    @Operation(
            summary = "Регистрация нового собачьего приюта."
    )
    public ResponseEntity<Object> add(@RequestBody DogShelter dogShelter) {
        return ResponseEntity.ok().body(dogShelterService.addShelter(dogShelter));
    }

    @PutMapping("/")
    @Operation(
            summary = "Обновление информации о приюте"
    )
    public ResponseEntity<Object> update(
            @RequestBody @Parameter(description = "Объект приюта") DogShelter dogShelter,
            @RequestParam @Parameter(description = "id приюта") long id) {

        return ResponseEntity.ok().body(dogShelterService.updateShelter(dogShelter, id));
    }

    @GetMapping("/")
    @Operation(
            summary = "Список приютов"
    )
    public ResponseEntity<List<DogShelter>> getAll() {
        return ResponseEntity.ok(dogShelterService.getShelter());
    }

    @GetMapping("/list{id}")
    @Operation(
            summary = "Список животных приюта"
    )
    public ResponseEntity<List<Dog>> getAnimal(@PathVariable @Parameter(description = "id приюта") long id) {
        return ResponseEntity.ok(dogShelterService.getAnimal(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление приюта"
    )

    public ResponseEntity<String> delete(@PathVariable @Parameter(description = "id приюта") long id) {
        return ResponseEntity.ok(dogShelterService.delShelter(id));
    }

}
