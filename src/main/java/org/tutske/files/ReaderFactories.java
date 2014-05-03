package org.tutske.files;



public class ReaderFactories {

	public static ReaderFactory<byte []> getByteReaderFactory () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		return new ByteReaderFactory (resolver);
	}

	public static ReaderFactory<byte []> getByteReaderFactory (FileResolver resolver) {
		return new ByteReaderFactory (resolver);
	}

	public static StringReaderFactory getStringReaderFactory () {
		FileResolver resolver = new FileResolverFactory ().regularFileResolver ();
		return new StringReaderFactory (resolver);
	}

	public static StringReaderFactory getStringReaderFactory (FileResolver resolver) {
		return new StringReaderFactory (resolver);
	}

}
