package utils;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import org.apache.commons.lang3.time.DurationFormatUtils;

import com.google.gson.JsonObject;

public class DateTimeUtils {
	
	public static final String DATE_TIME_FORMAT = "dd:MM:yyyy H:mm:ss";
	public static final String DURATION_FORMAT = "H:mm:ss";
	
	public static String convertLocalDateTimeToString(LocalDateTime dateTime){
		 return dateTime.format(DateTimeFormatter.ISO_DATE_TIME);
	}
	
	public static LocalDateTime convertStringToLocalDateTime(String stringDateTime){
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_TIME_FORMAT);
		return LocalDateTime.parse(stringDateTime, formatter);
	}

	public static boolean isValidDate(String date) {
		boolean valid = false;
		try{
			LocalDateTime.parse(date, DateTimeFormatter.ofPattern(DATE_TIME_FORMAT));
			valid = true;
		}catch(DateTimeParseException | NullPointerException e){
			FileLogger.getLogger().log("Invalid date : " + date);
		}
		return valid;
		
	}

	public static boolean isValidDuration(String duration) {
		boolean valid = false;
		String[] tokens = duration.split(":");
		if(tokens.length == 3){
			int hours = Integer.parseInt(tokens[0]);
			int minutes = Integer.parseInt(tokens[1]);
			int seconds = Integer.parseInt(tokens[2]);
			valid = (hours >= 0 && minutes >= 0 && seconds >= 0);
		}
		
		return valid;
	}

	public static Duration convertStringToDuration(String duration) {
		String[] tokens = duration.split(":");
		int hours = Integer.parseInt(tokens[0]);
		int minutes = Integer.parseInt(tokens[1]);
		int seconds = Integer.parseInt(tokens[2]);
		return Duration.ofHours(hours).plusMinutes(minutes).plusSeconds(seconds);
	}
	
	public static String convertDurationToString(Duration duration){		
		return DurationFormatUtils.formatDuration(duration.toMillis(), DURATION_FORMAT);
	}
	
	public static String convertDurationForDisplay(Duration duration){		
		String[] tokens = DurationFormatUtils.formatDuration(duration.toMillis(), DURATION_FORMAT).split(":");
		int hours = Integer.valueOf(tokens[0]);
		int minutes = Integer.valueOf(tokens[1]);
		int seconds = Integer.valueOf(tokens[2]);
		StringBuilder sb = new StringBuilder();
		if(hours > 0){			
			sb.append(hours + " hour(s), ");
		}
		if(minutes > 0){
			sb.append(minutes + " minute(s), and ");
		}		
		sb.append(seconds + " second(s)");
		return sb.toString();
	}
	
	
	public static LocalDateTime convertJsonDateTime(JsonObject json){
		StringBuilder sb = new StringBuilder();
		JsonObject date = json.get("date").getAsJsonObject();
		JsonObject time = json.get("time").getAsJsonObject();
		sb.append(String.format("%02d", date.get("day").getAsInt()));
		sb.append(":");
		sb.append(String.format("%02d", date.get("month").getAsInt()));
		sb.append(":");
		sb.append(String.format("%04d", date.get("year").getAsInt()));
		sb.append(" ");
		sb.append(String.format("%02d", time.get("hour").getAsInt()));
		sb.append(":");
		sb.append(String.format("%02d", time.get("minute").getAsInt()));
		sb.append(":");
		sb.append(String.format("%02d", time.get("second").getAsInt()));
		return convertStringToLocalDateTime(sb.toString());
	}

	public static Duration convertJsonDuration(JsonObject json) {
		StringBuilder sb = new StringBuilder("0:");
		sb.append(json.get("seconds").getAsInt());
		sb.append(":0");
		return Duration.ofSeconds(json.get("seconds").getAsInt());
//		return convertStringToDuration(sb.toString());
	}

}
