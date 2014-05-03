package org.tutske.files;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;


abstract class AbstractReader {

	protected File file;
	protected InputStream stream;

	protected AbstractReader (File file) {
		this.file = file;
	}

	protected AbstractReader (InputStream stream) {
		this.stream = stream;
	}

	protected void openStreamIfNeeded () throws IOException {
		if ( file == null ) { return; }
		stream = new FileInputStream (file);
	}

	protected void closeStreamIfNeeded () throws IOException {
		if ( file == null || stream == null ) { return; }
		stream.close ();
	}

	protected void cleanup () {
		try { closeStreamIfNeeded (); }
		catch (IOException e) { ReaderExceptionHandler.newFor (e).handle (); }
	}

}
