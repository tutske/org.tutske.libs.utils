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
public class ArrayConcatByteTest {

	@Parameter (value = 0) public byte [] firsts;
	@Parameter (value = 1) public byte [] seconds;
	@Parameter (value = 2) public byte [] thirds;

	@Parameter (value = 3) public byte [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new byte [] {},
				null,
				null,
				new byte [] {},
			},{
				new byte [] { 12, 34, 56 },
				null,
				null,
				new byte [] { 12, 34, 56 },
			},{
				new byte [] { 12, 34, 56 },
				new byte [] {},
				null,
				new byte [] { 12, 34, 56 }
			},{
				new byte [] {},
				new byte [] { 12, 34, 56 },
				null,
				new byte [] { 12, 34, 56 }
			},{
				new byte [] { 12, 34, 56 },
				new byte [] { 78, 90, 100},
				new byte [] {},
				new byte [] { 12, 34, 56, 78, 90, 100 }
			},{
				new byte [] { 12, 34, 56 },
				new byte [] { 78, 90, 100},
				new byte [] {},
				new byte [] { 12, 34, 56, 78, 90, 100 }
			},{
				new byte [] {},
				new byte [] { 12, 34, 56 },
				new byte [] { 78, 90, 100},
				new byte [] { 12, 34, 56, 78, 90, 100 }
			},{
				new byte [] { 12, 34, 56 },
				new byte [] { 78, 90, 100},
				new byte [] { 110, 120, 127},
				new byte [] { 12, 34, 56, 78, 90, 100, 110, 120, 127 }
			}
		});
	}

	public ArrayConcatByteTest () {
	}

	@Test
	public void it_should_concat_byte_arrays () {
		byte [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
