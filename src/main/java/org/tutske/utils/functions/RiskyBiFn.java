package org.tutske.utils.functions;

import org.tutske.utils.Exceptions;

import java.util.function.BiFunction;


@FunctionalInterface
public interface RiskyBiFn<S, T, U> extends BiFunction<S, T, U> {

	public U riskyApply (S s, T t) throws Exception;

	default public U apply (S s, T t) {
		try { return riskyApply (s, t); }
		catch ( Exception e ) {
			throw Exceptions.wrap (e);
		}
	}

}
