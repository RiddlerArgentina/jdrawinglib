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
package com.dkt.graphics.extras.formula;

import com.dkt.graphics.extras.GFormula;

/**
 * This class is used within {@link GFormula} representing
 * {@code ℝ → ℝ}<sup>2</sup> functions
 * 
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public abstract class ParametricCalculable extends AbstractCalculable {    
    /**
     * This method should return the x(t) value of the equation
     * 
     * @param t parameter
     * @return x(t)
     */
    public abstract double x(double t);
    
    /**
     * This method should return the y(t) value of the equation
     * 
     * @param t parameter
     * @return y(t)
     */
    public abstract double y(double t);
    
}
