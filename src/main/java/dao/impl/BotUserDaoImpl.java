package dao.impl;

import dao.abstr.BotUserDao;
import model.BotUser;
import storage.Storage;

public class BotUserDaoImpl implements BotUserDao {
    private static BotUserDaoImpl instance;

    public static BotUserDaoImpl getInstance() {
        if (instance == null) {
            instance = new BotUserDaoImpl();
        }
        return instance;
    }

    private BotUserDaoImpl() {
    }

    @Override
    public BotUser getBotUserById(int id) {
        BotUser botUser = Storage.USERS_TABLE.get(id);
        return botUser;
    }

    @Override
    public void addBotUser(int id, BotUser botUser) {
        Storage.USERS_TABLE.put(id,botUser);
    }

    @Override
    public BotUser[] getAllUsers() {
        return null;
    }

    @Override
    public void updateBotUser(int id, BotUser botUser) {
        Storage.USERS_TABLE.entrySet();
    }
}
