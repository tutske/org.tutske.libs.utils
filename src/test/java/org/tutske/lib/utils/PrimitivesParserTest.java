package org.tutske.lib.utils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.tutske.lib.utils.PrimitivesParser.*;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.function.Function;


public class PrimitivesParserTest {

	@Test
	public void it_should_parse_to_a_string () {
		assertThat (parse ("string", String.class), is ("string"));
	}

	@Test
	public void it_should_parse_to_a_number () {
		assertThat (parse ("1", Integer.class), is (1));
	}

	@Test
	public void it_should_parse_to_a_long () {
		assertThat (parse ("1", Long.class), is (1L));
	}

	@Test
	public void it_should_parse_to_a_float () {
		assertThat (parse ("1", Float.class), is (1.0F));
	}

	@Test
	public void it_should_parse_to_a_double () {
		assertThat (parse ("1", Double.class), is (1.0D));
	}

	@Test
	public void it_should_parse_to_a_boolean () {
		assertThat (parse ("true", Boolean.class), is (true));
		assertThat (parse ("false", Boolean.class), is (false));
	}

	@Test
	public void it_should_parse_dates () {
		assertThat (parse ("10000000", Date.class), is (new Date (10000000)));
	}

	@Test
	public void it_should_parse_iso_dates () {
		Date utc = createDate (TimeZone.getTimeZone ("UTC"), 2017, Calendar.MAY, 3, 21, 33, 6);
		Date zoned = createDate (TimeZone.getDefault (), 2017, Calendar.MAY, 3, 21, 33, 6);
		Date plus_one = createDate (TimeZone.getTimeZone ("GMT+01"), 2017, Calendar.MAY, 3, 21, 33, 6);

		assertThat (parse ("2017-05-03T21:33:06", Date.class), is (zoned));
		assertThat (parse ("2017-05-03T21:33:06.000", Date.class), is (zoned));
		assertThat (parse ("2017-05-03T21:33:06.000+0000", Date.class), is (utc));
		assertThat (parse ("2017-05-03T21:33:06.000+0100", Date.class), is (plus_one));
		assertThat (parse ("" + utc.getTime (), Date.class), is (utc));
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_about_non_parsable_dates () {
		parse ("remember remember the fifth of november", Date.class);
	}

	@Test (expected = RuntimeException.class)
	public void it_should_comaplain_when_different_types_cannot_be_converted_into_each_other () {
		parse (new Object (), Boolean.class);
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_if_it_does_not_know_how_to_convert_into_a_type () {
		parse ("", PrimitivesParser.class);
	}

	@Test
	public void it_should_convert_a_type_into_it_self () {
		TestType value = new TestType ();
		assertThat (parse (value, TestType.class), is (value));
	}

	@Test
	public void it_should_convert_a_type_and_an_asignable_class_to_itself () {
		TestType value = new TestType ();
		assertThat (parse (value, ITest.class), is (value));
	}

	@Test
	public void it_should_do_normal_string_parsing () {
		Object value = "true";
		assertThat (parse (value, Boolean.class), is (true));
	}

	@Test
	public void it_should_convert_anything_to_an_object () {
		TestType value = new TestType ();
		assertThat (parse (value, Object.class), is (value));
	}

	@Test
	public void it_should_be_able_to_convert_anything_into_a_string () {
		TestType value = new TestType () {
			@Override public String toString () {
				return "The Type To Test";
			}
		};

		assertThat (parse (value, String.class), is ("The Type To Test"));
	}

	@Test
	public void it_should_use_registered_converters () {
		PrimitivesParser.convert (TestType.class, Integer.class, (test) -> 1);
		TestType value = new TestType ();
		assertThat (parse (value, Integer.class), is (1));
	}

	@Test
	public void it_should_always_find_a_parser_to_strings () {
		Object test = new Object () {
			@Override public String toString () {
				return "Test Object";
			}
		};

		Function<Object, String> parser = (Function) PrimitivesParser.getParser (test.getClass (), String.class);
		assertThat (parser.apply (test), is ("Test Object"));
	}

	@Test
	public void it_should_always_find_a_parser_from_int_to_string () {
		Function<Integer, String> parser = PrimitivesParser.getParser (Integer.class, String.class);
		assertThat (parser.apply (12), is ("12"));
	}

	@Test
	public void it_should_give_a_converter_from_type_to_type () {
		Function<String, Long> converter = PrimitivesParser.getParser (String.class, Long.class);
		assertThat (converter.apply ("2"), is (2l));
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_when_it_does_not_know_how_to_convert_from_a_value () {
		PrimitivesParser.getParser (Map.class, List.class);
	}

	@Test (expected = RuntimeException.class)
	public void it_should_complain_when_it_dous_not_know_how_to_convert_into_a_value () {
		PrimitivesParser.getParser (String.class, List.class);
	}

	private Date createDate (TimeZone zone, int year, int month, int day, int hour, int minutes, int seconds) {
		Calendar calendar = Calendar.getInstance (zone);
		calendar.set (year, month, day, hour, minutes, seconds);
		calendar.set (Calendar.MILLISECOND, 0);

		return calendar.getTime ();
	}

	private static interface ITest {
	}

	private static class TestType implements ITest {
	}

}
