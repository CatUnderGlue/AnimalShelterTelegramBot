package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.DogNotFoundException;
import ru.codehunters.zaepestelegrambot.exception.ShelterException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.repository.DogShelterRepo;
import ru.codehunters.zaepestelegrambot.service.ShelterService;
import ru.codehunters.zaepestelegrambot.service.ShelterValidationService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DogShelterServiceImpl implements ShelterService<DogShelter, Dog> {

    private final DogShelterRepo dogRepo;
    private final ShelterValidationService valService;


    @Override
    public DogShelter addShelter(DogShelter shelter) {
        if (valService.isCompleteRecord(shelter)) {
            throw new ShelterException("Данные приюта не заполнены");
        }
        return dogRepo.save(shelter);
    }

    @Override
    public DogShelter updateShelter(DogShelter shelter, long id) {
        if (!dogRepo.existsById(id)) {
            throw new DogNotFoundException("Собачий приют не найден");
        }
        dogRepo.getReferenceById(id);
        if (valService.isCompleteRecord(shelter)) {
            throw new ShelterException("Данные приюта не заполнены");
        }
        return dogRepo.save(shelter);
    }


    @Override
    public List<DogShelter> getShelter() {
        return dogRepo.findAll();
    }

    @Override
    public List<Dog> getAnimal(long index) {
        return dogRepo.getReferenceById(index).getList();
    }


    @Override
    public String delShelter(long index) {
        String result;
        Optional<DogShelter> dogShelter = dogRepo.findById(index);
        if (dogShelter.isPresent()) {
            dogRepo.deleteById(index);
            result = "Запись удалена";
        } else {
            throw new DogNotFoundException("Собачки без приюта. Мы его не нашли(");
        }
        return result;
    }
}
