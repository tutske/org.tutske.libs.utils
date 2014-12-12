package org.tutske.files;

import java.io.*;


abstract class AbstractReader<T> {

	protected File file;
	protected InputStream stream;

	protected AbstractReader (File file) {
		this.file = file;
	}

	protected AbstractReader (InputStream stream) {
		this.stream = stream;
	}

	abstract public T read ();

	public T readAndClose () {
		T result = read ();
		try { stream.close (); }
		catch (IOException e) { ReaderExceptionHandler.newFor (e).handle (); }
		return result;
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
