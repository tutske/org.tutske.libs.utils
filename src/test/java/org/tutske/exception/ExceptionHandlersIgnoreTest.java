package org.tutske.exception;

import org.junit.Test;
import org.tutske.exception.BaseExceptionHandler;
import org.tutske.files.ReaderExceptionHandler;
import org.tutske.files.WriterExceptionHandler;

public class ExceptionHandlersIgnoreTest {

	@Test
	public void it_should_ignore_exceptions_BasicExceptionHandler () {
		try { riskyMethod (); }
		catch (Exception e) { BaseExceptionHandler.newFor (e).ignore (); }
	}

	@Test
	public void it_should_ignore_exceptions_WriterExceptionHandler () {
		try { riskyMethod (); }
		catch (Exception e) { WriterExceptionHandler.newFor (e).ignore (); }
	}

	@Test
	public void it_should_ignore_exceptions_ReaderExceptionHandler () {
		try { riskyMethod (); }
		catch (Exception e) { ReaderExceptionHandler.newFor (e).ignore (); }
	}

	private void riskyMethod () throws Exception {
		throw new Exception ("test exception");
	}

}
