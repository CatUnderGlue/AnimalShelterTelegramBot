package ru.codehunters.zaepestelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Chat;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.PhotoSize;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.ForwardMessage;
import com.pengrad.telegrambot.request.GetFile;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.GetFileResponse;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import ru.codehunters.zaepestelegrambot.constant.Information;
import ru.codehunters.zaepestelegrambot.constant.Keyboard;
import ru.codehunters.zaepestelegrambot.model.TrialPeriod;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup;
import ru.codehunters.zaepestelegrambot.repository.UserRepo;
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
    private final UserRepo userRepo;
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;
    private final VolunteerService volunteerService;
    private final TrialPeriodService trialPeriodService;
    private final ReportService reportService;
    private final Logger logger = LoggerFactory.getLogger(TelegramBotUpdatesListener.class);

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
                        logger.info("Обработка обновления: {}", update);
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        Chat chat = message.chat();
                        String text = message.text();
                        if (!userRepo.existsById(chatId)) {
                            userService.create(new User(chatId, chat.firstName(), chat.lastName(), ""));
                        }
                        User user = userService.getById(chatId);

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

                        switch (text) {
                            case "/start", "К выбору приютов" -> {
                                logger.info("Запустили бота/выбрали приют - ID:{}", chatId);
                                replyMarkup.sendStartMenu(chatId);
                            }
                            case Keyboard.CAT_SHELTER -> sendMenuStage("CAT", chatId);
                            case Keyboard.DOG_SHELTER -> sendMenuStage("DOG", chatId);
                            case Keyboard.INFORMATION_ABOUT_SHELTER -> {
                                logger.info("Узнать информацию о приюте - ID:{}", chatId);
                                shelterType = userService.getShelterById(chatId);
                                if ("CAT".equals(shelterType)) {
                                    replyMarkup.sendListMenuCat(chatId);
                                } else if ("DOG".equals(shelterType)) {
                                    replyMarkup.sendListMenuDog(chatId);
                                }
                            }
                            case Keyboard.MAIN_MENU -> {
                                logger.info("Главное меню - ID:{}", chatId);
                                user.setShelterType(null);
                                user.setShelterName(null);
                                userService.update(user);
                                replyMarkup.sendStartMenu(chatId);
                            }
                            case Keyboard.WORK_SCHEDULE -> {
                                logger.info("Расписание работы - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getTimetable());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getTimetable());
                                }
                            }
                            case Keyboard.LIST_OF_CATS -> {
                                logger.info("Список кошек - ID:{}", chatId);
                                List<Cat> catList = catShelterService.getShelterByName(user.getShelterName()).getList();
                                sendMessage(chatId, getStringFromList(catList));
                            }
                            case Keyboard.LIST_OF_DOGS -> {
                                logger.info("Список собак - ID:{}", chatId);
                                List<Dog> dogList = dogShelterService.getShelterByName(user.getShelterName()).getList();
                                sendMessage(chatId, getStringFromList(dogList));
                            }
                            case Keyboard.ABOUT_THE_SHELTER -> {
                                logger.info("О приюте - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getAboutMe());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getAboutMe());
                                }
                            }
                            case Keyboard.TB_GUIDELINES -> {
                                logger.info("Рекомендации о ТБ - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getSafetyAdvice());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getSafetyAdvice());
                                }
                            }
                            case Keyboard.CONTACT_DETAILS -> {
                                logger.info("Как отправить свои данные для связи - ID:{}", chatId);
                                sendMessage(chatId, "Введите номер телефона в формате: 89997776655");
                            }

                            case Keyboard.CONTACT_DETAILS_OF_THE_GUARD -> {
                                logger.info("Контактные данные охраны - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    sendMessage(chatId, catShelterService.getShelterByName(user.getShelterName()).getSecurity());
                                } else if (user.getShelterType().equals("DOG")) {
                                    sendMessage(chatId, dogShelterService.getShelterByName(user.getShelterName()).getSecurity());
                                }
                            }
                            case Keyboard.FAQ -> {
                                logger.info("Часто задаваемые вопросы - ID:{}", chatId);
                                if (user.getShelterType().equals("CAT")) {
                                    replyMarkup.menuCat(chatId);
                                } else if (user.getShelterType().equals("DOG")) {
                                    replyMarkup.menuDog(chatId);
                                }
                            }
                            case Keyboard.BACK_TO_ALL_ABOUT_CATS -> {
                                logger.info("Все о кошках - ID:{}", chatId);
                                replyMarkup.menuCat(chatId);
                            }
                            case Keyboard.BACK_TO_ALL_ABOUT_DOGS -> {
                                logger.info("Все о собаках - ID:{}", chatId);
                                replyMarkup.menuDog(chatId);
                            }
                            case Keyboard.RULES_FOR_DATING_A_CAT, Keyboard.RULES_FOR_DATING_A_DOG -> {
                                logger.info("Правила знакомства - ID:{}", chatId);
                                sendMessage(chatId, Information.ANIMAL_DATING_RULES);
                            }
                            case Keyboard.CAT_CARRIAGE, Keyboard.DOG_CARRIAGE -> {
                                logger.info("Перевозка - ID:{}", chatId);
                                sendMessage(chatId, Information.TRANSPORTATION_OF_THE_ANIMAL);
                            }
                            case Keyboard.REQUIRED_DOCUMENTS -> {
                                logger.info("Необходимые документы - ID:{}", chatId);
                                sendMessage(chatId, Information.LIST_OF_DOCUMENTS);
                            }
                            case Keyboard.LIST_OF_REASONS -> {
                                logger.info("Список причин для отказа выдачи питомца - ID:{}", chatId);
                                sendMessage(chatId, Information.LIST_OF_REASON_FOR_DENY);
                            }
                            case Keyboard.RECOMMENDATIONS_FOR_DOGS -> {
                                logger.info("Рекомендации для собак - ID:{}", chatId);
                                replyMarkup.rulesForDogs(chatId);
                            }
                            case Keyboard.RECOMMENDATIONS_FOR_CATS -> {
                                logger.info("Рекомендации для кошек - ID:{}", chatId);
                                replyMarkup.rulesForCats(chatId);
                            }
                            case Keyboard.PUPPY_SETUP, Keyboard.KITTEN_SETUP -> {
                                logger.info("Обустройство щенка/котенка - ID:{}", chatId);
                                sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY);
                            }
                            case Keyboard.ADULT_DOG_SETUP, Keyboard.ADULT_CAT_SETUP -> {
                                logger.info("Обустройство взрослого животного - ID:{}", chatId);
                                sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL);
                            }
                            case Keyboard.ARRANGEMENT_OF_DOG_WITH_DISABILITIES,
                                    Keyboard.ARRANGEMENT_OF_CAT_WITH_DISABILITIES -> {
                                logger.info("Обустройство животного с ограниченными возможностями - ID:{}", chatId);
                                sendMessage(chatId, Information.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL);
                            }
                            case Keyboard.DOG_HANDLERS_ADVICE -> {
                                logger.info("Советы кинолога - ID:{}", chatId);
                                sendMessage(chatId, Information.DOG_HANDLERS_ADVICE);
                            }
                            case Keyboard.PROVEN_CYNOLOGISTS -> {
                                logger.info("Проверенные кинологи для обращения - ID:{}", chatId);
                                sendMessage(chatId, Information.DOG_HANDLERS_CONTACTS);
                            }
                            case Keyboard.SEND_REPORT_FORM -> {
                                logger.info("Отправить форму отчёта - ID:{}", chatId);
                                sendReportExample(chatId);
                            }
                            case Keyboard.CALL_A_VOLUNTEER -> {
                                logger.info("Позвать волонтёра - ID:{}", chatId);
                                sendMessageToVolunteers(message);
                                sendMessage(chatId, "Первый освободившийся волонтёр ответит вам в ближайшее время");
                            }
                        }
                    });
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
        return CONFIRMED_UPDATES_ALL;
    }

    public void sendReportPhotoToVolunteer(Long reportId, Long volunteerId) {
        GetFile request = new GetFile(reportService.getById(reportId).getPhotoId());
        GetFileResponse getFileResponse = telegramBot.execute(request);
        TrialPeriod trialPeriod = trialPeriodService.getById(reportService.getById(reportId).getTrialPeriodId());
        if (getFileResponse.isOk()) {
            try {
                byte[] image = telegramBot.getFileContent(getFileResponse.file());
                SendPhoto sendPhoto = new SendPhoto(volunteerId, image);
                sendPhoto.caption("Id владельца: " + trialPeriod.getOwnerId() + "\n" +
                        "Id испытательного срока: " + trialPeriod.getId() + "\n" +
                        "Id отчёта:" + reportId);
                telegramBot.execute(sendPhoto);
            } catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }
    }

    private void sendMessage(Long chatId, String message) {
        SendResponse sendResponse = telegramBot.execute(new SendMessage(chatId, message));
        if (!sendResponse.isOk()) {
            logger.error(sendResponse.description());
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
        logger.info("Прилетел телефон - ID:{} тел:{} ", chatId, text);
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
            logger.error(e.getMessage(), e);
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

    private void sendMenuStage(String shelterType, Long chatId) {
        logger.info("Вызвано меню выбора приюта - ID:{}", chatId);
        ReplyMarkup replyMarkup = new ReplyMarkup(telegramBot, catShelterService, dogShelterService);
        User user = userService.getById(chatId);
        user.setShelterType(shelterType);
        userService.update(user);
        replyMarkup.sendMenuStage(chatId);
    }

    private String getStringFromList(List<?> list) {
        StringBuilder sb = new StringBuilder();
        list.forEach(o -> sb.append(o)
                .append("\n")
                .append("============").append("\n"));
        return sb.toString();
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
