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
package com.dkt.graphics.elements;

import java.awt.Graphics2D;

/**
 * This class represents a line segment
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GLine extends GraphicE {
    private int x1, y1, x2, y2;

    /**
     * Copy constructor
     *
     * @param e {@code GLine} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GLine(GLine e) {
        super(e);

        x1 = e.x1;
        x2 = e.x2;
        y1 = e.y1;
        y2 = e.y2;
    }

    /**
     * Constructs a new line segment based on the end points
     *
     * @param p1 start point
     * @param p2 end point
     */
    public GLine(
            final GPoint p1,
            final GPoint p2) throws NullPointerException
    {
        this(p1.x(), p1.y(), p2.x(), p2.y());
    }

    /**
     * Constructs a new line segment based on the end points
     *
     * @param x1 x coordinate of the start point
     * @param y1 y coordinate of the start point
     * @param x2 x coordinate of the end point
     * @param y2 y coordinate of the end point
     */
    public GLine(
            final int x1,
            final int y1,
            final int x2,
            final int y2)
    {
        this.x1 = x1;
        this.y1 = y1;
        this.x2 = x2;
        this.y2 = y2;
    }

    /**
     * Retrieves the middle point of the line
     *
     * @return middle point
     */
    public GPoint getMiddlePoint() {
        final int x = Math.min(x1, x2) + Math.abs(x1 - x2) / 2;
        final int y = Math.min(y1, y2) + Math.abs(y1 - y2) / 2;
        return new GPoint(x, y);
    }

    /**
     * Constructs a new line based on its start point, length and angle
     *
     * @param x x coordinate of the start point
     * @param y y coordinate of the start point
     * @param length length of the vector
     * @param angle angle (in degrees) of the line segment
     */
    public GLine (
            final int x,
            final int y,
            final double length,
            final double angle)
    {
        this.x1 = x;
        this.y1 = y;
        this.x2 = x + (int)(length * Math.cos(Math.toRadians(angle)));
        this.y2 = y + (int)(length * Math.sin(Math.toRadians(angle)));
    }

    /**
     * Retrieves an orthogonal line that segment contains (0, 0) as it's start
     * point
     *
     * @return Orthogonal {@code GLine}
     */
    public GLine getOrthogal() {
        return getOrthogal(0, 0);
    }

    /**
     * Retrieves an orthogonal line segment that contains (x, y) as it's start
     * point
     *
     * @param x x coordinate of the start point
     * @param y y coordinate of the start point
     * @return Orthogonal {@code GLine}
     */
    public GLine getOrthogal(final int x, final int y) {
        final GLine line = new GLine(this);

        line.x2 -= line.x1;
        line.y2 -= line.y1;
        line.y2 = -line.y2;
        line.x1 = 0;
        line.y1 = 0;

        line.traslate(x, y);

        return line;
    }

    /**
     * Retrieves an parallel line segment that contains (x, y) as it's start
     * point
     *
     * @param x x coordinate of the start point
     * @param y y coordinate of the start point
     * @return Orthogonal {@code GLine}
     */
    public GLine getParallel(final int x, final int y) {
        final GLine line = new GLine(this);

        line.traslate(-x1, -y1);
        line.traslate(x, y);

        return line;
    }

    /**
     * Retrieves a copy of the start point of this line segment
     *
     * @return start {@link GPoint} of the line segment
     */
    public GPoint getStartPoint() {
        return new GPoint(x1, y1);
    }

    /**
     * Retrieves a copy of the end point of this line segment
     *
     * @return end {@link GPoint} of the line segment
     */
    public GPoint getEndPoint() {
        return new GPoint(x2, y2);
    }

    /**
     * Retrieves the argument of the line segment (in radians)
     *
     * @return angle in radians
     */
    public double getRadArgument() {
        return Math.atan2(y2 - y1, x2 - x1);
    }

    /**
     * Retrieves the argument of the line segment (in degrees)
     *
     * @return angle in degrees
     */
    public double getArgument() {
        return Math.toDegrees(Math.atan2(y2 - y1, x2 - x1));
    }

    /**
     * Retrieves the length of the line segment
     *
     * @return length
     */
    public double modulus() {
        return Math.hypot(y2 - y1, x2 - x1);
    }

    /**
     * Tells if a point is contained in this line segment
     *
     * @param p point to check
     * @return {@code true} if the point is contained in the segment and
     * {@code false} otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public boolean contains(final GPoint p){
        if (p == null){
            throw new NullPointerException("The point can't be null");
        }

        return contains(p.x(), p.y());
    }

    /**
     * Tells if a point is contained in this line segment
     *
     * @param x the X coordinate of the point
     * @param y the Y coordinate of the point
     * @return {@code true} if the point is contained in the segment and
     * {@code false} otherwise
     */
    public boolean contains(final int x, final int y) {
        //http://stackoverflow.com/questions/17581738/
        // check-if-a-point-projected-on-a-line-segment-is-not-outside-it
        final double m  = (y2 - y1) / (x2 - x1);
        final double r1 = y1 + m * x1;
        final double r2 = y2 + m * x2;
        final double r  = y + m * x;

        //@TODO This can be unbranched
        return  r1 < r2 ? (r1 <= r & r <= r2) : (r2 <= r & r <= r1);
    }

    @Override
    public void draw(final Graphics2D g) {
        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawLine(x1, y1, x2, y2);
    }

    @Override
    public void traslate(final int x, final int y) {
        x1 += x;
        x2 += x;
        y1 += y;
        y2 += y;
    }

    @Override
    public GLine clone() {
        return new GLine(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 11 * hash + x1;
        hash = 11 * hash + y1;
        hash = 11 * hash + x2;
        hash = 11 * hash + y2;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GLine other = (GLine) obj;

        return !(
            x1 != other.x1 |
            y1 != other.y1 |
            x2 != other.x2 |
            y2 != other.y2
        );
    }
}
