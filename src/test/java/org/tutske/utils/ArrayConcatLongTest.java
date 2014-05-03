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
public class ArrayConcatLongTest {

	@Parameter (value = 0) public long [] firsts;
	@Parameter (value = 1) public long [] seconds;
	@Parameter (value = 2) public long [] thirds;

	@Parameter (value = 3) public long [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new long [] {},
				null,
				null,
				new long [] {},
			},{
				new long [] { 12L, 34L, 56L },
				null,
				null,
				new long [] { 12L, 34L, 56L },
			},{
				new long [] { 12L, 34L, 56L },
				new long [] {},
				null,
				new long [] { 12L, 34L, 56L },
			},{
				new long [] {},
				new long [] { 12L, 34L, 56L },
				null,
				new long [] { 12L, 34L, 56L },
			},{
				new long [] { 12L, 34L, 56L },
				new long [] { 78L, 90L, 100L },
				new long [] {},
				new long [] { 12L, 34L, 56L, 78L, 90L, 100L }
			},{
				new long [] { 12L, 34L, 56L },
				new long [] { 78L, 90L, 100L },
				new long [] {},
				new long [] { 12L, 34L, 56L, 78L, 90L, 100L }
			},{
				new long [] {},
				new long [] { 12L, 34L, 56L },
				new long [] { 78L, 90L, 100L },
				new long [] { 12L, 34L, 56L, 78L, 90L, 100L }
			},{
				new long [] { 12L, 34L, 56L },
				new long [] { 78L, 90L, 100L },
				new long [] { 110L, 120L, 127L },
				new long [] { 12L, 34L, 56L, 78L, 90L, 100L, 110L, 120L, 127L }
			}
		});
	}

	public ArrayConcatLongTest () {
	}

	@Test
	public void it_should_concat_long_arrays () {
		long [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
