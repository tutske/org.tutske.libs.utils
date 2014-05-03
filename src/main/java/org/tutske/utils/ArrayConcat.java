package org.tutske.utils;

import java.lang.reflect.Array;


public class ArrayConcat {

	@SuppressWarnings ("unchecked")
	public static <T> T [] concat (T [] ... arrays) {
		int length = sumLengths ((Object []) arrays);

		T [] result = (T []) Array.newInstance (arrays.getClass ().getComponentType ().getComponentType (), length);
		if ( result.length == 0 ) { return result; }
		copyOver (result, arrays[0], arrays, true);

		return result;
	}

	public static boolean [] concat (boolean [] first, boolean [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		boolean [] result = new boolean [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static byte [] concat (byte [] first, byte [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		byte [] result = new byte [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static char [] concat (char [] first, char [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		char [] result = new char [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static short [] concat (short [] first, short [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		short [] result = new short [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static int [] concat (int [] first, int [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		int [] result = new int [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static long [] concat (long [] first, long [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		long [] result = new long [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static float [] concat (float [] first, float [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		float [] result = new float [length];
		copyOver (result, first, arrays);
		return result;
	}

	public static double [] concat (double [] first, double [] ... arrays) {
		int length = sumLengths ((Object []) arrays) + getLength (first);
		double [] result = new double [length];
		copyOver (result, first, arrays);
		return result;
	}

	private static void copyOver (Object target, Object first, Object [] sources) {
		copyOver (target, first, sources, false);
	}

	private static void copyOver (Object target, Object first, Object [] sources, boolean skipFirst) {
		int currentIndex = getLength (first);
		System.arraycopy (first, 0, target, 0, currentIndex);
		int startIndex = skipFirst ? 1 : 0;
		for ( int i = startIndex; i < sources.length; i++ ) {
			int size = getLength (sources[i]);
			System.arraycopy (sources[i], 0, target, currentIndex, size);
			currentIndex += size;
		}
	}

	private static int sumLengths (Object ... arrays) {
		int sum = 0;
		for ( int i = 0; i < arrays.length; i++ ) { sum += getLength (arrays[i]); }
		return sum;
	}

	private static int getLength (Object arr) {
		return Array.getLength (arr);
	}

}
