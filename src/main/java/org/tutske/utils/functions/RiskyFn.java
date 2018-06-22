package org.tutske.utils.functions;

import org.tutske.utils.Exceptions;

import java.util.function.Function;


@FunctionalInterface
public interface RiskyFn<S, T> extends Function<S, T> {

	public T riskyApply (S s) throws Exception;

	default public T apply (S s) {
		try { return riskyApply (s); }
		catch ( Exception e ) {
			throw Exceptions.wrap (e);
		}
	}

}
