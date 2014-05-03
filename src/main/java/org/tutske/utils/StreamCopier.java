package org.tutske.utils;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;


public class StreamCopier {

	private static final int BUFFER_SIZE = 1024;

	private InputStream inputstream;
	private OutputStream outputstream;

	public StreamCopier (InputStream inputstream, OutputStream outputstream) {
		this.inputstream = inputstream;
		this.outputstream = outputstream;
	}

	public void copy () throws IOException {
		int bytesread = 0;
		byte [] buffer = new byte [BUFFER_SIZE];

		while ( true ) {
			bytesread = inputstream.read (buffer, 0, buffer.length - 1);
			if ( bytesread == -1 ) { break; }
			outputstream.write (buffer, 0, bytesread);
		}
	}

}
