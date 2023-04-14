package ru.codehunters.zaepestelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Information;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup;
import ru.codehunters.zaepestelegrambot.service.ReportService;
import ru.codehunters.zaepestelegrambot.service.TrialPeriodService;
import ru.codehunters.zaepestelegrambot.service.UserService;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@RequiredArgsConstructor
public class TelegramBotUpdatesListener implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final UserService userService;
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;
    private final VolunteerService volunteerService;
    private final TrialPeriodService trialPeriodService;
    private final ReportService reportService;

    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        ReplyMarkup replyMarkup = new ReplyMarkup(telegramBot, catShelterService, dogShelterService);
        try {
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        Chat chat = message.chat();
                        String text = message.text();

                        if (message.photo() != null) {
                            getReport(message);
                            return;
                        }

                        Pattern pattern = Pattern.compile("(\\d+)");
                        Matcher matcher = pattern.matcher(text);
                        if (matcher.find()) {
                            getContact(chatId, text);
                            return;
                        }

                        try {
                            User user = userService.getById(chatId);
                            String shelterType = user.getShelterType();
                            if (shelterType != null) {
                                if (shelterType.equals("DOG")) {
                                    dogShelterService.getShelter().forEach(dogShelter -> {
                                        if (dogShelter.getName().equals(text)) {
                                            user.setShelterName(dogShelter.getName());
                                            replyMarkup.sendMenuDog(chatId);
                                            userService.update(user);
                                        }
                                    });
                                } else if (shelterType.equals("CAT")) {
                                    catShelterService.getShelter().forEach(catShelter -> {
                                        if (catShelter.getName().equals(text)) {
                                            user.setShelterName(catShelter.getName());
                                            replyMarkup.sendMenuCat(chatId);
                                            userService.update(user);
                                        }

                                    });
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        switch (text) {
                            case "/start", "К выбору приютов" -> {
                                try {
                                    userService.getById(chatId);
                                } catch (NotFoundException e) {
                                    userService.create(new User(chatId, chat.firstName(),chat.lastName(), ""));
                                }
                                sendMessage(chatId, "Рады приветствовать Вас в нашем боте!");
                                sendMessage(chatId, "У нас здесь живут друзья разных пород и размеров - от веселых щенков до ласковых" +
                                        " котиков. Мы надеемся, что ты найдешь своего идеального друга здесь");
                                replyMarkup.sendStartMenu(chatId);
                            }
                            case "Приют для кошек" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    user.setShelterType("CAT");
                                    userService.update(user);
                                } catch (Exception e) {
                                    //todo
                                }
                                replyMarkup.sendMenuStage0(chatId);
                            }
                            case "Приют для собак" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    user.setShelterType("DOG");
                                    userService.update(user);
                                } catch (Exception e) {
                                    //todo
                                }
                                replyMarkup.sendMenuStage0(chatId);
                            }

                            case "Узнать информацию о приюте" -> {
                                String shelterType = userService.getShelterById(chatId);
                                if ("CAT".equals(shelterType)) {
                                    replyMarkup.sendListMenuCat(chatId);
                                } else if ("DOG".equals(shelterType)) {
                                    replyMarkup.sendListMenuDog(chatId);
                                }
                            }
                            case "Главное меню" -> {
                                User user = userService.getById(chatId);
                                user.setShelterType(null);
                                user.setShelterName(null);
                                userService.update(user);
                                replyMarkup.sendStartMenu(chatId);
                            }
                            case "Расписание работы" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    if (user.getShelterType().equals("CAT")){
                                        sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getTimetable());
                                    } else if (user.getShelterType().equals("DOG")) {
                                        sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getTimetable());
                                    }
                                } catch (NotFoundException e) {
                                    //todo
                                }
                            }
                            case "Список кошек" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    StringBuilder sb = new StringBuilder();
                                    catShelterService.getShelterByName(user.getShelterName()).getList()
                                            .forEach(cat -> sb
                                                    .append(cat).append("\n")
                                                    .append("============").append("\n"));
                                    sendMessage(chatId, sb.toString());
                                } catch (Exception e) {
                                    //todo
                                }
                            }
                            case "Список собачек" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    StringBuilder sb = new StringBuilder();
                                    dogShelterService.getShelterByName(user.getShelterName()).getList()
                                            .forEach(dog -> sb
                                                    .append(dog).append("\n")
                                                    .append("============").append("\n"));
                                    sendMessage(chatId, sb.toString());
                                } catch (Exception e) {
                                    //todo
                                }
                            }
                            case "О приюте" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    if (user.getShelterType().equals("CAT")){
                                        sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getAboutMe());
                                    } else if (user.getShelterType().equals("DOG")) {
                                        sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getAboutMe());
                                    }

                                } catch (Exception e) {
                                    //todo
                                }
                            }
                            case "Рекомендации о ТБ" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    if (user.getShelterType().equals("CAT")){
                                        sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getSafetyAdvice());
                                    } else if (user.getShelterType().equals("DOG")) {
                                        sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getSafetyAdvice());
                                    }
                                } catch (NotFoundException e) {
                                    //todo
                                }
                            }
                            case "Как отправить свои данные для связи" -> sendMessage(chatId, "Введите номер телефона в формате: 89997776655");
                            case "Контактные данные охраны" -> {
                                try {
                                    User user = userService.getById(chatId);
                                    if (user.getShelterType().equals("CAT")){
                                        sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getSecurity());
                                    } else if (user.getShelterType().equals("DOG")) {
                                        sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getSecurity());
                                    }
                                } catch (NotFoundException e) {
                                    //todo
                                }
                            }
                            case "Часто задаваемые вопросы" -> {
                                User user = userService.getById(chatId);
                                if (user.getShelterType().equals("CAT")){
                                    replyMarkup.Cat(chatId);
                                } else if (user.getShelterType().equals("DOG")) {
                                    replyMarkup.Dog(chatId);
                                }
                            }
                            case "Все о кошках", "Назад в \"Все о кошках\"" -> replyMarkup.menuCat(chatId);
                            case "Все о собаках", "Назад в \"Все о собаках\"" -> replyMarkup.menuDog(chatId);
                            case "Правила знакомства c кошкой", "Правила знакомства c собакой" ->
                                    sendMessage(chatId, Information.ANIMAL_DATING_RULES);
                            case "Перевозка кошки", "Перевозка собаки" ->
                                    sendMessage(chatId, Information.TRANSPORTATION_OF_THE_ANIMAL);
                            case "Необходимые документы" -> sendMessage(chatId, Information.LIST_OF_DOCUMENTS);
                            case "Список причин для отказа выдачи питомца" ->
                                    sendMessage(chatId, Information.LIST_OF_REASON_FOR_DENY);
                            case "Рекомендации для собак" -> replyMarkup.rulesForDogs(chatId);
                            case "Рекомендации для кошек" -> replyMarkup.rulesForCats(chatId);
                            case "Обустройство щенка", "Обустройство котенка" ->
                                    sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);
                            case "Обустройство взрослой собаки", "Обустройство взрослой кошки" ->
                                    sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL);
                            case "Обустройство собаки с ограниченными возможностями", "Обустройство кошки с ограниченными возможностями" ->
                                    sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL);
                            case "Советы кинолога" -> sendMessage(chatId, Information.DOG_HANDLERS_ADVICE);
                            case "Проверенные кинологи для обращения" ->
                                    sendMessage(chatId, Information.DOG_HANDLERS_CONTACTS);
                            case "Отправить форму отчёта" -> sendReportExample(chatId);
                            case "Позвать волонтёра", "Позвать волонтера" -> sendMessageToVolunteers(message);
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

    private void getContact(Long chatId, String text) {
        Pattern pattern = Pattern.compile("^(\\d{11})$");
        Matcher matcher = pattern.matcher(text);

        if (matcher.find()) {
            User byId = userService.getById(chatId);
            byId.setPhone(matcher.group(1));
            userService.update(byId);
            sendMessage(chatId, "Телефон принят");
        } else {
            sendMessage(chatId, "Неверно ввел");
        }
    }

    private void sendMessageToVolunteers(Message message) {
        Long chatId = message.chat().id();
        Integer integer = message.messageId();
        for (Volunteer volunteer : volunteerService.getAll()) {
            telegramBot.execute(new ForwardMessage(volunteer.getTelegramId(), chatId, integer));
        }
    }

    private void sendMessageToVolunteers(Long chatId) {
        for (Volunteer volunteer : volunteerService.getAll()) {
            telegramBot.execute(new SendMessage(volunteer.getTelegramId(), "Владелец животного с id " + chatId + " не отправлял отчёты более двух дней!"));
        }
    }

    private void sendReportExample(Long chatId) {
        try {
            byte[] photo = Files.readAllBytes(
                    Paths.get(Objects.requireNonNull(UpdatesListener.class.getResource("/static/img/Cat.jpg")).toURI())
            );
            SendPhoto sendPhoto = new SendPhoto(chatId, photo);
            sendPhoto.caption("""
                    Рацион: ваш текст;
                    Самочувствие: ваш текст;
                    Поведение: ваш текст;
                    """);
            telegramBot.execute(sendPhoto);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private void getReport(Message message) {
        PhotoSize photoSize = message.photo()[message.photo().length - 1];
        String caption = message.caption();
        Long chatId = message.chat().id();
        try {
            reportService.createFromTelegram(photoSize.fileId(), caption, chatId);
            sendMessage(chatId, "Ваш отчёт принят.");
        } catch (Exception e) {
            sendMessage(chatId, e.getMessage());
        }
    }

    @Scheduled(cron = "@daily")
    private void sendWarning() {
        for (User user : userService.getAll()) {
            for (TrialPeriod trialPeriod : trialPeriodService.getAllByOwnerId(user.getTelegramId())) {
                if ((trialPeriod.getReports().size() < 45 && !trialPeriod.getLastReportDate().isEqual(trialPeriod.getEndDate())) &&
                        trialPeriod.getLastReportDate().isBefore(LocalDate.now().minusDays(2))) {
                    sendMessage(user.getTelegramId(), "Вы не отправляли отчёты уже более двух дней. " +
                            "Пожалуйста, отправьте отчёт или выйдите на связь с волонтёрами.");
                    sendMessageToVolunteers(user.getTelegramId());
                }
            }

        }
    }

    @Scheduled(cron = "@daily")
    private void sendTrialPeriodStatus() {
        for (User user : userService.getAll()) {
            for (TrialPeriod trialPeriod : trialPeriodService.getAllByOwnerId(user.getTelegramId())) {
                if (trialPeriod.getResult().equals(TrialPeriod.Result.NOT_SUCCESSFUL)) {
                    sendMessage(user.getTelegramId(), Information.TRIAL_NOT_SUCCESSFUL);
                } else if (trialPeriod.getResult().equals(TrialPeriod.Result.EXTENDED)) {
                    sendMessage(user.getTelegramId(), Information.TRIAL_EXTENDED);
                } else if (trialPeriod.getResult().equals(TrialPeriod.Result.SUCCESSFUL)) {
                    sendMessage(user.getTelegramId(), Information.SUCCESSFUL);
                }
            }
        }
    }
}
