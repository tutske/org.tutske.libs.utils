package org.tutske.exception;


class BaseExceptionHandler implements ExceptionHandler {

	private Exception exception;

	public static ExceptionHandler newFor (Exception exception) {
		return new BaseExceptionHandler (exception);
	}

	public BaseExceptionHandler (Exception exception) {
		this.exception = exception;
	}

	@Override
	public void ignore () {
	}

	@Override
	public void handle () {
		throw new RuntimeException (exception);
	}

}
