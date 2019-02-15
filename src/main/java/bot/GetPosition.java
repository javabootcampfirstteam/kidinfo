package bot;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;

public class GetPosition {
	public static Poin ParseJson() throws IOException {
		String json = getPositionJson("проспект Дружбы народов 52/41");
//		String json = getPositionJson("юкковское шоссе 17");

		JsonObject jelement = (JsonObject) new JsonParser().parse(json);
		JsonObject point = (JsonObject) ((JsonObject) ( jelement).get("results").getAsJsonArray().get(0)).getAsJsonObject("geometry").get("location");
		JsonObject pointAdr = (JsonObject) ((JsonObject) ( jelement).get("results").getAsJsonArray().get(0));
		double lat = point.get("lat").getAsDouble();
		double lng = point.get("lng").getAsDouble();
		String adr = pointAdr.get("formatted_address").getAsString();

//		System.out.println("Адрес = " + adr);
//		System.out.println("latitude = " + lat);
//		System.out.println("longitude = " + lng);
		Poin poin = new Poin();
		poin.setLatitude(lat);
		poin.setLongitude(lng);
		poin.setPointAdr(adr);
		return poin;
	}
	public static String getPositionJson(String adres) throws IOException{
		String googleApi = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String googleKey = "&key=AIzaSyBuS8ozqw3pNqh2qTcGonzbrWka_8TCWHE";
		String googleAllAddress = googleApi + adres + googleKey;
		GetExample example = new GetExample();
		return example.run(googleAllAddress);
	}
}
