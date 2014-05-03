package org.tutske.files;

import java.io.File;
import java.io.OutputStream;


public interface WriterFactory<T> {

	public Writer<T> newInstance (String filename);
	public Writer<T> newInstance (File file);
	public Writer<T> newInstance (OutputStream stream);

}
