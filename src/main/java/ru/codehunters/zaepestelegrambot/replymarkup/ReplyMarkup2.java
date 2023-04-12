package ru.codehunters.zaepestelegrambot.replymarkup;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.repository.CatShelterRepo;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.util.*;

@RequiredArgsConstructor
@AllArgsConstructor
public class ReplyMarkup2 {
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;
    private TelegramBot telegramBot;


    public ReplyMarkup2(CatShelterServiceImpl catShelterService, TelegramBot telegramBot, DogShelterServiceImpl dogShelterService) {
        this.catShelterService = catShelterService;
        this.telegramBot = telegramBot;
        this.dogShelterService = dogShelterService;
    }

    public void sendAnimalMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                (new KeyboardButton("Кошачий приют")), (new KeyboardButton("Собачий приют")));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите приют который хотите посмотреть:");
    }

    /**
     * Список приютов
     *
     * @param chatId
     */
    public void sendListMenuCat(long chatId) {
            List<CatShelter> shelters = catShelterService.getShelter();
            List<KeyboardButton> buttons = new ArrayList<>();
            shelters.forEach(shelter -> buttons.add(new KeyboardButton(shelter.getName())));
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
            returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Список кошачих приютов");

        }
    public void sendListMenuDog(long chatId) {
           List<DogShelter> shelters = dogShelterService.getShelter();
            List<KeyboardButton> buttons = new ArrayList<>();
            shelters.forEach(shelter -> buttons.add(new KeyboardButton(shelter.getName())));
            ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
            returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Список собачих приютов");

        }
    public void sendMenuCat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Расписание работы приюта"),
                new KeyboardButton("Список кошек приюта"),
                new KeyboardButton("О кошачьем приюте"));
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Рекомендации о ТБ приюта"),
                new KeyboardButton("Принять данные для связи"),
                new KeyboardButton("Контактные данные охраны приюта"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Назад"));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о кошачем приюте");
    }
    public void sendMenuDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Расписание работы"),
                new KeyboardButton("Список собачек приюта"),
                new KeyboardButton("О приюте"));
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Рекомендации о ТБ"),
                new KeyboardButton("Принять данные для связи"),
                new KeyboardButton("Контактные данные охраны"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Назад"));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о собачем приюте");
    }

    public void returnResponseReplyKeyboardMarkup(ReplyKeyboardMarkup replyKeyboardMarkup, Long chatId, String text) {
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.oneTimeKeyboard(false);
        replyKeyboardMarkup.selective(false);

        SendMessage request = new SendMessage(chatId, text)
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        SendResponse sendResponse = telegramBot.execute(request);
    }
}
