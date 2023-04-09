package ru.codehunters.zaepestelegrambot.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.codehunters.zaepestelegrambot.exception.VolunteerNotFoundException;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

@RestController
@RequestMapping("volunteers")
@Tag(name = "Волонтёр", description = "CRUD-методы для работы с волонтёрами")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Всё хорошо, запрос выполнился."),
        @ApiResponse(responseCode = "400", description = "Есть ошибка в параметрах запроса."),
        @ApiResponse(responseCode = "404", description = "URL неверный или такого действия нет в веб-приложении."),
        @ApiResponse(responseCode = "500", description = "Во время выполнения запроса произошла ошибка на сервере.")
})
public class VolunteerController {
    private final VolunteerService volunteerService;

    public VolunteerController(VolunteerService volunteerService) {
        this.volunteerService = volunteerService;
    }

    @PostMapping
    @Operation(
            summary = "Создать волонтёра"
    )
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
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(volunteerService.getAll());
        } catch (VolunteerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(
            summary = "Получение волонтёра по id"
    )
    @Parameter(
            name = "id",
            description = "Id волонтёра",
            example = "1"
    )
    public ResponseEntity<Object> getById(@RequestParam Long volunteerId) {
        try {
            Volunteer volunteer = volunteerService.getById(volunteerId);
            return ResponseEntity.ok().body(volunteer);
        } catch (VolunteerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(
            summary = "Изменить волонтёра"
    )
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id волонтёра") Long telegramId,
                                            @RequestParam @Parameter(description = "Имя") String firstName,
                                            @RequestParam @Parameter(description = "Фамилия") String lastName) {
        try {
            return ResponseEntity.ok(volunteerService.create(new Volunteer(telegramId, firstName, lastName)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (VolunteerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
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
        try {
            volunteerService.delete(volunteer);
            return ResponseEntity.ok().body("Волонтёр успешно удалён");
        } catch (VolunteerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(
            summary = "Удаление волонтёра по id"
    )
    @Parameter(
            name = "id",
            description = "Id волонтёра",
            example = "1"
    )
    public ResponseEntity<String> deleteById(@RequestParam Long volunteerId) {
        try {
            volunteerService.deleteById(volunteerId);
            return ResponseEntity.ok().body("Волонтёр успешно удалён");
        } catch (VolunteerNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
