package org.tutske.lib.utils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.function.Function;


public class Config {

	public static Config create (String [] args) throws IOException {
		return create ("application", args);
	}

	public static Config create (String appname, String [] args) throws IOException {
		appname = appname.trim ().toLowerCase (Locale.ROOT).replace ("[^a-zA-Z0-9]+", "-");
		String optsFile = "./" + appname + ".properties";
		String envPrefix = appname.toUpperCase (Locale.ROOT).replace ("-", "_");

		Config config = new Config ();

		config.load (90, propertiesProvider (Config.class.getResourceAsStream (optsFile)));
		config.load (30, environmentProvider (envPrefix));
		config.load (15, argumentsProvider (System.getenv ().getOrDefault (envPrefix + "_OPTS", "").split ("\\s+")));
		config.load (10, argumentsProvider (args));
		config.load (20, propertiesProvider ((Path) config.compute (
			c -> Paths.get (c.has ("config") ? c.option ("config") : optsFile)
		)));

		return config;
	}

	public static Function<String, ConfigValue> environmentProvider () {
		return mapProvider (System.getenv (), key -> key.replace ("-", "_").toUpperCase ());
	}

	public static Function<String, ConfigValue> environmentProvider (String prefix) {
		return mapProvider (System.getenv (), key -> prefix + "_" + key.replace ("-", "_").toUpperCase ());
	}

	public static String [] extractTail (String ... args) {
		List<String> tail = new ArrayList<> ();

		boolean optionEnd = false;
		for ( String arg : args ) {
			if ( optionEnd || ! arg.startsWith ("--") ) { tail.add (arg); }
			optionEnd = optionEnd || "--".equals (arg);
		}

		return tail.toArray (new String [] {});
	}

	public static Function<String, ConfigValue> argumentsProvider (String ... args) {
		Map<String, String> options = new HashMap<> ();
		for ( String arg : args ) {
			if ( "--".equals (arg) ) { break; }
			if ( ! arg.startsWith ("--") ) { continue; }
			String [] parts = arg.split ("=", 2);
			options.put (parts[0].substring (2), parts.length > 1 ? parts[1] : "");
		}

		return mapProvider (options);
	}

	public static Function<String, ConfigValue> mapProvider (Map<String, String> map) {
		return mapProvider (map, Function.identity ());
	}

	public static Function<String, ConfigValue> mapProvider (
		Map<String, String> map, Function<String, String> normalize
	) {
		return key -> {
			String actualKey = normalize.apply (key);
			return new ConfigValue () {
				@Override public boolean exists () { return map.containsKey (actualKey); }
				@Override public String value () { return map.get (actualKey); }
			};
		};
	}

	public static Function<String, ConfigValue> propertiesProvider (Path file) throws IOException {
		if ( ! Files.exists (file) ) { return emptyProvider (); }
		try ( InputStream in = new BufferedInputStream (Files.newInputStream (file)) ) {
			return propertiesProvider (in);
		}
	}

	public static Function<String, ConfigValue> propertiesProvider (InputStream stream) throws IOException {
		if ( stream == null ) { return emptyProvider (); }
		Properties properties = new Properties ();
		properties.load (stream);
		return propertiesProvider (properties);
	}

	public static Function<String, ConfigValue> propertiesProvider (Properties properties) {
		return key -> new ConfigValue () {
			@Override public boolean exists () { return properties.containsKey (key); }
			@Override public String value () { return String.valueOf (properties.get (key)); }
		};
	}

	public static Function<String, ConfigValue> emptyProvider () {
		return key -> ABSENT;
	}

	private static final ConfigValue ABSENT = new ConfigValue () {
		@Override public boolean exists () { return false; }
		@Override public String value () { return null; }
	};

	private interface ConfigValue {
		boolean exists ();
		String value ();
	}

	private record PrioritizedProvider(int priority, Function<String, ConfigValue> provider) { }

	private final List<PrioritizedProvider> providers = new ArrayList<> ();
	private final Map<String, ConfigValue> valueCache = new HashMap<> ();
	private final Map<Function<Config, ?>, Object> computeCache = new HashMap<> ();

	public Config load (int priority, Function<String, ConfigValue> provider) {
		valueCache.clear ();
		computeCache.clear ();
		providers.add (new PrioritizedProvider (priority, provider));
		providers.sort (Comparator.comparing (p -> p.priority));
		return this;
	}

	public boolean has (String key) {
		return find (key).exists ();
	}

	protected String value (String key) {
		return find (key).value ();
	}

	private ConfigValue find (String key) {
		return valueCache.computeIfAbsent (key, k -> (
			providers.stream ().map (p -> p.provider.apply (k)).findFirst ().orElse (ABSENT)
		));
	}

	public <T> T compute (Function<Config, T> fn) {
		return (T) computeCache.computeIfAbsent (fn, f -> f.apply (this));
	}

	public void load (Function<String, ConfigValue> provider) {
		load (99, provider);
	}

	public String option (String key) {
		return optionOrDefault (key, null, Function.identity ());
	}

	public <T> T option (String key, Function<String, T> fn) {
		return optionOrDefault (key, null, fn);
	}

	public String optionOrDefault (String key, String otherwise) {
		return optionOrDefault (key, otherwise, Function.identity ());
	}

	public <T> T optionOrDefault (String key, T otherwise, Function<String, T> fn) {
		return has (key) ? fn.apply (value (key)) : otherwise;
	}

}
