/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2014 <dktcoding [at] gmail>
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
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GOval extends GFillableE {
    private final int w, h;
    private int x, y;

    /**
     * Copy constructor
     *
     * @param e {@code GOval} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GOval(GOval e) {
        super(e);
        x = e.x;
        y = e.y;
        h = e.h;
        w = e.w;
    }

    /**
     * Constructs an oval inscribed in a given rectangle
     *
     * @param r rectangle that will contain the oval
     * @throws NullPointerException if the rectangle is {@code null}
     */
    public GOval (GRectangle r) throws NullPointerException {
        if (r == null) {
            throw new NullPointerException("The rectangle can't be null");
        }

        this.x = r.x;
        this.y = r.y;
        this.w = r.w;
        this.h = r.h;
    }

    /**
     * Constructs an oval inscribed in a rectangle
     *
     * @param x x coordinate of the center of the rectangle
     * @param y x coordinate of the center of the rectangle
     * @param w horizontal size of the rectangle
     * @param h vertical size of the rectangle
     * @throws InvalidArgumentException if either {@code h < 0} or {@code w < 0}
     */
    public GOval(int x, int y, int w, int h) throws InvalidArgumentException {
        if (w < 0 | h < 0) {
            String msg = "The width and the height must be positive integers";
            throw new InvalidArgumentException(msg);
        }

        this.x = x - w / 2;
        this.y = y - h / 2;
        this.w = w;
        this.h = h;
    }

    @Override
    public void draw(final Graphics2D g) {
        if (fill()){
            g.setPaint(getFillPaint());
            g.fillOval(x, y, w, h);
        }

        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawOval(x, y, w, h);

    }

    @Override
    public void traslate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    @Override
    public GOval clone() {
        return new GOval(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + x;
        hash = 53 * hash + y;
        hash = 53 * hash + w;
        hash = 53 * hash + h;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GOval other = (GOval) obj;
        return !(
            x != other.x |
            y != other.y |
            w != other.w |
            h != other.h
        );
    }

    @Override
    public Area getShape() {
        return new Area(new Ellipse2D.Double(x, y, w, h));
    }
}
