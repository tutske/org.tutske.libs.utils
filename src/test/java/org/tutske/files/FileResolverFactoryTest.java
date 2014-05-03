package org.tutske.files;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.tutske.files.FileResolver;
import org.tutske.files.FileResolverFactory;
import org.tutske.utils.XDate;

public class FileResolverFactoryTest {

	@Test
	public void it_should_have_a_regular_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		assertThat (resolver, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_backup_file_resolver () {
		FileResolver resolver = new FileResolverFactory ().backupFileResolver();
		assertThat (resolver, not (nullValue ()));
	}

	@Test
	public void it_should_have_a_backup_file_resolver_based_on_date () {
		XDate date = XDate.newCurrentDate();
		FileResolver resolver = new FileResolverFactory ().backupFileResolver(date);
		assertThat (resolver, not (nullValue ()));
	}

}
