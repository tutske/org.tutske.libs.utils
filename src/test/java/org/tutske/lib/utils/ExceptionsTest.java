package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;


public class ExceptionsTest {

	@Test
	public void it_should_provide_runtime_exceptions () {
		assertThat (Exceptions.wrap (new Throwable ()), instanceOf (RuntimeException.class));
	}

	@Test
	public void it_should_leave_runtime_exceptions_alone () {
		Exception original = new RuntimeException ("original");
		RuntimeException wrapped = Exceptions.wrap (original);

		assertThat (wrapped, is (original));
		assertThat (wrapped.getMessage (), is (original.getMessage ()));
	}

	@Test
	public void it_should_have_the_same_message () {
		Exception original = new Exception ("original");
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		assertThat (wrapped.getMessage (), is (original.getMessage ()));
	}

	@Test
	public void it_should_unwrap_the_original_exception () {
		Exception original = new Exception ("original");
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		assertThat (wrapped.unwrap (), is (original));
	}

	@Test
	public void it_should_unwrap_as_a_specified_exception () {
		Exception original = new TestException ();
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		TestException unwrapped = wrapped.unwrap (TestException.class);

		assertThat (unwrapped, is (original));
	}

	@Test
	public void it_should_have_the_same_cause () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);

		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		assertThat (wrapped.getCause (), is (cause));
	}

	@Test
	public void it_should_have_the_same_stack_trace () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);

		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		ByteArrayOutputStream originalOut = new ByteArrayOutputStream ();
		original.printStackTrace (new PrintStream (originalOut));

		ByteArrayOutputStream wrappedOut = new ByteArrayOutputStream ();
		wrapped.printStackTrace (new PrintStream (wrappedOut));

		assertThat (new String (wrappedOut.toByteArray ()), is (new String (originalOut.toByteArray ())));
	}

	@Test
	public void it_should_have_the_same_stack_trace_with_writers () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);

		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		ByteArrayOutputStream originalOut = new ByteArrayOutputStream ();
		original.printStackTrace (new PrintWriter (originalOut));

		ByteArrayOutputStream wrappedOut = new ByteArrayOutputStream ();
		wrapped.printStackTrace (new PrintWriter (wrappedOut));

		assertThat (new String (wrappedOut.toByteArray ()), is (new String (originalOut.toByteArray ())));
	}

	@Test
	public void it_should_produce_the_same_stack_trace () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);

		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		StackTraceElement [] originalTrace = original.getStackTrace ();
		StackTraceElement [] wrappedTrace = wrapped.getStackTrace ();

		assertThat (wrappedTrace, arrayContaining (originalTrace));
	}

	public static class TestException extends Exception {
	}

}
