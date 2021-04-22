package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.util.stream.Stream;


public class HexDecodeTest {

	public static class Extras {
		@Test
		public void it_should_complain_when_input_has_odd_length () {
			assertThrows (IllegalArgumentException.class, () -> {
				Hex.getDecoder ().decode (new byte [] {'f'});
			});
		}

		@Test
		public void it_should_complain_when_input_has_odd_length_2 () {
			assertThrows (IllegalArgumentException.class, () -> {
				Hex.getDecoder ().decode (new byte[] { 'f' }, new byte[10]);
			});
		}
	}

	public static Stream<Arguments> cases () {
		return Stream.of (
			Arguments.of (Hex.getDecoder (), new byte [] { 'f', 'f' }, new byte [] { (byte) 0xff }),
			Arguments.of (Hex.getDecoder (), new byte [] { 'f', 'f', '9', 'a' }, new byte [] { (byte) 0xff, (byte) 0x9a }),
			Arguments.of (
				Hex.getDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			),

			Arguments.of (Hex.getUpperCaseDecoder (), new byte [] { 'F', 'F' }, new byte [] { (byte) 0xff }),
			Arguments.of (Hex.getUpperCaseDecoder (), new byte [] { 'F', 'F', '9', 'A' }, new byte [] { (byte) 0xff, (byte) 0x9a }),
			Arguments.of (
				Hex.getUpperCaseDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			),

			Arguments.of (Hex.getMixedDecoder (), new byte [] { 'F', 'F' }, new byte [] { (byte) 0xff }),
			Arguments.of (Hex.getMixedDecoder (), new byte [] { 'F', 'F', '9', 'a' }, new byte [] { (byte) 0xff, (byte) 0x9a }),
			Arguments.of (
				Hex.getMixedDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			),
			Arguments.of (Hex.getMixedDecoder (), new byte [] { 'f', 'f' }, new byte [] { (byte) 0xff }),
			Arguments.of (Hex.getMixedDecoder (), new byte [] { 'f', 'f', '9', 'a' }, new byte [] { (byte) 0xff, (byte) 0x9a }),
			Arguments.of (
				Hex.getMixedDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			),
			Arguments.of (Hex.getMixedDecoder (), new byte [] { 'f', 'F' }, new byte [] { (byte) 0xff }),
			Arguments.of (Hex.getMixedDecoder (), new byte [] { 'F', 'f', '9', 'A' }, new byte [] { (byte) 0xff, (byte) 0x9a }),
			Arguments.of (
				Hex.getMixedDecoder (),
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'b', 'C', 'd', 'E', 'f' },
				new byte [] { (byte) 0x01, (byte) 0x23, (byte ) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef }
			)
		);
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_decode_the_byte_array (Hex.Decoder decoder, byte [] source, byte [] expected) {
		assertThat (decoder.decode(source), is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_decode_into_a_byte_array (Hex.Decoder decoder, byte [] source, byte [] expected) {
		byte [] result = new byte [expected.length];
		decoder.decode(source, result);
		assertThat (result, is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_complain_when_the_target_array_is_not_big_enough (Hex.Decoder decoder, byte [] source, byte [] expected) {
		assertThrows (IllegalArgumentException.class, () -> {
			decoder.decode (source, new byte [expected.length - 1]);
		});
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_decode_a_string (Hex.Decoder decoder, byte [] source, byte [] expected) {
		byte [] result = decoder.decode (new String (source));
		assertThat (result, is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_decode_byte_buffers (Hex.Decoder decoder, byte [] source, byte [] expected) {
		ByteBuffer result = decoder.decode (ByteBuffer.wrap (source));
		assertThat (result.array (), is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_wrap_input_streams (Hex.Decoder decoder, byte [] source, byte [] expected) throws Exception {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);
		byte [] result = new byte [expected.length];
		wrapped.read (result, 0, result.length);
		assertThat (result, is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_wrap_an_input_stream_that_is_read_byte_by_byte (Hex.Decoder decoder, byte [] source, byte [] expected) throws Exception {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);
		byte [] result = new byte [expected.length];

		for ( int i = 0; i < result.length; i++ ) {
			result[i] = (byte) wrapped.read ();
		}

		assertThat (result, is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_not_read_past_the_length (Hex.Decoder decoder, byte [] source, byte [] expected) throws Exception {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);

		wrapped.read (new byte [expected.length], 0, expected.length);

		assertThat (wrapped.read (), is (-1));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_have_the_same_available_bytes_as_the_underlying_stream (Hex.Decoder decoder, byte [] source, byte [] expected) throws IOException {
		ByteArrayInputStream out = new ByteArrayInputStream (source);
		InputStream wrapped = decoder.wrap (out);
		assertThat (wrapped.available (), is (out.available ()));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_call_the_underlying_screams_close_method (Hex.Decoder decoder, byte [] source, byte [] expected) throws IOException {
		InputStream out = mock (InputStream.class);
		decoder.wrap (out).close ();
		verify (out).close ();
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_get_the_the_bytes_from_a_byte_buffer (Hex.Decoder decoder, byte [] source, byte [] expected) {
		ByteBuffer buffer = ByteBuffer.allocate (source.length);
		fillBuffer (buffer, source);
		ByteBuffer result = decoder.decode (buffer);
		assertThat (result.array (), equalTo (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_get_the_the_bytes_from_a_byte_buffer_with_leading_junk (Hex.Decoder decoder, byte [] source, byte [] expected) {
		ByteBuffer buffer = ByteBuffer.allocate (5 + source.length);
		fillBuffer (buffer, source, 5);
		ByteBuffer result = decoder.decode (buffer);
		assertThat (result.array (), equalTo (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	public void it_should_get_the_the_bytes_from_a_byte_buffer_with_leading_junk_and_an_array_offset (Hex.Decoder decoder, byte [] source, byte [] expected) {
		ByteBuffer buffer = ByteBuffer.wrap (new byte [10 + source.length], 5, source.length + 5).slice ();
		fillBuffer (buffer, source, 5);
		ByteBuffer result = decoder.decode (buffer);
		assertThat (result.array (), equalTo (expected));
	}

	private void fillBuffer (ByteBuffer buffer, byte [] bytes) {
		fillBuffer (buffer, bytes, 0);
	}

	private void fillBuffer (ByteBuffer buffer, byte [] bytes, int leadJunk) {
		for ( int i = 0; i < leadJunk; i++ ) { buffer.put ((byte) -1); }
		buffer.put (bytes, 0, bytes.length);
		buffer.flip ();
		if ( leadJunk > 0 ) { buffer.position (leadJunk); }
	}

}
