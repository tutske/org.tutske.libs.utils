package org.tutske.utils;

import java.util.Collection;


public class Checks {

	private static final Matcher<String> EMPTY_STRING = new Matcher<String> () {
		@Override public boolean matches (String value) {
			return value != null && value.isEmpty ();
		}
		@Override public String describe () {
			return "an empty string";
		}
	};
	private static final Matcher<Object> NULL = new Matcher<Object> () {
		@Override public boolean matches (Object value) {
			return value == null;
		}
		@Override public String describe () {
			return "a null value";
		}
	};
	private static final Matcher<Object> NOT_NULL = not (NULL);
	private static final Matcher<String> NOT_EMPTY_STRING = not (EMPTY_STRING);

	public static <T> void assure (Matcher<T> matcher, T ... values) {
		for ( T value : values ) {
			if ( ! matcher.matches (value) ) {
				throw new IllegalArgumentException (formatError (value, matcher));
			}
		}
	}

	public static <T> void check (T value, Matcher<T> matcher) {
		assert matcher.matches (value) : formatError (value, matcher);
	}

	public static Matcher<Object> nullValue () {
		return NULL;
	}

	public static Matcher<Object> notNull () {
		return NOT_NULL;
	}

	public static Matcher<String> emptyString () {
		return EMPTY_STRING;
	}

	public static Matcher<String> nonEmptyString () {
		return NOT_EMPTY_STRING;
	}

	public static <T> Matcher<T> is (final T expected) {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				return expected.equals (value);
			}
			@Override public String describe () {
				return "a value matching " + expected;
			}
		};
	}

	public static <T> Matcher<T> not (final Matcher<T> matcher) {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				return ! matcher.matches (value);
			}
			@Override public String describe () {
				return "not " + matcher.describe ();
			}
		};
	}

	public static <T extends Number> Matcher<T> between (final T lower, final T upper) {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				return value != null && value.doubleValue () >= lower.doubleValue () && value.doubleValue () < upper.doubleValue ();
			}
			@Override public String describe () {
				return "a number between " + lower.doubleValue () + " (inclusive) and " + upper.doubleValue () + " (exclusive)";
			}
		};
	}

	public static <T extends Collection> Matcher<T> notEmpty () {
		return not (empty ());
	}

	public static <T extends Collection> Matcher<T> empty () {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				return value.isEmpty ();
			}
			@Override public String describe () {
				return "an empty collection";
			}
		};
	}

	public static <T> Matcher<T> allOf (final Matcher<T> ... matchers) {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				for ( Matcher<T> matcher : matchers ) {
					if ( ! matcher.matches (value) ) { return false; }
				}
				return true;
			}
			@Override public String describe () {
				StringBuilder builder = new StringBuilder ();
				for ( Matcher<T> matcher : matchers ) {
					builder.append (", and is ").append (matcher.describe ());
				}
				builder.delete (0, 9).insert (0, "a value that is ");
				return builder.toString ();
			}
		};
	}

	public static <T> Matcher<T> anyOf (final Matcher<T> ... matchers) {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				for ( Matcher<T> matcher : matchers ) {
					if ( matcher.matches (value) ) { return true; }
				}
				return false;
			}
			@Override public String describe () {
				StringBuilder builder = new StringBuilder ();
				for ( Matcher<T> matcher : matchers ) {
					builder.append (", or is ").append (matcher.describe ());
				}
				builder.delete (0, 8).insert (0, "a value that is ");
				return builder.toString ();
			}
		};
	}

	public static <S, T extends Collection<S>> Matcher<T> containing (final Matcher<S> matcher) {
		return new Matcher<T> () {
			@Override public boolean matches (T value) {
				for ( S s : value ) {
					if ( matcher.matches (s) ) { return true; }
				}
				return false;
			}
			@Override public String describe () {
				return "a collection with an element that matches " + matcher.describe ();
			}
		};
	}

	private static <T> String formatError (T value, Matcher<T> matcher) {
		return String.format ("Argument did not validate\nexpected: %s\nbut got: %s",
			matcher.describe (), matcher.describeMismatch (value)
		);
	}

}
