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
public class ArrayConcatFloatTest {

	@Parameter (value = 0) public float [] firsts;
	@Parameter (value = 1) public float [] seconds;
	@Parameter (value = 2) public float [] thirds;

	@Parameter (value = 3) public float [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new float [] {},
				null,
				null,
				new float [] {},
			},{
				new float [] { 12.0F, 34.0F, 56.0F },
				null,
				null,
				new float [] { 12.0F, 34.0F, 56.0F },
			},{
				new float [] { 12.0F, 34.0F, 56.0F },
				new float [] {},
				null,
				new float [] { 12.0F, 34.0F, 56.0F },
			},{
				new float [] {},
				new float [] { 12.0F, 34.0F, 56.0F },
				null,
				new float [] { 12.0F, 34.0F, 56.0F },
			},{
				new float [] { 12.0F, 34.0F, 56.0F },
				new float [] { 78.0F, 90.0F, 100.0F },
				new float [] {},
				new float [] { 12.0F, 34.0F, 56.0F, 78.0F, 90.0F, 100.0F }
			},{
				new float [] { 12.0F, 34.0F, 56.0F },
				new float [] { 78.0F, 90.0F, 100.0F },
				new float [] {},
				new float [] { 12.0F, 34.0F, 56.0F, 78.0F, 90.0F, 100.0F }
			},{
				new float [] {},
				new float [] { 12.0F, 34.0F, 56.0F },
				new float [] { 78.0F, 90.0F, 100.0F },
				new float [] { 12.0F, 34.0F, 56.0F, 78.0F, 90.0F, 100.0F }
			},{
				new float [] { 12.0F, 34.0F, 56.0F },
				new float [] { 78.0F, 90.0F, 100.0F },
				new float [] { 110.0F, 120.0F, 127.0F },
				new float [] { 12.0F, 34.0F, 56.0F, 78.0F, 90.0F, 100.0F, 110.0F, 120.0F, 127.0F }
			}
		});
	}

	public ArrayConcatFloatTest () {
	}

	@Test
	public void it_should_concat_float_arrays () {
		float [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
