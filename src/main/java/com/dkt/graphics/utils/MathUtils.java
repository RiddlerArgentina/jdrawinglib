/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2023 <fede@riddler.com.ar>
 *
 * This program is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the
 * Free Software Foundation, either version 3 of the License, or any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but
 * WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along
 * with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.dkt.graphics.utils;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class MathUtils {
    /**
     * Don't let anyone initialize this class
     */
    private MathUtils(){}

    /**
     * This class is the Heaviside step function omitting the value of
     * {@code 1/2} for {@code t = 0}
     *
     * @param t parameter
     * @return {@code 0} if {@code t < 0} and {@code 1} otherwise
     */
    public static double hs(double t) {
        if (t < 0) {
            return 0;
        }
        return 1;
    }

    /**
     * Rectangular function
     *
     * @param t parameter
     * @return {@code 1} if {@code -0.5 <= t <= 0.5} and {@code 0} otherwise
     * @see MathUtils#boxcar(double, double, double)
     */
    public static double rect(double t) {
        return boxcar(t, -0.5, 0.5);
    }

    /**
     * Boxcar function
     *
     * @param t parameter
     * @param a the left bound
     * @param b the right bound
     * @return {@code 1} if {@code a <= t <= b} and {@code 0} otherwise
     */
    public static double boxcar(double t, double a, double b) {
        if (t <= b & t >= a){
            return 1;
        }
        return 0;
    }

    /**
     * Provides an unbranched version of min for POSITIVE ints, it does 7
     * operations instead of branching (which is somewhat slower, but it's
     * mainly here for educational purposes)<br>
     * The original code can be found in:
     * http://graphics.stanford.edu/~seander/bithacks.html#IntegerMinOrMax
     *
     * @param a first value to compare
     * @param b second value to compare
     * @return the smallest number
     */
    public static int min(final int a, final int b){
        final int ab = a - b;
        return a & (ab >> 31) | b & (~ab >> 31);
    }

    /**
     * Provides an unbranched version of max for POSITIVE ints, it does 7
     * operation instead of branching (which is somewhat slower, but it's mainly
     * here for educational purposes)<br>
     * The original code can be found in:
     * http://graphics.stanford.edu/~seander/bithacks.html#IntegerMinOrMax
     *
     * @param a first value to compare
     * @param b second value to compare
     * @return the biggest number
     */
    public static int max(final int a, final int b){
        final int ab = a - b;
        return b & (ab >> 31) | a & (~ab >> 31);
    }

    /**
     * Computes the absolute value of a number without branching<br>
     * The original code can be found in:
     * http://graphics.stanford.edu/~seander/bithacks.html#IntegerAbs
     *
     * @param n number
     * @return the absolute value of this number
     */
    public static int abs(int n){
        final int n31 = n >> 31;
        return (n + n31) ^ n31;
    }
}
