package ru.codehunters.zaepestelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup1;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup2;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup3;
import ru.codehunters.zaepestelegrambot.service.UserService;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdatesListener1 implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final UserService userService;
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        ReplyMarkup1 replyMarkup = new ReplyMarkup1(telegramBot);
        ReplyMarkup2 replyMarkup2 = new ReplyMarkup2(catShelterService,telegramBot,dogShelterService);
        ReplyMarkup3 replyMarkup3 = new ReplyMarkup3(telegramBot);
        try {
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        Chat chat = message.chat();
                        String text = message.text();

                        if ("/start".equals(text)) {

                            sendMessage(chatId, "Рады приветствовать Вас в нашем боте!");
                            sendMessage(chatId, "У нас здесь живут друзья разных пород и размеров - от веселых щенков до ласковых" +
                                    " котиков. Мы надеемся, что ты найдешь своего идеального друга здесь");
                            replyMarkup.sendStartMenu(chatId);
                        } else if ("Приют для кошек".equals(text)) {
                            try {
                                User user = userService.getById(chatId);
                                user.setShelter("CAT");
                                userService.update(user);
                            } catch (Exception e) {
                                userService.create(new User(chatId, chat.firstName(), chat.lastName(), "","CAT"));
                            }
                            replyMarkup.sendMenuStage0(chatId);
                        } else if ("Приют для собак".equals(text)) {
                            try {
                                User user = userService.getById(chatId);
                                user.setShelter("DOG");
                                userService.update(user);
                            } catch (Exception e) {
                                userService.create(new User(chatId, chat.firstName(), chat.lastName(), "", "DOG"));
                            }
                            replyMarkup.sendMenuStage0(chatId);
                        } else if ("Узнать информацию о приюте".equals(text)) {
                            String shelter = userService.getShelterById(chatId);
                            if ("CAT".equals(shelter)) {
                                replyMarkup2.sendMenuCat(chatId);
                            }else if ("DOG".equals(shelter)) {
                                replyMarkup2.sendMenuDog(chatId);
                            }
                        } else if ("Как взять животное из приюта".equals(text)) {
                            String shelter = userService.getShelterById(chatId);
                            if ("CAT".equals(shelter)) {
                                replyMarkup3.menuCat(chatId);
                            } else if ("DOG".equals(shelter)) {
                                replyMarkup3.menuDog(chatId);
                            }
                        } else if ("Прислать отчет о питомце".equals(text)) {
                            sendMessage(chatId, "Тут будет код из Listener4");
                        }else if ("Позвать волонтёра".equals(text)) {
                            sendMessage(chatId, "Тут будет код из Listener4");}
                    });
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return CONFIRMED_UPDATES_ALL;
    }

    private void sendMessage(Long chatId, String message) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, message));
        if (!sendResponse.isOk()) {
            System.out.printf("\nОшибка во время отправки сообщения: %s\n", sendResponse.description());
        }
    }
}