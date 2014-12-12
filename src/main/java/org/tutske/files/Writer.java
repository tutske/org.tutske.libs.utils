package org.tutske.files;


/**
 * Writes data to the target for this writer.
 *
 * title
 *
 * The `feed ()` method can be called multiple times. At least one of the `write
 * ()` methods should be called. After calling one of the `write ()` methods,
 *
 * further calls to one of the `write ()` methods or to the `feed ()` method
 * will throw a `RuntimeException`.
 *
 * Usage:
 *
 * ```java
 * String firstline = "put this text in the file."
 * String secondline = "put this text on the second line."
 *
 * WriterFactories.getStringWriterFactory ()
 *     .newInstance ("path/to/file.ext")
 *     .write (firstline, secondline);
 * ```
 *
 * @author Jeroen Matthijssens <jeroen.matthijssens@xcomm.be>
 *
 * @param <T>
 */
public interface Writer<T> {

	public Writer<T> feed (T ... data);
	public Writer<T> write (T ... data);
	public Writer<T> write ();
	public Writer<T> writeAndClose ();

}
