package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.BadRequestException;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.repository.DogShelterRepo;
import ru.codehunters.zaepestelegrambot.service.ShelterService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DogShelterServiceImpl implements ShelterService<DogShelter, Dog> {

    private final DogShelterRepo dogRepo;


    @Override
    public DogShelter addShelter(DogShelter shelter) {
        if (EntityUtils.checkForObjectNullEmptyOrWhitespace(shelter)) {
            throw new BadRequestException("Данные приюта не заполнены");
        }
        return dogRepo.save(shelter);
    }

    @Override
    public DogShelter updateShelter(DogShelter shelter) {
        Optional<DogShelter> dogShelterId = dogRepo.findById(shelter.getId());
        if(dogShelterId.isEmpty()){
            throw new NotFoundException("Приют не найден. Собачки остались без дома");
        }
        EntityUtils.copyNonNullFields(dogShelterId.get(), shelter);
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
            throw new NotFoundException("Собачки без приюта. Мы его не нашли(");
        }
        return result;
    }
}
