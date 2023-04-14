package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.repository.CatShelterRepo;
import ru.codehunters.zaepestelegrambot.service.ShelterService;


import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CatShelterServiceImpl implements ShelterService<CatShelter,Cat> {


    private final CatShelterRepo catRepo;

    @Override
    public CatShelter addShelter(CatShelter shelter) {
        return catRepo.save(shelter);
    }

    @Override
    public CatShelter updateShelter(CatShelter catShelter) {
        CatShelter currentShelter = getSheltersId(catShelter.getId());
        EntityUtils.copyNonNullFields(catShelter, currentShelter);
        return catRepo.save(currentShelter);
    }

    @Override
    public CatShelter getSheltersId(long id) {
        Optional<CatShelter> shelterId = catRepo.findById(id);
        if (shelterId.isEmpty()){
            throw new NotFoundException("Приют не найден. Кошки остались без дома");
        }
        return shelterId.get();
    }

    @Override
    public CatShelter getShelterByName(String name) {
        Optional<CatShelter> shelterId = catRepo.findByName(name);
        if (shelterId.isEmpty()){
            throw new NotFoundException("Приют не найден. Кошки остались без дома");
        }
        return shelterId.get();
    }

    @Override
    public List<CatShelter> getShelter() {
        return catRepo.findAll();
    }

    @Override
    public List<Cat> getAnimal(long index) {
        return getSheltersId(index).getList();
    }

    @Override
    public String delShelter(long index) {
        String result;
        Optional<CatShelter> catShelter = catRepo.findById(index);
        if (catShelter.isPresent()) {
            catRepo.deleteById(index);
            result = "Запись удалена";
        } else {
            throw new NotFoundException("Котятки остались без приюта. Не нашли приют");
        }
        return result;
    }
}
