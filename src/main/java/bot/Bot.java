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

            if ("/start".equals(messageFromTelegram)){
                botUserService.addUser(currentUserId, new BotUser(telegramUserName));
                sendMsg(currentChatId, "Привет " + telegramUserName + ", вы впервые у нас, добавляем вас в базу");
                sendMsg(currentChatId, "/reg - регистрация\n/info - информация");

            } else  {





                BotUser currentUser = botUserService.getUser(currentUserId);
                List<String> currentContext = currentUser.getContext();



                if (currentContext.isEmpty()){

                    switch (messageFromTelegram){
                        case "/reg" :{
                            sendMsg(currentChatId, "регстрация");
                            sendMsg(currentChatId, "Введитте имя");
                            setContextToUser(currentUserId,"/reg");
                            break;
                        }
                        case "/info" :{
                            sendMsg(currentChatId, "информация");
                            setContextToUser(currentUserId,"/info");
                            break;
                        }
                        default:{
                            sendMsg(currentChatId,"неизвестная команда");
                        }
                    }



                } else {
                    int contextPosition = 0;
                    switch (currentContext.get(contextPosition++)){
                        case "/reg" :{

                            if (currentContext.size() == contextPosition){
                                //ДОБАВЛЕНИЕ  ИМЕНИ
                            } else {
                                switch (currentContext.get(contextPosition++)){

                                }
                            }
                            break;
                        }
                        case "/info" :{



                            break;
                        }
                        case "/list_activities" :{
                            // проверка роли
                            sendMsg(currentChatId, "список");
                            break;
                        }

                        default:{
                            sendMsg(currentChatId,"неизвестная команда");
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


    private void setContextToUser(Integer userId, String context){
        BotUser currentUser = botUserService.getUser(userId);
        currentUser.getContext().add(context);
    }

    private void removeLastContextElement(Integer userId){
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
