==========================================================================================
Utils
==========================================================================================


Exceptions
==========================================================================================

.. rubric:: tldr;

Wrap exceptions that you don't want to declare on your methods with a utility method.

.. code-block:: java

	try { something ("that", "may", "fail"); }
	catch ( Exception e ) { throw Exceptions.wrap (e); }


.. rubric:: the problem

There are two classes of exceptions, checked and unchecked exceptions. Checked exceptions
need to appear as part of the method definitions. You also need to declare or catch any&
checked exception thrown by any of the methods used in a method.

.. code-block:: java

	public void fail () throws Exception {
		// We don't really fail ;)
	}

	// declares it throws an exception only because fail says it throws.
	public void run () throws Exception {
		fail ();
	}

When libraries have declared their methods with a ``throws`` clause we have to deal with them
somehow, either by declaring that we throw them, or by wrapping in a custom exception

.. code-block:: java
	:caption: Lib.java

	public interface Lib {
		public void use () throws LibException;
	}

.. code-block:: java
	:caption: Code.java

	public void run () {
		Libs.getInstance ().use ();
	}

The class ``Code.java`` would not compile. But if we declare that we also throw
``LibException``, we are exposing the fact that we use ``Lib``. Something that we may want
to change later on.

We can also wrap the exception in a custom one

.. code-block:: java
	:caption: Code.java

	public class CodeException extends Exception { /* ... */ }

	public void run () throws CodeException {
		try { Libs.getInstance ().use (); }
		catch ( LibException e) { throw new CodeException (e.getMessage (), e); }
	}

Now we have to declare a custom exception, we have to keep declaring that a ``CodeException``
can be thrown on all other methods that use this, and if we are writing a library our
users will be faced with the same problem.

Instead of wrapping it in a custom exception we could wrap it in a ``RuntimeException``,
but we don't want to wrap uncheched exceptions, and we don't want our
stacktraces to look different.


.. rubric:: how utils help

To solve these problems  ``Exceptions`` has a utility
method that will help you in these situations.

.. code-block:: java
	:caption: Code.java

	import org.tutske.libs.utils.Exceptions;

	public void run () {
		try { Libs.getInstance ().use (); }
		catch ( Exception e) { throw Exceptions.wrap (e); }
	}

This will convert an exception into a ``WrappedException`` if it is not already a runtime
exception. These ``WrappedExceptions`` will have the same stacktrace as the original
exception, the same message and the same causes. It will behave as the original message
but does not require you to declare it.


Functions
==========================================================================================

Java 8 introduced functional interfaces, and added some basic definition in the package
``java.util.functions``. Unfortionately these interfaces do not allow you to throw
exceptions.

