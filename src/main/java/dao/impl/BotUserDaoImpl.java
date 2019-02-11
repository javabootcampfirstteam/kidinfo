package dao.impl;

import dao.abstr.BotUserDao;
import model.BotUser;
import storage.Storage;

public class BotUserDaoImpl implements BotUserDao {
    //Поле класса, в котором хранится объект
    private static BotUserDaoImpl instance;

    public static BotUserDaoImpl getInstance() {
        if (instance == null) {
            instance = new BotUserDaoImpl();
        }
        return instance;
    }

//pattern singlton
    //Приватный конструктор для того, чтоб его нельзя было вызвать извне
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
}
