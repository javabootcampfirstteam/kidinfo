package dao.abstr;

import model.BotEvent;

public interface BotEventDao {

    BotEvent getBotEventById(int id);
    void addBotEvent(int id, BotEvent botEvent);
    BotEvent[] getAllEvent();

}
