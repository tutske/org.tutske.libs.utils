package org.tutske.lib.utils;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;


@RunWith (Parameterized.class)
public class HexDecodeTest {

	public static class Extras {
		@Test (expected = IllegalArgumentException.class)
		public void it_should_complain_when_input_has_odd_length () {
			Hex.getDecoder ().decode (new byte [] {'f'});
		}

		@Test (expected = IllegalArgumentException.class)
		public void it_should_complain_when_input_has_odd_length_2 () {
			Hex.getDecoder ().decode (new byte [] {'f'}, new byte [10]);
		}
	}

	@Parameterized.Parameters
	public static List<Object []> data () {
		return Arrays.asList (new Object [][] {
			{ Hex.getDecoder (), new byte [] { 'f', 'f' }, new byte [] { (byte) 0xff } },
			{ Hex.getDecoder (), new byte [] { 'f', 'f', '9', 'a' }, new byte [] { (byte) 0xff, (byte) 0x9a } },
			{
				Hex.getDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			},

			{ Hex.getUpperCaseDecoder (), new byte [] { 'F', 'F' }, new byte [] { (byte) 0xff } },
			{ Hex.getUpperCaseDecoder (), new byte [] { 'F', 'F', '9', 'A' }, new byte [] { (byte) 0xff, (byte) 0x9a } },
			{
				Hex.getUpperCaseDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			},

			{ Hex.getMixedDecoder (), new byte [] { 'F', 'F' }, new byte [] { (byte) 0xff } },
			{ Hex.getMixedDecoder (), new byte [] { 'F', 'F', '9', 'a' }, new byte [] { (byte) 0xff, (byte) 0x9a } },
			{
				Hex.getMixedDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			},
			{ Hex.getMixedDecoder (), new byte [] { 'f', 'f' }, new byte [] { (byte) 0xff } },
			{ Hex.getMixedDecoder (), new byte [] { 'f', 'f', '9', 'a' }, new byte [] { (byte) 0xff, (byte) 0x9a } },
			{
				Hex.getMixedDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			},
			{ Hex.getMixedDecoder (), new byte [] { 'f', 'F' }, new byte [] { (byte) 0xff } },
			{ Hex.getMixedDecoder (), new byte [] { 'F', 'f', '9', 'A' }, new byte [] { (byte) 0xff, (byte) 0x9a } },
			{
				Hex.getMixedDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'b', 'C', 'd', 'E', 'f' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			},

		});
	}

	@Parameterized.Parameter (value = 0) public Hex.Decoder decoder;
	@Parameterized.Parameter (value = 1) public byte [] source;
	@Parameterized.Parameter (value = 2) public byte [] expected;

	@Test
	public void it_should_decode_the_byte_array () {
		assertThat (decoder.decode(source), is (expected));
	}

	@Test
	public void it_should_decode_into_a_byte_array () {
		byte [] result = new byte [expected.length];
		decoder.decode(source, result);
		assertThat (result, is (expected));
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_the_target_array_is_not_big_enough () {
		decoder.decode (source, new byte [expected.length - 1]);
	}

	@Test
	public void it_should_decode_a_string () {
		byte [] result = decoder.decode (new String (source));
		assertThat (result, is (expected));
	}

	@Test
	public void it_should_decode_byte_buffers () {
		ByteBuffer result = decoder.decode (ByteBuffer.wrap (source));
		assertThat (result.array (), is (expected));
	}

	@Test
	public void it_should_wrap_input_streams () throws Exception {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);
		byte [] result = new byte [expected.length];
		wrapped.read (result, 0, result.length);
		assertThat (result, is (expected));
	}

	@Test
	public void it_should_wrap_an_input_stream_that_is_read_byte_by_byte () throws Exception {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);
		byte [] result = new byte [expected.length];

		for ( int i = 0; i < result.length; i++ ) {
			result[i] = (byte) wrapped.read ();
		}

		assertThat (result, is (expected));
	}

	@Test
	public void it_should_have_the_same_available_bytes_as_the_underlying_stream () throws IOException {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);
		assertThat (wrapped.available (), is (out.available ()));
	}

	@Test
	public void it_should_call_the_underlying_screams_close_method () throws IOException {
		InputStream out = mock (InputStream.class);
		decoder.wrap (out).close ();
		verify (out).close ();
	}

	@Test
	public void it_should_get_the_the_bytes_from_a_byte_buffer () {
		ByteBuffer buffer = ByteBuffer.allocate (10);
		decoder.decode (buffer);
	}

}
