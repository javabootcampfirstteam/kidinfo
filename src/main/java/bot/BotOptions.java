package bot;

public class BotOptions {
    public static final String BOT_NAME = "KininfoTelegramBot";
    public static final String BOT_TOKEN = "667519149:AAH2_KLHbq-fUC4yj01iSPSgj7XohCM10bU";
    private static String PROXY_HOST = "61.19.201.13";
    private static Integer PROXY_PORT = 8080;

    static String getProxy(){
        return PROXY_HOST;
    }

    static Integer getPort(){
        return PROXY_PORT;
    }
}
