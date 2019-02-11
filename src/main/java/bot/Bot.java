package bot;

import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


public class Bot extends TelegramLongPollingBot {

    private static String BOT_NAME = "KininfoTelegramBot";
    private static String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";

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


        switch (textMessage) {
            case "/start":
            sendMsg(message,"Hello " + userName);
                break;

            case "/reg":

                //usersMap.put()
                break;

            default:
            sendMsg(message,"What do you mean " + userName + "?");
                break;

        }
    }

    public String getBotUsername() {
        return BOT_NAME;
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
