package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.ShelterException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.repository.CatShelterRepo;
import ru.codehunters.zaepestelegrambot.service.ShelterService;
import ru.codehunters.zaepestelegrambot.service.ShelterValidationService;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CatShelterServiceImpl implements ShelterService<CatShelter, Cat> {


    private final CatShelterRepo catRepo;
    private final ShelterValidationService valService;


    @Override
    public CatShelter addShelter(CatShelter catShelter) {
        if (!valService.isCompleteRecord(catShelter)) {
            throw new ShelterException("Данные приюта не заполнены");
        }
        return catRepo.save(catShelter);
    }

    @Override
    public CatShelter updateShelter(CatShelter catShelter) {
        if (!valService.isCompleteRecord(catShelter)) {
            throw new ShelterException("Данные приюта не заполнены");
        }
        return catRepo.save(catShelter);
    }


    @Override
    public List<Cat> giveAnimal(long id) {
        return catRepo.getReferenceById(id).getList();
    }

    @Override
    public List<CatShelter> getShelter() {
        return catRepo.findAll();
    }

    @Override
    public CatShelter addAnimalList(Cat cat, long id) {
        catRepo.getReferenceById(id).getList().add(cat);
        return catRepo.save(catRepo.getReferenceById(id));
    }

    @Override
    public CatShelter delAnimalList(Cat cat, long id) {
        catRepo.getReferenceById(id).getList().remove(cat);
        return catRepo.save(catRepo.getReferenceById(id));
    }

    @Override
    public void delShelter(long index) {
        catRepo.deleteById(index);
    }
}
