package org.tutske.files;

import java.io.File;
import java.io.InputStream;


class ByteReaderFactory implements ReaderFactory<byte []>{

	private FileResolver fileresolver;

	ByteReaderFactory (FileResolver fileresolver) {
		this.fileresolver = fileresolver;
	}

	public Reader<byte []> newInstance (String filename) {
		return new ByteReader (fileresolver.getFile (filename));
	}

	public Reader<byte []> newInstance (File file) {
		return new ByteReader (file);
	}

	public Reader<byte []> newInstance (InputStream stream) {
		return new ByteReader (stream);
	}

}
