package org.tutske.files;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import org.tutske.utils.StreamCopier;


class ByteReader extends AbstractReader<byte []> implements Reader<byte []> {

	public ByteReader (File file) { super (file); }
	public ByteReader (InputStream stream) { super (stream); }

	public byte [] read () {
		try { return readStreamToBytes (); }
		catch (IOException e) { ReaderExceptionHandler.newFor (e).handle (); }
		finally { cleanup (); }

		return new byte [0];
	}

	private byte [] readStreamToBytes () throws IOException {
		openStreamIfNeeded ();
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream ();
		new StreamCopier (stream, bytestream).copy ();
		closeStreamIfNeeded ();
		return bytestream.toByteArray ();
	}

}
