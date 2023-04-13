
package ru.codehunters.zaepestelegrambot.listener;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.codehunters.zaepestelegrambot.model.shelters.CatShelter;
import ru.codehunters.zaepestelegrambot.model.shelters.DogShelter;
import ru.codehunters.zaepestelegrambot.replymarkup.ReplyMarkup2;
import ru.codehunters.zaepestelegrambot.service.impl.CatShelterServiceImpl;
import ru.codehunters.zaepestelegrambot.service.impl.DogShelterServiceImpl;

import java.util.List;


@Component
@AllArgsConstructor
public class UpdatesListener2 implements UpdatesListener {

    private final TelegramBot telegramBot;
    private final CatShelterServiceImpl catShelterService;
    private final DogShelterServiceImpl dogShelterService;

    private Long idCat;
    private Long idDog;
    CatShelter catShelter;
@Autowired
    public UpdatesListener2(TelegramBot telegramBot, CatShelterServiceImpl catShelterService, DogShelterServiceImpl dogShelterService) {
        this.telegramBot = telegramBot;
        this.catShelterService = catShelterService;
        this.dogShelterService = dogShelterService;
    }



    @PostConstruct
    public void init() {
        telegramBot.setUpdatesListener(this);
    }

    @Override
    public int process(List<Update> updates) {
        ReplyMarkup2 replyMarkup = new ReplyMarkup2(catShelterService,telegramBot,dogShelterService);
        try {
            updates.stream()
                    .filter(update -> update.message() != null)
                    .forEach(update -> {
                        Message message = update.message();
                        Long chatId = message.chat().id();
                        String text = message.text();

                        if ("Назад".equals(text)) {
                            replyMarkup.sendAnimalMenu(chatId);
                        }
                        else if ("Кошачий приют".equals(text)) {
                            replyMarkup.sendListMenuCat(chatId);
                                }
                        else if ("Собачий приют".equals(text)) {
                            replyMarkup.sendListMenuDog(chatId);
                        }
                        else if (isNameCatShelter(text)) {
                            replyMarkup.sendMenuCat(chatId);
                            idCat=isNameCatShelterId(text);
                            //    catShelter =loadCat(idCat);
                        }
                        else if (isNameDogShelter(text)) {
                            replyMarkup.sendMenuDog(chatId);
                            idDog=isNameDogShelterId(text);
                        }
                        else if ("Расписание работы приюта".equals(text)) {


                       //    sendMessage(chatId, catShelter.getTimetable());
                        }
                        else if ("Список кошек приюта".equals(text)) {
                      //      sendMessage(chatId,catShelterService.getSheltersId(idCat).getList().toString());
                        }
                        else if ("О кошачьем приюте".equals(text)) {
                   //         sendMessage(chatId,catShelterService.getSheltersId(idCat).getAboutMe());
                        }
                        else if ("Рекомендации о ТБ приюта".equals(text)) {
                   //         sendMessage(chatId,catShelterService.getSheltersId(idCat).getSafetyAdvice());
                        }
                        else if ("Принять данные для связи".equals(text)) {

                        }
                        else if ("Контактные данные охраны приюта".equals(text)) {
                    //        sendMessage(chatId,catShelterService.getSheltersId(idCat).getSecurity());
                        }
                        else if ("Расписание работы".equals(text)) {
                  //          sendMessage(chatId,dogShelterService.getSheltersId(idCat).getTimetable());
                        }
                        else if ("Список собачек приюта".equals(text)) {
                   //         sendMessage(chatId,dogShelterService.getSheltersId(idDog).getList().toString());
                        }
                        else if ("О приюте".equals(text)) {
                    //        sendMessage(chatId,dogShelterService.getSheltersId(idDog).getAboutMe());
                        } else if ("Рекомендации о ТБ".equals(text)) {
                   //         sendMessage(chatId,dogShelterService.getSheltersId(idDog).getSecurity());
                        } else if ("Контактные данные охраны".equals(text)) {
                  //          sendMessage(chatId,dogShelterService.getSheltersId(idDog).getSecurity());
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

    private boolean isNameCatShelter(String text){
        for (CatShelter catShelter:catShelterService.getShelter()){
            if (catShelter.getName().equals(text)){
                return true;
            }
        }
        return  false;
    }
    private boolean isNameDogShelter(String text){
        for (DogShelter dogShelter:dogShelterService.getShelter()){
            if (dogShelter.getName().equals(text)){
                return true;
            }
        }
        return  false;
    }
    private Long isNameCatShelterId(String text){
        for (CatShelter catShelter:catShelterService.getShelter()){
            if (catShelter.getName().equals(text)){
                return catShelter.getId();
            }
        }
        return  0l;
    }
    private Long isNameDogShelterId(String text){
        for (DogShelter dogShelter:dogShelterService.getShelter()){
            if (dogShelter.getName().equals(text)){
                return dogShelter.getId();
            }
        }
        return  0l;
    }

  /*  private CatShelter loadCat (Long id){
    return catShelterService.getSheltersId(id);
    }*/

}

