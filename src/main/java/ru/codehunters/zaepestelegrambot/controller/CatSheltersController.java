package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;

import java.util.List;

@RestController
@RequestMapping("cats/shelters")
@Tag(name = "Кошачий приют", description = "CRUD-методы для работы с приютом")
@RequiredArgsConstructor
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, кошки довольны."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class CatSheltersController {

    private final CatShelterServiceImpl catShelterServiceImpl;

    @PostMapping("/")
    @Operation(
            summary = "Регистрация нового кошачьего приюта."
    )
    public ResponseEntity<Object> add(@RequestBody CatShelter catShelter) {
        return ResponseEntity.ok().body(catShelterServiceImpl.addShelter(catShelter));
    }

    @PutMapping("/")
    @Operation(
            summary = "Обновление информации о приюте"
    )
    public ResponseEntity<Object> update(
            @RequestBody @Parameter(description = "Объект приюта") CatShelter catShelter,
            @RequestParam @Parameter(description = "id приюта") long id) {

        return ResponseEntity.ok().body(catShelterServiceImpl.updateShelter(catShelter, id));
    }

    @GetMapping("/")
    @Operation(
            summary = "Список приютов"
    )
    public ResponseEntity<List<CatShelter>> getAll() {
        return ResponseEntity.ok(catShelterServiceImpl.getShelter());
    }

    @GetMapping("/list{id}")
    @Operation(
            summary = "Список животных приюта"
    )
    public ResponseEntity<List<Cat>> getAnimal(@PathVariable @Parameter(description = "id приюта") long id) {
        return ResponseEntity.ok(catShelterServiceImpl.getAnimal(id));
    }

    @DeleteMapping("/{id}")
    @Operation(
            summary = "Удаление приюта"
    )

    public ResponseEntity<String> delete(@PathVariable @Parameter(description = "id приюта") long id) {
        return ResponseEntity.ok(catShelterServiceImpl.delShelter(id));
    }

}