import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

public class kidinfoTelegraBot extends TelegramLongPollingBot {

    public static void main(String[] args) {
        ApiContextInitializer.init();
        TelegramBotsApi botapi = new TelegramBotsApi();
        try {
            botapi.registerBot(new Bot);
        } catch (TelegramApiException ex) {
            ex.printStackTrace();
        }
    }

    public String getBotToken() {
        return "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";
    }

    public void onUpdateReceived(Update update) {

    }

    public String getBotUsername() {
        return "USER";
    }
}
