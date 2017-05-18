package org.tutske.utils;

import java.io.FilterOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Arrays;


public class Hex {
	private static final byte [] UPPER_CHARS = new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
	private static final byte [] LOWER_CHARS = new byte [] { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };

	public static class Encoder {
		private static final Encoder UPPER_CASE = new Encoder (UPPER_CHARS);
		private static final Encoder LOWER_CASE = new Encoder (LOWER_CHARS);

		private final byte [] chars;

		private Encoder (byte [] chars) {
			this.chars = chars;
		}

		public byte [] encode (byte [] src) {
			byte [] result = new byte [src.length * 2];
			encode (src, result);
			return result;
		}

		public int encode (byte [] src, byte [] dest) {
			return encode (src, 0, dest, 0, src.length);
		}

		private int encode (byte [] src, int srcPos, byte [] dest, int destPos, int length) {
			if ( dest.length - destPos - 1 < length ) {
				throw new IllegalArgumentException ("Destination array to small");
			}

			for ( int i = 0; i < length; i++ ) {
				int j = destPos + i * 2;
				int upper = (src[srcPos + i] & 0xf0) >> 4;
				int lower = src[srcPos + i] & 0x0f;
				dest[j] = chars[upper];
				dest[j + 1] = chars[lower];
			}

			return destPos + length;
		}

		public String encodeToString (byte [] src) {
			return new String (encode (src), Charset.forName ("utf-8"));
		}

		public ByteBuffer encode (ByteBuffer buffer) {
			byte [] dest = new byte [buffer.remaining () * 2];

			if ( buffer.hasArray () ) {
				encode (buffer.array (), buffer.arrayOffset () + buffer.position (), dest, 0, buffer.limit ());
			} else {
				byte [] source = new byte [buffer.remaining ()];
				buffer.get (source);
				encode (source, dest);
			}

			return ByteBuffer.wrap (dest);
		}

		public OutputStream wrap (OutputStream out) {
			return new EncOutputStream (out, this);
		}
	}

	public static class Decoder {
		private static Decoder LOWER_CASE = new Decoder (LOWER_CHARS);
		private static Decoder UPPER_CASE = new Decoder (UPPER_CHARS);
		private static Decoder MIXED_CASE = new Decoder (LOWER_CHARS, UPPER_CHARS);

		private int [] table = new int [255];

		private Decoder (byte [] ... chars) {
			Arrays.fill (table, (byte) -1);
			for ( byte [] arr : chars ) {
				for ( int i = 0; i < arr.length; i++ ) {
					table[arr[i]] = i;
				}
			}
		}

		public byte [] decode (byte [] src) {
			if ( (src.length & 0x01) > 0 ) {
				throw new IllegalArgumentException ("byte should have even size");
			}

			byte [] result = new byte [src.length / 2];
			decode (src, result);
			return result;
		}

		public int decode (byte [] src, byte [] dest) {
			return decode (src, 0, dest, 0, src.length);
		}

		public int decode (byte [] src, int srcPos, byte [] dest, int destPos, int length) {
			if ( (length & 0x01) > 0 ) {
				throw new IllegalArgumentException ("byte should have even size");
			}

			if ( dest.length - destPos < length / 2 ) {
				throw new IllegalArgumentException ("destination array is not big enough");
			}

			for ( int i = 0; i < length / 2; i++ ) {
				int j = srcPos + (i * 2);
				int r = (table [src[j]] << 4) + table [src[j + 1]];
				dest[destPos + i] = (byte) r;
			}

			return 0;
		}

		public byte [] decode (String src) {
			return decode (src.getBytes ());
		}

		public ByteBuffer decode (ByteBuffer buffer) {
			byte [] dest;

			if ( buffer.hasArray () ) {
				dest = new byte [buffer.remaining () / 2];
				decode (buffer.array (), buffer.arrayOffset () + buffer.position (), dest, 0, buffer.limit ());
			} else {
				byte [] source = new byte [buffer.remaining ()];
				buffer.get (source);
				dest = decode (source);
			}

			return ByteBuffer.wrap (dest);
		}

		public InputStream wrap (InputStream in) {
			return new DecInputStream (in, this);
		}
	}

	public static Encoder getEncoder () {
		return Encoder.LOWER_CASE;
	}

	public static Encoder getUpperCaseEncoder () {
		return Encoder.UPPER_CASE;
	}

	public static Decoder getDecoder () {
		return Decoder.LOWER_CASE;
	}

	private static class EncOutputStream extends FilterOutputStream {
		private boolean closed = false;
		private Encoder encoder;

		EncOutputStream(OutputStream os, Encoder encoder) {
			super(os);
			this.encoder = encoder;
		}

		@Override
		public void write(int b) throws IOException {
			byte[] buf = new byte[1];
			buf[0] = (byte)(b & 0xff);
			write(buf, 0, 1);
		}

		@Override
		public void write(byte[] source, int off, int len) throws IOException {
			if ( closed ) { throw new IOException("Stream is closed"); }
			if (len == 0) { return; }

			if ( off < 0 || len < 0 || off + len > source.length ) {
				throw new ArrayIndexOutOfBoundsException ();
			}

			out.write (encoder.encode (source));
		}
	}

	private static class DecInputStream extends InputStream {
		private final Decoder decoder;
		private final InputStream stream;

		byte [] buff = new byte [1];

		private DecInputStream (InputStream stream, Decoder decoder) {
			this.decoder = decoder;
			this.stream = stream;
		}

		@Override
		public int read () throws IOException {
			return read(buff, 0, 1) == -1 ? -1 : buff[0] & 0xff;
		}

		@Override
		public int read(byte[] b, int off, int len) throws IOException {
			int size = len * 2;
			byte [] read = new byte [size];

			int n = stream.read (read, 0, size);
			int m = (n & 0x01) > 0 ? n - 1 : n;
			decoder.decode (read, 0, b, off, m);

			return m / 2;
		}

		@Override
		public int available() throws IOException {
			return stream.available();
		}

		@Override
		public void close() throws IOException {
			stream.close();
		}
	}

}
