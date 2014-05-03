package org.tutske.files;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.tutske.files.FileResolver;
import org.tutske.files.RegularFileResolver;


public class RegularFileResolverTest {

	private String filename;
	private FileResolver resolver;


	@Before
	public void setup () {
		filename = "test.txt";
		resolver = new RegularFileResolver ();
	}

	@Test
	public void it_should_produce_a_file_with_the_right_name () {
		File file = resolver.getFile (filename);
		assertThat (file.getName (), is (filename));
	}

	@Test
	public void it_should_have_the_filename_in_the_full_path () {
		File file = resolver.getFile (filename);
		assertThat (file.getAbsolutePath (), containsString (filename));
	}

}
