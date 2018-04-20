package org.tutske.utils;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.Charset;


public class ResourceTest {

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
	public void it_should_get_resources_from_files_uri_version () throws Exception {
		InputStream stream = Resource.getResource (new URI ("file://src/test/resources/file.txt"));
		assertThat (read (stream), is ("Content of file."));
	}

	@Test
	public void it_should_get_resources_from_classpath_uri_version () throws Exception {
		InputStream stream = Resource.getResource (new URI ("resource://file.txt"));
		assertThat (read (stream), is ("Content of file."));
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
