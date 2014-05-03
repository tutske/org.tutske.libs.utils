package org.tutske.files;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;


class ByteWriter extends AbstractWriter implements Writer<byte []> {

	protected ByteWriter (File file) { super (file); }
	protected ByteWriter (OutputStream stream) { super (stream); }

	@Override
	public Writer<byte []> feed (byte [] ... data) {
		try { writeToStream (data); }
		catch (IOException e) { WriterExceptionHandler.newFor (e).handle (); }
		return this;
	}

	@Override
	public Writer<byte []> write (byte [] ... data) {
		feed (data);
		write ();
		return this;
	}

	@Override
	public Writer<byte []> write () {
		try { closeStreamIfNeeded (); }
		catch (IOException e) { WriterExceptionHandler.newFor (e).handle (); }
		return this;
	}

	private void writeToStream (byte [] ... data) throws IOException {
		openStreamIfNeeded ();
		for ( byte [] bytes : data) { stream.write (bytes); }
	}

}
