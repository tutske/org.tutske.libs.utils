package org.tutske.files;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;

import org.tutske.Settings;


public class StringReaderFactory implements ReaderFactory<String>{

	private FileResolver fileresolver;

	StringReaderFactory (FileResolver fileresolver) {
		this.fileresolver = fileresolver;
	}

	public Reader<String> newInstance (String filename) {
		File file = fileresolver.getFile (filename);
		Charset charset = Settings.CHARSET;
		return new StringReader (file, charset);
	}

	public Reader<String> newInstance (File file) {
		return new StringReader (file, Settings.CHARSET);
	}

	public Reader<String> newInstance (InputStream stream) {
		return new StringReader (stream, Settings.CHARSET);
	}

	public Reader<String> newInstance (String filename, Charset charset) {
		File file= fileresolver.getFile (filename);
		return new StringReader (file, charset);
	}

	public Reader<String> newInstance (File file, Charset charset) {
		return new StringReader (file, charset);
	}

	public Reader<String> newInstance (InputStream stream, Charset charset) {
		return new StringReader (stream, charset);
	}

}
