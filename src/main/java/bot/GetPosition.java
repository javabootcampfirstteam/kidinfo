package Test_02;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;

public class GetPosition {
	public static void ParseJson() throws IOException {
		String json = getPositionJson("юкковское шоссе 17");
		JsonObject jelement = (JsonObject) new JsonParser().parse(json);
		JsonObject point = (JsonObject) ((JsonObject) ( jelement).get("results").getAsJsonArray().get(0)).getAsJsonObject("geometry").get("location");
		double lat = point.get("lat").getAsDouble();
		double lng = point.get("lng").getAsDouble();
		//System.out.println(lat);
		//System.out.println(lng);
	}
	public static String getPositionJson(String adres) throws IOException{
		String googleApi = "https://maps.googleapis.com/maps/api/geocode/json?address=";
		String googleKey = "&key=AIzaSyBuS8ozqw3pNqh2qTcGonzbrWka_8TCWHE";
		String googleAllAddress = googleApi + adres + googleKey;
		GetExample example = new GetExample();
		return example.run(googleAllAddress);
	}
}
