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
public class ArrayConcatCharTest {

	@Parameter (value = 0) public char [] firsts;
	@Parameter (value = 1) public char [] seconds;
	@Parameter (value = 2) public char [] thirds;

	@Parameter (value = 3) public char [] expected;

	@Parameters
	public static Collection<Object []> parameters () {
		return Arrays.asList(new Object[][] {
			{
				new char [] {},
				null,
				null,
				new char [] {},
			},{
				new char [] { 'a', 'b', 'c' },
				null,
				null,
				new char [] { 'a', 'b', 'c' },
			},{
				new char [] { 'a', 'b', 'c' },
				new char [] {},
				null,
				new char [] { 'a', 'b', 'c' }
			},{
				new char [] {},
				new char [] { 'a', 'b', 'c' },
				null,
				new char [] { 'a', 'b', 'c' }
			},{
				new char [] { 'a', 'b', 'c' },
				new char [] { 'd', 'e', 'f' },
				new char [] {},
				new char [] { 'a', 'b', 'c', 'd', 'e', 'f' }
			},{
				new char [] { 'a', 'b', 'c' },
				new char [] {},
				new char [] { 'd', 'e', 'f' },
				new char [] { 'a', 'b', 'c', 'd', 'e', 'f' }
			},{
				new char [] {},
				new char [] { 'a', 'b', 'c' },
				new char [] { 'd', 'e', 'f' },
				new char [] { 'a', 'b', 'c', 'd', 'e', 'f' }
			},{
				new char [] { 'a', 'b', 'c' },
				new char [] { 'd', 'e', 'f' },
				new char [] { 'g', 'h', 'i' },
				new char [] { 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i' }
			}
		});
	}

	public ArrayConcatCharTest () {
	}

	@Test
	public void it_should_concat_char_arrays () {
		char [] result;

		if ( seconds == null ) { result = ArrayConcat.concat (firsts); }
		else if ( thirds == null ) { result = ArrayConcat.concat (firsts, seconds); }
		else { result = ArrayConcat.concat (firsts,  seconds, thirds); }

		assertThat (result, equalTo (expected));
	}

}
