package org.tutske.utils;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.junit.Test;
import org.junit.Before;
import org.tutske.utils.StreamCopier;

public class StreamCopierTest {

	private final static int ARRAY_SIZE = 20;

	byte [] bytes;
	private InputStream inputstream;
	private ByteArrayOutputStream outputstream;

	@Before
	public void setup () {
		bytes = new byte [ARRAY_SIZE];
		new Random ().nextBytes (bytes);
		inputstream = new ByteArrayInputStream (bytes);
		outputstream = new ByteArrayOutputStream ();
	}

	@Test
	public void it_should_copy_empty_stream () throws IOException {
		byte [] bytes = new byte [0];
		InputStream inputstream = new ByteArrayInputStream (bytes);
		ByteArrayOutputStream outputstream = new ByteArrayOutputStream ();

		new StreamCopier (inputstream, outputstream).copy ();

		assertThat (outputstream.toByteArray (), equalTo (bytes));
	}

	@Test
	public void it_should_copy_stream_exactly () throws IOException {
		new StreamCopier (inputstream, outputstream).copy ();
		assertThat (outputstream.toByteArray (), equalTo (bytes));
	}

}
