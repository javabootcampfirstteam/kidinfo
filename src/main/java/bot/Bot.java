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
import service.abstr.BotEventService;
import service.abstr.BotUserService;
import service.impl.botEventServiceImpl;
import service.impl.botUserServiceImpl;

import java.time.LocalDateTime;
import java.util.ArrayList;
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
        //update.getUpdateId();

        String eventName;
        String eventType;
        LocalDateTime eventDateTime;
        String eventLocation;
        String eventContact;
        String eventPhone;
        String eventAdditional;
        int eventOwner;

        //Получаем сообщение из апдейта
        Message message = update.getMessage();
        if (message != null & message.hasText()) {
            //Получаем текст сообщения
            String messageFromTelegram = message.getText();
            //Получаем телеграмовского юзера
            User userFromTelegram = message.getFrom();
            //Получаем Имя юзера из телеграма
            telegramUserName = userFromTelegram.getFirstName();
            //Поучаем текущий userId
            currentUserId = userFromTelegram.getId();
            //Получаем текущий chatId
            currentChatId = message.getChatId();
            String currenMessage = null;


            if (!botUserService.isUserExistById(currentUserId)) {
                botUserService.addUser(currentUserId, new BotUser(telegramUserName));
            }

            BotUser currentUser = botUserService.getUser(currentUserId);

            List<String> currentContext = currentUser.getContext();

            if (!currentContext.isEmpty()) {

                switch (currentContext.get(contextPosition)) {
                    case "regEvent":
//                        if (currentContext.size() == contextPosition) {
                        eventLocation = messageFromTelegram;
                        setContextToUser(currentUserId, "nameEvent");
                        currenMessage = "Введите название мероприятия";
                        contextPosition++;
//                        }
                        break;
                    case "nameEvent":
                        eventName = messageFromTelegram;

                        break;
                }
            }


            SendMessage sendMessage = new SendMessage().setChatId(currentChatId);
            sendMessage.setReplyMarkup(replyKeyboardMarkup);
            sendMessage.setText(getMessage(messageFromTelegram, currenMessage));


            try {
                execute(sendMessage);
            } catch (TelegramApiException e) {
                e.printStackTrace();
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

    public String getMessage(String msg, String returnMessage) {

        ArrayList<KeyboardRow> keyboard = new ArrayList<>();
        KeyboardRow keyboardFirstRow = new KeyboardRow();
        KeyboardRow keyboardSecondRow = new KeyboardRow();

        replyKeyboardMarkup.setSelective(true);
        replyKeyboardMarkup.setResizeKeyboard(true);
        replyKeyboardMarkup.setOneTimeKeyboard(false);

        if (msg.equals("/start")) {


//            BotUser currentUser = botUserService.getUser(currentUserId);
            setContextToUser(currentUserId, msg);
//            List<String> currentContext = currentUser.getContext();


            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("События\uD83D\uDE18\uD83D\uDE04");
            keyboardFirstRow.add("Доб.Событие");
            keyboardSecondRow.add("Обратная Связь\uD83E\uDD19");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return "Здравствуйте " + telegramUserFirstName + ", Выберите интересующий Вас раздел";
        }

        if (msg.equals("События\uD83D\uDE18\uD83D\uDE04")) {
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

        if (msg.equals("Доб.Событие")) {
            if (!botUserService.isUserExistById(currentUserId)) {
                botUserService.addUser(currentUserId, new BotUser(telegramUserName));
            }
            keyboard.clear();
            keyboardFirstRow.clear();
//            keyboardFirstRow.add("11");
//            keyboardFirstRow.add("22");
            keyboardSecondRow.add("Главное Меню⬆️");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            setContextToUser(currentUserId, "regEvent");
            return "Выберите район\n" +
                    "1-Петроградский\n" +
                    "2-Василеостровский\n";
        }

        //--------------- THE MOST LAST MENU ---------------//

        if (msg.equals("Главное Меню⬆️")) {

            keyboard.clear();
            keyboardFirstRow.clear();
            keyboardFirstRow.add("События\uD83D\uDE18\uD83D\uDE04");
            keyboardFirstRow.add("Доб.Событие");
            keyboardSecondRow.add("Обратная Связь\uD83E\uDD19");
            keyboard.add(keyboardFirstRow);
            keyboard.add(keyboardSecondRow);
            replyKeyboardMarkup.setKeyboard(keyboard);
            return telegramUserFirstName + ", Выберите интересующий Вас раздел";
        }

//        return "Спасибо что выбрали нашу Авиакомпанию, всего хорошего Вам " + telegramUserFirstName;
        return returnMessage;
    }
}
