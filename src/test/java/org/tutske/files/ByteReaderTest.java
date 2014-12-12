package org.tutske.files;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.*;
import java.util.Random;

import org.junit.*;
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
		when (stream.read (Mockito.any (byte [].class), Mockito.any (int.class), Mockito.any (int.class)))
			.thenReturn (-1);

		reader = new ByteReader (stream);
		reader.read ();
	}

	@Test
	public void it_should_close_the_stream_if_it_is_asked_to_do_so () throws IOException {
		InputStream stream = mock (InputStream.class);
		when (stream.read (Mockito.any (byte [].class), Mockito.any (int.class), Mockito.any (int.class)))
			.thenReturn (-1);

		reader = new ByteReader (stream);
		reader.readAndClose ();

		verify (stream).close ();
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
