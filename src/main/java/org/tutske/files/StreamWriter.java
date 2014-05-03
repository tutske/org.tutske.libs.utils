package org.tutske.files;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.tutske.utils.StreamCopier;


class StreamWriter extends AbstractWriter implements Writer<InputStream> {

	protected StreamWriter (File file) { super (file); }
	protected StreamWriter (OutputStream stream) { super (stream); }

	@Override
	public Writer<InputStream> feed (InputStream ... data) {
		try { writeToStream (data); }
		catch (IOException e) { WriterExceptionHandler.newFor (e).handle (); }
		return this;
	}

	@Override
	public Writer<InputStream> write (InputStream ... data) {
		feed (data);
		write ();
		return this;
	}

	@Override
	public Writer<InputStream> write () {
		try { closeStreamIfNeeded (); }
		catch (IOException e) { WriterExceptionHandler.newFor (e).handle (); }
		return this;
	}

	private void writeToStream (InputStream ... data) throws IOException {
		openStreamIfNeeded ();
		for (InputStream source : data ) {
			new StreamCopier (source, stream).copy ();
		}
	}

}
