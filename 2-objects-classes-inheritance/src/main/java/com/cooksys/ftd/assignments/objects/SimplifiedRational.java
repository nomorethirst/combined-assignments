package com.cooksys.ftd.assignments.objects;

import java.util.stream.IntStream;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;

public class SimplifiedRational implements IRational {
    private int numerator;

    /**
     * @param denominator
     *            the denominator to set
     */
    public void setDenominator(int denominator) {
	this.denominator = denominator;
    }

    /**
     * @param numerator
     *            the numerator to set
     */
    public void setNumerator(int numerator) {
	this.numerator = numerator;
    }

    private int denominator;

    /**
     * Determines the greatest common denominator for the given values
     *
     * @param a
     *            the first value to consider
     * @param b
     *            the second value to consider
     * @return the greatest common denominator, or shared factor, of `a` and `b`
     * @throws IllegalArgumentException
     *             if a <= 0 or b < 0
     */
    public static int gcd(int a, int b) throws IllegalArgumentException {
	if (a <= 0 || b < 0)
	    throw new IllegalArgumentException();
	if (b==0)
	    return a;
        return gcd(b, a%b);
        // The following worked when testing gcd only, but caused SimplifiedRationalProperties
        // to go into infinite loop.....why?
        /*
	return IntStream
		.range(1, Math.min(a, b))
		.filter(x -> a % x == 0 && b % x == 0)
		.max()
		.getAsInt();
		*/
    }

    /**
     * Simplifies the numerator and denominator of a rational value.
     * <p>
     * For example: `simplify(10, 100) = [1, 10]` or: `simplify(0, 10) = [0, 1]`
     *
     * @param numerator
     *            the numerator of the rational value to simplify
     * @param denominator
     *            the denominator of the rational value to simplify
     * @return a two element array representation of the simplified numerator
     *         and denominator
     * @throws IllegalArgumentException
     *             if the given denominator is 0
     */
    public static int[] simplify(int numerator, int denominator) throws IllegalArgumentException {
	if (denominator == 0)
	    throw new IllegalArgumentException("Denominator is 0");
	if (numerator == 0)
	    return new int[] { 0, denominator };
	int gcd = gcd(Math.abs(numerator), Math.abs(denominator));
	return new int[] { numerator / gcd, denominator / gcd };
    }

    /**
     * Constructor for rational values of the type:
     * <p>
     * `numerator / denominator`
     * <p>
     * Simplification of numerator/denominator pair should occur in this method.
     * If the numerator is zero, no further simplification can be performed
     *
     * @param numerator
     *            the numerator of the rational value
     * @param denominator
     *            the denominator of the rational value
     * @throws IllegalArgumentException
     *             if the given denominator is 0
     */
    public SimplifiedRational(int numerator, int denominator) throws IllegalArgumentException {
	if (denominator == 0)
	    throw new IllegalArgumentException("Denominator is 0");
	int[] simplified = simplify(numerator, denominator);
	this.numerator = simplified[0];
	this.denominator = simplified[1];
    }

    /**
     * @return the numerator of this rational number
     */
    @Override
    public int getNumerator() {
	return this.numerator;
    }

    /**
     * @return the denominator of this rational number
     */
    @Override
    public int getDenominator() {
	return this.denominator;
    }

    /**
     * Specializable constructor to take advantage of shared code between
     * Rational and SimplifiedRational
     * <p>
     * Essentially, this method allows us to implement most of IRational methods
     * directly in the interface while preserving the underlying type
     *
     * @param numerator
     *            the numerator of the rational value to construct
     * @param denominator
     *            the denominator of the rational value to construct
     * @return the constructed rational value (specifically, a
     *         SimplifiedRational value)
     * @throws IllegalArgumentException
     *             if the given denominator is 0
     */
    @Override
    public SimplifiedRational construct(int numerator, int denominator) throws IllegalArgumentException {
	if (denominator == 0)
	    throw new IllegalArgumentException("Denominator is 0");
	return new SimplifiedRational(numerator, denominator);
    }

    /**
     * @param obj
     *            the object to check this against for equality
     * @return true if the given obj is a rational value and its numerator and
     *         denominator are equal to this rational value's numerator and
     *         denominator, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
	if (SimplifiedRational.class.isInstance(obj) && ((SimplifiedRational) obj).getNumerator() == getNumerator()
		&& ((SimplifiedRational) obj).getDenominator() == getDenominator())
	    return true;
	else
	    return false;
    }

    /**
     * If this is positive, the string should be of the form
     * `numerator/denominator`
     * <p>
     * If this is negative, the string should be of the form
     * `-numerator/denominator`
     *
     * @return a string representation of this rational value
     */
    @Override
    public String toString() {
	int n = getNumerator();
	int d = getDenominator();
	return (n < 0 != d < 0 ? "-" : "") + Math.abs(n) + "/" + Math.abs(d);
    }
}
