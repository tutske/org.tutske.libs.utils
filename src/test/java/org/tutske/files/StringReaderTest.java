package org.tutske.files;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tutske.exception.ReaderException;
import org.tutske.files.Reader;
import org.tutske.files.StringReader;


public class StringReaderTest {

	private String content;
	private InputStream stream;
	private Reader<String> reader;

	@Before
	public void setup () {
		content = "Is this the real live?";
		byte [] bytes = content.getBytes(Charset.defaultCharset());
		stream = new ByteArrayInputStream (bytes);
		reader = new StringReader (stream, Charset.defaultCharset ());
	}

	@Test
	public void it_should_read_empty_streams () {
		InputStream stream = new ByteArrayInputStream (new byte [0]);

		reader = new StringReader (stream, Charset.defaultCharset ());
		String result = reader.read ();

		assertThat (result.length (), is (0));
		assertThat (result, equalTo (""));
	}

	@Test
	public void it_should_provide_bytes_exactly () {
		String result = reader.read ();
		assertThat (result, equalTo (content));
	}

	@Test
	public void it_should_not_try_to_close_a_given_stream () throws IOException {
		InputStream stream = mock (InputStream.class);
		doThrow (new IOException ()).when (stream).close ();

		reader = new StringReader (stream, Charset.defaultCharset ());
	}

	@Test (expected = ReaderException.class)
	public void it_should_throw_the_appropriate_exception_when_stream_fails () throws IOException {
		InputStream stream = mock (InputStream.class);
		doThrow (new IOException ()).when (stream)
			.read (Mockito.any (byte [].class), anyInt (), anyInt ());

		reader = new StringReader (stream, Charset.defaultCharset ());
		reader.read ();

		fail ("The read method should throw an exception");
	}

	@Test
	public void it_should_read_from_files () throws IOException {
		File file = File.createTempFile ("test", "ext");
		reader = new StringReader (file, Charset.defaultCharset ());
		assertThat (reader.read ().length (), is (0));
	}

}
