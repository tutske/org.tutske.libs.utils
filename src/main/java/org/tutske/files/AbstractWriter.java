package org.tutske.files;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;


abstract class AbstractWriter {

	protected File file;
	protected OutputStream stream;

	protected AbstractWriter (File file) {
		this.file = file;
	}

	protected AbstractWriter (OutputStream stream) {
		this.stream = stream;
	}

	protected void openStreamIfNeeded () throws IOException {
		if ( file == null || stream != null ) { return; }
		stream = new FileOutputStream (file);
	}

	protected void closeStreamIfNeeded () throws IOException {
		stream.flush ();
		if ( file != null && stream != null ) { stream.close (); }
	}

	protected void cleanup () {
		try { closeStreamIfNeeded (); }
		catch (IOException e) { WriterExceptionHandler.newFor (e).handle (); }
	}

}
