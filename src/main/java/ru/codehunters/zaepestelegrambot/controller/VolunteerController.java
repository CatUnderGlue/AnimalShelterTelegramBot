package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.List;

@RestController
@RequestMapping("volunteers")
@Tag(name = "Волонтёр", description = "CRUD-методы для работы с волонтёрами")
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    @Operation(
            summary = "Создать волонтёра"
    )
    @ApiResponse(responseCode = "200", description = "Волонтёр срок создан")
    @ApiResponse(responseCode = "400", description = "Параметры запроса отсутствуют или имеют некорректный формат")
    @ApiResponse(responseCode = "500", description = "Произошла ошибка, не зависящая от вызывающей стороны")
    public ResponseEntity<Volunteer> create(@RequestParam @Parameter(description = "Телеграм id волонтёра") Long telegramId,
                                            @RequestParam @Parameter(description = "Имя") String firstName,
                                            @RequestParam @Parameter(description = "Фамилия") String lastName) {
        try {
            return ResponseEntity.ok(volunteerService.create(new Volunteer(telegramId, firstName, lastName)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping()
    @Operation(
            summary = "Получение всех волонтёров"
    )
    public ResponseEntity<List<Volunteer>> getAll() {
        return ResponseEntity.ok(volunteerService.getAll());
    }

    @DeleteMapping()
    @Operation(
            summary = "Удаление волонтёра"
    )
    @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "Волонтёр в формате json",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Volunteer.class)
                    )
            }
    )
    public ResponseEntity<String> delete(@RequestBody Volunteer volunteer) {
        volunteerService.delete(volunteer);
        return ResponseEntity.ok().build();
    }
}
