package org.tutske.utils.terminal;

import static org.tutske.utils.terminal.Colors.*;
import static org.tutske.utils.terminal.Colors.Color.*;
import static org.tutske.utils.terminal.Colors.Modifier.*;

import org.tutske.utils.Clock;

import java.io.PrintStream;


public class ProgressBar {

	private static class Spinners {
		private static String [] simple = new String [] { "|", "/", "-", "\\", "|", "/", "-", "\\" };
		private static String [] braille = new String [] { "⣾", "⣽", "⣻", "⢿", "⡿", "⣟", "⣯", "⣷" };
		private static String [] hbar = new String [] { "▁", "▃", "▄", "▅", "▆", "▇", "█", "▇", "▆", "▅", "▄", "▃" };
		private static String [] arrows = new String [] {"←", "↖", "↑", "↗", "→", "↘", "↓", "↙" };
		private static String [] signal = new String [] { "⸩      ", ")⸩     ", "»)⸩    ", "⠐»)⸩   ", " ⠐»)⸩  ",
			"  ⠐»)⸩ ", "   ⠐»)⸩", "    ⠐»)", "     ⠐»", "      ⠐", "       ", "       ", "      ⸨", "     ⸨(",
			"    ⸨(«", "   ⸨(«⠐", "  ⸨(«⠐ ", " ⸨(«⠐  ", "⸨(«⠐   ", "(«⠐    ", "«⠐     ", "⠐      ", "       ",
			"       ", "       "
		};
	}

	public static class Style {

		public static final Style standard = new Style ("[", "#", " ", "]", Spinners.simple, 50);
		public static final Style star = new Style ("[", "*", "_", "]", Spinners.braille, 50);
		public static final Style clock = new Style ("[", "*", "_", "]", Spinners.arrows, 50);
		public static final Style bar = new Style ("[", "*", "-", "]", Spinners.hbar, 60);
		public static final Style signal = new Style ("[", "*", "-", "]", Spinners.signal, 60);

		final String start;
		final String end;
		final String fill;
		final String blank;
		final String [] spinner;
		final long delay;

		public Style (String start, String fill, String blank, String end, String[] spinner, long delay) {
			this.start = start;
			this.end = end;
			this.fill = fill;
			this.blank = blank;
			this.spinner = spinner;
			this.delay = delay;
		}
	}

	private Style style = Style.standard;

	private final Clock clock;
	private final PrintStream out;
	private final int length;

	private String message;
	private int i = 0;
	private long last = 0;

	public ProgressBar (int length) {
		this ("", length);
	}

	public ProgressBar (String msg, int length) {
		this (new Clock.SystemClock (), System.out, msg, length);
	}

	public ProgressBar (Clock clock, PrintStream out, String msg, int length) {
		this.clock = clock;
		this.out = out;
		this.length = length;
		this.message = msg;
	}

	public ProgressBar setMessage (String message) {
		this.message = message;
		return this;
	}

	public ProgressBar setStyle (Style style) {
		this.style = style;
		return this;
	}

	public void showProgress (int current, int total) {
		long c = System.currentTimeMillis ();
		if ( current != total && c - last < style.delay ) { return; }
		last = c;

		float percentage = ((float) current) / total;
		if ( ++i >= style.spinner.length ) { i = 0; }

		out.format ("\033[2K\r%1$6.2f %% " + c (m (FG (GREEN)), "%4$s") + " %2$s " + c (m (FG (GREEN)), "♦") + " %3$s",
			percentage * 100, createBar (percentage), message, style.spinner[i]
		);
	}

	private String createBar (float percentage) {
		StringBuilder builder = new StringBuilder ();
		builder.append (style.start).append (m (FG (GREY)));
		int limit = (int) (percentage * length + 0.5);
		int count = 0;
		while ( count < limit ) { builder.append (style.fill); count++; }
		builder.append (m (DIM, FG (DGREY)));
		while ( count < length ) { builder.append (style.blank); count++; }
		builder.append (m (RESET)).append (style.end);

		return builder.toString ();
	}

}
