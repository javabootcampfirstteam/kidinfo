package service.impl;

import dao.abstr.BotUserDao;
import dao.impl.UserDaoImpl;
import model.BotUser;
import service.abstr.BotUserService;

public class BotUserServiceImpl implements BotUserService {

    private static BotUserServiceImpl instance;

    private BotUserServiceImpl() {
    }

    public static BotUserServiceImpl getInstance(){
        if(instance==null){
            instance = new BotUserServiceImpl();
        }
        return instance;
    }

    private BotUserDao botUserDao = UserDaoImpl.getInstance();

    @Override
    public void AddUser(Integer id, BotUser botUser) {
//        Storage.USERS_TABLE.getId();

    }

    @Override
    public BotUser getUser(Integer id) {
//        return Storage.USERS_TABLE.get(id);
        return null;
    }

    @Override
    public boolean isUserExistById(Integer id){
    return botUserDao.getBotUserById(id) == null;
    }

}
