package org.tutske.utils.terminal;


public class Colors {

	private static final String ESCAPE_CHAR = "\u001B";
	private static final String BEGIN_CHARS = "[";
	private static final String END_CHARS = "m";
	private static final String RESET = "\u001B[0m";

	public static class Modifier {
		public static Modifier RESET = new Modifier (0);
		public static Modifier BOLD = new Modifier (1);
		public static Modifier DIM = new Modifier (2);
		public static Modifier FG (Color color) {
			return new Modifier (30 + color.number);
		}
		public static Modifier BG (Color color) {
			return new Modifier (40 + color.number);
		}

		protected int number;
		public Modifier (int number) {
			this.number = number;
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
