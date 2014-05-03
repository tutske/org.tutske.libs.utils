package org.tutske.utils;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.tutske.utils.ArrayConcat;


public class ArrayConcatTest {

	@Test
	public void it_should_concat_array_of_stings () {
		String [] first = new String [] {"And", "I", "would", "do"};
		String [] second = new String [] {"anything", "for", "love"};
		String [] expected = new String [] {"And", "I", "would", "do", "anything", "for", "love"};

		String [] result = ArrayConcat.concat (first, second);

		assertThat (result, equalTo (expected));
	}

	@Test
	public void it_should_concat_no_arrays () {
		String [] result = ArrayConcat.<String> concat ();
		assertThat (result, not (nullValue ()));
	}

	@Test
	public void it_should_concat_all_empty_arrays () {
		Object [] firsts = new Object [0];
		Object [] seconds = new Object [0];

		Object [] result = ArrayConcat.concat (firsts, seconds);
		assertThat (result.length, is (0));
	}

	@Test
	public void it_should_concat_arrays_when_one_is_empty () {
		Object [] firsts = new Object [0];
		Object [] seconds = new Object [] {new Object (), new Object ()};
		Object [] thirds = new Object [0];

		Object [] result = ArrayConcat.concat (firsts, seconds, thirds);
		assertThat (result, equalTo (seconds));
	}

	@Test
	public void it_should_concat_a_single_array () {
		Object [] firsts = new Object [] {new Object (), new Object ()};

		Object [] result = ArrayConcat.concat (firsts);

		assertThat (result, equalTo (firsts));
	}

	@Test
	public void it_should_do_stuff () {
		Object [] firsts = new Object [] {new Object (), new Object ()};
		Object [] seconds = new Object [] {new Object (), new Object ()};
		Object [] thirds = new Object [] {new Object (), new Object ()};

		Object [] result = ArrayConcat.concat (firsts, seconds, thirds);

		Object [] expected = new Object [] {
			firsts[0], firsts[1],
			seconds[0], seconds[1],
			thirds[0], thirds[1]
		};
		assertThat (result, equalTo (expected));
	}

	@Test
	public void it_should_concat_arrays_of_arrays () {
		Object [][] firsts = new Object [][] {
			new Object [] { new Object (), new Object () },
			new Object [] { new Object (), new Object () },
		};
		Object [][] seconds = new Object [][] {
			new Object [] { new Object (), new Object () },
			new Object [] { new Object (), new Object () },
		};

		Object [][] result = ArrayConcat.concat (firsts, seconds);

		assertThat (result, hasItemInArray (firsts[0]));
		assertThat (result, hasItemInArray (firsts[1]));
		assertThat (result, hasItemInArray (seconds[0]));
		assertThat (result, hasItemInArray (seconds[1]));
	}

}
