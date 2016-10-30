package utils;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import models.Location;

//http://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452&sensor=true
public class GoogleParser {

	private static final String GOOGLE_GEOCODE_URL = "http://maps.googleapis.com/maps/api/geocode/json?latlng=";
	private final static String USER_AGENT = "Mozilla/5.0";

	/**
	 * Sends a request to googles geo code rest interface and parses the json response
	 * @param latitude  String representing latitude
	 * @param longitude  String representing longitude
	 * @return  Human readable address representing the specified coordinates
	 */
	public static String parseLatLong(String latitude, String longitude) {
		String display = "unknown";
		StringBuilder sb = new StringBuilder();
		sb.append(GOOGLE_GEOCODE_URL);
		sb.append(latitude);
		sb.append(",");
		sb.append(longitude);
		sb.append("&");
		sb.append("sensor=true");
		String url = sb.toString();
		try {

			URL obj = new URL(url);
			HttpURLConnection con = (HttpURLConnection) obj.openConnection();

			// add request header
			con.setRequestMethod("POST");
			con.setRequestProperty("User-Agent", USER_AGENT);
			con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

			// Send post request
			con.setDoOutput(true);
			DataOutputStream wr = new DataOutputStream(con.getOutputStream());
			wr.flush();
			wr.close();

			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				response.append(inputLine);
			}
			in.close();

			JsonParser parser = new JsonParser();
			JsonObject json = (JsonObject) parser.parse(response.toString());
			JsonArray results = (JsonArray) json.get("results");
			JsonObject firstResult = (JsonObject) results.get(0);
			JsonElement formattedAddress = (JsonElement) firstResult.get("formatted_address");

			// remove quotes from start and end
			display = formattedAddress.toString().replaceAll("^\"|\"$", "");
		} catch (Exception e) {

		}

		return display;

	}

	public static List<String> convertLocationsForDisplay(List<Location> locations) {
		List<String> formattedAddresses = new ArrayList<String>();
		for (Location location : locations) {
			formattedAddresses.add(parseLatLong(String.valueOf(location.latitude), String.valueOf(location.longitude)));
		}
		return formattedAddresses;
	}

	public static String parseLatLong(Double latitude, Double longitude) {
		return parseLatLong(String.valueOf(latitude), String.valueOf(longitude));
	}

}
