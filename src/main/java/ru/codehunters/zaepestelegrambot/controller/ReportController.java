package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.exception.ReportNotFoundException;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.service.ReportService;

import java.time.LocalDate;

@RestController
@RequestMapping("reports")
@Tag(name = "Отчёт", description = "CRUD-методы для работы с отчётами")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @Operation(
            summary = "Создать отчёт"
    )
    public ResponseEntity<Long> create(@RequestParam @Parameter(description = "Id фотографии") String photoId,
                                       @RequestParam @Parameter(description = "Рацион животного") String foodRation,
                                       @RequestParam @Parameter(description = "Общее самочувствие и привыкание к новому месту") String generalHealth,
                                       @RequestParam @Parameter(description = "Изменение в поведении") String behaviorChanges,
                                       @RequestParam @Parameter(description = "Дата получения") LocalDate receiveDate,
                                       @RequestParam @Parameter(description = "Id испытательного срока") Long trialPeriodId) {
        try {
            return ResponseEntity.ok(reportService.create(new Report(photoId, foodRation, generalHealth, behaviorChanges, receiveDate, trialPeriodId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }


    @GetMapping()
    @Operation(
            summary = "Получение всех отчётов"
    )
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(reportService.getAll());
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("trialId")
    @Operation(
            summary = "Получение всех отчётов по id испытательного срока"
    )
    public ResponseEntity<Object> getAllByTrialPeriodId(@RequestParam Long id) {
        try {
            return ResponseEntity.ok(reportService.gelAllByTrialPeriodId(id));
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("date")
    @Operation(
            summary = "Получение отчёта по дате и id испытательного срока"
    )
    @Parameters(value = {
            @Parameter(
                    name = "date",
                    description = "Дата создания отчёта",
                    example = "2023-12-31"
            ),
            @Parameter(
                    name = "id",
                    description = "Id испытательного срока",
                    example = "1"
            )
    }
    )
    public ResponseEntity<Object> getByDateAndTrialId(@RequestParam LocalDate date, @RequestParam Long id) {
        try {
            return ResponseEntity.ok(reportService.getByDateAndTrialId(date, id));
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение отчёта по id"
    )
    @Parameter(
            name = "id",
            description = "Id испытательного срока",
            example = "1"
    )
    public ResponseEntity<Object> getById(@RequestParam Long reportId) {
        try {
            Report report = reportService.getById(reportId);
            return ResponseEntity.ok().body(report);
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(
            summary = "Изменить отчёт"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Id отчёта") Long id,
                                          @RequestParam @Parameter(description = "Id фотографии") String photoId,
                                          @RequestParam @Parameter(description = "Рацион животного") String foodRation,
                                          @RequestParam @Parameter(description = "Общее самочувствие и привыкание к новому месту") String generalHealth,
                                          @RequestParam @Parameter(description = "Изменение в поведении") String behaviorChanges,
                                          @RequestParam @Parameter(description = "Дата получения") LocalDate receiveDate,
                                          @RequestParam @Parameter(description = "Id испытательного срока") Long trialPeriodId) {
        try {
            return ResponseEntity.ok(reportService.update(new Report(id ,photoId, foodRation, generalHealth, behaviorChanges, receiveDate, trialPeriodId)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping()
    @Operation(
            summary = "Удаление отчёта"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Отчёт в формате json",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Report.class)
                    )
            }
    )
    public ResponseEntity<String> delete(@RequestBody Report report) {
        try {
            reportService.delete(report);
            return ResponseEntity.ok().body("Отчёт успешно удалён");
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление отчёта по id"
    )
    @Parameter(
            name = "id",
            description = "Id отчёта",
            example = "1"
    )
    public ResponseEntity<String> deleteById(@RequestParam Long id) {
        try {
            reportService.deleteById(id);
            return ResponseEntity.ok().body("Отчёт успешно удалён");
        } catch (ReportNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
