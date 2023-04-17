package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.repository.CatShelterRepo;

import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CatShelterServiceImplTest {

    @Mock
    private CatShelterRepo catShelterRepo;
    @InjectMocks
    private CatShelterServiceImpl catShelterService;

    private final CatShelter currentCatShelter = new CatShelter
            (1L, "name", "loc", "tt", "am", "sec", "sa");

    @Test
    @DisplayName("Тест на сохранение в БД")
    void addShelter() {
        when(catShelterRepo.save(currentCatShelter)).thenReturn(currentCatShelter);
        CatShelter result = catShelterService.addShelter(currentCatShelter);

        assertNotNull(result);
        assertEquals(currentCatShelter, result);
    }

    @Test
    @DisplayName("Тест метода update и поиск приюта по id," +
            "взяли приют по id, обновили объект и обновили в базе")
    public void testUpdateShelter_Success() {
        when(catShelterRepo.findById(currentCatShelter.getId()))
                .thenReturn(Optional.of(currentCatShelter));
        CatShelter actualShelter = catShelterService.getSheltersId(currentCatShelter.getId());
        actualShelter.setName("nameTest");
        when(catShelterRepo.save(actualShelter))
                .thenReturn(currentCatShelter);
        CatShelter updatedShelter = catShelterService.updateShelter(currentCatShelter);

        verify(catShelterRepo).save(currentCatShelter);
        assertEquals(updatedShelter, actualShelter);
    }

    @Test
    @DisplayName("Тест метода поиск приюта по id")
    void testGetSheltersId_WhenShelterExists() {
        when(catShelterRepo.findById(currentCatShelter.getId())).thenReturn(Optional.of(currentCatShelter));
        CatShelter actualShelter = catShelterService.getSheltersId(currentCatShelter.getId());
        assertEquals(currentCatShelter, actualShelter);
    }

    @Test
    @DisplayName("Тест метода поиск приюта по id c появлением исключения")
    void testGetSheltersId_WhenShelterDoesNotExist() {
        when(catShelterRepo.findById(currentCatShelter.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> catShelterService
                .getSheltersId(currentCatShelter.getId()));
        assertEquals("Приют не найден. Кошки остались без дома", exception.getMessage());
    }

    @Test
    @DisplayName("Тест метода поиск приюта по имени")
    void getShelterByName() {
        Optional<CatShelter> expectedShelter = Optional.of(new CatShelter());
        expectedShelter.get().setName("name");
        when(catShelterRepo.findByName("name")).thenReturn(expectedShelter);
        Optional<CatShelter> actualShelter = Optional.of(catShelterService.getShelterByName("name"));
        assertEquals(expectedShelter, actualShelter);
    }

    @Test
    @DisplayName("Тест метода поиск приюта по имени c исключением")
    void getShelterByNameException() {
        when(catShelterRepo.findByName("name")).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                catShelterService.getShelterByName("name"));
        assertEquals("Приют не найден. Кошки остались без дома", exception.getMessage());

    }

    @Test
    @DisplayName("Тест метода выдача всех приютов ")
    void getShelter() {
        List<CatShelter> actualList;
        List<CatShelter> expectedList = new ArrayList<>();
        expectedList.add(new CatShelter());
        expectedList.add(new CatShelter());
        expectedList.add(new CatShelter());
        when(catShelterRepo.findAll()).thenReturn(expectedList);
        actualList = catShelterService.getShelter();
        assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Тест метода выдача всех животных приюта ")
    void getAnimal() {
        Cat cat1 = new Cat("1", 1, true, true, 1L);
        Cat cat2 = new Cat("2", 2, true, true, 2L);
        List<Cat> testCatList = Arrays.asList(cat1, cat2);
        currentCatShelter.setList(testCatList);
        when(catShelterRepo.findById(currentCatShelter.getId())).thenReturn(Optional.of(currentCatShelter));
        List<Cat> returnedCatList = catShelterService.getAnimal(currentCatShelter.getId());
        assertNotNull(returnedCatList);
        assertThat(returnedCatList).isEqualTo(testCatList);
    }

    @Test
    @DisplayName("Тест метода удаление по id ")
    public void testDelShelter_Success() {
        when(catShelterRepo.findById(currentCatShelter.getId())).thenReturn(Optional.of(currentCatShelter));
        doNothing().when(catShelterRepo).deleteById(currentCatShelter.getId());
        String result = catShelterService.delShelter(currentCatShelter.getId());

        verify(catShelterRepo).deleteById(currentCatShelter.getId());

        assertThat(result).isEqualTo("Запись удалена");
    }

    @Test
    @DisplayName("Тест метода удаление по id с исключением")
    public void testDelShelter_Failure() {
        long invalidId = 999L;
        when(catShelterRepo.findById(invalidId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> catShelterService
                .delShelter(invalidId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Котятки остались без приюта. Не нашли приют");
        verify(catShelterRepo, never()).deleteById(invalidId);
    }
}
