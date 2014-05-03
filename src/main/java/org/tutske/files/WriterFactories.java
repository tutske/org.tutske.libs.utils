package org.tutske.files;

import java.io.InputStream;


public class WriterFactories {

	public static WriterFactory<byte []> getByteWriterFactory () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		return new ByteWriterFactory (resolver);
	}

	public static WriterFactory<byte []> getByteWriterFactory (FileResolver resolver) {
		return new ByteWriterFactory (resolver);
	}

	public static StringWriterFactory getStringWriterFactory () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		return new StringWriterFactory (resolver);
	}

	public static StringWriterFactory getStringWriterFactory (FileResolver resolver) {
		return new StringWriterFactory (resolver);
	}

	public static WriterFactory<InputStream> getStreamWriterFactory () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		return new StreamWriterFactory (resolver);
	}

	public static WriterFactory<InputStream> getStreamWriterFactory (FileResolver resolver) {
		return new StreamWriterFactory (resolver);
	}

}
