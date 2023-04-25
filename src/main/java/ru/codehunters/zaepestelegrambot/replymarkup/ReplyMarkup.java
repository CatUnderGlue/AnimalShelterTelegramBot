package ru.codehunters.zaepestelegrambot.replymarkup;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.request.SendMessage;
import lombok.RequiredArgsConstructor;
import ru.codehunters.zaepestelegrambot.constant.Information;
import ru.codehunters.zaepestelegrambot.constant.Keyboard;
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
                new KeyboardButton(Keyboard.CAT_SHELTER),
                new KeyboardButton(Keyboard.DOG_SHELTER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER),
                new KeyboardButton(Keyboard.SEND_REPORT_FORM));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, Information.WELCOME);
    }


    public void sendMenuStage(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.INFORMATION_ABOUT_SHELTER),
                new KeyboardButton(Keyboard.FAQ),
                new KeyboardButton(Keyboard.SEND_REPORT_FORM));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Выберите:");
    }

    public void sendListMenuCat(long chatId) {
        List<CatShelter> shelters = catShelterService.getShelter();
        List<KeyboardButton> buttons = new ArrayList<>();
        shelters.forEach(shelter -> buttons.add(new KeyboardButton(shelter.getName())));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Список кошачьих приютов");
    }

    public void sendListMenuDog(long chatId) {
        List<DogShelter> shelters = dogShelterService.getShelter();
        List<KeyboardButton> buttons = new ArrayList<>();
        shelters.forEach(shelter -> buttons.add(new KeyboardButton(shelter.getName())));
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(buttons.toArray(new KeyboardButton[0]));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Список собачьих приютов");
    }

    public void sendMenuCat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.WORK_SCHEDULE),
                new KeyboardButton(Keyboard.LIST_OF_CATS),
                new KeyboardButton(Keyboard.ABOUT_THE_SHELTER));
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.TB_GUIDELINES),
                new KeyboardButton(Keyboard.CONTACT_DETAILS),
                new KeyboardButton(Keyboard.CONTACT_DETAILS_OF_THE_GUARD));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о кошачьем приюте");
    }

    public void sendMenuDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.WORK_SCHEDULE),
                new KeyboardButton(Keyboard.LIST_OF_DOGS),
                new KeyboardButton(Keyboard.ABOUT_THE_SHELTER));
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.TB_GUIDELINES),
                new KeyboardButton(Keyboard.CONTACT_DETAILS),
                new KeyboardButton(Keyboard.CONTACT_DETAILS_OF_THE_GUARD));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.CALL_A_VOLUNTEER));
        replyKeyboardMarkup.addRow(new KeyboardButton(Keyboard.MAIN_MENU));

        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Информация о собачьем приюте");
    }

    public void menuCat(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.REQUIRED_DOCUMENTS),
                new KeyboardButton(Keyboard.LIST_OF_REASONS)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.RECOMMENDATIONS_FOR_CATS),
                new KeyboardButton(Keyboard.CALL_A_VOLUNTEER),
                new KeyboardButton(Keyboard.CONTACT_DETAILS)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.MAIN_MENU)
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Все о кошках");
    }

    public void menuDog(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.REQUIRED_DOCUMENTS),
                new KeyboardButton(Keyboard.LIST_OF_REASONS)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.RECOMMENDATIONS_FOR_DOGS),
                new KeyboardButton(Keyboard.CALL_A_VOLUNTEER),
                new KeyboardButton(Keyboard.CONTACT_DETAILS)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.MAIN_MENU)
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, "Все о собаках");
    }

    public void rulesForDogs(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.RULES_FOR_DATING_A_DOG),
                new KeyboardButton(Keyboard.DOG_CARRIAGE),
                new KeyboardButton(Keyboard.PUPPY_SETUP)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.ADULT_DOG_SETUP),
                new KeyboardButton(Keyboard.ARRANGEMENT_OF_DOG_WITH_DISABILITIES)
                );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.DOG_HANDLERS_ADVICE),
                new KeyboardButton(Keyboard.PROVEN_CYNOLOGISTS));
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.BACK_TO_ALL_ABOUT_DOGS)
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, Keyboard.RECOMMENDATIONS_FOR_DOGS);
    }

    public void rulesForCats(long chatId) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(
                new KeyboardButton(Keyboard.RULES_FOR_DATING_A_CAT),
                new KeyboardButton(Keyboard.CAT_CARRIAGE)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.ADULT_CAT_SETUP),
                new KeyboardButton(Keyboard.KITTEN_SETUP),
                new KeyboardButton(Keyboard.ARRANGEMENT_OF_CAT_WITH_DISABILITIES)
        );
        replyKeyboardMarkup.addRow(
                new KeyboardButton(Keyboard.BACK_TO_ALL_ABOUT_CATS)
        );
        returnResponseReplyKeyboardMarkup(replyKeyboardMarkup, chatId, Keyboard.RECOMMENDATIONS_FOR_CATS);
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
}
