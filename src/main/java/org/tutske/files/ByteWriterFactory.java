package org.tutske.files;

import java.io.File;
import java.io.OutputStream;


class ByteWriterFactory implements WriterFactory<byte []> {

	private FileResolver fileresolver;

	ByteWriterFactory (FileResolver fileresolver) {
		this.fileresolver = fileresolver;
	}

	@Override
	public Writer<byte []> newInstance (String filename) {
		File file = fileresolver.getFile (filename);
		return newInstance (file);
	}

	@Override
	public Writer<byte []> newInstance (File file) {
		return new ByteWriter (file);
	}

	@Override
	public Writer<byte []> newInstance (OutputStream stream) {
		return new ByteWriter (stream);
	}

}
