package ru.codehunters.zaepestelegrambot.service.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.repository.DogShelterRepo;

import java.util.*;


import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class DogShelterServiceImplTest {

    @Mock
    private DogShelterRepo dogShelterRepo;
    @InjectMocks
    private DogShelterServiceImpl shelterService;

    private final DogShelter currentDogShelter = new DogShelter
            (1L,"name","loc","tt","am","sec","sa");

    @Test
    @DisplayName("Тест на сохранение в БД")
    void addShelter() {
        when(dogShelterRepo.save(currentDogShelter)).thenReturn(currentDogShelter);
        DogShelter result = shelterService.addShelter(currentDogShelter);

        assertNotNull(result);
        assertEquals(currentDogShelter, result);
    }
    @Test
    @DisplayName("Тест метода update и поиск приюта по id," +
            "взяли приют по id, обновили объект и обновили в базе")
    public void testUpdateShelter_Success() {
        when(dogShelterRepo.findById(currentDogShelter.getId()))
                .thenReturn(Optional.of(currentDogShelter));
        DogShelter actualShelter = shelterService.getSheltersId(currentDogShelter.getId());
        actualShelter.setName("nameTest");
        when(dogShelterRepo.save(actualShelter))
                .thenReturn(currentDogShelter);
        DogShelter updatedShelter = shelterService.updateShelter(currentDogShelter);

        verify(dogShelterRepo).save(currentDogShelter);
        assertEquals(updatedShelter, actualShelter);
    }

    @Test
    @DisplayName("Тест метода поиск приюта по id")
    void testGetSheltersId_WhenShelterExists() {
        when(dogShelterRepo.findById(currentDogShelter.getId())).thenReturn(Optional.of(currentDogShelter));
        DogShelter actualShelter = shelterService.getSheltersId(currentDogShelter.getId());
        assertEquals(currentDogShelter, actualShelter);
    }
    @Test
    @DisplayName("Тест метода поиск приюта по id c появлением исключения")
    void testGetSheltersId_WhenShelterDoesNotExist() {
        when(dogShelterRepo.findById(currentDogShelter.getId())).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () -> shelterService
                .getSheltersId(currentDogShelter.getId()));
        assertEquals("Приют не найден. Собачки остались без дома", exception.getMessage());
    }

    @Test
    @DisplayName("Тест метода поиск приюта по имени")
    void getShelterByName() {
        Optional<DogShelter> expectedShelter = Optional.of(new DogShelter());
        expectedShelter.get().setName("name");
        when(dogShelterRepo.findByName("name")).thenReturn(expectedShelter);
        Optional<DogShelter> actualShelter = Optional.of(shelterService.getShelterByName("name"));
        assertEquals(expectedShelter, actualShelter);
    }
    @Test
    @DisplayName("Тест метода поиск приюта по имени c исключением")
    void getShelterByNameException() {
        when(dogShelterRepo.findByName("name")).thenReturn(Optional.empty());
        NotFoundException exception = assertThrows(NotFoundException.class, () ->
                shelterService.getShelterByName("name"));
        assertEquals("Приют не найден. Собачки остались без дома", exception.getMessage());

    }

    @Test
    @DisplayName("Тест метода выдача всех приютов ")
    void getShelter() {
        List<DogShelter> actualList;
        List<DogShelter> expectedList = new ArrayList<>();
        expectedList.add(new DogShelter());
        expectedList.add(new DogShelter());
        expectedList.add(new DogShelter());
        when(dogShelterRepo.findAll()).thenReturn(expectedList);
        actualList = shelterService.getShelter();
        assertEquals(expectedList, actualList);
    }

    @Test
    @DisplayName("Тест метода выдача всех животных приюта ")
    void getAnimal() {
        Dog dog1 = new Dog("1",1,true,true,1L);
        Dog dog2 = new Dog("2",2,true,true,2L);
        List<Dog> testDogList = Arrays.asList(dog1, dog2);
        currentDogShelter.setList(testDogList);
        when(dogShelterRepo.findById(currentDogShelter.getId())).thenReturn(Optional.of(currentDogShelter));
        List<Dog> returnedDogList = shelterService.getAnimal(currentDogShelter.getId());
        assertNotNull(returnedDogList);
        assertThat(returnedDogList).isEqualTo(testDogList);
    }

    @Test
    @DisplayName("Тест метода удаление по id ")
    public void testDelShelter_Success() {
        when(dogShelterRepo.findById(currentDogShelter.getId())).thenReturn(Optional.of(currentDogShelter));
        doNothing().when(dogShelterRepo).deleteById(currentDogShelter.getId());
        String result = shelterService.delShelter(currentDogShelter.getId());

        verify(dogShelterRepo).deleteById(currentDogShelter.getId());

        assertThat(result).isEqualTo("Запись удалена");
    }
    @Test
    @DisplayName("Тест метода удаление по id с исключением")
    public void testDelShelter_Failure() {
        long invalidId = 999L;
        when(dogShelterRepo.findById(invalidId)).thenReturn(Optional.empty());
        assertThatThrownBy(() -> shelterService
                .delShelter(invalidId))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Собачки без приюта. Мы его не нашли(");
        verify(dogShelterRepo, never()).deleteById(invalidId);
    }
}