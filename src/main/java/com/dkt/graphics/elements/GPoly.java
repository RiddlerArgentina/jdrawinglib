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
 * <a href="http://erich.realtimerendering.com/ptinpoly/">...</a>
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPoly extends GMultiPoint {

    /**
     * Copy constructor
     *
     * @param e {@code GPoly} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GPoly(GPoly e) {
        super(e);
    }

    /**
     * Generates an empty polygon
     */
    public GPoly() {
        super(0);
    }

    /**
     * Generates an empty polygon with the specified reserved space
     *
     * @param size the reserved number of points
     */
    public GPoly(final int size) {
        super(size);
    }

    /**
     * @param xs array containing all the x coordinates
     * @param ys array containing all the y coordinates
     * @throws IllegalArgumentException if either array is {@code null}
     * @throws InvalidArgumentException if the array size doesn't match
     */
    public GPoly(final int[] xs, final int[]ys) {
        super(xs, ys);
    }

    @Override
    public void draw(final Graphics2D g) {
        if (fill()){
            g.setPaint(getFillPaint());
            g.fillPolygon(xs, ys, size);
        }

        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawPolygon(xs, ys, size);
    }

    @Override
    public GPoly clone() {
        return new GPoly(this);
    }
}
