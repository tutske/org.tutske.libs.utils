package org.tutske.lib.utils;

import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
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

	private static final Map<Class<?>, ConvertMap<?>> converters = new HashMap<> ();

	static {
		ConvertMap<String> strings = new ConvertMap<> ();

		strings.put (String.class, Function.identity ());
		strings.put (Integer.class, Integer::parseInt);
		strings.put (Long.class, Long::parseLong);
		strings.put (Float.class, Float::parseFloat);
		strings.put (Double.class, Double::parseDouble);
		strings.put (Boolean.class, Boolean::parseBoolean);
		strings.put (Date.class, PrimitivesParser::parseDate);
		strings.put (Path.class, value -> {
			if ( ! value.startsWith ("~/") ) { return Paths.get (value); }
			return Paths.get (System.getProperty ("user.home")).resolve (value.substring (2));
		});

		strings.put (URI.class, value -> {
			try { return new URI (value); }
			catch ( URISyntaxException e ) { throw Exceptions.wrap (e); }
		});

		converters.put (String.class, strings);
	}

	public static <S, T> void convert (Class<S> source, Class<T> target, Function<S, T> converter) {
		converters.computeIfAbsent (source, ignore -> new ConvertMap<S> ());
		converters.get (source).put (target, (Function) converter);
	}

	public static <S, T> T parse (S value, Class<T> clazz) {
		if ( value == null ) { return (T) value; }
		if ( clazz.isAssignableFrom (value.getClass ()) ) { return (T) value; }
		if ( String.class.equals (clazz) ) { return (T) String.valueOf (value); }

		ConvertMap<S> map = (ConvertMap) converters.get (value.getClass ());
		if ( map == null ) {
			throw new IllegalArgumentException (String.format (
				"Conversion not supported for source type %s -> %s (%s).",
				value.getClass (), clazz, value
			));
		}

		Function<S, ?> fn = map.get (clazz);
		if ( fn == null ) {
			throw new IllegalArgumentException (String.format (
				"Conversion not supported for target type %s -> %s (%s).",
				value.getClass (), clazz, value
			));
		}

		return (T) fn.apply (value);
	}

	public static <S, T> Function<S, T> getParser (Class<S> source, Class<T> target) {
		if ( String.class.equals (target) ) { return (Function) String::valueOf; }

		if ( ! converters.containsKey (source) ) {
			throw new IllegalArgumentException (String.format (
				"Conversion not supported for source type %s -> %s.",
				source, target
			));
		}

		Function<S, T> converter = (Function) converters.get (source).get (target);
		if ( converter == null ) {
			throw new IllegalArgumentException (String.format (
				"Conversion not supported for target type %s -> %s.",
				source, target
			));
		}

		return converter;
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
