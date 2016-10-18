package utils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.regex.Pattern;

public class DateTimeUtils {
	
	public static final String DATE_TIME_FORMAT = "dd:MM:yyyy H:mm:ss";
	
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
		}catch(DateTimeParseException e){
			System.out.println("Invalid date pattern - please use " + DATE_TIME_FORMAT);
		}
		return valid;
		
	}

}
