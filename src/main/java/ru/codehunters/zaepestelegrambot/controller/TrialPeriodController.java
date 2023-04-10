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
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.time.LocalDate;
import java.util.ArrayList;

@RestController
@RequestMapping("trialperiods")
@Tag(name = "Испытательный срок", description = "CRUD-методы для работы с испытательными сроками")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class TrialPeriodController {

    private final TrialPeriodService trialPeriodService;

    public TrialPeriodController(TrialPeriodService trialPeriodService) {
        this.trialPeriodService = trialPeriodService;
    }

    @PostMapping
    @Operation(
            summary = "Создать испытательный срок"
    )
    public ResponseEntity<Long> create(@RequestParam @Parameter(description = "Дата начала испытательного срока") LocalDate startDate,
                                       @RequestParam @Parameter(description = "Дата окончания испытательного срока") LocalDate endDate,
                                       @RequestParam @Parameter(description = "Состояние") TrialPeriod.Result result,
                                       @RequestParam @Parameter(description = "Id хозяина животного") Long ownerId) {
        try {
            return ResponseEntity.ok(trialPeriodService.create(new TrialPeriod(startDate, endDate, startDate.plusDays(1), new ArrayList<>(), result, ownerId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(
            summary = "Получение всех испытательных сроков"
    )
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(trialPeriodService.getAll());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("owner")
    @Operation(summary = "Получение всех испытательных сроков по id хозяина")
    public ResponseEntity<Object> getAllByOwnerId(@RequestParam @Parameter(description = "Id хозяина животного") Long ownerId) {
        try {
            return ResponseEntity.ok(trialPeriodService.getAllByOwnerId(ownerId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение испытательного срока по id"
    )
    @Parameter(
            name = "id",
            description = "Id ипытательного срока",
            example = "1"
    )
    public ResponseEntity<Object> getById(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(trialPeriodService.getById(id));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(
            summary = "Изменить испытательный срок"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Id испытательного срока") Long id,
                                              @RequestParam @Parameter(description = "Дата начала испытательного срока") LocalDate startDate,
                                              @RequestParam @Parameter(description = "Дата окончания испытательного срока") LocalDate endDate,
                                              @RequestParam @Parameter(description = "Состояние") TrialPeriod.Result result,
                                              @RequestParam @Parameter(description = "Id хозяина животного") Long ownerId) {
        try {
            return ResponseEntity.ok(trialPeriodService.update(new TrialPeriod(id, startDate, endDate, startDate.plusDays(1), new ArrayList<>(), result, ownerId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(summary = "Удаление испытательного срока по id")
    public ResponseEntity<String> deleteById(@RequestParam @Parameter(description = "Id испытательного срока") Long id) {
        try {
            trialPeriodService.deleteById(id);
            return ResponseEntity.ok().body("Испытательный срок успешно удалён");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
