package bot;

public class BotOptions {
    private static String BOT_NAME = "KininfoTelegramBot";
    private static String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";
    private static String PROXY_HOST = "61.19.201.13";
    private static Integer PROXY_PORT = 8080;

    static String getToken(){
        return BOT_TOKEN;
    }

    static String getName(){
        return BOT_NAME;
    }

    static String getProxy(){
        return PROXY_HOST;
    }

    static Integer getPort(){
        return PROXY_PORT;
    }
}
