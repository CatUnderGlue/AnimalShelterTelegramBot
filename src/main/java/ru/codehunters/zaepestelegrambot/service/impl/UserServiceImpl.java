package ru.codehunters.zaepestelegrambot.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.repository.UserRepo;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    @Override
    public User create(User user)  {
        return userRepo.save(user);
    }

    @Override
    public User getById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return optionalUser.get();
    }

    @Override
    public String getShelterById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return optionalUser.get().getShelter();
    }

    @Override
    public List<User> getAll() {
        List<User> all = userRepo.findAll();
        if (all.isEmpty()) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return all;
    }

    @Override
    public User update(User user) {
        if (user.getTelegramId() == null || getById(user.getTelegramId()) == null) {
            throw new NotFoundException("Пользователь не найден!");
        }
        return userRepo.save(user);
    }

    @Override
    public void delete(User user) {
        userRepo.delete(getById(user.getTelegramId()));
    }

    @Override
    public void deleteById(Long id) {
        userRepo.deleteById(getById(id).getTelegramId());
    }
}
