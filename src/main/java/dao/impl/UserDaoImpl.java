package dao.impl;

import dao.abstr.BotUserDao;
import model.BotUser;
import storage.Storage;

public class UserDaoImpl implements BotUserDao {
    //Поле класса, в котором хранится объект
    private static UserDaoImpl instance;

    public static UserDaoImpl getInstance() {
        if (instance == null) {
            instance = new UserDaoImpl();
        }
        return instance;
    }

//pattern singlton
    //Приватный конструктор для того, чтоб его нельзя было вызвать извне
    private UserDaoImpl() {
    }

    @Override
    public BotUser getBotUserById(int id) {
//        BotUser botUser = Storage,USERS_TABLE.getId();
//        return botUser;
    return null;
    }

    @Override
    public void addBotUser(int id, BotUser botUser) {
//        Storage.USERS_TABLE.put(id,botUser)
    }


}
