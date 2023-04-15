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
        User user = optionalUser.get();
        return user.getShelterType();
    }

    @Override
    public List<User> getAll() {
        return userRepo.findAll();
    }

    @Override
    public User update(User user) {
        Optional<User> optionalUser = userRepo.findById(user.getTelegramId());
        if (optionalUser.isEmpty()){
           throw new NotFoundException("Пользователь не найден!");
        }
        User currentUser = optionalUser.get();
        EntityUtils.copyNonNullFields(user, currentUser);
        return userRepo.save(currentUser);
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
