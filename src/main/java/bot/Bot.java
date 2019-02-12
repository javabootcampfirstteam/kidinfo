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

    private static String BOT_NAME = "KininfoTelegramBot";
    private static String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";


    //Создаем экземпляр класса для UserService
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
            String textMessage = message.getText();
            User user = message.getFrom();
            String userName = user.getFirstName();
            Integer id = user.getId();

            botUserService.setContext(textMessage);

            switch (textMessage) {
                case "/start":
                    if (!botUserService.isUserExistById(id)) {
                        botUserService.addUser(id, new BotUser(userName));
                        sendMsg(message, "Привет " + userName + ", вы впервые у нас, добавляем вас в базу");
//                        setButton(message);
                    } else {
                        sendMsg(message, "Привет " + userName + ", я тебя знаю.");
                    }

                    break;
                case "/info":
//                    sendMsg(message,"Информация");
                    break;
                case "/reg":
//                    sendMsg(message,"Регистрация");

                    break;

                case "/pwd":
                    sendMsg(message,botUserService.getContext());
                    break;
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


    private void sendMsg(Message message, String txtMessage) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(message.getChatId());
//        sendMessage.setReplyToMessageId(message.getMessageId());
        sendMessage.setText(txtMessage);

        try {
//            setButton(sendMessage);
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
