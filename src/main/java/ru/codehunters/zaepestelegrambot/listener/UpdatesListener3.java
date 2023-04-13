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
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup3;
import ru.codehunters.zaepestelegrambot.service.UserService;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class UpdatesListener3 implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final VolunteerService volunteerService;
    private final UserService userService;

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

                        User user = new User();
                        sendContact(message);

                        switch (text) {
                            case "/start" -> {
                                sendMessage(chatId, "Рады приветствовать тебя в нашем боте!");
                                replyMarkup3.sendMenu(chatId);

                                user.setTelegramId(chatId);
                                userService.create(user);

                            }
                            case "Часто задаваемые вопросы" -> {
                                sendMessage(chatId, "У нас здесь живут друзья разных пород и размеров - от веселых щенков до ласковых" +
                                        " котиков. Мы надеемся, что ты найдешь своего идеального друга здесь");
                                replyMarkup3.CatOrDog(chatId);
                            }
                            case "Позвать волонтера" -> sendMessageToVolunteers(message);

                            case "Все о кошках", "Назад в \"Все о кошках\"" -> replyMarkup3.menuCat(chatId);

                            case "Ввести свои данные для связи" ->
                                sendMessage(chatId, "Введите номер 890001112233");

                            case "Все о собаках", "Назад в \"Все о собаках\"" -> replyMarkup3.menuDog(chatId);

                            case "Правила знакомства c кошкой", "Правила знакомства c собакой" ->
                                    sendMessage(chatId, Information.ANIMAL_DATING_RULES);

                            case "Перевозка кошки", "Перевозка собаки" ->
                                    sendMessage(chatId, Information.TRANSPORTATION_OF_THE_ANIMAL);

                            case "Необходимые документы" -> sendMessage(chatId, Information.LIST_OF_DOCUMENTS);

                            case "Список причин для отказа выдачи питомца" ->
                                    sendMessage(chatId, Information.LIST_OF_REASON_FOR_DENY);

                            case "Рекомендации для собак" -> replyMarkup3.rulesForDogs(chatId);

                            case "Рекомендации для кошек" -> replyMarkup3.rulesForCats(chatId);

                            case "Обустройство щенка", "Обустройство котенка" ->
                                    sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);

                            case "Обустройство взрослой собаки", "Обустройство взрослой кошки" ->
                                    sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL);

                            case "Обустройство собаки с ограниченными возможностями", "Обустройство кошки с ограниченными возможностями" ->
                                    sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL);

                            case "Советы кинолога" -> sendMessage(chatId, Information.DOG_HANDLERS_ADVICE);

                            case "Проверенные кинологи для обращения" ->
                                    sendMessage(chatId, Information.DOG_HANDLERS_CONTACTS);

                            case "Назад в меню" -> replyMarkup3.sendMenu(chatId);

                            case "Назад в меню выбора" -> replyMarkup3.CatOrDog(chatId);

                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
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

    private void sendContact(Message message) {
        Long chatId = message.chat().id();
        String txt = message.text();
        Pattern pattern = Pattern.compile("^(\\d{11})$");
        Matcher matcher = pattern.matcher(txt);

        if (matcher.find()) {
            User byId = userService.getById(chatId);
            byId.setPhone(matcher.group(1));
            userService.update(byId);
            sendMessage(chatId, "Телефон принят");
        } else {
            sendMessage(chatId, "Неверно ввел");
        }


    }


}

