package org.tutske.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.tutske.Settings;


public enum XDateFormat {

	YMD ("yyyy-MM-dd"),
	YMD_12 ("yyyy-MM-dd hh:mm:ss"),
	YMD_24 ("yyyy-MM-dd HH:mm:ss"),
	DMY ("dd-MM-yyyy"),
	DMY_12 ("dd-MM-yyyy hh:mm:ss"),
	DMY_24 ("dd-MM-yyyy HH:mm:ss"),
	YMDT24 ("yyyy-MM-dd'T'HH:mm:ss");

	private SimpleDateFormat dateFormat;

	/* -- static constructors -- */

	public static XDateFormat getDefault () {
		return YMD;
	}

	/* -- constructors -- */

	XDateFormat (String format) {
		dateFormat = new SimpleDateFormat (format, Settings.LOCALE);
	}

	/* -- getters & setters -- */

	public DateFormat getFormat () { return dateFormat; }
	public String getPatternString () { return dateFormat.toPattern (); }

	/* -- basics -- */

	public String format (Date date) {
		return getFormat ().format (date);
	}

	public Date parse (String dateString) {
		try { return getFormat ().parse (dateString); }
		catch (ParseException e) { throw new RuntimeException (e); }
	}

	@Override
	public String toString () {
		return String.format ("<XDatePattern: %s>", getFormat ());
	}

}
