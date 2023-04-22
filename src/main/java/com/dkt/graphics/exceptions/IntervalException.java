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
package com.dkt.graphics.exceptions;

/**
 * This exception is usually thrown when something goes wrong with an interval
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class IntervalException extends RuntimeException {
    /**
     * Generates a new Exception based on the starting point of the interval,
     * the last point and the step.
     *
     * @param msg The message you want to show
     * @param xs The starting point of the interval
     * @param xf The ending point of the interval
     * @param s The step
     */
    public IntervalException(String msg, double xs, double xf, double s){
        super(String.format("%s [xs: %5.2f xf: %5.2f s:%5.2f]",msg, xs, xf, s));
    }

    /**
     * Generates a new Exception based on the starting point of the interval,
     * the last point. Usually is used when {@code xf < xs}
     *
     * @param msg The message you want to show
     * @param xs The starting point of the interval
     * @param xf The ending point of the interval
     */
    public IntervalException(String msg, int xs, int xf) {
        super(String.format("%s [xs: %d xf: %d]",msg, xs, xf));
    }

    /**
     * Generates a new Exception to be thrown when a value is not in an interval
     *
     * @param msg The message you want to show
     * @param xs The starting point of the interval
     * @param xf The ending point of the interval
     * @param x  The value
     */
    public IntervalException(String msg, int xs, int xf, int x) {
        super(String.format("%s [xs: %d xf: %d x: %d]",msg, xs, xf, x));
    }
}
