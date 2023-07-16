package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.stream.Stream;


class HexEncodeTest {

	public static Stream<Arguments> cases () {
		return Stream.of (
			Arguments.of (Hex.getEncoder (), new byte [] {}, new byte [] {}),
			Arguments.of (Hex.getEncoder (), new byte [] { (byte) 0xff }, new byte [] { 'f', 'f' }),
			Arguments.of (Hex.getEncoder (), new byte [] { (byte) 0xff, (byte) 0x9a }, new byte [] { 'f', 'f', '9', 'a' }),
			Arguments.of (
				Hex.getEncoder (),
				new byte [] { (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef },
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }
			),
			Arguments.of (Hex.getUpperCaseEncoder (), new byte [] { (byte) 0xff }, new byte [] { 'F', 'F' }),
			Arguments.of (
				Hex.getUpperCaseEncoder (),
				new byte [] { (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef },
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' }
			)
		);
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_encode_the_byte_array (Hex.Encoder encoder, byte [] source, byte [] expected) {
		assertThat (encoder.encode (source), is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_encode_into_a_byte_array (Hex.Encoder encoder, byte [] source, byte [] expected) {
		byte [] result = new byte [expected.length];
		encoder.encode (source, result);
		assertThat (result, is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_complain_when_the_target_array_is_not_big_enough (Hex.Encoder encoder, byte [] source, byte [] expected) {
		if ( source.length == 0 ) { return; }
		assertThrows (IllegalArgumentException.class, () -> {
			encoder.encode (source, new byte [source.length]);
		});
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_encode_to_string (Hex.Encoder encoder, byte [] source, byte [] expected) {
		String result = encoder.encodeToString (source);
		assertThat (result, is (new String (expected)));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_encode_byte_buffers (Hex.Encoder encoder, byte [] source, byte [] expected) {
		ByteBuffer result = encoder.encode (ByteBuffer.wrap (source));
		assertThat (result.array (), is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_wrap_output_streams (Hex.Encoder encoder, byte [] source, byte [] expected) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);
		wrapped.write (source, 0, source.length);
		assertThat (out.toByteArray (), is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_wrap_output_streams_written_byte_by_byte (Hex.Encoder encoder, byte [] source, byte [] expected) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);
		for ( byte b : source ) { wrapped.write (b); }
		assertThat (out.toByteArray (), is (expected));
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_complain_when_writing_invalid_array_parts (Hex.Encoder encoder, byte [] source, byte [] expected) throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);
		assertThrows (ArrayIndexOutOfBoundsException.class, () -> {
			wrapped.write (source, 0, source.length + 1);
		});
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_complain_when_writing_when_original_output_stream_is_closed (Hex.Encoder encoder, byte [] source, byte [] expected) throws Exception {
		ClosingByteArrayOutputStream out = new ClosingByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);

		out.close ();

		assertThrows (IOException.class, () -> {
			wrapped.write (source, 0, source.length);
		});
	}

	@ParameterizedTest
	@MethodSource ("cases")
	void it_should_complain_when_writing_when_the_wrapped_output_stream_is_closed (Hex.Encoder encoder, byte [] source, byte [] expected) throws Exception {
		ClosingByteArrayOutputStream out = new ClosingByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);

		wrapped.close ();

		assertThrows (IOException.class, () -> {
			wrapped.write (source, 0, source.length);
		});
	}

	private static class ClosingByteArrayOutputStream extends OutputStream {
		private boolean closed = false;
		private ByteArrayOutputStream out = new ByteArrayOutputStream ();

		@Override public void close () { this.closed = true; }

		@Override
		public void write (int b) throws IOException {
			if ( closed ) { throw new IOException ("Stream was closed"); }
			out.write (b);
		}

		@Override
		public synchronized void write (byte [] b, int off, int len) throws IOException {
			if ( closed ) { throw new IOException ("Stream was closed"); }
			super.write (b, off, len);
		}

		public void writeBytes (byte[] b) throws IOException { write (b, 0, b.length); }
		public void reset () { out.reset (); }
		public int size () { return out.size (); }
		public byte [] toByteArray () { return out.toByteArray (); }
	}

}
