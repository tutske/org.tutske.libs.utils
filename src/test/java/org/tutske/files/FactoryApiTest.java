package org.tutske.files;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.junit.Test;
import org.tutske.files.FileResolver;
import org.tutske.files.FileResolverFactory;
import org.tutske.files.Reader;
import org.tutske.files.ReaderFactories;
import org.tutske.files.ReaderFactory;
import org.tutske.files.StringReaderFactory;
import org.tutske.files.StringWriterFactory;
import org.tutske.files.Writer;
import org.tutske.files.WriterFactories;
import org.tutske.files.WriterFactory;


public class FactoryApiTest {

	@Test
	public void it_should_have_a_string_reader () {
		StringReaderFactory readers = ReaderFactories.getStringReaderFactory();
		assertThat (readers, not (nullValue ()));

		Reader<String> reader;
		reader = readers.newInstance(new File (""));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance (new ByteArrayInputStream (new byte [0]));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance("");
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance(new File (""), Charset.defaultCharset());
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance (new ByteArrayInputStream (new byte [0]), Charset.defaultCharset());
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance("", Charset.defaultCharset());
		assertThat (reader, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_string_reader_with_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		StringReaderFactory readers = ReaderFactories.getStringReaderFactory(resolver);
		assertThat (readers, not (nullValue ()));

		Reader<String> reader;
		reader = readers.newInstance(new File (""));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance (new ByteArrayInputStream (new byte [0]));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance("");
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance(new File (""), Charset.defaultCharset());
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance (new ByteArrayInputStream (new byte [0]), Charset.defaultCharset());
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance("", Charset.defaultCharset());
		assertThat (reader, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_byte_array_reader () {
		ReaderFactory<byte []> readers = ReaderFactories.getByteReaderFactory();
		assertThat (readers, not (nullValue ()));

		Reader<byte []> reader;
		reader = readers.newInstance(new File (""));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance (new ByteArrayInputStream (new byte [0]));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance("");
		assertThat (reader, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_byte_array_reader_with_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		ReaderFactory<byte []> readers = ReaderFactories.getByteReaderFactory(resolver);
		assertThat (readers, not (nullValue ()));

		Reader<byte []> reader;
		reader = readers.newInstance(new File (""));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance (new ByteArrayInputStream (new byte [0]));
		assertThat (reader, not (nullValue ()));

		reader = readers.newInstance("");
		assertThat (reader, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_byte_array_writer () {
		WriterFactory<byte []> writers = WriterFactories.getByteWriterFactory ();
		assertThat (writers, not (nullValue ()));

		Writer<byte []> writer;
		writer = writers.newInstance(new File (""));
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream ());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("");
		assertThat (writer, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_byte_array_writer_with_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		WriterFactory<byte []> writers = WriterFactories.getByteWriterFactory (resolver);
		assertThat (writers, not (nullValue ()));

		Writer<byte []> writer;
		writer = writers.newInstance(new File (""));
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream ());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("");
		assertThat (writer, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_string_writer () {
		StringWriterFactory writers = WriterFactories.getStringWriterFactory ();
		assertThat (writers, not (nullValue ()));

		Writer<String> writer;
		writer = writers.newInstance(new File (""));
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream ());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("");
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance(new File (""), Charset.defaultCharset());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream (), Charset.defaultCharset());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("", Charset.defaultCharset());
		assertThat (writer, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_string_writer_with_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		StringWriterFactory writers = WriterFactories.getStringWriterFactory (resolver);
		assertThat (writers, not (nullValue ()));

		Writer<String> writer;
		writer = writers.newInstance(new File (""));
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream ());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("");
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance(new File (""), Charset.defaultCharset());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream (), Charset.defaultCharset());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("", Charset.defaultCharset());
		assertThat (writer, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_stream_writer () {
		WriterFactory<InputStream> writers = WriterFactories.getStreamWriterFactory ();
		assertThat (writers, not (nullValue ()));

		Writer<InputStream> writer;
		writer = writers.newInstance(new File (""));
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream ());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("");
		assertThat (writer, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_stream_writer_with_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		WriterFactory<InputStream> writers = WriterFactories.getStreamWriterFactory (resolver);
		assertThat (writers, not (nullValue ()));

		Writer<InputStream> writer;
		writer = writers.newInstance(new File (""));
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance (new ByteArrayOutputStream ());
		assertThat (writer, not (nullValue ()));

		writer = writers.newInstance("");
		assertThat (writer, not (nullValue ()));
	}

}
