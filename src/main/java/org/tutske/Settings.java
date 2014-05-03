package org.tutske;

import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.Locale;
import java.util.TimeZone;


public class Settings {

	public static final Charset CHARSET = Charset.defaultCharset ();
	public static final Locale LOCALE = Locale.getDefault ();
	public static final TimeZone TIMEZONE = TimeZone.getDefault ();
	public static final int DATE_FORMAT = DateFormat.LONG;

}
