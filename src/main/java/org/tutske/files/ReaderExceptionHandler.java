package org.tutske.files;

import org.tutske.exception.ExceptionHandler;
import org.tutske.exception.ReaderException;

public class ReaderExceptionHandler implements ExceptionHandler {

	private Exception exception;

	public static ExceptionHandler newFor (Exception exception) {
		return new ReaderExceptionHandler (exception);
	}

	public ReaderExceptionHandler (Exception exception) {
		this.exception = exception;
	}

	@Override
	public void ignore () {
	}

	@Override
	public void handle () {
		throw new ReaderException (exception);
	}

}
