package ru.codehunters.zaepestelegrambot.replymarkup;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
public class ReplyMarkup {

    private final TelegramBot telegramBot;
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;

    public void sendStartMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Приют для кошек"),
                new KeyboardButton("Приют для собак"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтёра"),
                new KeyboardButton("Отправить форму отчёта"));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите приют:");
    }

    public void sendMenuStage0(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Узнать информацию о приюте"),
                new KeyboardButton("Часто задаваемые вопросы"),
                new KeyboardButton("Отправить форму отчёта"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтера"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Главное меню"));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите:");
    }

    public void sendListMenuCat(long chatId) {
        List<CatShelter> shelters = catShelterService.getShelter();
        List<KeyboardButton> buttons = new ArrayList<>();
        shelters.forEach(shelter -> buttons.add(new KeyboardButton(shelter.getName())));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Список кошачьих приютов");
        replyKeyboardMarkup.addRow(new KeyboardButton("Главное меню"));
    }

    public void sendListMenuDog(long chatId) {
        List<DogShelter> shelters = dogShelterService.getShelter();
        List<KeyboardButton> buttons = new ArrayList<>();
        shelters.forEach(shelter -> buttons.add(new KeyboardButton(shelter.getName())));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Список собачьих приютов");
        replyKeyboardMarkup.addRow(new KeyboardButton("Главное меню"));
    }

    public void sendMenuCat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Расписание работы"),
                new KeyboardButton("Список кошек"),
                new KeyboardButton("О приюте"));
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Рекомендации о ТБ"),
                new KeyboardButton("Как отправить свои данные для связи"),
                new KeyboardButton("Контактные данные охраны"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтера"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Главное меню"));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о кошачьем приюте");
    }

    public void sendMenuDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Расписание работы"),
                new KeyboardButton("Список собачек"),
                new KeyboardButton("О приюте"));
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Рекомендации о ТБ"),
                new KeyboardButton("Как отправить свои данные для связи"),
                new KeyboardButton("Контактные данные охраны"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтера"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Главное меню"));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о собачьем приюте");
    }

    public void Cat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Все о кошках")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Главное меню")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "О ком нужна информация?");
    }

    public void Dog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Все о собаках")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Главное меню")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "О ком нужна информация?");
    }

    public void menuCat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Необходимые документы"),
                new KeyboardButton("Список причин для отказа выдачи питомца")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Рекомендации для кошек"),
                new KeyboardButton("Позвать волонтера"),
                new KeyboardButton("Как отправить свои данные для связи")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Главное меню")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Все о кошках");
    }

    public void menuDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Необходимые документы"),
                new KeyboardButton("Список причин для отказа выдачи питомца")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Рекомендации для собак"),
                new KeyboardButton("Позвать волонтера"),
                new KeyboardButton("Как отправить свои данные для связи")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Главное меню")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Все о собаках");
    }

    public void rulesForDogs(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Правила знакомства c собакой"),
                new KeyboardButton("Перевозка собаки"),
                new KeyboardButton("Обустройство щенка")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Обустройство взрослой собаки"),
                new KeyboardButton("Обустройство собаки с ограниченными возможностями")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Советы кинолога"),
                new KeyboardButton("Проверенные кинологи для обращения"));
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Назад в \"Все о собаках\" ")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Рекомендации для собак");
    }

    public void rulesForCats(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Правила знакомства c кошкой"),
                new KeyboardButton("Перевозка кошки")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Обустройство взрослой кошки"),
                new KeyboardButton("Обустройство котенка"),
                new KeyboardButton("Обустройство кошки с ограниченными возможностями")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Назад в \"Все о кошках\" ")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Рекомендации для кошек");
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
