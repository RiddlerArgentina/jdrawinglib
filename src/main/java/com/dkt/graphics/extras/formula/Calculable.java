/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2018 <fede@riddler.com.ar>
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
package com.dkt.graphics.extras.formula;

import com.dkt.graphics.exceptions.DomainException;
import com.dkt.graphics.extras.GFormula;

/**
 * This class is used within {@link GFormula} representing
 * {@code ℝ → ℝ} functions
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public abstract class Calculable extends AbstractCalculable {
    /**
     * This method should f the function's value.<br>
     * It's not <i>strictly</i> necessary, but the idea is for this functions to
     * be deterministic since they are usually plotted as fixed elements on the
     * canvas
     *
     * @param x the input argument
     * @return {@code f(x)}
     * @throws DomainException methods that implement this interface should
     * throw a {@code DomainException} if {@code x} is not part of the domain
     * of the function
     */
    public abstract double f(double x) throws DomainException;
}
