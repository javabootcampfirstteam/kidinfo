package bot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import model.BotEvent;
import model.BotUser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.abstr.BotEventService;
import service.abstr.BotUserService;
import service.impl.botEventServiceImpl;
import service.impl.botUserServiceImpl;
import storage.Storage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class Bot extends TelegramLongPollingBot {


    //Переменные для модели события
    String eventName;
    String eventType;
    LocalDateTime eventDateTime;
    Point eventLocation;
    String eventContact;

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMM yy HH:mm");


		String telFrom = "79213275090";
		String senderFrom = "TEST-SMS";

    //Misha
    private static final String BOT_NAME = "KininfoTelegramBot";
    private static final String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";
//	Stas
//	private static final String BOT_NAME = "cas_to_everyone_bot";
//	private static final String BOT_TOKEN = "666755919:AAEq93Nf-OLJ4r2zjhpUdICue5XAKI2q9Bc";
    //Yuri
//    private static final String BOT_NAME = "balabbolBot";
//    private static final String BOT_TOKEN = "788017408:AAGhd7up3I-F2WhBBD60JXHGAXSn8xDLZ6w";

//    BotUserService botUserService = botUserServiceImpl.getInstance();
//    BotEventService botEventService = botEventServiceImpl.getInstance();

    public Bot(DefaultBotOptions options) {
        super(options);
    }


    public String getBotToken() {
        return BOT_TOKEN;
    }


    public void onUpdateReceived(Update update) {
        String startMessage = "/reg - регистрация\n/info - информация\n/addevent - Добавить событие\n" +
                "/listevents - Список мероприятий\n/listmyevents - Список моих мероприятий";
        Message message = update.getMessage();
        String messageFromTelegram = message.getText();
        User userFromTelegram = message.getFrom();
        String telegramUserName = userFromTelegram.getFirstName();
        Integer currentUserId = userFromTelegram.getId();
        Long currentChatId = message.getChatId();


        if(message.getLocation()!=null){
            double currentLatitude = message.getLocation().getLatitude();
            double currentLongitude = message.getLocation().getLongitude();
//            botUserService.getUser(currentUserId).getLocation();
            if(!botUserService.getUser(currentUserId).getMyEvents().isEmpty()){
                List<Integer> myEvents = botUserService.getUser(currentUserId).getMyEvents();
                for(Integer i:myEvents){
                    double eventLatitude = botEventService.getEvent(i).getEventLocation().getLatitude();
                    double eventLongitude = botEventService.getEvent(i).getEventLocation().getLongitude();
                    if(((eventLatitude-0.002)<currentLatitude & currentLatitude<(eventLatitude+0.002))
                            &
                            ((eventLongitude-0.003)<currentLongitude & currentLongitude<(currentLongitude+0.003))){
                        Smsq.sendSms(telFrom,botUserService.getUser(currentUserId).getName() + " " + botUserService.getUser(currentUserId).getSurname()
                                + " находится на " + botEventService.getEvent(i).getEventName(),senderFrom);
                    }
//                                for(Integer i: arr){
//                    BotEvent currentEvent = botEventService.getEvent(i);
//                    sendMsg(currentChatId,"Мои события\n" + currentEvent.getEventName() +
//                            " - " +
//                            currentEvent.getEventDateTime().format(formatter) + "\n" + currentEvent.getEventLocation().getPointAdr() + "\n---\n" );
                }
            }sendMsg(currentChatId, startMessage);

        }

        if (message != null & message.hasText()) {

            //Если юзером вводится команда /start
            if ("/start".equals(messageFromTelegram)) {

                if (!botUserService.isUserExistById(currentUserId)) {
                    //Пользователя нет в базе
//                    sendMsg(currentChatId, "Привет " + telegramUserName + ", вы впервые у нас, добавляем вас в базу");
                    sendMsg(currentChatId, startMessage);
                    botUserService.addUser(currentUserId, new BotUser(telegramUserName));
                } else
//                    пользователь есть в базе
                {
                    sendMsg(currentChatId, startMessage);
//                    sendMsg(currentChatId, "Привет " + botUserService.getUser(currentUserId).getName() + ", Мы уже знакомы с вами!");
                }

//Пользователь вводит отличное от /start
            } else {

                BotUser currentUser = botUserService.getUser(currentUserId);
                List<String> currentContext = currentUser.getContext();
//Если контекст пустой
                if (currentContext.isEmpty()) {

                    switch (messageFromTelegram) {
                        case "/reg": {
                            if (botUserService.getUser(currentUserId).getRole() == null) {
                                sendMsg(currentChatId, "Регистрация\nВведите имя");
                                setContextToUser(currentUserId, "/reg");
                            } else {
                                sendMsg(currentChatId, "Уже зарегистрирован");
                                sendMsg(currentChatId, startMessage);
                            }
                            break;
                        }
                        case "/info": {
                            sendMsg(currentChatId, "Информация");
                            sendMsg(currentChatId, startMessage);
                            break;
                        }

                        case "/addevent":
                            if (botUserService.getUser(currentUserId).getRole() != null) {
                                sendMsg(currentChatId, "Добавить событие\nВведите название события");
                                setContextToUser(currentUserId, messageFromTelegram);
                            } else {
                                sendMsg(currentChatId, "Не соответствующая роль\n" + startMessage);
                            }

                            break;
                        case "/listevents":
                            if (!botEventService.isEventExists()) {
                                sendMsg(currentChatId, "Выберите мероприятие для добавление в \"Мои мероприятия\"");
                                for (int i : Storage.EVENTS_TABLE.keySet()) {
                                    sendMsg(currentChatId, "/addtomyevent" + i + " - " + Storage.EVENTS_TABLE.get(i).getEventName() + ": " + Storage.EVENTS_TABLE.get(i).getEventDateTime().format(formatter) + "\n" +
                                            Storage.EVENTS_TABLE.get(i).getEventType() + "\n" + Storage.EVENTS_TABLE.get(i).getEventContact() + "\n" + Storage.EVENTS_TABLE.get(i).getEventLocation().getPointAdr() + "\n---\n");
                                    setContextToUser(currentUserId, "/addtomyevent");
                                }
                            } else {
                                sendMsg(currentChatId, startMessage);
                            }
                            break;

                        case "/listmyevents":
                            if(!botUserService.getUser(currentUserId).getMyEvents().isEmpty()){
                                List<Integer> myEvents = botUserService.getUser(currentUserId).getMyEvents();
//                                Integer [] arr = new Integer[botUserService.getUser(currentUserId).getMyEvents().size()];
                                for(Integer i:myEvents){
//                                for(Integer i: arr){
                                    BotEvent currentEvent = botEventService.getEvent(i);
                                    sendMsg(currentChatId,"Мои события\n" + currentEvent.getEventName() +
                                            " - " +
                                            currentEvent.getEventDateTime().format(formatter) + "\n" + currentEvent.getEventLocation().getPointAdr() + "\n---\n" );
                                }
                            }sendMsg(currentChatId, startMessage);
                            break;
                        default: {
                            sendMsg(currentChatId, "Неизвестная команда");
                        }
                    }

//Здесь уже будет непосрдественно обрабаотываться вложенность контекста и добавление данных в базу.
                } else {

                    if (messageFromTelegram.contains("/addtomyevent")) {
//                        ArrayList<Integer> myEvents = botEventService.getEvent(Integer.parseInt(messageFromTelegram)).getMyEvents();
//                        myEvents.add(Integer.parseInt(messageFromTelegram.substring(11,messageFromTelegram.length()-1)));
                        botUserService.getUser(currentUserId).setMyEvents(Integer.parseInt(messageFromTelegram.substring(13, messageFromTelegram.length())));
                        sendMsg(currentChatId, startMessage);
                        currentContext.clear();

                    } else {

                        int contextPosition = 0;
                        switch (currentContext.get(contextPosition++)) {
//                        Основной процесс регистрации
                            case "/reg": {
                                if (currentContext.size() == contextPosition) {
                                    currentUser.setName(messageFromTelegram);
                                    sendMsg(currentChatId, "Введите фамилию");
                                    setContextToUser(currentUserId, "/surname");
                                } else {
                                    switch (currentContext.get(contextPosition++)) {
                                        case "/surname":
                                            if (currentContext.size() == contextPosition) {
                                                currentUser.setSurname(messageFromTelegram);
//                                                sendMsg(currentChatId, "Ваша роль");
                                                setContextToUser(currentUserId, "/role");
                                                String[] role = {"Админ", "Ребенок", "Родитель"};
                                                createButtons(message, role, "Ваша роль");
                                            } else {
                                                switch (currentContext.get(contextPosition++)) {
                                                    case "/role":
                                                        currentUser.setRole(messageFromTelegram);
                                                        sendMsg(currentChatId, startMessage);
                                                        currentContext.clear();
                                                        break;
                                                }
                                            }
                                            break;
                                    }
                                }
                                break;
                            }
//Основной процесс добавления ивента
                            case "/addevent":
                                if (currentContext.size() == contextPosition) {
                                    eventName = messageFromTelegram;
                                    setContextToUser(currentUserId, "/addeventtype");
                                    String[] eventType = {"Мастер-класс", "Соревнование"};
                                    createButtons(message, eventType, "Тип события:");
                                } else {
                                    switch (currentContext.get(contextPosition++)) {
                                        case "/addeventtype":
                                            if (currentContext.size() == contextPosition) {
                                                eventType = messageFromTelegram;
                                                sendMsg(currentChatId, "Дата мероприятия в формате ДД.ММ.ГГ ЧЧ:ММ");
                                                setContextToUser(currentUserId, "/eventdate");
                                            } else {
                                                switch (currentContext.get(contextPosition++)) {
                                                    case "/eventdate":
                                                        if (currentContext.size() == contextPosition) {
                                                            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yy HH:mm");
                                                            eventDateTime = LocalDateTime.parse(messageFromTelegram, formatter);
                                                            sendMsg(currentChatId, "Контакт администратора");
                                                            setContextToUser(currentUserId, "/eventcontact");
                                                        } else {
                                                            switch (currentContext.get(contextPosition++)) {
                                                                case "/eventcontact":
                                                                    if (currentContext.size() == contextPosition) {
                                                                        eventContact = messageFromTelegram;
//																	BotEvent botEvent = new BotEvent(eventName, eventType, eventDateTime, eventContact);
//																	botEventService.addEvent(botEvent);
                                                                        sendMsg(currentChatId, "Адрес:");
                                                                        setContextToUser(currentUserId, "/eventlocation");
                                                                    } else {
                                                                        switch (currentContext.get(contextPosition++)) {
                                                                            case "/eventlocation":
                                                                                if (currentContext.size() == contextPosition) {
                                                                                    try {
                                                                                        eventLocation = getPointByAdres(messageFromTelegram);
                                                                                    } catch (IOException e) {
                                                                                        e.printStackTrace();
                                                                                    }
                                                                                    BotEvent botEvent = new BotEvent(eventName, eventType, eventDateTime, eventContact, eventLocation);
                                                                                    botEventService.addEvent(botEvent);
                                                                                    sendMsg(currentChatId, startMessage);
                                                                                    currentContext.clear();
                                                                                }
                                                                        }
                                                                    }
                                                            }
                                                        }
                                                }
                                            }
                                    }
                                }
                                break;

                            case "/info": {


                                break;
                            }
                            case "/listevents": {
                                // проверка роли
                                sendMsg(currentChatId, "список");
                                break;
                            }

                            default: {
                                sendMsg(currentChatId, "неизвестная команда");
                                sendMsg(currentChatId, startMessage);
                            }
                        }
                    }
                }


            }


        }

    }

    public String getBotUsername() {
        return BOT_NAME;
    }

    private void setContextToUser(Integer userId, String context) {
        BotUser currentUser = botUserService.getUser(userId);
        currentUser.getContext().add(context);
    }

    private void removeLastContextElement(Integer userId) {
        BotUser currentUser = botUserService.getUser(userId);
        currentUser.getContext().remove(currentUser.getContext().size());
    }


    private void sendMsg(Long chatId, String txtMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(txtMessage);


        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    private void createButtons(Message message, String[] buttons, String textMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.enableMarkdown(true);

        // Создаем клавиуатуру
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);

        // Создаем список строк клавиатуры
        List<KeyboardRow> keyboard = new ArrayList<>();

        // Первая строчка клавиатуры
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        // Добавляем кнопки в первую строчку клавиатуры
        for (String i : buttons) {
            keyboardFirstRow.add(i);
        }
        // Вторая строчка клавиатуры
//        KeyboardRow keyboardSecondRow = new KeyboardRow();
        // Добавляем кнопки во вторую строчку клавиатуры
//        keyboardSecondRow.add("Команда 3");
//        keyboardSecondRow.add("Команда 4");

        // Добавляем все строчки клавиатуры в список
        keyboard.add(keyboardFirstRow);
//        keyboard.add(keyboardSecondRow);
        // и устанваливаем этот список нашей клавиатуре
        replyKeyboardMarkup.setKeyboard(keyboard);

        sendMessage.setChatId(message.getChatId().toString());
        sendMessage.setText(textMessage);

        try {
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }

    }

    /*private void initKeyboard(ArrayList<KeyboardRow> keyboard, KeyboardRow keyboardFirstRow, KeyboardRow keyboardSecondRow) {
        keyboard.clear();
        keyboardFirstRow.clear();
        keyboardFirstRow.add("События\uD83D\uDE18\uD83D\uDE04");
        keyboardFirstRow.add("Регион оповещения");
        keyboardSecondRow.add("Главное Меню⬆️");
        keyboard.add(keyboardFirstRow);
        keyboard.add(keyboardSecondRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
    }*/


    public static Point getPointByAdres(String adres) throws IOException {
        OkHttpClient client = new OkHttpClient();
        String googleApi = "https://maps.googleapis.com/maps/api/geocode/json?address=";
        String googleKey = "&key=AIzaSyBuS8ozqw3pNqh2qTcGonzbrWka_8TCWHE";
        String url = googleApi + adres + googleKey;
        Request request = new Request.Builder()
                .url(url)
                .build();
        Response response = client.newCall(request).execute();
        String json = response.body().string();
        JsonObject jelement = (JsonObject) new JsonParser().parse(json);
        JsonObject point = (JsonObject) ((JsonObject) (jelement).get("results").getAsJsonArray().get(0)).getAsJsonObject("geometry").get("location");
        JsonObject pointAdr = (JsonObject) ((JsonObject) (jelement).get("results").getAsJsonArray().get(0));
        double lat = point.get("lat").getAsDouble();
        double lng = point.get("lng").getAsDouble();
        String adr = pointAdr.get("formatted_address").getAsString();
        Point poin = new Point();
        poin.setLatitude(lat);
        poin.setLongitude(lng);
        poin.setPointAdr(adr);
        return poin;
    }

}
