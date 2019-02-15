package dao.abstr;

import model.BotEvent;

public interface BotEventDao {

    BotEvent getBotEventById(int id);
    void addBotEvent(BotEvent botEvent);
    BotEvent[] getAllEvent();
    int countEvents();

}
