package org.tutske.utils;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import java.util.Arrays;
import java.util.Collection;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.tutske.utils.ArrayConcat;

import static org.junit.runners.Parameterized.Parameters;
import static org.junit.runners.Parameterized.Parameter;


@RunWith (Parameterized.class)
public class ArrayConcatShortTest {

	@Parameter (value = 0) public short [] firsts;
	@Parameter (value = 1) public short [] seconds;
	@Parameter (value = 2) public short [] thirds;

	@Parameter (value = 3) public short [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new short [] {},
				null,
				null,
				new short [] {},
			},{
				new short [] { 12, 34, 56 },
				null,
				null,
				new short [] { 12, 34, 56 },
			},{
				new short [] { 12, 34, 56 },
				new short [] {},
				null,
				new short [] { 12, 34, 56 }
			},{
				new short [] {},
				new short [] { 12, 34, 56 },
				null,
				new short [] { 12, 34, 56 }
			},{
				new short [] { 12, 34, 56 },
				new short [] { 78, 90, 100},
				new short [] {},
				new short [] { 12, 34, 56, 78, 90, 100 }
			},{
				new short [] { 12, 34, 56 },
				new short [] { 78, 90, 100},
				new short [] {},
				new short [] { 12, 34, 56, 78, 90, 100 }
			},{
				new short [] {},
				new short [] { 12, 34, 56 },
				new short [] { 78, 90, 100},
				new short [] { 12, 34, 56, 78, 90, 100 }
			},{
				new short [] { 12, 34, 56 },
				new short [] { 78, 90, 100},
				new short [] { 110, 120, 127},
				new short [] { 12, 34, 56, 78, 90, 100, 110, 120, 127 }
			}
		});
	}

	public ArrayConcatShortTest () {
	}

	@Test
	public void it_should_concat_short_arrays () {
		short [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
