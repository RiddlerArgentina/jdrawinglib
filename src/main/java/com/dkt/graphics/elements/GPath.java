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
package com.dkt.graphics.elements;

import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.Graphics2D;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPath extends GMultiPoint {
    /**
     * Copy constructor
     *
     * @param e {@code GPath} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GPath(GPath e) {
        super(e);
    }

    /**
     * @param size initial reserved size
     * @throws NegativeArraySizeException if size is less than zero
     */
    public GPath(final int size) {
        super(size);
    }

    /**
     * @param xs list of X coordinates
     * @param ys list of Y coordinates
     * @throws IllegalArgumentException if either array is {@code null}
     * @throws InvalidArgumentException if the array size doesn't match
     */
    public GPath(final int[] xs, final int[]ys) {
        super(xs, ys);
    }

    /**
     * Creates a new empty Path
     */
    public GPath() {
        super(0);
    }

    @Override
    public void draw(final Graphics2D g) {
        if (fill()){
            g.setPaint(getFillPaint());
            g.fillPolygon(xs, ys, size);
        }

        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawPolyline(xs, ys, size);
    }

    @Override
    public GPath clone() {
        return new GPath(this);
    }
}
