package org.tutske.files;

import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;

import org.junit.Ignore;
import org.junit.Test;
import org.junit.Before;
import org.tutske.files.StringWriter;
import org.tutske.files.Writer;


public class StringWriterTest {

	private String first = "Something\n";
	private String second = "else\n";
	private String third = "The end\n";

	private ByteArrayOutputStream stream;
	private Writer<String> writer;

	@Before
	public void setup () {
		stream = new ByteArrayOutputStream ();
		writer = new StringWriter (stream, Charset.defaultCharset());
	}

	@Test
	public void it_should_write_strings () {
		writer.write (first, second, third);
		String result = new String (stream.toByteArray(), Charset.defaultCharset());
		assertThat (result, is (first + second + third));
	}

	@Test
	public void it_should_write_strings_when_fed () {
		writer.feed (first);
		writer.feed (second);
		writer.feed (third);
		writer.write ();

		String result = new String (stream.toByteArray(), Charset.defaultCharset());
		assertThat (result, is (first + second + third));
	}

	@Test
	public void it_should_write_when_fed_multiple_strings () {
		writer.feed (first, second, third);
		writer.write ();

		String result = new String (stream.toByteArray(), Charset.defaultCharset());
		assertThat (result, is (first + second + third));
	}

	@Test
	public void it_should_write_when_both_fed_and_written_to () {
		writer.feed (first, second);
		writer.write (third, third);

		String result = new String (stream.toByteArray(), Charset.defaultCharset());
		assertThat (result, is (first + second + third + third));
	}

	@Test
	public void it_should_chain_feed_and_write_calls () {
		writer.feed (first).feed (second).feed (third).write ();

		String result = new String (stream.toByteArray(), Charset.defaultCharset());
		assertThat (result, is (first + second + third));
	}

	@Test
	public void it_should_write_to_file () throws IOException {
		File file = File.createTempFile ("test", "ext");
		assumeThat (file.length (), is (0L));
		file.deleteOnExit();

		writer = new StringWriter (file, Charset.defaultCharset());
		writer.write (first);

		assertThat (file.length (), greaterThan (0L));
	}

	@Test
	@Ignore ("Not implemented yet.")
	public void it_should_not_close_the_stream () {
		
	}

}
