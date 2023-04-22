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

import java.awt.Graphics2D;
import java.awt.geom.Arc2D;
import java.awt.geom.Area;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GArc extends GFillableE {
    private int x, y;
    private final int w, h, sa, aa;

    /**
     * Copy constructor
     *
     * @param e {@code GArc} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GArc(GArc e){
        super(e);

        x = e.x;
        y = e.y;
        w = e.w;
        h = e.h;
        sa = e.sa;
        aa = e.aa;
    }

    /**
     * Generates an arc of a circle
     *
     * @param x x coordinate of the center of the circle
     * @param y y coordinate of the center of the circle
     * @param r radius of the circle
     * @param a angle of the arc (in degrees)
     * @param o offset of the arc (in degrees)
     */
    public GArc(
            final int x,
            final int y,
            final int r,
            final int a,
            final int o)
    {
        this.x = x - r;
        this.y = y - r;
        this.w = r * 2;
        this.h = r * 2;
        this.sa = o;
        this.aa = a;
    }

    /**
     * Generates an arc of an oval
     *
     * @param x x coordinate of the lower left corner
     * @param y y coordinate of the lower left corner
     * @param w width of the rectangle
     * @param h height of the rectangle
     * @param a angle of the arc (in degrees)
     * @param o offset of the arc (in degrees)
     */
    public GArc(
            final int x,
            final int y,
            final int w,
            final int h,
            final int a,
            final int o)
    {
        this.x = x - w / 2;
        this.y = y - h / 2;
        this.w = w;
        this.h = h;
        this.sa = o;
        this.aa = a;
    }

    @Override
    public void draw(final Graphics2D g) {
        if (fill()){
            g.setPaint(getFillPaint());
            g.fillArc(x, y, w, h, sa, aa);
        }

        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawArc(x, y, w, h, sa, aa);
    }

    @Override
    public void traslate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Moves the center of this arc to the given coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(final int x, final int y) {
        this.x = x - w / 2;
        this.y = y - h / 2;
    }

    @Override
    public GArc clone() {
        return new GArc(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 47 * hash + x;
        hash = 47 * hash + y;
        hash = 47 * hash + w;
        hash = 47 * hash + h;
        hash = 47 * hash + sa;
        hash = 47 * hash + aa;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GArc other = (GArc) obj;
        return !(
            x  != other.x  |
            y  != other.y  |
            w  != other.w  |
            h  != other.h  |
            sa != other.sa |
            aa != other.aa
        );
    }

    @Override
    public Area getShape() {
        return new Area(new Arc2D.Double(x, y, w, h, sa, aa, Arc2D.OPEN));
    }
}
