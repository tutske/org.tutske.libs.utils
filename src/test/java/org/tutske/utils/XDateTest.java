package org.tutske.utils;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Calendar;
import java.util.Date;

import org.junit.Before;
import org.junit.Test;
import org.tutske.utils.XDate;

public class XDateTest {

	Date basedate;
	XDate date;

	@Before
	public void setup () {
		Calendar c = Calendar.getInstance();
		c.set(2014, 1, 1, 12, 0, 0);
		basedate = c.getTime ();

		date = new XDate (basedate);
	}

	@Test
	public void it_shoudl_have_the_rigt_year () {
		assertThat (date.getYear (), is (2014));
	}

	@Test
	public void it_should_have_the_right_month () {
		assertThat (date.getMonth (), is (1));
	}

	@Test
	public void it_should_have_the_rigt_day () {
		assertThat (date.getDay (), is (1));
	}

	@Test
	public void it_should_have_the_right_hours () {
		assertThat (date.getHour (), is (12));
	}

	@Test
	public void it_should_have_the_rigth_minutes () {
		assertThat (date.getMinutes (), is (0));
	}

	@Test
	public void it_should_have_the_rigth_seconds () {
		assertThat (date.getSeconds (), is (0));
	}

	@Test
	public void it_should_have_milliseconds () {
		// milliseconds have not been specified with the calendar.
		assertThat (date.getMillis(), greaterThan (-1));
		assertThat (date.getMillis(), lessThan (1000));
	}

	@Test
	public void it_hould_have_the_rigt_timestamp () {
		assertThat (date.getTimestamp (), is (date.getTimestamp ()));
	}

}
