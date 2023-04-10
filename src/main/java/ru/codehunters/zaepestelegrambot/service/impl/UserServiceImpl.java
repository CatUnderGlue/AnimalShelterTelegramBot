package ru.codehunters.zaepestelegrambot.service.impl;

import org.springframework.stereotype.Service;
import ru.codehunters.zaepestelegrambot.exception.UserNotFoundException;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.repository.UserRepo;
import ru.codehunters.zaepestelegrambot.service.UserService;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;

    public UserServiceImpl(UserRepo userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public User create(User user)  {
        return userRepo.save(user);
    }

    @Override
    public User getById(Long id) {
        Optional<User> optionalUser = userRepo.findById(id);
        if (optionalUser.isEmpty()) {
            throw new UserNotFoundException();
        }
        return optionalUser.get();
    }

    @Override
    public List<User> getAll() {
        List<User> all = userRepo.findAll();
        if (all.isEmpty()) {
            throw new UserNotFoundException();
        }
        return all;
    }

    @Override
    public User update(User user) {
        if (user.getTelegramId() == null || getById(user.getTelegramId()) == null) {
            throw new UserNotFoundException();
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
