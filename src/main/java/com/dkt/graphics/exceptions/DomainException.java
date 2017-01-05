/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2016 <dktcoding [at] gmail>
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
 * This exception is usually thrown when one attempts to calculate {@code f(x)}
 * and {@code x} is not a part of the domain of {@code f}
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class DomainException extends RuntimeException {
    /**
     * This constructor takes the value and a string representation of the
     * formula
     *
     * @param formula The {@code String} representation of the formula
     * @param x The value
     */
    public DomainException(String formula, double x){
        super(String.format("%f is not in the domain of '%s'", x, formula));
    }
}
