package org.tutske.utils.functions;

import org.tutske.utils.Exceptions;

import java.util.function.Consumer;


@FunctionalInterface
public interface RiskyConsumer<T> extends Consumer<T> {

	public void riskyAccept (T t) throws Exception;

	default void accept (T t) {
		try { riskyAccept (t); }
		catch ( Exception e ) {
			throw Exceptions.wrap (e);
		}
	}

}
