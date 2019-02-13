package bot;

import model.BotUser;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.ReplyKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardButton;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.KeyboardRow;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import service.abstr.BotUserService;
import service.impl.BotUserServiceImpl;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private static String BOT_NAME = BotOptions.BOT_NAME;
    private static String BOT_TOKEN = BotOptions.BOT_TOKEN;


    BotUserService botUserService = BotUserServiceImpl.getInstance();

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
                                switch (currentContext.get(contextPosition++)){
                                    case "/surname":
                                        if (currentContext.size() == contextPosition) {
                                            currentUser.setSurname(messageFromTelegram);
                                            sendMsg(currentChatId, "Ваш район проживания");
                                            setContextToUser(currentUserId, "/location");
                                        } else {
                                            switch (currentContext.get(contextPosition++)){
                                                case "/location":
                                                    currentUser.setSurname(messageFromTelegram);
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

    public void setButton(SendMessage sendMessage) {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(true);
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardRow = new KeyboardRow();
        keyboardRow.add(new KeyboardButton("/reg"));
        keyboardRow.add(new KeyboardButton("/info"));
        keyboardRow.add(new KeyboardButton("/start"));
        keyboard.add(keyboardRow);
        replyKeyboardMarkup.setKeyboard(keyboard);
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
//            setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
