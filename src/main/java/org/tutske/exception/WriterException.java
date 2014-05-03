package org.tutske.exception;

public class WriterException extends RuntimeException {

	private static final long serialVersionUID = 2780502243503864195L;

	public WriterException () { super (); }
	public WriterException (String msg) { super (msg); }
	public WriterException (Throwable cause) { super (cause); }
	public WriterException (String msg, Throwable cause) { super (msg, cause); }

}
