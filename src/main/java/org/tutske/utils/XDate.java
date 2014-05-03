package org.tutske.utils;

import java.util.Calendar;
import java.util.Date;

import org.tutske.Settings;

public class XDate {

	private Calendar calendar;

	/* -- static constructors -- */

	public static XDate newCurrentDate () {
		return new XDate (System.currentTimeMillis ());
	}

	public static XDate newFromString (String dateString) {
		return newFromString (dateString, XDateFormat.getDefault ());
	}

	public static XDate newFromString (String dateString, XDateFormat format) {
		Date date = format.parse (dateString);
		return newFromDate (date);
	}

	public static XDate newFromDate (Date date) {
		return new XDate (date);
	}

	/* -- constructors -- */

	public XDate (long timestamp) {
		this (new Date (timestamp));
	}

	public XDate (Date date) {
		calendar = Calendar.getInstance (Settings.TIMEZONE, Settings.LOCALE);
		calendar.setTime (date);
	}

	/* -- getters & setters -- */

	public long getTimestamp () {
		return calendar.getTimeInMillis ();
	}

	public String getTimestampString () {
		return getTimestampString (XDateFormat.getDefault ());
	}

	public String getTimestampString (XDateFormat dateFormat) {
		return dateFormat.format (calendar.getTime ());
	}

	public int getYear () { return calendar.get(Calendar.YEAR); }
	public int getMonth () { return calendar.get (Calendar.MONTH); }
	public int getDay () { return calendar.get (Calendar.DAY_OF_MONTH); }

	public int getHour () { return calendar.get (Calendar.HOUR_OF_DAY); }
	public int getMinutes () { return calendar.get (Calendar.MINUTE); }
	public int getSeconds () { return calendar.get (Calendar.SECOND); }
	public int getMillis () { return calendar.get (Calendar.MILLISECOND); }

	/* -- basics -- */

	public String toString () {
		return String.format ("<XDate: %s>", calendar.toString ());
	}

}
