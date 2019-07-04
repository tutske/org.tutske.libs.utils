package org.tutske.lib.utils;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class Functions {

	public static Runnable fn (Action fn) { return fn; }
	public static <S, T> Function<S, T> fn (RiskyFn<S, T> fn) { return fn; }
	public static <S, T, U> BiFunction<S, T, U> fn (RiskyBiFn<S, T, U> fn) { return fn; }
	public static <T> Consumer<T> fn (RiskyConsumer<T> fn) { return fn; }
	public static <S, T> BiConsumer<S, T> fn (RiskyBiConsumer<S, T> fn) { return fn; }
	public static <T> Supplier<T> fn (RiskySupplier<T> fn) { return fn; }

	@FunctionalInterface
	public static interface Action extends Runnable {
		public void riskyRun () throws Exception;
		default void run () {
			try { riskyRun (); }
			catch ( Exception e ) { throw Exceptions.wrap (e); }
		}
	}

	@FunctionalInterface
	public static interface RiskySupplier<T> extends Supplier<T> {
		public T riskyGet () throws Exception;
		default T get () {
			try { return riskyGet (); }
			catch ( Exception e ) { throw Exceptions.wrap (e); }
		}
	}

	@FunctionalInterface
	public static interface RiskyFn<S, T> extends Function<S, T> {
		public T riskyApply (S s) throws Exception;
		default public T apply (S s) {
			try { return riskyApply (s); }
			catch ( Exception e ) { throw Exceptions.wrap (e); }
		}
	}

	@FunctionalInterface
	public static interface RiskyConsumer<T> extends Consumer<T> {
		public void riskyAccept (T t) throws Exception;
		default void accept (T t) {
			try { riskyAccept (t); }
			catch ( Exception e ) { throw Exceptions.wrap (e); }
		}
	}

	@FunctionalInterface
	public static interface RiskyBiFn<S, T, U> extends BiFunction<S, T, U> {
		public U riskyApply (S s, T t) throws Exception;
		default public U apply (S s, T t) {
			try { return riskyApply (s, t); }
			catch ( Exception e ) { throw Exceptions.wrap (e); }
		}
	}

	@FunctionalInterface
	public static interface RiskyBiConsumer<S, T> extends BiConsumer<S, T> {
		public void riskyAccept (S s, T t) throws Exception;
		default void accept (S s, T t) {
			try { riskyAccept (s, t); }
			catch ( Exception e ) { throw Exceptions.wrap (e); }
		}
	}

}
