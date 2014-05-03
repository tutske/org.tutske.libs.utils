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
public class ArrayConcatIntTest {

	@Parameter (value = 0) public int [] firsts;
	@Parameter (value = 1) public int [] seconds;
	@Parameter (value = 2) public int [] thirds;

	@Parameter (value = 3) public int [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new int [] {},
				null,
				null,
				new int [] {},
			},{
				new int [] { 12, 34, 56 },
				null,
				null,
				new int [] { 12, 34, 56 },
			},{
				new int [] { 12, 34, 56 },
				new int [] {},
				null,
				new int [] { 12, 34, 56 }
			},{
				new int [] {},
				new int [] { 12, 34, 56 },
				null,
				new int [] { 12, 34, 56 }
			},{
				new int [] { 12, 34, 56 },
				new int [] { 78, 90, 100},
				new int [] {},
				new int [] { 12, 34, 56, 78, 90, 100 }
			},{
				new int [] { 12, 34, 56 },
				new int [] { 78, 90, 100},
				new int [] {},
				new int [] { 12, 34, 56, 78, 90, 100 }
			},{
				new int [] {},
				new int [] { 12, 34, 56 },
				new int [] { 78, 90, 100},
				new int [] { 12, 34, 56, 78, 90, 100 }
			},{
				new int [] { 12, 34, 56 },
				new int [] { 78, 90, 100},
				new int [] { 110, 120, 127},
				new int [] { 12, 34, 56, 78, 90, 100, 110, 120, 127 }
			}
		});
	}

	public ArrayConcatIntTest () {
	}

	@Test
	public void it_should_concat_int_arrays () {
		int [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
