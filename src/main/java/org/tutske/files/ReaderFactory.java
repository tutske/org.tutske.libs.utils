package org.tutske.files;

import java.io.File;
import java.io.InputStream;


public interface ReaderFactory<T> {

	public Reader<T> newInstance (String filename);
	public Reader<T> newInstance (File file);
	public Reader<T> newInstance (InputStream stream);

}
