package org.tutske.files;

import java.io.File;
import java.io.OutputStream;
import java.nio.charset.Charset;


class StringWriter implements Writer<String> {

	private Writer<byte []> bytewriter;
	private Charset charset;

	public StringWriter (File file, Charset charset) {
		this.bytewriter = WriterFactories.getByteWriterFactory ().newInstance (file);
		this.charset = charset;
	}

	public StringWriter (OutputStream stream, Charset charset) {
		this.bytewriter = WriterFactories.getByteWriterFactory ().newInstance (stream);
		this.charset = charset;
	}

	@Override
	public Writer<String> feed (String ... data) {
		bytewriter.feed (convert (data));
		return this;
	}

	@Override
	public Writer<String> write (String ... data) {
		bytewriter.write (convert (data));
		return this;
	}

	@Override
	public Writer<String> write () {
		bytewriter.write ();
		return this;
	}

	private byte [][] convert (String [] data) {
		byte [][] converted = new byte [data.length][];
		for ( int i = 0; i < data.length; i++ ) {
			converted[i] = data[i].getBytes (charset);
		}
		return converted;
	}

}
