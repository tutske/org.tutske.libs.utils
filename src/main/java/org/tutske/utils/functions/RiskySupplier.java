package org.tutske.utils.functions;

import org.tutske.utils.Exceptions;

import java.util.function.Supplier;


@FunctionalInterface
public interface RiskySupplier<T> extends Supplier<T> {

	public T riskyGet () throws Exception;

	default T get () {
		try { return riskyGet (); }
		catch ( Exception e ) {
			throw Exceptions.wrap (e);
		}
	}

}
