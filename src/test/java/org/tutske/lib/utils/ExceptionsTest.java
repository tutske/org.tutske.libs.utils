package org.tutske.lib.utils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import org.junit.Test;


public class ExceptionsTest {

	@Test
	public void it_should_provide_runtime_exceptions () {
		RuntimeException wrapped = Exceptions.wrap (new Throwable ());
	}

	@Test
	public void it_should_leave_runtime_exceptions_alone () {
		Exception original = new RuntimeException ("original");
		RuntimeException wrapped = Exceptions.wrap (original);

		assertThat (wrapped, is (original));
		assertThat (wrapped.getMessage (), is (original.getMessage ()));
	}

}
