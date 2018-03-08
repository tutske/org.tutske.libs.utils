package org.tutske.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;


public class PrimitivesParser {

	private static class ConvertMap<T> extends HashMap<Class<?>, Function<T, ?>> {
	}

	private static final SimpleDateFormat [] formats = new SimpleDateFormat [] {
		new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss.SSSSX"),
		new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ssX"),
		new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss"),
		new SimpleDateFormat ("yyyy/MM/dd"),
		new SimpleDateFormat ("dd/MM/yyyy"),
		new SimpleDateFormat ("dd/MM/yy")
	};

	private static final String ERROR_FORMAT = "Clazz not supported for conversion: %s (%s)";

	private static final Map<Class<?>, ConvertMap> converters = new HashMap<> ();

	static {
		ConvertMap<String> strings = new ConvertMap<> ();

		strings.put (String.class, Function.identity ());
		strings.put (Integer.class, Integer::parseInt);
		strings.put (Long.class, Long::parseLong);
		strings.put (Float.class, Float::parseFloat);
		strings.put (Double.class, Double::parseDouble);
		strings.put (Boolean.class, Boolean::parseBoolean);
		strings.put (Date.class, PrimitivesParser::parseDate);

		converters.put (String.class, strings);
	}

	public static <S, T> void convert (Class<S> source, Class<T> target, Function<S, T> converter) {
		if ( ! converters.containsKey (source) ) {
			converters.put (source, new ConvertMap<S> ());
		}
		converters.get (source).put (target, converter);
	}

	public static <S, T> T parse (S value, Class<T> clazz) {
		if ( value == null ) { return (T) value; }
		if ( clazz.isAssignableFrom (value.getClass ()) ) { return (T) value; }
		if ( String.class.equals (clazz) ) { return (T) String.valueOf (value); }

		ConvertMap<S> map = converters.get (value.getClass ());
		if ( map == null ) { throw new RuntimeException ("Can not convert from " + value.getClass ()); }

		Function<S, ?> fn = map.get (clazz);
		if ( fn == null ) {
			throw new IllegalArgumentException (String.format (
				"Conversion not supported for %s -> %s (%s).",
				value.getClass (), clazz, value
			));
		}

		return (T) fn.apply (value);
	}

	public static Date parseDate (String value) {
		for ( SimpleDateFormat format : formats ) {
			try { return format.parse (value); }
			catch ( ParseException ignore ) {}
		}

		try { return new Date (Long.parseLong (value)); }
		catch ( NumberFormatException ignore ) {}

		throw new IllegalArgumentException ("Not a valid date representation: `" + value + "`");
	}

}
