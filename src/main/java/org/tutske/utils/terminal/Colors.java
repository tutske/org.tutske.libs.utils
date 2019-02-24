package org.tutske.utils.terminal;


public class Colors {

	private static final String ESCAPE_CHAR = "\u001B";
	private static final String BEGIN_CHARS = "[";
	private static final String END_CHARS = "m";
	private static final String RESET = "\u001B[0m";

	public static class Modifier {
		public static final Modifier RESET = new Modifier (0);
		public static final Modifier BOLD = new Modifier (1);
		public static final Modifier DIM = new Modifier (2);

		public static final Modifier GREY = FG (Color.GREY);
		public static final Modifier DGREY = FG (Color.DGREY);
		public static final Modifier RED = FG (Color.RED);
		public static final Modifier DRED = FG (Color.DRED);
		public static final Modifier GREEN = FG (Color.GREEN);
		public static final Modifier DGREEN = FG (Color.DGREEN);
		public static final Modifier YELLOW = FG (Color.YELLOW);
		public static final Modifier DYELLOW = FG (Color.DYELLOW);
		public static final Modifier BLUE = FG (Color.BLUE);
		public static final Modifier DBLUE = FG (Color.DBLUE);
		public static final Modifier PURPLE = FG (Color.PURPLE);
		public static final Modifier DPURPLE = FG (Color.DPURPLE);
		public static final Modifier CYAN = FG (Color.CYAN);
		public static final Modifier DCYAN = FG (Color.DCYAN);
		public static final Modifier WHITE = FG (Color.WHITE);
		public static final Modifier DWHITE = FG (Color.DWHITE);

		public static final Modifier BG_GREY = BG (Color.GREY);
		public static final Modifier BG_DGREY = BG (Color.DGREY);
		public static final Modifier BG_RED = BG (Color.RED);
		public static final Modifier BG_DRED = BG (Color.DRED);
		public static final Modifier BG_GREEN = BG (Color.GREEN);
		public static final Modifier BG_DGREEN = BG (Color.DGREEN);
		public static final Modifier BG_YELLOW = BG (Color.YELLOW);
		public static final Modifier BG_DYELLOW = BG (Color.DYELLOW);
		public static final Modifier BG_BLUE = BG (Color.BLUE);
		public static final Modifier BG_DBLUE = BG (Color.DBLUE);
		public static final Modifier BG_PURPLE = BG (Color.PURPLE);
		public static final Modifier BG_DPURPLE = BG (Color.DPURPLE);
		public static final Modifier BG_CYAN = BG (Color.CYAN);
		public static final Modifier BG_DCYAN = BG (Color.DCYAN);
		public static final Modifier BG_WHITE = BG (Color.WHITE);
		public static final Modifier BG_DWHITE = BG (Color.DWHITE);

		public static Modifier FG (Color color) {
			return new Modifier (30 + color.number);
		}
		public static Modifier BG (Color color) {
			return new Modifier (40 + color.number);
		}

		protected int number;
		public final String chars;

		public Modifier (int number) {
			this.number = number;
			this.chars = ESCAPE_CHAR + BEGIN_CHARS + number + END_CHARS;
		}

		public String m (String content) {
			return chars + content + RESET.chars;
		}

		public StringBuilder m (StringBuilder builder, String content) {
			return builder.append (chars).append (content).append (RESET.chars);
		}
	}

	public static class Color {
		public static final Color GREY = new Color (0);
		public static final Color DGREY = new Color (GREY.number + 60);
		public static final Color RED = new Color (1);
		public static final Color DRED = new Color (RED.number + 60);
		public static final Color GREEN = new Color (2);
		public static final Color DGREEN = new Color (GREEN.number + 60);
		public static final Color YELLOW = new Color (3);
		public static final Color DYELLOW = new Color (YELLOW.number + 60);
		public static final Color BLUE = new Color (4);
		public static final Color DBLUE = new Color (BLUE.number + 60);
		public static final Color PURPLE = new Color (5);
		public static final Color DPURPLE = new Color (PURPLE.number + 60);
		public static final Color CYAN = new Color (6);
		public static final Color DCYAN = new Color (CYAN.number + 60);
		public static final Color WHITE = new Color (7);
		public static final Color DWHITE = new Color (WHITE.number + 60);

		public static final Color DEFAULT = new Color (9);

		protected int number;
		Color (int number) {
			this.number = number;
		}
	}

	public static String m (Modifier ... modifiers) {
		return m (new StringBuilder (), modifiers).toString ();
	}

	public static StringBuilder m (StringBuilder builder, Modifier ... modifiers) {
		builder.append (ESCAPE_CHAR).append (BEGIN_CHARS);
		for ( Modifier modifier : modifiers ) {
			builder.append (modifier.number).append (";");
		}
		builder.replace (builder.length () - 1, builder.length (), END_CHARS);
		return builder;
	}

	public static String c (String modifiers, String content) {
		return c (new StringBuilder (), modifiers, content).toString ();
	}

	public static StringBuilder c (StringBuilder builder, String modifiers, String content) {
		return builder.append (modifiers).append (content).append (RESET);
	}

	public static String clearColor (String colored) {
		return colored.replaceAll (ESCAPE_CHAR + ".*?m", "");
	}

}
