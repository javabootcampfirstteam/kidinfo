package bot;

import model.BotUser;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.abstr.BotEventService;
import service.abstr.BotUserService;
import service.impl.botEventServiceImpl;
import service.impl.botUserServiceImpl;

import java.util.List;

public class Bot extends TelegramLongPollingBot {


    private Long currentChatId;
    private Integer currentUserId;
    private String telegramUserName;
    String lastMessage = "";
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // create keyboard
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
                    sendMsg(currentChatId, "/reg - регистрация\n/info - информация");
                    botUserService.addUser(currentUserId, new BotUser(telegramUserName));
                    sendMsg(currentChatId, "Привет " + telegramUserName + ", вы впервые у нас, добавляем вас в базу");
                } else
//                    пользователь есть в базе
                {
                    sendMsg(currentChatId, "Привет " + botUserService.getUser(currentUserId).getName() + ", Мы уже знакомы с вами!");
                }

//Пользователь вводит отличное от /start
            } else {

                BotUser currentUser = botUserService.getUser(currentUserId);
                List<String> currentContext = currentUser.getContext();

                if (currentContext.isEmpty()) {

                    switch (messageFromTelegram) {
                        case "/reg": {
                            sendMsg(currentChatId, "Регистрация");
                            sendMsg(currentChatId, "Введитте имя");
                            setContextToUser(currentUserId, "/reg");

                            break;
                        }
                        case "/info": {
                            sendMsg(currentChatId, "информация");
                            setContextToUser(currentUserId, "/info");
                            break;
                        }
                        default: {
                            sendMsg(currentChatId, "неизвестная команда");
                        }
                    }


                } else {
                    int contextPosition = 0;
                    switch (currentContext.get(contextPosition++)) {
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
                                            sendMsg(currentChatId, "Ваш район проживания");
                                            setContextToUser(currentUserId, "/location");
                                        } else {
                                            switch (currentContext.get(contextPosition++)) {
                                                case "/location":
                                                    currentUser.setLocation(messageFromTelegram);
                                                    sendMsg(currentChatId, "Регистрация окончена");
                                                    break;
                                            }
                                        }
                                        break;
                                }
                            }
                            break;
                        }
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

}
