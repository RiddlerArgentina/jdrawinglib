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
package com.dkt.graphics.elements;

import java.awt.Graphics2D;

/**
 * This class represents a <b>mutable</b> point
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GPoint extends GraphicE {
    private final boolean drawCross;
    private int cs;
    private int x, y;

    /**
     * Copy constructor
     *
     * @param e {@code GPoint} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GPoint(GPoint e) {
        super(e);

        cs = e.cs;
        x  = e.x;
        y  = e.y;
        drawCross = e.drawCross;
    }

    /**
     * Creates a new {@code GPoint}
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     */
    public GPoint(final int x, final int y) {
        this(x, y, 0);
    }

    /**
     * Creates a new {@code GPoint} with a given cross size
     *
     * @param x The x coordinate of the point
     * @param y The y coordinate of the point
     * @param cs Cross size
     */
    public GPoint (
            final int x,
            final int y,
            final int cs)
    {
        this.drawCross = cs == 0;
        this.cs = cs;
        this.x = x;
        this.y = y;
    }

    /**
     * Retrieves the X coordinate of this point
     *
     * @return x coordinate
     */
    public int x() {
        return x;
    }

    /**
     * Retrieves the Y coordinate of this point
     *
     * @return y coordinate
     */
    public int y() {
        return y;
    }

    /**
     * Calculates the distance between this point and the one passed as an
     * argument
     *
     * @param p the reference point
     * @return distance between points
     * @throws NullPointerException if point is {@code null}
     */
    public double distance(final GPoint p) {
        if (p == null){
            throw new NullPointerException("Point can't be null");
        }

        return distance(p.x, p.y);
    }

    /**
     * Calculates the distance between this point and the one passed as an
     * argument
     *
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return distance between points
     */
    public double distance(final int x, final int y) {
        return Math.hypot(this.x - x, this.y - y);
    }

    /**
     * Is {@code a->b->c} a counterclockwise (levorotary) turn?
     *
     * @param a first point
     * @param b second point
     * @param c third point
     * @return {@code {-1, 0, +1}} if {@code a->b->c} is respectively a
     * clockwise, collinear; counterclockwise turn
     * @author http://algs4.cs.princeton.edu/25applications/Point2D.java.html
     */
    public static int ccw(
            final GPoint a,
            final GPoint b,
            final GPoint c)
    {
        final double area = (b.x - a.x) * (c.y - a.y)
                          - (b.y - a.y) * (c.x - a.x);

        if (area < 0) {
            return -1;
        } else if (area > 0) {
            return 1;
        }

        return  0;
    }

    @Override
    public void draw(final Graphics2D g) {
        g.setPaint(getPaint());

        if (drawCross){
            g.fillRect(x, y, 1, 1);
        } else {
            g.setStroke(getStroke());
            g.drawLine(x - cs, y     , x + cs, y     );
            g.drawLine(x     , y - cs, x     , y + cs);
        }
    }

    @Override
    public void traslate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Moves this point to the given coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return String.format("(%d, %d)", x, y);
    }

    @Override
    public GPoint clone() {
        return new GPoint(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 59 * hash + (drawCross ? 1 : 0);
        hash = 59 * hash + cs;
        hash = 59 * hash + x;
        hash = 59 * hash + y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GPoint other = (GPoint) obj;
        return !(
            drawCross != other.drawCross |
            cs        != other.cs |
            x         != other.x |
            y         != other.y
        );
    }

}
