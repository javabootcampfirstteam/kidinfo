package service.abstr;

import model.BotEvent;

public interface BotEventService {
    void addEvent(BotEvent botEvent);
    BotEvent getEvent(Integer id);
    boolean isEventExistById(Integer id);
    boolean isEventExists();
}
