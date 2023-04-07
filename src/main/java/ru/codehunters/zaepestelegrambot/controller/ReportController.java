package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.service.ReportService;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("reports")
@Tag(name = "Отчёт", description = "CRUD-методы для работы с отчётами")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    @Operation(
            summary = "Создать отчёт"
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
    public ResponseEntity<Long> create(@RequestBody Report report) {
        return ResponseEntity.ok(reportService.create(report));
    }

    @GetMapping()
    @Operation(
            summary = "Получение всех отчётов"
    )
    public ResponseEntity<List<Report>> getAll() {
        return ResponseEntity.ok(reportService.getAll());
    }

    @GetMapping("trialId")
    @Operation(
            summary = "Получение всех отчётов по id испытательного срока"
    )
    public ResponseEntity<List<Report>> getAllByTrialPeriodId(@RequestParam Long id) {
        return ResponseEntity.ok(reportService.gelAllByTrialPeriodId(id));
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
    public ResponseEntity<Report> getByDateAndTrialId(@RequestParam LocalDate date, @RequestParam Long id) {
        return ResponseEntity.ok(reportService.getByDateAndTrialId(date, id));
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
    public ResponseEntity<Report> getById(@RequestParam Long reportId) {
        return ResponseEntity.ok(reportService.getById(reportId));
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
        reportService.delete(report);
        return ResponseEntity.ok().build();
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
        reportService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
