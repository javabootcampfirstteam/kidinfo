import bot.Bot;
import bot.Poin;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.telegram.telegrambots.ApiContextInitializer;
import org.telegram.telegrambots.bots.DefaultBotOptions;
import org.telegram.telegrambots.meta.ApiContext;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.io.IOException;

import static bot.Smsq.roundOut;

public class Main {
	private static String PROXY_HOST = "61.19.201.13";
	private static Integer PROXY_PORT = 8080;

	public static void main(String[] args) throws IOException {

		/** Отправка СМС. START */
		// Округление до 3 знаков
		String text = "Санкт-Петербург Юкковское шоссе 17";
		Poin p = getPointByAdres(text);
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
	/** Вот здесь!!! Отправка SMS */
//	sendSms(telFrom, txtFrom, senderFrom);
/** Отправка СМС. FINISH */

		ApiContextInitializer.init();
		TelegramBotsApi botapi = new TelegramBotsApi();
		DefaultBotOptions botOptions = ApiContext.getInstance(DefaultBotOptions.class);
//		botOptions.setProxyHost(PROXY_HOST);
//		botOptions.setProxyPort(PROXY_PORT);
//		botOptions.setProxyType(DefaultBotOptions.ProxyType.SOCKS5);

		try {
			botapi.registerBot(new Bot(botOptions));
		} catch (TelegramApiException ex) {
			ex.printStackTrace();
		}
	}

	public static Poin getPointByAdres(String adres) throws IOException {
		OkHttpClient client = new OkHttpClient();
		String googleApi = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String googleKey = "&key=AIzaSyBuS8ozqw3pNqh2qTcGonzbrWka_8TCWHE";
		String url = googleApi + adres + googleKey;
		Request request = new Request.Builder()
				.url(url)
				.build();
		Response response = client.newCall(request).execute();
		String json = response.body().string();
		JsonObject jelement = (JsonObject) new JsonParser().parse(json);
		JsonObject point = (JsonObject) ((JsonObject) ( jelement).get("results").getAsJsonArray().get(0)).getAsJsonObject("geometry").get("location");
		JsonObject pointAdr = (JsonObject) ((JsonObject) ( jelement).get("results").getAsJsonArray().get(0));
		double lat = point.get("lat").getAsDouble();
		double lng = point.get("lng").getAsDouble();
		String adr = pointAdr.get("formatted_address").getAsString();
		Poin poin = new Poin();
		poin.setLatitude(lat);
		poin.setLongitude(lng);
		poin.setPointAdr(adr);
		return poin;
	}
}
