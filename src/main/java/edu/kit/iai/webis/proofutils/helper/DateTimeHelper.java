/*
 * Copyright (c) 2025-2026
 * Karlsruhe Institute of Technology - Institute for Automation and Applied Informatics (IAI)
 */
package edu.kit.iai.webis.proofutils.helper;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.TimeZone;

/**
 * a helper class to format date and time stamps
 */
public class DateTimeHelper {

	/**
	 * Enum for converting time due to precision
	 */
	public enum EpochTimeScale {
		/**
		 * convert long as millisecond
		 */
		MILLISECOND,
		/**
		 * convert long as second
		 */
		SECOND
	}

	/**
	 * enumeration for different date and time formats, e.g.
	 * {@link #RFC3339}, {@link #ISO8601}, etc.
	 */
	public enum FormatType {
		/**
		 * the default RFC3339 format ('yyyy-MM-dd'T'HH:mm:ss')
		 */
		RFC3339("yyyy-MM-dd'T'HH:mm:ss"),
		/**
		 * the RFC3339 format with a blank separator ('yyyy-MM-dd' 'HH:mm:ss')
		 */
		RFC3339_BLANK("yyyy-MM-dd' 'HH:mm:ss"),
		/**
		 * the ISO8601 format with ('yyyy-MM-dd'T'HH:mm:ss', same as {@link #RFC3339})
		 */
		ISO8601("yyyy-MM-dd'T'HH:mm:ss"),
		/**
		 * the default date format ('yyyy-MM-dd')
		 */
		DATE("yyyy-MM-dd"),
		/**
		 * the default time format ('HH:mm:ss')
		 */
		TIME_HMS("HH:mm:ss"),
		/**
		 * the default time format with milliseconds ('HH:mm:ss.SSS')
		 */
		TIME_HMSMS("HH:mm:ss.SSS"),
		/**
		 * the default time format for minutes and second only ('mm:ss')
		 */
		TIME_MS("mm:ss"),
		/**
		 * the default date and time format ('yyyy-MM-dd'T'HH:mm:ss', same as {@link #RFC3339})
		 */
		DEFAULT_DATE_TIME("yyyy-MM-dd'T'HH:mm:ss");


		private String formatString;

		private FormatType(String formatString) {
			this.formatString = formatString;
		}

		/**
		 * get the format string representation for (re)formatting
		 * @return the format string representation
		 */
		private String getFormatString() {
			return this.formatString;
		}
	}

	private DateTimeHelper() {
	}

	/**
	 * do the conversion from a DateTimeStamp given as long value provided by an
	 * InfluxDB query to an arbitrary format given by the user via options
	 *
	 * @param value the long value (time given by InfluxDB query)
	 * @return the converted DateTimeStamp using the default format
	 *         ({@link FormatType#ISO8601})
	 */
	public static String doConversion(long value) {
		return DateTimeHelper.doConversion(value, FormatType.DEFAULT_DATE_TIME);
	}

	/**
	 * do the conversion of the given long value to an given format with respect to
	 * {@link ZonedDateTime}.
	 *
	 * @param value      the long time value (UNIX time stamp)
	 * @param formatType the {@link FormatType}
	 * @return the converted DateTimeStamp
	 */
	public static String doConversion(long value, FormatType formatType) {
		Instant i = Instant.ofEpochSecond(value);
		ZonedDateTime z = ZonedDateTime.ofInstant(i, TimeZone.getDefault().toZoneId());

		final FormatType ft = formatType == null ? FormatType.DEFAULT_DATE_TIME : formatType;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ft.getFormatString());
		return z.format(dtf);
	}

	/**
	 * do the conversion of the given long value to an given format with respect to
	 * {@link ZonedDateTime}.
	 *
	 * @param value      the long time value (UNIX time stamp)
	 * @param formatType the {@link FormatType}
	 * @return the converted DateTimeStamp
	 */
	public static String doConversion(long value, FormatType formatType, EpochTimeScale scale ) {
		Instant i = EpochTimeScale.SECOND == scale ? Instant.ofEpochSecond(value) : Instant.ofEpochMilli(value);
		ZonedDateTime z = ZonedDateTime.ofInstant(i, TimeZone.getDefault().toZoneId());

		final FormatType ft = formatType == null ? FormatType.DEFAULT_DATE_TIME : formatType;
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern(ft.getFormatString());
		return z.format(dtf);
	}

	/**
	 * convert a DateTimeStamp string with a given {@link DateTimeFormatter} to a
	 * {@link ZonedDateTime}
	 *
	 * @param date      the given date and time as a string corresponding with the
	 *                  given formatter
	 * @param formatter the given formatter representing the format of the given
	 *                  date time string
	 * @return the ZonedDateTime representation of the given DateTimeStamp
	 */
	public static ZonedDateTime convertDate(String date, DateTimeFormatter formatter) {
		LocalDateTime localDateTime;
		try {
			localDateTime = LocalDateTime.parse(date, formatter);
		} catch (DateTimeParseException e1) {
			try {
				// System.err.println("Missing precise time information. Time is set to 0 a.m.");
				localDateTime = LocalDate.parse(date, formatter).atStartOfDay();
			} catch (DateTimeParseException e2) {
				// REFACTOR: we need some kind of notification of the user here
				// System.err.println("Provided date and format can not be matched with each other.");
				try {
					return ZonedDateTime.parse(date, formatter);
				} catch (DateTimeParseException e3) {
					localDateTime = LocalDateTime.of(1970, 1, 1, 0, 0);// MIN;
				}
			}
		}
		return ZonedDateTime.of(localDateTime, ZoneId.systemDefault());
	}

	/**
	 * get the current time as a readable String for the default format
	 * {@link FormatType#ISO8601}
	 * @return the current time as a readable String
	 */
	public static String getCurrentTimeAsString() {
		return getCurrentTimeAsString( DateTimeHelper.FormatType.ISO8601 );
	}

	/**
	 * get the current time as a readable String for a given format
	 * @param format the given format ({@link FormatType})
	 * @return the current time as a readable String
	 */
	public static String getCurrentTimeAsString( FormatType format ) {
		return DateTimeHelper.doConversion( System.currentTimeMillis() / 1000, format );
	}



}
