package org.tutske.files;

import java.io.File;
import java.io.InputStream;
import java.nio.charset.Charset;


class StringReader implements Reader<String> {

	private Reader<byte []> bytereader;
	private Charset charset;

	protected StringReader (File file, Charset charset) {
		this.bytereader = ReaderFactories.getByteReaderFactory ().newInstance (file);
		this.charset = charset;
	}

	protected StringReader (InputStream stream, Charset charset) {
		this.bytereader = ReaderFactories.getByteReaderFactory ().newInstance (stream);
		this.charset = charset;
	}

	public String read () {
		return new String (bytereader.read (), charset);
	}

}
