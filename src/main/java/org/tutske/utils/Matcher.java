package org.tutske.utils;

public interface Matcher<T> {

	public boolean matches (T value);
	public String describe ();
	default public String describeMismatch (T value) {
		return value == null ? "null" : value.toString ();
	}

}
