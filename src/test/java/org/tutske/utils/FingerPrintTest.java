package org.tutske.utils;

import static org.junit.Assert.assertThat;
import static org.tutske.utils.RegexMatcher.matches;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.tutske.utils.FingerPrint;
import org.tutske.utils.XDate;


public class FingerPrintTest {

	private FingerPrint fingerprint;

	@Test
	public void it_should_have_a_uuid () {
		fingerprint = FingerPrint.newInstance ();
		assertThat (fingerprint.getUUID (), not (nullValue ()));
	}

	@Test
	public void it_should_have_the_uuid_as_string () {
		fingerprint = FingerPrint.newInstance ();
		String uuid = fingerprint.getUUIDAsString ();
		String pattern = "[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}";
		assertThat (uuid, matches (pattern));
	}

	@Test
	public void it_should_have_a_date () {
		fingerprint = FingerPrint.newInstance ();
		assertThat (fingerprint.getTime (), not (nullValue ()));

		XDate date = XDate.newCurrentDate();
		fingerprint = new FingerPrint (date);
		assertThat (fingerprint.getTime (), is (date));
	}

	@Test
	public void it_should_format_the_date () {
		XDate date = XDate.newCurrentDate();
		fingerprint = new FingerPrint (date);
		assertThat (fingerprint.getTimestampString(), not (nullValue ()));
	}

	@Test
	public void it_should_format_the_date_as_a_long () {
		XDate date = XDate.newCurrentDate();
		fingerprint = new FingerPrint (date);
		assertThat (fingerprint.getTimestamp (), is (date.getTimestamp()));
	}

}
