package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.ShelterException;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.repository.DogShelterRepo;
import ru.codehunters.zaepestelegrambot.service.ShelterService;
import ru.codehunters.zaepestelegrambot.service.ShelterValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DogShelterServiceImpl implements ShelterService<DogShelter, Dog> {

    private final DogShelterRepo dogRepo;
    private final ShelterValidationService valService;


    @Override
    public DogShelter addShelter(DogShelter shelter) {
        if (!valService.isCompleteRecord(shelter)) {
            throw new ShelterException("Данные приюта не заполнены");
        }
        return dogRepo.save(shelter);
    }

    @Override
    public DogShelter updateShelter(DogShelter shelter) {
        if (!valService.isCompleteRecord(shelter)) {
            throw new ShelterException("Данные приюта не заполнены");
        }
        return dogRepo.save(shelter);
    }


    @Override
    public List<Dog> giveAnimal(long id) {
        return dogRepo.getReferenceById(id).getList();
    }

    @Override
    public List<DogShelter> getShelter() {
        return dogRepo.findAll();
    }

    @Override
    public DogShelter addAnimalList(Dog animal, long id) {
        dogRepo.getReferenceById(id).getList().add(animal);
        return dogRepo.save(dogRepo.getReferenceById(id));
    }

    @Override
    public DogShelter delAnimalList(Dog animal, long id) {
        dogRepo.getReferenceById(id).getList().remove(animal);

        return dogRepo.save(dogRepo.getReferenceById(id));
    }

    @Override
    public void delShelter(long index) {
        dogRepo.deleteById(index);
    }
}
