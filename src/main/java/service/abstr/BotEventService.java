package service.abstr;

import model.BotEvent;

public interface BotEventService {
    void addEvent(Integer id, BotEvent botEvent);
    BotEvent getEvent(Integer id);
    boolean isUserExistById(Integer id);
}
