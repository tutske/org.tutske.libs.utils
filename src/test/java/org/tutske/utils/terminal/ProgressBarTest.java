package org.tutske.utils.terminal;

import static org.junit.Assert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;


public class ProgressBarTest {

	ByteArrayOutputStream stream = new ByteArrayOutputStream ();
	PrintStream out = new PrintStream (stream);

	@Test
	public void it_should_print_bar_to_stdout () {
		createBar ("", 10).showProgress (1, 10);
		assertThat (output (), containsString ("[#         ]"));
	}

	@Test
	public void it_should_print_bar_to_stdout_of_the_right_length () {
		createBar ("", 10).showProgress (2, 10);
		assertThat (output (), containsString ("[##        ]"));
	}

	@Test
	public void it_should_print_a_bar_with_the_rigth_length_of_arbitrary_fractions () {
		createBar ("", 10).showProgress (203, 1000);
		assertThat (output (), containsString ("[##        ]"));
	}

	@Test
	public void it_should_round_up_for_arbitrary_fractions () {
		createBar ("", 10).showProgress (283, 1000);
		assertThat (output (), containsString ("[###       ]"));
	}

	@Test
	public void it_should_show_the_message_in_the_bar () {
		createBar ("The Message!", 30).showProgress (9, 10);
		assertThat (output (), containsString ("The Message!"));
	}

	@Test
	public void it_should_print_the_newly_set_message_on_subsequent_showings () {
		createBar ("The Message!", 30).setMessage ("New Message").showProgress (9, 10);
		assertThat (output (), containsString ("New Message"));
		assertThat (output (), not (containsString ("The Message!")));
	}

	private ProgressBar createBar (String msg, int length) {
		return new ProgressBar (out, msg, length).setStyle (ProgressBar.Style.standard);
	}

	private String output () {
		return Colors.clearColor (new String (stream.toByteArray ()));
	}

}
