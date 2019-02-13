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


    private long chat_id;
    String lastMessage = "";
    ReplyKeyboardMarkup replyKeyboardMarkup = new ReplyKeyboardMarkup(); // create keyboard
    String userFirstName;


    //Misha
//    private static final String BOT_NAME = "KininfoTelegramBot";
//    private static final String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";

    //Stas
//    private static final String BOT_NAME = “cas_to_everyone_bot”;
//    private static final String BOT_TOKEN = “666755919:AAEq93Nf-OLJ4r2zjhpUdICue5XAKI2q9Bc”;

    //Yuri
//    private static final String BOT_NAME = “cas_to_everyone_bot”;
//    private static final String BOT_TOKEN = “666755919:AAEq93Nf-OLJ4r2zjhpUdICue5XAKI2q9Bc”;


    BotUserService botUserService = BotUserServiceImpl.getInstance();

    public Bot(DefaultBotOptions options) {
        super(options);
    }

    public String getBotToken() {
        return BOT_TOKEN;
    }


    public void onUpdateReceived(Update update) {
        //update.getUpdateId();
        SendMessage sendMessage = new SendMessage().setChatId(update.getMessage().getChatId());
        chat_id = update.getMessage().getChatId();
        String text = update.getMessage().getText();
        sendMessage.setReplyMarkup(replyKeyboardMarkup);
        Message message = update.getMessage();
        String telegramUserName = message.getFrom().getFirstName();
        this.userFirstName = telegramUserName;

        try {
            sendMessage.setText(getMessage(text));
            execute(sendMessage);
        } catch (TelegramApiException e) {
            e.printStackTrace();
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

    public String getMessage(String msg) {

        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if(msg.equals("/start")) {
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("События\uD83D\uDE18\uD83D\uDE04");
            keyboardFirstRow.add("Доб.Событие");
            keyboardSecondRow.add("Обратная Связь\uD83E\uDD19");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Здравствуйте " + userFirstName + ", Выберите интересующий Вас раздел";
        }

        if(msg.equals("События\uD83D\uDE18\uD83D\uDE04")) {
            keyboard.clear();
            keyboardFirstRow.clear();
            //keyboardFirstRow.add("1-");
            //keyboardFirstRow.add("2-");
            keyboardSecondRow.add("Главное Меню⬆️");
            //keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Актуальные мероприятия неподалеку от Вас: ";
        }
        //____________________________________________________

        if(msg.equals("Доб.Событие")) {
            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("11");
            keyboardFirstRow.add("22");
            keyboardSecondRow.add("Главное Меню⬆️");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Выберите";
        }

        //--------------- THE MOST LAST MENU ---------------//

        if(msg.equals("Главное Меню⬆️")) {

            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("События\uD83D\uDE18\uD83D\uDE04");
            keyboardFirstRow.add("Доб.Событие");
            keyboardSecondRow.add("Обратная Связь\uD83E\uDD19");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return userFirstName + ", Выберите интересующий Вас раздел";
        }

        return "Спасибо что выбрали нашу Авиакомпанию, всего хорошего Вам " + userFirstName;
    }
}
