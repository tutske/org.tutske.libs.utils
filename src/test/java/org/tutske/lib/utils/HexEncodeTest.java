package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.List;


@RunWith (Parameterized.class)
public class HexEncodeTest {

	@Parameterized.Parameters
	public static List<Object []> data () {
		return Arrays.asList (new Object [][] {
			{ Hex.getEncoder (), new byte [] {}, new byte [] {} },
			{ Hex.getEncoder (), new byte [] { (byte) 0xff }, new byte [] { 'f', 'f' } },
			{ Hex.getEncoder (), new byte [] { (byte) 0xff, (byte) 0x9a }, new byte [] { 'f', 'f', '9', 'a' } },
			{
				Hex.getEncoder (),
				new byte [] { (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef },
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' }
			},
			{ Hex.getUpperCaseEncoder (), new byte [] { (byte) 0xff }, new byte [] { 'F', 'F' } },
			{
				Hex.getUpperCaseEncoder (),
				new byte [] { (byte) 0x01, (byte) 0x23, (byte) 0x45, (byte) 0x67, (byte) 0x89, (byte) 0xab, (byte) 0xcd, (byte) 0xef },
				new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' }
			},
		});
	}

	@Parameterized.Parameter (value = 0) public Hex.Encoder encoder;
	@Parameterized.Parameter (value = 1) public byte [] source;
	@Parameterized.Parameter (value = 2) public byte [] expected;

	@Test
	public void it_should_encode_the_byte_array () {
		assertThat (encoder.encode (source), is (expected));
	}

	@Test
	public void it_should_encode_into_a_byte_array () {
		byte [] result = new byte [expected.length];
		encoder.encode (source, result);
		assertThat (result, is (expected));
	}

	@Test (expected = IllegalArgumentException.class)
	public void it_should_complain_when_the_target_array_is_not_big_enough () {
		if ( source.length == 0 ) { throw new IllegalArgumentException (); }
		encoder.encode (source, new byte [source.length]);
	}

	@Test
	public void it_should_encode_to_string () {
		String result = encoder.encodeToString (source);
		assertThat (result, is (new String (expected)));
	}

	@Test
	public void it_should_encode_byte_buffers () {
		ByteBuffer result = encoder.encode (ByteBuffer.wrap (source));
		assertThat (result.array (), is (expected));
	}

	@Test
	public void it_should_wrap_output_streams () throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);
		wrapped.write (source, 0, source.length);
		assertThat (out.toByteArray (), is (expected));
	}

	@Test
	public void it_should_wrap_output_streams_written_byte_by_byte () throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);
		for ( byte b : source ) { wrapped.write (b); }
		assertThat (out.toByteArray (), is (expected));
	}

	@Test (expected = ArrayIndexOutOfBoundsException.class)
	public void it_should_complain_when_writing_invalid_array_parts () throws Exception {
		ByteArrayOutputStream out = new ByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);
		wrapped.write (source, 0, source.length + 1);
	}

	@Test (expected = IOException.class)
	public void it_should_complain_when_writing_when_original_output_stream_is_closed () throws Exception {
		ClosingByteArrayOutputStream out = new ClosingByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);

		out.close ();

		wrapped.write (source, 0, source.length);
	}

	@Test (expected = IOException.class)
	public void it_should_complain_when_writing_when_the_wrapped_output_stream_is_closed () throws Exception {
		ClosingByteArrayOutputStream out = new ClosingByteArrayOutputStream ();
		OutputStream wrapped = encoder.wrap (out);

		wrapped.close ();

		wrapped.write (source, 0, source.length);
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