.. code-block:: java
	:caption: Main.java

	public void run (BiFunction<String, Integer, String> fn) {
		int i = 0;
		String result = fn.apply ("START", i++);
		while ( ! "DONE".equals (result) ) {
			fn.apply ("CONTINUE", i++);
		}
	}

	public String process (String step, int index) throws Exception {
		if ( "START".equals (step) { return ""; }
		if ( index < 5 ) { return ""; }
		return "DONE";
	}

	public static void main (String [] args) {
		run (Main::process);
	}

This will not compile. ``process`` throws an exception so it cannot be a ``BiFunction``.
To solve this problem this package has a class ``Functions`` with static helper methods
and functional interface that extend those found in ``java.util.functions`` that will
allow you to throw Exceptions.

These extended functions will catch any exceptions and wrap them as a runtime exception.

.. code-block:: java
	:caption: Main.java

	import static org.tutske.lib.utils.Functions.fn;

	public static void main (String [] args) {
		run (fn (Main::process));
	}

Now the code will compile. When an ``process`` receives an index greater than 5 a
``WrappedException`` will be thrown that looks and feels like the original exception.

Variants of ``fn`` exists for ``Function``, ``BiFunction``, ``Runnable``, ``Consumer``,
``BiConsumer``, and ``Supplier``.

A simular problem happens when you want to create a thread with a method that throws an
exception.

.. code-block:: java
	:caption: Main.java

	public static void stop () throws InterruptedException  {
		// stop everything gracefully.
	}

	public static void main (String [] args) {
		// does not compile
		Runtime.getRuntime ().addShutdownHook (new Thread (Main::stop));
	}

However this time we don't want the exception to propagate. Here ``Functions`` provides
methods to register shutdown hooks that wont crash.

.. code-block:: java
	:caption: Main.java

	public static void main (String [] args) {
		Functions.onShutdown (Main::stop);
	}

When an exception occurs a stacktrace is printed to stderr. This may not be the desired
behaviour, in which case you can specify what should happen with exceptions.

.. code-block:: java
	:caption: Main.java

	public static void main (String [] args) {
		Functions.onShutdown (Main::stop, ex -> {
			logger.error ("Failed to shutdown properly", ex);
			ex.printStackTrace ();
		);
	}

Or define a reusable method that will handle exceptions.

.. code-block:: java
	:caption: Main.java

	public void handleException (Throwable ex) {
		logger.error ("Failed to shutdown properly", ex);
		ex.printStackTrace ();
	}

	public static void main (String [] args) {
		Functions.onShutdown (Main::stopDB, handleException);
		Functions.onShutdown (Main::stopServer, handleException);
		Functions.onShutdown (Main::stopCaches, handleException);
	}

.. rubric:: recommendations

- Do not declare methods that accept any of the interfaces declared on ``Functions``.

  Pretend that you will only get interfaces declared in ``java.util.functions``, and let
  the users of your methods add an extra call to ``fn ()``. This allows your methods and
  classes to be used where ``Functions`` is not available while adding very little
  overhead when someone does want to pass in something declares it throws Exceptions.

- Be careful when wrapping methods that get called asynchronously.

  Your method will still throw exceptions even if they are not declared on a method. This
  may cause some threads to fail, and ever stop parts of your application.

  Wrapping your methods in ``fn ()`` does not mean exceptions are handled, only that you
  are no longer encumbered by declaring them on whole chains of methods. You may still
  want to catch them at the relevant places, and recover if necessary.


Hex
==========================================================================================

Simularly to the Base64 encoding classes provided with java 8 ``Hex`` has encoding and
decoding classes for hexadecimal representations.

.. code-block:: java

	Hex.Encoder encoder = Hex.getEncoder ();
	String encoded = encoder.encodeToString (bytes);

There are two encoders, one that encodes everything in numbers and lowercase letters
(``getEncoder ()``) and one that encodes everything in numbers and uppercose letters
(``getUpperCaseEncoder ()``).

There are three decoders. One that expects lowercase letters (``getDecoder ()``), one
that expects upper case letters (``getUpperCaseDecoder ()``), and one that ignores &
casing (``getMixedDecoder ()``).


Primitives Parser
==========================================================================================

Often times data is represented as a string, but we want the represented value instead of
the string. This convertion can be done by hand (e.g., ``Integer.parseInt ("1");``). But
you may not always know what type it should be.

.. code-block:: java

	public <T extends Number> T addOne (String representation, Class<T> clazz) {
		// ?
	}

You could go over all the cases that you want to support, Integer, Long, Float, Double. But
perhaps you also want to support BigInteger?

Parsing strings to primitives can be handled by these utilities.

.. code-block:: java

	public <T extends Number> T addOne (String representation, Class<T> clazz) {
		return PrimitivesParser.parse (representation, clazz) + 1;
	}

You can also get a method that will parse values from one type into an other.

.. code-block:: java

	Function<String, Integer> parse = PrimitivesParser.getParser (String.class, Integer.class);
	Stream.of ("1", "2").map (parse).collect (Collectors.toList ());

Support for Integer, Long, Float, Double, Boolean, Date, Path, and Uri is build in.
This may not be a complete list of things that are needed. Lukely you can teach the parser
to convert into other times (and ever from types that are not strings.

.. code-block:: java

	PrimitivesParser.convert (String.class, BigInteger.class, BigInteger::new);
	PrimitivesParser.convert (Integer.class, BigInteger.class, in -> (
		new BigInteger (String.valueOf (in))
	));
