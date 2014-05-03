package org.tutske.exception;

import org.junit.Test;
import org.tutske.exception.BaseExceptionHandler;
import org.tutske.exception.ReaderException;
import org.tutske.exception.WriterException;
import org.tutske.files.ReaderExceptionHandler;
import org.tutske.files.WriterExceptionHandler;


public class ExceptionHandlerHandlerTest {

	@Test (expected = RuntimeException.class)
	public void it_should_handle_exceptions_BasicExceptionHandler () {
		try { riskyMethod (); }
		catch (Exception e) { BaseExceptionHandler.newFor (e).handle (); }
	}

	@Test (expected = WriterException.class)
	public void it_should_handle_exceptions_WriterExceptionHandler () {
		try { riskyMethod (); }
		catch (Exception e) { WriterExceptionHandler.newFor (e).handle (); }
	}

	@Test (expected = ReaderException.class)
	public void it_should_handle_exceptions_ReaderExceptionHandler () {
		try { riskyMethod (); }
		catch (Exception e) { ReaderExceptionHandler.newFor (e).handle (); }
	}

	private void riskyMethod () throws Exception {
		throw new Exception ("test exception");
	}

}
