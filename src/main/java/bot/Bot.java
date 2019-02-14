package bot;

import model.BotEvent;
import model.BotUser;
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

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Bot extends TelegramLongPollingBot {


    //Переменные для модели события
    String eventName;
    String eventType;
    LocalDateTime eventDateTime;
    String eventLocation;
    String eventContact;
    String eventPhone;
    String eventAdditional;
    int eventOwner;


    private Long currentChatId;
    private Integer currentUserId;
    private String telegramUserName;
    String lastMessage = "";


    String telegramUserFirstName;
    int contextPosition = 0;

    //Misha
    private static final String BOT_NAME = "KininfoTelegramBot";
    private static final String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";
    //Stas
//    private static final String BOT_NAME = "cas_to_everyone_bot";
//    private static final String BOT_TOKEN = "666755919:AAEq93Nf-OLJ4r2zjhpUdICue5XAKI2q9Bc";
    //Yuri
//    private static final String BOT_NAME = "balabbolBot";
//    private static final String BOT_TOKEN = "788017408:AAGhd7up3I-F2WhBBD60JXHGAXSn8xDLZ6w";


    BotUserService botUserService = botUserServiceImpl.getInstance();
    BotEventService botEventService = botEventServiceImpl.getInstance();

    public Bot(DefaultBotOptions options) {
        super(options);
    }


    public String getBotToken() {
        return BOT_TOKEN;
    }


    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();


        if (message != null & message.hasText()) {
            String messageFromTelegram = message.getText();
            User userFromTelegram = message.getFrom();
            String telegramUserName = userFromTelegram.getFirstName();
            Integer currentUserId = userFromTelegram.getId();
            Long currentChatId = message.getChatId();

            //Если юзером вводится команда /start
            if ("/start".equals(messageFromTelegram)) {

                if (!botUserService.isUserExistById(currentUserId)) {
                    //Пользователя нет в базе
                    sendMsg(currentChatId, "Привет " + telegramUserName + ", вы впервые у нас, добавляем вас в базу");
                    sendMsg(currentChatId, "/reg - регистрация\n/info - информация\n/addevent - Добавить событие");
                    botUserService.addUser(currentUserId, new BotUser(telegramUserName));
                } else
//                    пользователь есть в базе
                {
                    sendMsg(currentChatId, "Привет " + botUserService.getUser(currentUserId).getName() + ", Мы уже знакомы с вами!");
                }

//Пользователь вводит отличное от /start
            } else {

                BotUser currentUser = botUserService.getUser(currentUserId);
                List<String> currentContext = currentUser.getContext();
//Если контекст пустой
                if (currentContext.isEmpty()) {

                    switch (messageFromTelegram) {
                        case "/reg": {
                            sendMsg(currentChatId, "Регистрация\nВведите имя");
                            setContextToUser(currentUserId, "/reg");

                            break;
                        }
                        case "/info": {
                            sendMsg(currentChatId, "Информация");
                            setContextToUser(currentUserId, "/info");
                            break;
                        }

                        case "/addevent":
                            sendMsg(currentChatId, "Добавить событие\nВведите название события");
                            setContextToUser(currentUserId, messageFromTelegram);
//                            System.out.println("w");
                            break;

                        default: {
                            sendMsg(currentChatId, "Неизвестная команда");
                        }
                    }

//Здесь уже будет непосрдественно обрабаотываться вложенность контекста и добавяление данных в базу.
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
                                            sendMsg(currentChatId, "Ваша роль");
                                            setContextToUser(currentUserId, "/role");
                                            String[] role = {"Админ","Ребенок","Родитель"};
                                            createButtons(message, role, "Ваша роль");
                                        } else {
                                            switch (currentContext.get(contextPosition++)) {
                                                case "/role":
                                                    currentUser.setRole(messageFromTelegram);
                                                    sendMsg(currentChatId, "Регистрация окончена");
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
                                                                    BotEvent botEvent = new BotEvent(eventName, eventType, eventDateTime, eventContact);
                                                                    botEventService.addEvent(botEvent);
                                                                    sendMsg(currentChatId, "Событие добавлено!\n/reg - регистрация\n/info - информация\n/addevent - Добавить событие");
                                                                    currentContext.clear();
//                                                                    System.out.println("sss");
//                                                                    setContextToUser(currentUserId, "/start");
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
                        case "/list_activities": {
                            // проверка роли
                            sendMsg(currentChatId, "список");
                            break;
                        }

                        default: {
                            sendMsg(currentChatId, "неизвестная команда");
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

}
