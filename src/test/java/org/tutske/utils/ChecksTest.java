package org.tutske.utils;

import static org.junit.Assert.fail;
import static org.tutske.utils.Checks.*	;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.List;


@RunWith (Parameterized.class)
public class ChecksTest {

	@Parameterized.Parameters (name = "label: {0}")
	public static List<Object []> data () {
		return Arrays.asList (new Object [][] {
			{
				"between zero and ten, failure",
				between (0, 10),
				new Integer [] { 10 },
				true
			},
			{
				"between zero and ten, success",
				between (0, 10),
				new Integer [] { 9, 0, 7, 3 },
				false
			},

			{
				"null value, failure",
				nullValue (),
				new Object [] { new Object () },
				true
			},
			{
				"null value, success",
				nullValue (),
				new Object [] { null },
				false
			},

			{
				"not null value, failure",
				notNull (),
				new Object [] { null },
				true
			},
			{
				"not null value, success",
				notNull (),
				new Object [] { new Object () },
				false
			},

			{
				"direct comparison, failure",
				is (true),
				new Boolean [] { false },
				true,
			},
			{
				"direct comparison, success",
				is (true),
				new Boolean [] { true },
				false,
			},

			{
				"empty string, failure",
				emptyString (),
				new String [] { "non empty string" },
				true,
			},
			{
				"empty string, success",
				emptyString (),
				new String [] { "" },
				false,
			},

			{
				"non empty string, failure",
				nonEmptyString (),
				new String [] { "" },
				true,
			},
			{
				"non empty string, success",
				nonEmptyString (),
				new String [] { "non empty string" },
				false,
			},

			{
				"empty collection, failure",
				empty (),
				new List [] { Arrays.asList ("something") },
				true,
			},
			{
				"empty collection, success",
				empty (),
				new List [] { Arrays.asList () },
				false,
			},

			{
				"not empty collection, failure",
				notEmpty (),
				new List [] { Arrays.asList () },
				true,
			},
			{
				"not empty collection, success",
				notEmpty (),
				new List [] { Arrays.asList ("something") },
				false,
			},

			{
				"all of matchers should match, failure (first)",
				allOf (between (0, 50), between (0, 40), between (0, 30)),
				new Integer [] { 60 },
				true
			},
			{
				"all of matchers should match, failure (second)",
				allOf (between (0, 50), between (0, 40), between (0, 30)),
				new Integer [] { 45 },
				true
			},
			{
				"all of matchers should match, failure (third)",
				allOf (between (0, 50), between (0, 40), between (0, 30)),
				new Integer [] { 35 },
				true
			},
			{
				"all of matchers should match, success",
				allOf (between (0, 50), between (0, 40), between (0, 30)),
				new Integer [] { 25 },
				false
			},

			{
				"any of matchers should match, failure",
				anyOf (emptyString (), is ("empty string")),
				new String [] { "not empty" },
				true
			},
			{
				"any of matchers should match, success (first",
				anyOf (emptyString (), is ("empty string")),
				new String [] { "" },
				false
			},
			{
				"any of matchers should match, success (second",
				anyOf (emptyString (), is ("empty string")),
				new String [] { "empty string" },
				false
			},

			{
				"complex matcher chaining",
				allOf (
					containing (not (between (0, 10))),
					containing (not (between (20, 30)))
				),
				new List [] { Arrays.asList (0, 9) },
				true
			}
		});
	}

	@Parameterized.Parameter (value = 0) public String label;
	@Parameterized.Parameter (value = 1) public Matcher matcher;
	@Parameterized.Parameter (value = 2) public Object [] values;
	@Parameterized.Parameter (value = 3) public boolean expect;

	@Test
	public void it_should_validate_parameters () {
		Exception exception = null;

		try { assure (matcher, values); }
		catch (Exception e) { exception = e; }

		if ( expect && exception == null ) {
			fail ("Did not throw an exception");
		}
		if ( expect && ! (exception instanceof IllegalArgumentException) ) {
			fail ("Got different exception: " + exception.getClass ());
		}
	}

}
