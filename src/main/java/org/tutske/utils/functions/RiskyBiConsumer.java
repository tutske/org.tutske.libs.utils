package org.tutske.utils.functions;

import org.tutske.utils.Exceptions;

import java.util.function.BiConsumer;


@FunctionalInterface
public interface RiskyBiConsumer<S, T> extends BiConsumer<S, T> {

	public void riskyAccept (S s, T t) throws Exception;

	default void accept (S s, T t) {
		try { riskyAccept (s, t); }
		catch ( Exception e ) {
			throw Exceptions.wrap (e);
		}
	}

}
