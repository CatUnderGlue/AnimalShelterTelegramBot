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
    @Operation(summary = "Создать волонтёра")
    public ResponseEntity<Object> create(@RequestParam @Parameter(description = "Телеграм id волонтёра") Long telegramId,
                                         @RequestParam @Parameter(description = "Имя") String firstName,
                                         @RequestParam @Parameter(description = "Фамилия") String lastName) {
        try {
            return ResponseEntity.ok(volunteerService.create(new Volunteer(telegramId, firstName, lastName)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping()
    @Operation(summary = "Получение всех волонтёров")
    public ResponseEntity<Object> getAll() {
        try {
            return ResponseEntity.ok(volunteerService.getAll());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @GetMapping("id")
    @Operation(summary = "Получение волонтёра по id")
    public ResponseEntity<Object> getById(@RequestParam @Parameter(description = "Id волонтёра") Long volunteerId) {
        try {
            Volunteer volunteer = volunteerService.getById(volunteerId);
            return ResponseEntity.ok().body(volunteer);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @PutMapping
    @Operation(summary = "Изменить волонтёра")
    public ResponseEntity<Object> update(@RequestParam @Parameter(description = "Телеграм id волонтёра") Long telegramId,
                                         @RequestParam(required = false) @Parameter(description = "Имя") String firstName,
                                         @RequestParam(required = false) @Parameter(description = "Фамилия") String lastName) {
        try {
            return ResponseEntity.ok(volunteerService.update(new Volunteer(telegramId, firstName, lastName)));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }

    @DeleteMapping("id")
    @Operation(summary = "Удаление волонтёра по id")
    public ResponseEntity<String> deleteById(@RequestParam @Parameter(description = "Id волонтёра") Long volunteerId) {
        try {
            volunteerService.deleteById(volunteerId);
            return ResponseEntity.ok().body("Волонтёр успешно удалён");
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }
    }
}
