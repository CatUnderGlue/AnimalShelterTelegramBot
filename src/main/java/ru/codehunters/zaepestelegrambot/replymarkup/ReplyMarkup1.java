package ru.codehunters.zaepestelegrambot.replymarkup;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import lombok.RequiredArgsConstructor;

public class ReplyMarkup1 {

    private final TelegramBot telegramBot;

    public ReplyMarkup1(TelegramBot telegramBot) {
        this.telegramBot = telegramBot;
    }

    public void sendStartMenu(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Приют для кошек"),
                new KeyboardButton("Приют для собак"));
//        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтёра"),
//                new KeyboardButton("Отправить форму отчёта"));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите приют:");
    }

    public void sendMenuStage0(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton("Узнать информацию о приюте"),
                new KeyboardButton("Как взять животное из приюта"),
                new KeyboardButton("Прислать отчет о питомце"));
        replyKeyboardMarkup.addRow(new KeyboardButton("Позвать волонтера"));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите:");
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
