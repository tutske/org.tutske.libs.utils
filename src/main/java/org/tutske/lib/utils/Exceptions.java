package org.tutske.lib.utils;

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
			super (throwable);
			this.throwable = throwable;
		}

		public Throwable unwrap () { return throwable; }
		public <T> T unwrap (Class<T> clazz) { return (T) throwable; }

		public String getMessage () {
			return throwable == null ? super.getMessage () : throwable.getMessage ();
		}

		public String getLocalizedMessage () {
			return throwable == null ? super.getMessage () : throwable.getMessage ();
		}

		public synchronized Throwable getCause () {
			return throwable == null ? super.getCause () : throwable.getCause ();
		}

		public synchronized Throwable initCause (Throwable cause) {
			return throwable == null ? super.initCause (cause) : throwable.initCause (cause);
		}

		public String toString () {
			return throwable == null ? super.toString () : throwable.toString ();
		}

		public void printStackTrace () {
			if ( throwable == null ) { super.printStackTrace (); }
			else { throwable.printStackTrace (); }
		}

		public void printStackTrace (PrintStream stream) {
			if ( throwable == null ) { super.printStackTrace (stream); }
			else { throwable.printStackTrace (stream); }
		}

		public void printStackTrace (PrintWriter writer) {
			if ( throwable == null ) { super.printStackTrace (writer); }
			else { throwable.printStackTrace (writer); }
		}

		public StackTraceElement[] getStackTrace () {
			return throwable == null ? super.getStackTrace () : throwable.getStackTrace ();
		}

		public void setStackTrace (StackTraceElement[] stackTrace) {
			if ( throwable == null ) { super.setStackTrace (stackTrace); }
			else { throwable.setStackTrace (stackTrace); }
		}

		public synchronized Throwable fillInStackTrace () {
			return throwable == null ? super.fillInStackTrace () : throwable.fillInStackTrace ();
		}
	}

}
