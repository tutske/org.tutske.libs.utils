package org.tutske.utils.functions;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class Functions {

	public static <S, T> Function<S, T> fn (RiskyFn<S, T> fn) { return fn; }
	public static <S, T, U> BiFunction<S, T, U> fn (RiskyBiFn<S, T, U> fn) { return fn; }
	public static <T> Consumer<T> fn (RiskyConsumer<T> fn) { return fn; }
	public static <S, T> BiConsumer<S, T> fn (RiskyBiConsumer<S, T> fn) { return fn; }
	public static <T> Supplier<T> fn (RiskySupplier<T> fn) { return fn; }

}
