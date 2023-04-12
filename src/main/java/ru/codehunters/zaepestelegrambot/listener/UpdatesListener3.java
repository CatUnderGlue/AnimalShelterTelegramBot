package ru.codehunters.zaepestelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import ru.codehunters.zaepestelegrambot.model.Information;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup3;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.List;

@Component
@RequiredArgsConstructor
public class UpdatesListener3 implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final VolunteerService volunteerService;


    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        ReplyMarkup3 replyMarkup3 = new ReplyMarkup3(telegramBot);
        try {
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        String text = message.text();

                        if ("/start".equals(text)) {
                            sendMessage(chatId, "Рады приветствовать тебя в нашем боте!");
                            replyMarkup3.sendMenu(chatId);
                        } else if ("Часто задаваемые вопросы".equals(text)) {
                            sendMessage(chatId, "У нас здесь живут друзья разных пород и размеров - от веселых щенков до ласковых" +
                                    " котиков. Мы надеемся, что ты найдешь своего идеального друга здесь");
                            replyMarkup3.CatOrDog(chatId);

                        }
//                        else if ("Позвать волонтера".equals(text)) {
//                            sendMessageToVolunteers(message);
//                     }
                        else if ("Все о кошках".equals(text)) {
                            replyMarkup3.menuCat(chatId);

                        } else if ("Все о собаках".equals(text)) {
                            replyMarkup3.menuDog(chatId);

                        } else if ("Правила знакомства c кошкой".equals(text) || "Правила знакомства c собакой".equals(text)) {
                            sendMessage(chatId, Information.ANIMAL_DATING_RULES);

                        } else if ("Перевозка кошки".equals(text) || "Перевозка собаки".equals(text)) {
                            sendMessage(chatId, Information.TRANSPORTATION_OF_THE_ANIMAL);

                        } else if ("Необходимые документы".equals(text)) {
                            sendMessage(chatId, Information.LIST_OF_DOCUMENTS);

                        } else if ("Список причин для отказа выдачи питомца".equals(text)) {
                            sendMessage(chatId, Information.LIST_OF_REASON_FOR_DENY);

                        } else if ("Рекомендации для собак".equals(text)) {
                            replyMarkup3.rulesForDogs(chatId);

                        } else if ("Рекомендации для кошек".equals(text)) {
                            replyMarkup3.rulesForCats(chatId);

                        } else if ("Обустройство щенка".equals(text) || "Обустройство котенка".equals(text)) {
                            sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);

                        } else if ("Обустройство взрослой собаки".equals(text) ||
                                "Обустройство взрослой кошки".equals(text)) {
                            sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL);

                        } else if ("Обустройство собаки с ограниченными возможностями".equals(text) ||
                                "Обустройство кошки с ограниченными возможностями".equals(text)) {
                            sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL);

                        } else if ("Советы кинолога".equals(text)) {
                            sendMessage(chatId, Information.DOG_HANDLERS_ADVICE);

                        } else if ("Проверенные кинологи для обращения".equals(text)) {
                            sendMessage(chatId, Information.DOG_HANDLERS_CONTACTS);

                        } else if ("Назад в меню".equals(text)) {
                            replyMarkup3.sendMenu(chatId);

                        } else if ("Назад в меню выбора".equals(text)) {
                            replyMarkup3.CatOrDog(chatId);

                        } else if ("Назад в \"Все о собаках\"".equals(text)) {
                            replyMarkup3.menuDog(chatId);

                        } else if ("Назад в \"Все о кошках\"".equals(text)) {
                            replyMarkup3.menuCat(chatId);

                        }
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

    private void sendMessageToVolunteers(Message message) {
        Long chatId = message.chat().id();
        Integer integer = message.messageId();
        for (Volunteer volunteer : volunteerService.getAll()) {
            telegramBot.execute(new ForwardMessage(volunteer.getTelegramId(), chatId, integer));
        }
    }
}
