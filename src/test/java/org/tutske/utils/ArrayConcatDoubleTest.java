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
public class ArrayConcatDoubleTest {

	@Parameter (value = 0) public double [] firsts;
	@Parameter (value = 1) public double [] seconds;
	@Parameter (value = 2) public double [] thirds;

	@Parameter (value = 3) public double [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new double [] {},
				null,
				null,
				new double [] {},
			},{
				new double [] { 12.0D, 34.0D, 56.0D },
				null,
				null,
				new double [] { 12.0D, 34.0D, 56.0D },
			},{
				new double [] { 12.0D, 34.0D, 56.0D },
				new double [] {},
				null,
				new double [] { 12.0D, 34.0D, 56.0D },
			},{
				new double [] {},
				new double [] { 12.0D, 34.0D, 56.0D },
				null,
				new double [] { 12.0D, 34.0D, 56.0D },
			},{
				new double [] { 12.0D, 34.0D, 56.0D },
				new double [] { 78.0D, 90.0D, 100.0D },
				new double [] {},
				new double [] { 12.0D, 34.0D, 56.0D, 78.0D, 90.0D, 100.0D }
			},{
				new double [] { 12.0D, 34.0D, 56.0D },
				new double [] { 78.0D, 90.0D, 100.0D },
				new double [] {},
				new double [] { 12.0D, 34.0D, 56.0D, 78.0D, 90.0D, 100.0D }
			},{
				new double [] {},
				new double [] { 12.0D, 34.0D, 56.0D },
				new double [] { 78.0D, 90.0D, 100.0D },
				new double [] { 12.0D, 34.0D, 56.0D, 78.0D, 90.0D, 100.0D }
			},{
				new double [] { 12.0D, 34.0D, 56.0D },
				new double [] { 78.0D, 90.0D, 100.0D },
				new double [] { 110.0D, 120.0D, 127.0D },
				new double [] { 12.0D, 34.0D, 56.0D, 78.0D, 90.0D, 100.0D, 110.0D, 120.0D, 127.0D }
			}
		});
	}

	public ArrayConcatDoubleTest () {
	}

	@Test
	public void it_should_concat_double_arrays () {
		double [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
