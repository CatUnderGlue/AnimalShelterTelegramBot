package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.Parameters;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.model.Report;
import ru.codehunters.zaepestelegrambot.service.ReportService;

import java.time.LocalDate;
import java.util.List;

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
    public ResponseEntity<Report> create(@RequestParam @Parameter(description = "Id фотографии") String photoId,
                                         @RequestParam @Parameter(description = "Рацион животного") String foodRation,
                                         @RequestParam @Parameter(description = "Общее самочувствие и привыкание к новому месту") String generalHealth,
                                         @RequestParam @Parameter(description = "Изменение в поведении") String behaviorChanges,
                                         @RequestParam @Parameter(description = "Id испытательного срока") Long trialPeriodId) {
        return ResponseEntity.ok(reportService.create(new Report(photoId, foodRation, generalHealth, behaviorChanges, LocalDate.now(), trialPeriodId)));
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
    public ResponseEntity<List<Report>> getAllByTrialPeriodId(@RequestParam @Parameter(description = "id испытательного срока") Long id) {
        return ResponseEntity.ok(reportService.gelAllByTrialPeriodId(id));
    }

    @GetMapping("dateandtrial")
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
    public ResponseEntity<Report> getByDateAndTrialId(@RequestParam @Parameter(description = "Дата получения отчёта") LocalDate date,
                                                      @RequestParam @Parameter(description = "id испытательного срока") Long id) {
        return ResponseEntity.ok(reportService.getByDateAndTrialId(date, id));
    }

    @GetMapping("id")
    @Operation(summary = "Получение отчёта по id")
    public ResponseEntity<Report> getById(@RequestParam @Parameter(description = "Id испытательного срока") Long reportId) {
        Report report = reportService.getById(reportId);
        return ResponseEntity.ok().body(report);
    }

    @PutMapping
    @Operation(
            summary = "Изменить отчёт"
    )
    public ResponseEntity<Report> update(@RequestParam @Parameter(description = "Id отчёта") Long id,
                                         @RequestParam(required = false) @Parameter(description = "Id фотографии") String photoId,
                                         @RequestParam(required = false) @Parameter(description = "Рацион животного") String foodRation,
                                         @RequestParam(required = false) @Parameter(description = "Общее самочувствие и привыкание к новому месту") String generalHealth,
                                         @RequestParam(required = false) @Parameter(description = "Изменение в поведении") String behaviorChanges,
                                         @RequestParam(required = false) @Parameter(description = "Дата получения") LocalDate receiveDate,
                                         @RequestParam(required = false) @Parameter(description = "Id испытательного срока") Long trialPeriodId) {
        return ResponseEntity.ok(reportService.update(new Report(id, photoId, foodRation, generalHealth, behaviorChanges, receiveDate, trialPeriodId)));
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
        return ResponseEntity.ok().body("Report deleted successfully");
    }
}
