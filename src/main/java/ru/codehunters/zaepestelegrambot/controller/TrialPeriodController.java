package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("trialperiods")
@Tag(name = "Испытательный срок", description = "CRUD-методы для работы с испытательными сроками")
public class TrialPeriodController {

    private final TrialPeriodService trialPeriodService;

    public TrialPeriodController(TrialPeriodService trialPeriodService) {
        this.trialPeriodService = trialPeriodService;
    }

    @PostMapping
    @Operation(
            summary = "Создать испытательный срок"
    )
    @ApiResponse(responseCode = "200", description = "Испытательный срок создан")
    @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат")
    @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")
    public ResponseEntity<Long> create(@RequestParam @Parameter(description = "Дата начала испытательного срока")LocalDate startDate,
                                       @RequestParam @Parameter(description = "Дата окончания испытательного срока")LocalDate endDate,
                                       @RequestParam @Parameter(description = "Состояние")TrialPeriod.Result result,
                                       @RequestParam @Parameter(description = "Id хозяина животного")Long ownerId){
        try{
            return ResponseEntity.ok(trialPeriodService.create(new TrialPeriod(startDate, endDate, startDate.plusDays(1), new ArrayList<>(), result, ownerId)));
        }catch (IllegalArgumentException e){
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(
            summary = "Получение всех испытательных сроков"
    )
    public ResponseEntity<List<TrialPeriod>> getAll(){
        return ResponseEntity.ok(trialPeriodService.getAll());
    }

    @GetMapping("owner")
    @Operation(
            summary = "Получение всех испытательных сроков по id хозяина"
    )
    @Parameter(
            name = "ownerId",
            description = "Id хозяина животного",
            example = "1"
    )
    public ResponseEntity<List<TrialPeriod>> getAllByOwnerId(@RequestParam Long ownerId){
        return ResponseEntity.ok(trialPeriodService.getAllByOwnerId(ownerId));
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
    public ResponseEntity<TrialPeriod> getById(@RequestParam Long id){
        return ResponseEntity.ok(trialPeriodService.getById(id));
    }

    @DeleteMapping()
    @Operation(
            summary = "Удаление испытательного срока"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Испытательный срок в формате json",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = TrialPeriod.class)
                    )
            }
    )
    public ResponseEntity<String> delete(@RequestBody TrialPeriod trialPeriod) {
        trialPeriodService.delete(trialPeriod);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление испытательного срока по id"
    )
    @Parameter(
            name = "id",
            description = "Id испытательного срока",
            example = "1"
    )
    public ResponseEntity<String> deleteById(@RequestParam Long id) {
        trialPeriodService.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
