package bot;

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
import service.abstr.BotUserService;
import service.impl.BotUserServiceImpl;

import java.util.ArrayList;
import java.util.List;


public class Bot extends TelegramLongPollingBot {

    private static String BOT_NAME = "KininfoTelegramBot";
    private static String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";


    BotUserService bus = BotUserServiceImpl.getInstance();


    public Bot(DefaultBotOptions options) {
        super(options);
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void onUpdateReceived(Update update) {

        Message message = update.getMessage();
        String textMessage = message.getText();
        User user = message.getFrom();
        String userName = user.getFirstName();
        Integer id = user.getId();

        switch (textMessage) {
            case "/start":
                if (!bus.isUserExistById(id)) {
                    bus.addUser(id, new BotUser(userName));
                    sendMsg(message, "Привет " + userName + ", вы впервые у нас, добавляем вас в базу");

                } else {
                    sendMsg(message, "Привет " + userName + ", я тебя знаю.");
                }

                break;
            case "/listallusers":
//                    bus.
                break;
            case "/removebuttons":

                break;

        }


    }

    public String getBotUsername() {
        return BOT_NAME;
    }


    private void setButton() {
        ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup();
        List<KeyboardRow> keyboard = new ArrayList<>();

    }


    private void remButton() {
        ReplyKeyboardMarkup rkm = new ReplyKeyboardMarkup();
        //        s.setReplyMarkup(rkm);
//
        List<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardButtons = new KeyboardRow();
//        keyboardButtons.add("Регистрация");
//        keyboardButtons.add("Инфо");
//        keyboard.add(keyboardButtons);

//        rkm.setKeyboard(keyboard);
//        s.enableMarkdown(true);

        keyboardButtons.removeAll(keyboard);

    }

    private void sendMsg(Message outMessage, String txtMessage) {
        SendMessage s = new SendMessage();
        s.setChatId(outMessage.getChatId());
        s.setText(txtMessage);

        try {
            execute(s);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }
}
