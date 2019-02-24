package org.tutske.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamCopier {

	private static final int BUFFER_SIZE = 1024 * 8;

	private final int bufferSize;
	private final InputStream in;
	private final OutputStream out;

	public StreamCopier (InputStream inputstream, OutputStream outputstream) {
		this (BUFFER_SIZE, inputstream, outputstream);
	}

	public StreamCopier (int bufferSize, InputStream inputstream, OutputStream outputstream) {
		this.in = inputstream;
		this.out = outputstream;
		this.bufferSize = bufferSize;
	}

	public StreamCopier copy () throws IOException {
		int bytesread = 0;
		byte [] buffer = new byte [bufferSize];

		while ( (bytesread = in.read (buffer, 0, buffer.length)) != -1 ) {
			out.write (buffer, 0, bytesread);
		}

		return this;
	}

}
