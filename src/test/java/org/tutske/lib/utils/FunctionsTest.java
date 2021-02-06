package org.tutske.lib.utils;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.tutske.lib.utils.Functions.fn;

import org.junit.jupiter.api.Test;

import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;


public class FunctionsTest {

	@Test
	public void it_should_accept_errors_in_functions () {
		Throwable t = ex ("fail", fn ((value) -> {
			if ( "fail".equals (value) ) { throw new Exception (); }
			return value;
		}));

		assertThat (t, instanceOf (RuntimeException.class));
	}

	@Test
	public void it_should_accept_errors_in_bi_functions () {
		Throwable t = ex ("fail", "value", fn ((a, b) -> {
			if ( "fail".equals (a) || "fail".equals (b)) { throw new Exception (); }
			return a.length () > b.length () ? a : b;
		}));

		assertThat (t, instanceOf (RuntimeException.class));
	}

	@Test
	public void it_should_accept_errors_in_consumers () {
		Throwable t = ex ("fail", fn ((value) -> {
			if ( "fail".equals (value) ) { throw new Exception (); }
		}));

		assertThat (t, instanceOf (RuntimeException.class));
	}

	@Test
	public void it_should_accept_errors_in_bi_consumers () {
		Throwable t = ex ("fail", "value", fn ((a, b) -> {
			if ( "fail".equals (a) || "fail".equals (b)) { throw new Exception (); }
		}));

		assertThat (t, instanceOf (RuntimeException.class));
	}

	@Test
	public void it_should_accept_errors_in_suppliers () {
		Throwable t = ex (fn (() -> {
			if ( true ) { throw new Exception (); }
			return "other";
		}));

		assertThat (t, instanceOf (RuntimeException.class));
	}

	@Test
	public void it_should_accept_errors_in_runnables () {
		Throwable t = ex (fn ((Functions.Action) () -> {
			throw new Exception ();
		}));

		assertThat (t, instanceOf (RuntimeException.class));
	}


	@Test
	public void it_should_run_functions_normally () {
		String result = test ("value", fn ((value) -> {
			if ( "fail".equals (value) ) { throw new Exception (); }
			return value;
		}));

		assertThat (result, is ("value"));
	}

	@Test
	public void it_should_run_bi_functions_normally () {
		String result = test ("longest", "value", fn ((a, b) -> {
			if ( "fail".equals (a) || "fail".equals (b)) { throw new Exception (); }
			return a.length () > b.length () ? a : b;
		}));

		assertThat (result, is ("longest"));
	}

	@Test
	public void it_should_run_consumers_normally () {
		test ("value", fn ((value) -> {
			if ( "fail".equals (value) ) { throw new Exception (); }
		}));
	}

	@Test
	public void it_should_run_bi_consumers_normally () {
		test ("longest", "value", fn ((a, b) -> {
			if ( "fail".equals (a) || "fail".equals (b)) { throw new Exception (); }
		}));
	}

	@Test
	public void it_should_run_suppliers_normally () {
		String result = test (fn (() -> {
			if ( false ) { throw new Exception (); }
			return "other";
		}));

		assertThat (result, is ("other"));
	}

	@Test
	public void it_should_run_runnables_normally () {
		test (Functions.fn (() -> { }));
	}

	/* -- helpers -- */

	private void test (Runnable fn) { fn.run (); }
	private <S, T> T test (S in, Function<S, T> fn) { return fn.apply (in); }
	private <S, T, U> U test (S s, T t, BiFunction<S, T, U> fn) { return fn.apply (s, t); }
	private <S> void test (S s, Consumer<S> fn) { fn.accept (s); }
	private <S, T> void test (S s, T t, BiConsumer<S, T> fn) { fn.accept (s, t); }
	private <S> S test (Supplier<S> fn) { return fn.get (); }

	private <S, T> Throwable ex (S in, Function<S, T> fn) { return catchEx (() -> fn.apply (in)); }
	private <S, T, U> Throwable ex (S s, T t, BiFunction<S, T, U> fn) { return catchEx (() -> fn.apply (s, t)); }
	private Throwable ex (Runnable fn) { return catchEx (fn); }
	private <S> Throwable ex (S s, Consumer<S> fn) { return catchEx (() -> fn.accept (s)); }
	private <S, T> Throwable ex (S s, T t, BiConsumer<S, T> fn) { return catchEx (() -> fn.accept (s, t)); }
	private <S> Throwable ex (Supplier<S> fn) { return catchEx (() -> fn.get ()); }

	private Throwable catchEx (Runnable fn) {
		try { fn.run (); }
		catch ( Exception e ) { return e; }
		return null;
	}

}
