package org.tutske.files;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;

import org.tutske.Settings;


public class StringWriterFactory implements WriterFactory<String> {

	private FileResolver fileresolver;

	StringWriterFactory (FileResolver fileresolver) {
		this.fileresolver = fileresolver;
	}

	@Override
	public Writer<String> newInstance (String filename) {
		File file = fileresolver.getFile (filename);
		Charset charset = Settings.CHARSET;
		return new StringWriter (file, charset);
	}

	@Override
	public Writer<String> newInstance (File file) {
		return new StringWriter (file, Settings.CHARSET);
	}

	@Override
	public Writer<String> newInstance (OutputStream stream) {
		return new StringWriter (stream, Settings.CHARSET);
	}

	public Writer<String> newInstance (String filename, Charset charset) {
		File file = fileresolver.getFile (filename);
		return new StringWriter (file, charset);
	}

	public Writer<String> newInstance (File file, Charset charset) {
		return new StringWriter (file, charset);
	}

	public Writer<String> newInstance (OutputStream stream, Charset charset) {
		return new StringWriter (stream, charset);
	}

}
