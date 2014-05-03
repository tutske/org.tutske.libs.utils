package org.tutske.exception;

public class ReaderException extends RuntimeException {

	private static final long serialVersionUID = 636671774175974898L;

	public ReaderException () { super (); }
	public ReaderException (String msg) { super (msg); }
	public ReaderException (Throwable cause) { super (cause); }
	public ReaderException (String msg, Throwable cause) { super (msg, cause); }

}
