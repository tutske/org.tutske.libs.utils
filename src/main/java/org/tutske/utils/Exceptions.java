package org.tutske.utils;

import java.io.PrintStream;
import java.io.PrintWriter;


public class Exceptions {

	public static RuntimeException wrap (Throwable throwable) {
		if ( throwable instanceof RuntimeException ) { return (RuntimeException) throwable; }
		return new WrappedException (throwable);
	}

	public static class WrappedException extends RuntimeException {
		private final Throwable throwable;

		public WrappedException (Throwable throwable) {
			this.throwable = throwable;
		}

		public Throwable unwrap () { return throwable; }
		public <T> T unwrap (Class<T> clazz) { return (T) throwable; }

		public String getMessage () { return throwable.getMessage (); }
		public String getLocalizedMessage () { return getMessage (); }
		public synchronized Throwable getCause () { return throwable.getCause (); }
		public synchronized Throwable initCause (Throwable cause) { return throwable.initCause (cause); }
		public String toString () { return throwable.toString (); }
		public void printStackTrace () { throwable.printStackTrace (); }
		public void printStackTrace (PrintStream stream) { throwable.printStackTrace (stream); }
		public void printStackTrace (PrintWriter writer) { throwable.printStackTrace (writer); }
		public StackTraceElement[] getStackTrace () { return throwable.getStackTrace (); }
		public void setStackTrace (StackTraceElement[] stackTrace) { throwable.setStackTrace (stackTrace); }

		public synchronized Throwable fillInStackTrace () {
			return throwable == null ? super.fillInStackTrace () : throwable.fillInStackTrace ();
		}
	}

}
