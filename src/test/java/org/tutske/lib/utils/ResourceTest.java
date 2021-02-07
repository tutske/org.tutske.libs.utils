package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;


public class ResourceTest {

	@Test
	public void it_should_get_resources_from_the_path_of_a_file () throws Exception {
		InputStream stream = Resource.getResource ("src/test/resources/file.txt");
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_get_resources_from_files () throws Exception {
		InputStream stream = Resource.getResource ("file://src/test/resources/file.txt");
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_get_resources_from_classpath () throws Exception {
		InputStream stream = Resource.getResource ("resource://file.txt");
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_get_resources_from_classpath_with_classpath_prefix () throws Exception {
		InputStream stream = Resource.getResource ("classpath://file.txt");
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_get_resources_from_files_uri_version () throws Exception {
		InputStream stream = Resource.getResource (new URI ("file://src/test/resources/file.txt"));
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_get_resources_from_classpath_uri_version () throws Exception {
		InputStream stream = Resource.getResource (new URI ("resource://file.txt"));
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_complain_when_passed_a_null_uri () throws Exception {
		assertThrows (Exception.class, () -> {
			Resource.getResource ((URI) null);
		});
	}

	@Test
	public void it_should_complain_when_passed_a_null_string () throws Exception {
		assertThrows (Exception.class, () -> {
			Resource.getResource ((String) null);
		});
	}

	@Test
	public void it_should_complain_when_the_uri_scheme_is_not_supported () throws Exception {
		assertThrows (Exception.class, () -> {
			Resource.getResource ("bogus://file.txt");
		});
	}

	@Test
	public void it_should_complain_when_the_passed_string_does_not_respresent_a_uri () throws Exception {
		assertThrows (Exception.class, () -> {
			Resource.getResource ("this is not a uri to a resource");
		});
	}

	private String read (InputStream stream) throws IOException {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		int index = 0;
		byte [] buffer = new byte [1 << 13];
		while ( (index = stream.read (buffer)) != -1 ) {
			out.write (buffer, 0, index);
		}

		return new String (out.toByteArray (), Charset.forName ("utf-8"));
	}

}
