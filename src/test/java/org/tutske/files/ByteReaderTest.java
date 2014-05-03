package org.tutske.files;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tutske.exception.ReaderException;
import org.tutske.files.ByteReader;
import org.tutske.files.Reader;


public class ByteReaderTest {

	private static final int ARRAY_SIZE = 20;

	private byte [] bytes;
	private InputStream stream;
	private Reader<byte []> reader;

	@Before
	public void setup () {
		bytes = new byte [ARRAY_SIZE];
		new Random ().nextBytes (bytes);
		stream = new ByteArrayInputStream (bytes);
		reader = new ByteReader (stream);
	}

	@Test
	public void it_should_read_empty_streams () {
		byte [] bytes = new byte [0];
		InputStream stream = new ByteArrayInputStream (bytes);

		reader = new ByteReader (stream);
		byte [] result = reader.read ();

		assertThat (result.length, is (0));
		assertThat (result, equalTo (bytes));
	}

	@Test
	public void it_should_provide_bytes_exactly () {
		byte [] result = reader.read ();
		assertThat (result, equalTo (bytes));
	}

	@Test
	public void it_should_not_try_to_close_a_given_stream () throws IOException {
		InputStream stream = mock (InputStream.class);
		doThrow (new IOException ()).when (stream).close ();

		reader = new ByteReader (stream);
	}

	@Test (expected = ReaderException.class)
	public void it_should_throw_the_appropriate_exception_when_stream_fails ()
	throws IOException {
		InputStream stream = mock (InputStream.class);
		doThrow (new IOException ()).when (stream)
			.read (Mockito.any (byte [].class), anyInt (), anyInt ());

		reader = new ByteReader (stream);
		reader.read ();

		fail ("The read method should throw an exception");
	}

	@Test
	public void it_should_read_from_files () throws IOException {
		File file = File.createTempFile ("test", "ext");
		reader = new ByteReader (file);
		assertThat (reader.read ().length, is (0));
	}

}
