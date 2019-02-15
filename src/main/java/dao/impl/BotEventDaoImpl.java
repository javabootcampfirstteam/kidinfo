package dao.impl;

import dao.abstr.BotEventDao;
import model.BotEvent;
import storage.Storage;

public class BotEventDaoImpl implements BotEventDao {

    private static BotEventDaoImpl instance;

    public static BotEventDaoImpl getInstance() {
        if (instance == null) {
            instance = new BotEventDaoImpl();
        }
        return instance;
    }

    @Override
    public BotEvent getBotEventById(int id) {
        return Storage.EVENTS_TABLE.get(id);
    }

    @Override
    public void addBotEvent(BotEvent botEvent) {
        Storage.EVENTS_TABLE.put(Storage.eventIndex++,botEvent);
    }

    @Override
    public BotEvent[] getAllEvent() {
//        return Storage.EVENTS_TABLE.values();
        return null;
    }

    @Override
    public boolean isEventExist() {
        return Storage.EVENTS_TABLE.isEmpty();
    }

}
