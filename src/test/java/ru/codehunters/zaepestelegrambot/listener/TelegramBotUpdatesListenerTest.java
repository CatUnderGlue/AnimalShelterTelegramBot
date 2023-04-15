package ru.codehunters.zaepestelegrambot.listener;

import com.pengrad.telegrambot.BotUtils;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.SendPhoto;
import com.pengrad.telegrambot.response.SendResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.codehunters.zaepestelegrambot.exception.NotFoundException;
import ru.codehunters.zaepestelegrambot.model.Information;
import ru.codehunters.zaepestelegrambot.model.User;
import ru.codehunters.zaepestelegrambot.model.Volunteer;
import ru.codehunters.zaepestelegrambot.model.animals.Cat;
import ru.codehunters.zaepestelegrambot.model.animals.Dog;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.service.UserService;
import ru.codehunters.zaepestelegrambot.service.VolunteerService;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TelegramBotUpdatesListenerTest {
    @Mock
    TelegramBot telegramBot;
    @Mock
    UserService userService;
    @Mock
    CatShelterServiceImpl catShelterService;
    @Mock
    DogShelterServiceImpl dogShelterService;
    @Mock
    VolunteerService volunteerService;
    @InjectMocks
    TelegramBotUpdatesListener telegramBotUpdatesListener;
    private final SendResponse sendResponse = BotUtils.fromJson("""
            {
            "ok": true
            }
            """, SendResponse.class);

    private final String messageTextJson;

    {
        try {
            messageTextJson = Files.readString(
                    Path.of(Objects.requireNonNull(TelegramBotUpdatesListenerTest.class.getResource("message_update.json")).toURI()));
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private final User catUser = new User(777L, "CatUnderGlue", "", "", "CAT", "SomeShelterName");
    private final User dogUser = new User(777L, "CatUnderGlue", "", "", "DOG", "SomeShelterName");
    private final CatShelter catShelter = new CatShelter(1L, "SomeShelterName", "location",
            "timetable", "about me", "security", "safety advice");
    private final DogShelter dogShelter = new DogShelter(1L, "SomeShelterName", "location",
            "timetable", "about me", "security", "safety advice");
    private final Cat cat = new Cat(1L, "CatName", 2, true, true, null, 1L);
    private final Dog dog = new Dog(1L, "DogName", 2, true, true, null, 1L);

    @Test
    void handleStartCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "/start"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(
                """
                        Рады приветствовать Вас в нашем боте!
                        У нас здесь живут друзья разных пород и размеров - от веселых щенков до ласковых котиков.
                        Мы надеемся, что ты найдешь своего идеального друга здесь
                        Выберите приют:
                        """, actual.getParameters().get("text"));
    }

    @Test
    void handleChooseCatsSheltersCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Приют для кошек"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Выберите:", actual.getParameters().get("text"));
    }

    @Test
    void handleChooseDogsSheltersCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Приют для собак"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Выберите:", actual.getParameters().get("text"));
    }

    @Test
    void handleListOfSheltersCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Узнать информацию о приюте"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);

        when(userService.getShelterById(any())).thenReturn("CAT");
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Список кошачьих приютов", actual.getParameters().get("text"));

        when(userService.getShelterById(any())).thenReturn("DOG");
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Список собачьих приютов", actual.getParameters().get("text"));
    }

    @Test
    void handleMainMenuCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Главное меню"), Update.class);
        User user = new User(777L, "CatUnderGlue", "", "", "CAT", "SomeShelterName");
        User userWithoutShelter = new User(777L, "CatUnderGlue", "", "", null, null);
        when(userService.getById(777L)).thenReturn(user);
        when(userService.update(userWithoutShelter)).thenReturn(userWithoutShelter);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(
                """
                        Рады приветствовать Вас в нашем боте!
                        У нас здесь живут друзья разных пород и размеров - от веселых щенков до ласковых котиков.
                        Мы надеемся, что ты найдешь своего идеального друга здесь
                        Выберите приют:
                        """, actual.getParameters().get("text"));
    }

    @Test
    void handleTimetableCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Расписание работы"), Update.class);
        SendMessage actual = catShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("timetable", actual.getParameters().get("text"));
        actual = dogShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("timetable", actual.getParameters().get("text"));
    }

    @Test
    void handleAnimalListCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Список кошек"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        catShelter.setList(List.of(cat));
        when(catShelterService.getShelterByName(any())).thenReturn(catShelter);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Имя: CatName, Возраст: 2, Состояние здоровья: здоров, Вакцинация: привит\n============\n",
                actual.getParameters().get("text"));

        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Список собачек"), Update.class);
        when(userService.getById(any())).thenReturn(dogUser);
        dogShelter.setList(List.of(dog));
        when(dogShelterService.getShelterByName(any())).thenReturn(dogShelter);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Имя: DogName, Возраст: 2, Состояние здоровья: здоров, Вакцинация: привит\n============\n",
                actual.getParameters().get("text"));
    }

    @Test
    void handleAboutShelterCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "О приюте"), Update.class);
        SendMessage actual = catShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("about me", actual.getParameters().get("text"));
        actual = dogShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("about me", actual.getParameters().get("text"));
    }

    @Test
    void handleSafetyAdviceCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Рекомендации о ТБ"), Update.class);
        SendMessage actual = catShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("safety advice", actual.getParameters().get("text"));
        actual = dogShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("safety advice", actual.getParameters().get("text"));
    }

    @Test
    void handleContactCommand() {
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Как отправить свои данные для связи"), Update.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Введите номер телефона в формате: 89997776655", actual.getParameters().get("text"));
    }

    @Test
    void handleSecurityContactCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Контактные данные охраны"), Update.class);
        SendMessage actual = catShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("security", actual.getParameters().get("text"));
        actual = dogShelterTester(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("security", actual.getParameters().get("text"));
    }

    @Test
    void handleFAQCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Часто задаваемые вопросы"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о кошках", actual.getParameters().get("text"));
        when(userService.getById(any())).thenReturn(dogUser);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о собаках", actual.getParameters().get("text"));
    }

    @Test
    void handleBackToFAQCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Назад в \\\"Все о кошках\\\""), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о кошках", actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Назад в \\\"Все о собаках\\\""), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Все о собаках", actual.getParameters().get("text"));
    }
    @Test
    void handleAnimalDatingCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Правила знакомства c кошкой"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.ANIMAL_DATING_RULES, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Правила знакомства c собакой"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.ANIMAL_DATING_RULES, actual.getParameters().get("text"));
    }

    @Test
    void handleAnimalTransportationCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Перевозка кошки"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.TRANSPORTATION_OF_THE_ANIMAL, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Перевозка собаки"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.TRANSPORTATION_OF_THE_ANIMAL, actual.getParameters().get("text"));
    }

    @Test
    void handleDocumentsListCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Необходимые документы"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.LIST_OF_DOCUMENTS, actual.getParameters().get("text"));
    }

    @Test
    void handleReasonsForDenyCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Список причин для отказа выдачи питомца"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.LIST_OF_REASON_FOR_DENY, actual.getParameters().get("text"));
    }
    @Test
    void handleRecommendationsForAnimalCommands() {
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Рекомендации для собак"), Update.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Рекомендации для собак", actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Рекомендации для кошек"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Рекомендации для кошек", actual.getParameters().get("text"));
    }

    @Test
    void handleImprovementForLittleAnimalCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Обустройство щенка"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Обустройство котенка"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.RECOMMENDATIONS_HOME_IMPROVEMENT_KITTEN_PUPPY, actual.getParameters().get("text"));
    }

    @Test
    void handleImprovementForAdultAnimalCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Обустройство взрослой собаки"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Обустройство взрослой кошки"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.RECOMMENDATIONS_HOME_IMPROVEMENT_ADULT_ANIMAL, actual.getParameters().get("text"));
    }

    @Test
    void handleImprovementForDisabledAnimalCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Обустройство собаки с ограниченными возможностями"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL, actual.getParameters().get("text"));
        update = BotUtils.fromJson(messageTextJson.replace("%text%", "Обустройство кошки с ограниченными возможностями"), Update.class);
        actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.RECOMMENDATIONS_HOME_IMPROVEMENT_DISABLED_ANIMAL, actual.getParameters().get("text"));
    }

    @Test
    void handleDogHandlersAdviseCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Советы кинолога"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.DOG_HANDLERS_ADVICE, actual.getParameters().get("text"));
    }

    @Test
    void handleDogHandlersContactsCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Проверенные кинологи для обращения"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        SendMessage actual = getSendMessage(update);
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals(Information.DOG_HANDLERS_CONTACTS, actual.getParameters().get("text"));
    }

    @Test
    void handleSendReportExampleCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Отправить форму отчёта"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendPhoto> argumentCaptor = ArgumentCaptor.forClass(SendPhoto.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        Mockito.reset(telegramBot);
        SendPhoto actual = argumentCaptor.getValue();
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("""
                    Рацион: ваш текст;
                    Самочувствие: ваш текст;
                    Поведение: ваш текст;
                    """, actual.getParameters().get("caption"));
    }

    @Test
    void handleSendMessageToVolunteersCommand() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "Позвать волонтёра"), Update.class);
        when(userService.getById(any())).thenThrow(NotFoundException.class);
        when(volunteerService.getAll()).thenReturn(List.of(new Volunteer(123L, "firstName", "lastName")));
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Первый освободившийся волонтёр ответит вам в ближайшее время", actual.getParameters().get("text"));
    }

    @Test
    void handleSheltersMenuCommands() {
        Update update = BotUtils.fromJson(messageTextJson.replace("%text%", "SomeShelterName"), Update.class);
        when(userService.getById(any())).thenReturn(catUser);
        when(userService.update(catUser)).thenReturn(catUser);
        when(catShelterService.getShelter()).thenReturn(List.of(catShelter));
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        SendMessage actual = argumentCaptor.getValue();
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Информация о кошачьем приюте", actual.getParameters().get("text"));

        when(userService.getById(any())).thenReturn(dogUser);
        when(userService.update(dogUser)).thenReturn(dogUser);
        when(dogShelterService.getShelter()).thenReturn(List.of(dogShelter));
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot, times(2)).execute(argumentCaptor.capture());
        actual = argumentCaptor.getValue();
        Assertions.assertEquals(update.message().chat().id(), actual.getParameters().get("chat_id"));
        Assertions.assertEquals("Информация о собачьем приюте", actual.getParameters().get("text"));
    }

    private SendMessage catShelterTester(Update update) {
        when(userService.getById(any())).thenReturn(catUser);
        when(catShelterService.getShelterByName(any())).thenReturn(catShelter);
        return getSendMessage(update);
    }

    private SendMessage dogShelterTester(Update update) {
        when(userService.getById(any())).thenReturn(dogUser);
        when(dogShelterService.getShelterByName(any())).thenReturn(dogShelter);
        return getSendMessage(update);
    }

    private SendMessage getSendMessage(Update update) {
        when(telegramBot.execute(any())).thenReturn(sendResponse);
        telegramBotUpdatesListener.process(Collections.singletonList(update));
        ArgumentCaptor<SendMessage> argumentCaptor = ArgumentCaptor.forClass(SendMessage.class);
        Mockito.verify(telegramBot).execute(argumentCaptor.capture());
        Mockito.reset(telegramBot);
        return argumentCaptor.getValue();
    }
}