import bot.Bot;
import bot.Poin;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import static bot.GetPosition.ParseJson;
import static bot.Smsq.roundOut;

public class Main {

	private static String PROXY_HOST = "61.19.201.13";
	private static Integer PROXY_PORT = 8080;

	public static void main(String[] args) throws IOException {
		ParseJson();

		/** Отправка СМС. START */
		// Округление до 3 знаков
		Poin p = ParseJson();
		double latitudeX = p.getLatitude();// 60.11172
//      System.out.println("getLatitude = " + p.getLatitude());
		double latitudeXBetween = 0.002;
		double longitudeX = p.getLongitude();// 30.267897
//      System.out.println("getLongitude = " + p.getLongitude());
		double longitudeBetween = 0.003;
		String pointAdr = p.getPointAdr();
		System.out.println("Адрес = " + p.getPointAdr());
		roundOut(latitudeX, 3);
		roundOut(longitudeX, 3);

		String telFrom = "79213275090";
		String txtFrom = pointAdr;
		String senderFrom = "TEST-SMS";
// TODO Auto-generated method stub
//  sendSms(telFrom, txtFrom, senderFrom);
/** Отправка СМС. FINISH */

		ApiContextInitializer.init();
		TelegramBotsApi botapi = new TelegramBotsApi();

		DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
//        botOptions.setProxyHost(PROXY_HOST);
//        botOptions.setProxyPort(PROXY_PORT);
//        botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

		try {
			botapi.registerBot(new Bot(botOptions));
		} catch (TelegramApiException ex) {
			ex.printStackTrace();
		}
	}

}
