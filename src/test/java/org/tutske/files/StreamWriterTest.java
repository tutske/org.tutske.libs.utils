package org.tutske.files;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Random;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.tutske.exception.WriterException;
import org.tutske.files.StreamWriter;
import org.tutske.files.Writer;
import org.tutske.utils.ArrayConcat;


public class StreamWriterTest {

	private static final int RAND_SIZE = 5;

	ByteArrayOutputStream stream;
	Writer<InputStream> writer;

	@Before
	public void setup () {
		stream = new ByteArrayOutputStream ();
		writer = new StreamWriter (stream);
	}

	@Test
	public void it_should_write_nothing_at_all () {
		writer.write ();
		assertThat (stream.toByteArray ().length, is (0));
	}

	@Test
	public void it_should_have_the_right_length_when_writing_streams () {
		InputStream first = getRandomStream ();
		InputStream second = getRandomStream ();

		writer.write (first, second);

		assertThat (stream.toByteArray ().length, is (RAND_SIZE * 2));
	}

	@Test
	public void it_should_have_the_right_bytes () {
		byte [] bytes = new byte [] {12, 23, 45, 56};
		InputStream first = new ByteArrayInputStream (bytes);

		writer.write (first);

		assertThat (stream.toByteArray (), equalTo (bytes));
	}

	@Test
	public void it_should_have_all_bytes_when_writing_multiple_streams () {
		byte [] firstbytes = new byte [] {12, 23, 45, 56};
		InputStream first = new ByteArrayInputStream (firstbytes);
		byte [] secondbytes = new byte [] {112, 123, -45, 127};
		InputStream second = new ByteArrayInputStream (secondbytes);

		writer.write (first, second);

		byte [] expected = ArrayConcat.concat (firstbytes, secondbytes);
		assertThat (stream.toByteArray (), equalTo (expected));
	}

	@Test (expected = WriterException.class)
	public void it_should_throw_the_right_exception_if_the_stream_fails ()
	throws IOException {
		OutputStream stream = mock (OutputStream.class);
		doThrow (new IOException ()).when (stream).write(Mockito.any (byte [].class));
		doThrow (new IOException ()).when (stream).write(Mockito.any (byte [].class), anyInt (), anyInt ());

		writer = new StreamWriter (stream);
		writer.write (getRandomStream (), getRandomStream ());
		fail ("The write should cause an error");
	}

	@Test
	public void it_should_write_to_file () throws IOException {
		File file = File.createTempFile ("test", "ext");
		assumeThat (file.length (), is (0L));
		file.deleteOnExit();

		writer = new StreamWriter (file);
		writer.write (getRandomStream ());

		assertThat (file.length (), greaterThan (0L));
	}

	private static InputStream getRandomStream () {
		byte [] bytes = new byte [5];
		new Random ().nextBytes (bytes);
		return new ByteArrayInputStream (bytes);
	}

}
