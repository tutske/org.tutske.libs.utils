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
public class ArrayConcatBooleanTest {

	@Parameter (value = 0) public boolean [] firsts;
	@Parameter (value = 1) public boolean [] seconds;
	@Parameter (value = 2) public boolean [] thirds;

	@Parameter (value = 3) public boolean [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new boolean [] {},
				null,
				null,
				new boolean [] {},
			},{
				new boolean [] { false, true, false },
				null,
				null,
				new boolean [] { false, true, false },
			},{
				new boolean [] { false, true, false },
				new boolean [] {},
				null,
				new boolean [] { false, true, false }
			},{
				new boolean [] {},
				new boolean [] { false, true, false },
				null,
				new boolean [] { false, true, false }
			},{
				new boolean [] { false, true, false },
				new boolean [] { false, true, false },
				new boolean [] {},
				new boolean [] { false, true, false, false, true, false }
			},{
				new boolean [] { false, true, false },
				new boolean [] { false, true, false },
				new boolean [] {},
				new boolean [] { false, true, false, false, true, false }
			},{
				new boolean [] {},
				new boolean [] { false, true, false },
				new boolean [] { false, true, false },
				new boolean [] { false, true, false, false, true, false }
			},{
				new boolean [] { false, true, false },
				new boolean [] { false, true, false },
				new boolean [] { false, true, false },
				new boolean [] { false, true, false, false, true, false, false, true, false }
			}
		});
	}

	public ArrayConcatBooleanTest () {
	}

	@Test
	public void it_should_concat_boolean_arrays () {
		boolean [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
