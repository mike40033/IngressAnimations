package com.teasearch.utils;

public class MathUtils {

	public static final double PHI = (Math.sqrt(5)+1)/2;
	public static final double TRIBO = 1.839286755214161;

	/** "correct" mod function. */
	public static int mod(int a, int n) {
		if (n < 0) return mod(a,-n);
		if (n == 0) return a%n; // crash and burn
		int rtn = a%n;
		while (rtn < 0) rtn += n;
		return rtn;
	}

}
