package dev.gsitgithub.webapp.config.utils;

import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;

//import org.joda.time.DateTime;
//import org.joda.time.format.DateTimeFormatter;
//import org.joda.time.format.ISODateTimeFormat;

/**
 * @author: gsitgithub
 */
public class DateUtil {

	public static final DateTimeFormatter ISO8061_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;
	private static final ZoneId DEFAULT_ZONE = ZoneId.of("UTC");
	
	public static ZoneId getDefaultZone() {
		return DEFAULT_ZONE;
	}
	public static ZonedDateTime toZonedDateTime(Date date) {
		return date.toInstant().atZone(DateUtil.getDefaultZone());
	}
	public static Date toDate(ZonedDateTime date) {
		return DateConvertUtils.asUtilDate(date, DEFAULT_ZONE);
	}
	
	public static ZonedDateTime getDateFromIso8061DateString(String dateString) {
		return ZonedDateTime.parse(dateString, ISO8061_FORMATTER);
	}

	public static String getCurrentDateAsIso8061String() {
		ZonedDateTime today = ZonedDateTime.now(DEFAULT_ZONE);
		return ISO8061_FORMATTER.format(today);
	}

	public static String getDateDateAsIso8061String(ZonedDateTime date) {
		return ISO8061_FORMATTER.format(date);
	}

	public static long periodToMillis(Period period) {
		LocalDateTime start = LocalDateTime.now();
		return start.until(start.plus(period), ChronoUnit.MILLIS);
	}
	public static long periodToSeconds(Period period) {
		LocalDateTime start = LocalDateTime.now();
		return start.until(start.plus(period), ChronoUnit.SECONDS);
	}
}
