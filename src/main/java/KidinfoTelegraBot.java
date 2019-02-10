import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;


public class KidinfoTelegraBot extends TelegramLongPollingBot {

    private static String BOT_NAME = "KininfoTelegramBot";
    private static String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";

    public KidinfoTelegraBot(DefaultBotOptions options) {
        super(options);
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }

    public void onUpdateReceived(Update update) {
        
    }

    public String getBotUsername() {
        return BOT_NAME;
    }
}
