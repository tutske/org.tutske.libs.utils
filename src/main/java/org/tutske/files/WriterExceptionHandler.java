package org.tutske.files;

import org.tutske.exception.ExceptionHandler;
import org.tutske.exception.WriterException;

public class WriterExceptionHandler implements ExceptionHandler {

	private Exception exception;

	public static WriterExceptionHandler newFor (Exception exception) {
		return new WriterExceptionHandler (exception);
	}

	public WriterExceptionHandler (Exception exception) {
		this.exception = exception;
	}

	@Override
	public void ignore () {
	}

	@Override
	public void handle () {
		throw new WriterException (exception);
	}

}
