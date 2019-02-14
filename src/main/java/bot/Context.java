package bot;

import model.BotUser;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.User;

import java.util.List;

public class Context {

 /*   public void onUpdateReceived(Update update) {

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
*/
}
