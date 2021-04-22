package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;


class ExceptionsTest {

	@Test
	void it_should_provide_runtime_exceptions () {
		assertThat (Exceptions.wrap (new Throwable ()), instanceOf (RuntimeException.class));
	}

	@Test
	void it_should_leave_runtime_exceptions_alone () {
		Exception original = new RuntimeException ("original");
		RuntimeException wrapped = Exceptions.wrap (original);

		assertThat (wrapped, is (original));
		assertThat (wrapped.getMessage (), is (original.getMessage ()));
	}

	@Test
	void it_should_have_the_same_message () {
		Exception original = new Exception ("original");
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (wrapped.getMessage (), is (original.getMessage ()));
	}

	@Test
	void it_should_have_the_same_localized_message () {
		Exception original = new Exception ("original");
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (wrapped.getLocalizedMessage (), is (original.getLocalizedMessage ()));
	}

	@Test
	void it_should_have_the_same_string_output () {
		Exception original = new Exception ("original");
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (wrapped.toString (), is (original.toString ()));
	}

	@Test
	void it_should_unwrap_the_original_exception () {
		Exception original = new Exception ("original");
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (wrapped.unwrap (), is (original));
	}

	@Test
	void it_should_unwrap_as_a_specified_exception () {
		Exception original = new TestException ();
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		TestException unwrapped = wrapped.unwrap (TestException.class);
		assertThat (unwrapped, is (original));
	}

	@Test
	void it_should_have_the_same_cause () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (wrapped.getCause (), is (cause));
	}

	@Test
	void it_should_have_the_same_stack_trace () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (getStreamedTrace (wrapped), is (getStreamedTrace (original)));
	}

	@Test
	void it_should_have_the_same_stack_trace_with_writers () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);
		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);
		assertThat (getWrittenTrace (wrapped), is (getWrittenTrace (original)));
	}

	@Test
	void it_should_produce_the_same_stack_trace () {
		Exception cause = new Exception ("cause");
		Exception original = new Exception ("original", cause);

		Exceptions.WrappedException wrapped = (Exceptions.WrappedException) Exceptions.wrap (original);

		StackTraceElement [] originalTrace = original.getStackTrace ();
		StackTraceElement [] wrappedTrace = wrapped.getStackTrace ();

		assertThat (wrappedTrace, arrayContaining (originalTrace));
	}

	@Test
	void it_should_wrap_null_causes () {
		Exception wrapped = Exceptions.wrap (null);
		assertThat (wrapped.getMessage (), nullValue ());
		assertThat (wrapped.getLocalizedMessage (), nullValue ());
		assertThat (wrapped.getCause (), nullValue ());
		assertThat (wrapped.toString (), containsString ("WrappedException"));
		assertThat (getWrittenTrace (wrapped), containsString ("WrappedException"));
		assertThat (getStreamedTrace (wrapped), containsString ("WrappedException"));
		assertThat (wrapped.getStackTrace ()[0].getClassName (), containsString ("Exceptions"));
	}

	private String getWrittenTrace (Exception ex) {
		ByteArrayOutputStream err = new ByteArrayOutputStream ();
		PrintWriter writer = new PrintWriter (err);
		ex.printStackTrace (writer);
		writer.flush ();
		return new String (err.toByteArray ());
	}

	private String getStreamedTrace (Exception e) {
		ByteArrayOutputStream err = new ByteArrayOutputStream ();
		PrintStream stream = new PrintStream (err);
		e.printStackTrace (new PrintStream (stream));
		stream.flush ();
		return new String (err.toByteArray ());
	}

	public static class TestException extends Exception {
	}

}
