package ru.codehunters.zaepestelegrambot.replymarkup;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ReplyMarkup3 {

    private TelegramBot telegramBot;

    public ReplyMarkup3(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void returnResponseReplyKeyboardMarkup(ReplyKeyboardMarkup replyKeyboardMarkup, Long chatId, String text) {
        replyKeyboardMarkup.resizeKeyboard(true);
        replyKeyboardMarkup.oneTimeKeyboard(false);
        replyKeyboardMarkup.selective(false);

        SendMessage request = new SendMessage(chatId, text)
                .replyMarkup(replyKeyboardMarkup)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true);
        telegramBot.execute(request);
    }

    public void sendMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Часто задаваемые вопросы"));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Часто задаваемые вопросы");
    }

    public void CatOrDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Все о кошках"),
                new KeyboardButton("Все о собаках")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Назад в меню")
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
                new KeyboardButton("Ввести свои данные для связи")

        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Назад в меню выбора")
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
                new KeyboardButton("Позвать волонтера")
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton("Назад в меню выбора")
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Все о собаках");
    }

    public void rulesForDogs(long chatId){
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
    public void rulesForCats(long chatId){
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

}
