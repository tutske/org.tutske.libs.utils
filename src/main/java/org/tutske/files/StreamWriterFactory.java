package org.tutske.files;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;


class StreamWriterFactory implements WriterFactory<InputStream> {

	private FileResolver fileresolver;

	protected StreamWriterFactory (FileResolver fileresolver) {
		this.fileresolver = fileresolver;
	}

	@Override
	public Writer<InputStream> newInstance (String filename) {
		File file= fileresolver.getFile (filename);
		return new StreamWriter (file);
	}

	@Override
	public Writer<InputStream> newInstance (File file) {
		return new StreamWriter (file);
	}

	@Override
	public Writer<InputStream> newInstance (OutputStream stream) {
		return new StreamWriter (stream);
	}

}
