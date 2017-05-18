package org.tutske.utils;

import java.util.Date;


public interface Clock {

	public static class SystemClock implements Clock {
		@Override public Date now () {
			return new Date (System.currentTimeMillis ());
		}
	}

	public Date now ();

}
