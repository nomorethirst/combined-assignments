package com.cooksys.ftd.assignments.control;

/**
 * The Fibonacci sequence is simply and recursively defined: the first two elements are `1`, and
 * every other element is equal to the sum of its two preceding elements. For example:
 * <p>
 * [1, 1] =>
 * [1, 1, 1 + 1]  => [1, 1, 2] =>
 * [1, 1, 2, 1 + 2] => [1, 1, 2, 3] =>
 * [1, 1, 2, 3, 2 + 3] => [1, 1, 2, 3, 5] =>
 * ...etc
 */
public class Fibonacci {

    /**
     * Calculates the value in the Fibonacci sequence at a given index. For example,
     * `atIndex(0)` and `atIndex(1)` should return `1`, because the first two elements of the
     * sequence are both `1`.
     *
     * @param i the index of the element to calculate
     * @return the calculated element
     * @throws IllegalArgumentException if the given index is less than zero
     */
    public static int atIndex(int i) throws IllegalArgumentException {
    	if ( i < 0 ) {
    		throw new IllegalArgumentException();
    	}
    	if ( i < 2 ) {
    		return 1;
    	}
    	int last = 1;
    	int secondToLast = 1;
    	int fib = 2;
    	for (int j = 2; j <= i; j++) {
    		fib = last + secondToLast;
    		secondToLast = last;
    		last = fib;
    	}
    	return fib;
    }
    		
    

    /**
     * Calculates a slice of the fibonacci sequence, starting from a given start index (inclusive) and
     * ending at a given end index (exclusive).
     *
     * @param start the starting index of the slice (inclusive)
     * @param end   the ending index of the slice(exclusive)
     * @return the calculated slice as an array of int elements
     * @throws IllegalArgumentException if either the given start or end is negative, or if the
     *                                  given end is less than the given start
     */
    public static int[] slice(int start, int end) throws IllegalArgumentException {
    	if (start < 0 || end < 0 || end < start) {
    		throw new IllegalArgumentException();
    	}
    	
    	// handle case where start == end (so as not to init array of size 0)
    	if (start == end) {
    		// This doesn't work b/c test wants "empty" on slice(0,0).  Correct?
    		//return new int[]{atIndex(start)};
    		return new int[0];
    	}

    	// init array of correct size and go through sequence
    	int[] slice = new int[end-start];
    	int secondToLast = atIndex(start);
    	slice[0] = secondToLast;
    	if (end == start+1) {
    		return slice;
    	}
    	int last = atIndex(start+1);
    	slice[1] = last;
    	for (int i = 2; i < slice.length; i++) {
    		slice[i] = secondToLast + last;
    		secondToLast = last;
    		last = slice[i];
    	}
    	
    	return slice;
    }

    /**
     * Calculates the beginning of the fibonacci sequence, up to a given count.
     *
     * @param count the number of elements to calculate
     * @return the beginning of the fibonacci sequence, up to the given count, as an array of int elements
     * @throws IllegalArgumentException if the given count is negative
     */
    public static int[] fibonacci(int count) throws IllegalArgumentException {
    	if (count < 0) {
			throw new IllegalArgumentException();
    	}
    	return slice(0, count);
    }
}
