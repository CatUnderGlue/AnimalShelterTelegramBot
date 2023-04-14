package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.repository.CatShelterRepo;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CatShelterServiceImplTest {

    @Mock
    private CatShelterRepo catShelterRepo;
    @InjectMocks
    private CatShelterServiceImpl catShelterService;

    final CatShelter catShelter = new CatShelter();

    List<CatShelter> actualList = new ArrayList<>();


    @Test
    @DisplayName("Тест на сохранение в БД")
    void addShelter() {
        when(catShelterRepo.save(catShelter)).thenReturn(catShelter);
        CatShelter result = catShelterService.addShelter(catShelter);

        assertNotNull(result);
        assertEquals(catShelter, result);

    }

    @Test
    void updateShelter() {
        when(catShelterRepo.save(catShelter)).thenReturn(catShelter);
        CatShelter result = catShelterService.addShelter(catShelter);

        assertNotNull(result);
        assertEquals(catShelter, result);
    }

    @Test
    void testGetSheltersId_WhenShelterExists() {
        long shelterId = 1L;
        CatShelter shelter = new CatShelter();
        shelter.setId(shelterId);
        when(catShelterRepo.findById(shelterId)).thenReturn(Optional.of(shelter));
        CatShelter actualShelter = catShelterService.getSheltersId(shelterId);
        assertEquals(shelter, actualShelter);
    }
    @Test
    void testGetSheltersId_WhenShelterDoesNotExist() {
        long shelterId = 1L;
        when(catShelterRepo.findById(shelterId)).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> catShelterService.getSheltersId(shelterId));
        assertEquals("Приют не найден. Кошки остались без дома", exception.getMessage());
    }

    @Test
    void getShelterByName() {
        Optional<CatShelter> expectedShelter = Optional.of(new CatShelter());
        expectedShelter.get().setName("name");
        when(catShelterRepo.findByName("name")).thenReturn(expectedShelter);
        Optional<CatShelter> actualShelter = Optional.of(catShelterService.getShelterByName("name"));
        assertEquals(expectedShelter, actualShelter);
    }

    @Test
    void getShelter() {
        List<CatShelter> expectedList = new ArrayList<>();
        expectedList.add(new CatShelter());
        expectedList.add(new CatShelter());
        expectedList.add(new CatShelter());
        when(catShelterRepo.findAll()).thenReturn(expectedList);
        actualList = catShelterService.getShelter();
        assertEquals(expectedList, actualList);
    }

    @Test
    void getAnimal() {

    }

    @Test
    void delShelter() {
    }
}