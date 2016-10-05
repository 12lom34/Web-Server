package web.socket;

import java.time.Instant;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Optional;

/**
 * Utility methods for Echo Web Server functionality.
 * 
 * @author OLikho
 *
 */
public interface WebSocketUtils {
	/**
	 * Login input message (current implementation print to Console).
	 * @param message
	 */
	static void log(String message) {
		Optional<String> msg = Optional.ofNullable(message);
		msg.ifPresent(System.out::println);
	}

	/**
	 * Generate current UTC time using 'full' formatting.
	 * @return current time as string object
	 */
	static String getCurrentTimeInUTC() {
		ZonedDateTime utcDateTime = Instant.now().atZone(ZoneId.of("UTC"));
		DateTimeFormatter formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.FULL);
		return formatter.format(utcDateTime);
	}
	
	/**
	 * Generate current time using into ISO formatting.
	 * @return current time as string object
	 */
	static String getCurrentTime() {
		return DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(ZonedDateTime.now());
	}
}
