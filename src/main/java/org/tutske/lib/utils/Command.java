package org.tutske.lib.utils;

import java.util.Locale;
import java.util.Map;


public class Command {

	public static void run (String [] args, Map<String, CommandFunction> fns) throws Exception {
		run (Config.create (args), Config.extractTail (args), fns);
	}

	public static void run (Config config, String [] tail, Map<String, CommandFunction> fns) throws Exception {
		boolean hasValidCmd = tail.length > 0 && fns.containsKey (tail[0].toLowerCase (Locale.ROOT));
		String cmd = hasValidCmd ? tail[0].toLowerCase (Locale.ROOT) : "__main__";

		CommandFunction fn = fns.get (cmd);

		if ( fn == null ) {
			String actual = tail.length > 0 ? tail[0] : cmd;
			String valids = "`" + String.join ("`, `", fns.keySet ()) + "`";
			String msg = "Got invalid command `" + actual + "`, should be one of: " + valids;
			throw new RuntimeException (msg);
		}

		fn.execute (hasValidCmd ? tail[0] : cmd, config, stripHead (hasValidCmd ? 1 : 0, tail));
	}

	public static interface CommandFunction {
		public void execute (String cmd, Config config, String [] tail) throws Exception;
	}

	public static String [] stripHead (int amount, String [] items) {
		if ( amount == 0 || items.length == 0 ) { return items; }
		if ( amount >= items.length ) { return new String [] {}; }

		String [] copy = new String [items.length - amount];
		System.arraycopy (items, amount, copy, 0, copy.length);

		return copy;
	}

}
