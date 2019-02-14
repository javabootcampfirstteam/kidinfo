package service.impl;

import dao.abstr.BotUserDao;
import dao.impl.BotUserDaoImpl;
import model.BotUser;
import service.abstr.BotUserService;

public class botUserServiceImpl implements BotUserService {

    private static botUserServiceImpl instance;

    private botUserServiceImpl() {
    }

    public static botUserServiceImpl getInstance(){
        if(instance==null){
            instance = new botUserServiceImpl();
        }
        return instance;
    }

    private BotUserDao botUserDao = BotUserDaoImpl.getInstance();

    @Override
    public void addUser(Integer id, BotUser botUser) {
        botUserDao.addBotUser(id,botUser);

    }

    @Override
    public BotUser getUser(Integer id) {
        return botUserDao.getBotUserById(id);
    }

    @Override
    public boolean isUserExistById(Integer id){
    return botUserDao.getBotUserById(id) != null;
    }

//    @Override
//    public void updateUser(Integer id, BotUser botUser) {
//        botUserDao.upda
//    }
}
