package org.tutske.files;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.Before;
import org.mockito.Mockito;
import org.tutske.exception.WriterException;
import org.tutske.files.ByteWriter;
import org.tutske.files.Writer;
import org.tutske.utils.ArrayConcat;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Random;


public class ByteWriterTest {

	private static final int BYTE_ARRAY_SIZE = 5;

	private byte [] firsts;
	private byte [] seconds;
	private byte [] thirds;

	private ByteArrayOutputStream stream;
	private Writer<byte []> writer;

	@Before
	public void setup () {
		firsts = new byte [BYTE_ARRAY_SIZE];
		seconds = new byte [BYTE_ARRAY_SIZE];
		thirds = new byte [BYTE_ARRAY_SIZE];

		Random random = new Random ();
		random.nextBytes (firsts);
		random.nextBytes (seconds);
		random.nextBytes (thirds);

		stream = new ByteArrayOutputStream ();
		writer = new ByteWriter (stream);
	}

	@Test
	public void it_should_write_bytes () {
		writer.write (firsts, seconds, thirds);

		byte [] expected = ArrayConcat.concat (firsts, seconds, thirds);
		assertThat (stream.toByteArray (), equalTo (expected));
	}

	@Test
	public void it_should_write_arrays_of_bytes () {
		writer.write (firsts, seconds, thirds);

		byte [] expected = ArrayConcat.concat (firsts, seconds, thirds);
		assertThat (stream.toByteArray (), equalTo (expected));
	}

	@Test (expected = WriterException.class)
	public void it_should_throw_the_right_exception_if_the_stream_fails ()
	throws IOException {
		OutputStream stream = mock (OutputStream.class);
		doThrow (new IOException ()).when (stream).write(Mockito.any (byte [].class));
		doThrow (new IOException ()).when (stream).write(Mockito.any (byte [].class), anyInt (), anyInt ());

		writer = new ByteWriter (stream);
		writer.write (firsts);
		fail ("The write should cause an error");
	}

	@Test
	public void it_should_write_to_file () throws IOException {
		File file = File.createTempFile("test", "ext");
		assumeThat (file.length (), is (0L));
		file.deleteOnExit();

		writer = new ByteWriter (file);
		writer.write (new byte [20]);

		assertThat (file.length (), is (20L));
	}
}
