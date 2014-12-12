package org.tutske.files;


public interface Reader<T> {

	public T read ();
	public T readAndClose ();

}
