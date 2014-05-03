package org.tutske.files;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;

import java.io.File;

import org.junit.Before;
import org.junit.Test;
import org.tutske.files.BackupFileResolver;
import org.tutske.files.FileResolver;
import org.tutske.utils.XDate;


public class BackupFileResolverTest {

	private String filename;
	private XDate date;
	private FileResolver resolver;

	@Before
	public void setup () {
		filename = "test.txt";
		date = XDate.newCurrentDate ();
		resolver = new BackupFileResolver (date);
	}

	@Test
	public void it_should_produce_a_file_with_the_right_name () {
		File file = resolver.getFile (filename);
		assertThat (file.getName (), containsString (filename));
	}

	@Test
	public void it_should_produce_a_file_based_on_the_date () {
		File file = resolver.getFile (filename);
		String datestring = date.getTimestampString ();
		assertThat (file.getName (), containsString (datestring));
	}

}