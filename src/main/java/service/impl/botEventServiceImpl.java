package service.impl;


import dao.abstr.BotEventDao;
import dao.impl.BotEventDaoImpl;
import model.BotEvent;
import service.abstr.BotEventService;

public class botEventServiceImpl implements BotEventService {

    private static botEventServiceImpl instance;

    private botEventServiceImpl() {
    }

    public static botEventServiceImpl getInstance() {
        if (instance == null) {
            instance = new botEventServiceImpl();
        }
        return instance;
    }

    private BotEventDao botEventDao = BotEventDaoImpl.getInstance();

    @Override
    public void addEvent(BotEvent botEvent) {
        botEventDao.addBotEvent(botEvent);
    }

    @Override
    public BotEvent getEvent(Integer id) {
        return botEventDao.getBotEventById(id);
    }

    @Override
    public boolean isEventExistById(Integer id) {
        return botEventDao.getBotEventById(id) != null;
    }

    @Override
    public boolean isEventExists() {
        return botEventDao.isEventExist();
    }
}
