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
import java.awt.geom.Area;
import java.awt.geom.Ellipse2D;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GCircle extends GFillableE {
    private int x, y, d, r;

    /**
     * Copy constructor
     *
     * @param e {@code GCircle} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GCircle(GCircle e) {
        super(e);

        e.r = r;
        e.d = d;
        e.x = x;
        e.y = y;
    }

    /**
     * @param x X coordinate of the center
     * @param y Y coordinate of the center
     * @param r radius
     */
    public GCircle(final int x, final int y, int r) {
        r = Math.abs(r);
        this.x = x - r;
        this.y = y - r;
        this.d = 2 * r;
        this.r = r;
    }

    /**
     * Retrieves the X coordinate of the center of the circle
     *
     * @return x coordinate of the center
     */
    public int x() {
        return x + r;
    }

    /**
     * Retrieves the Y coordinate of the center of the circle
     *
     * @return y coordinate of the center
     */
    public int y() {
        return y + r;
    }

    /**
     * Returns the radius of the circle
     *
     * @return radius
     */
    public int getRadius() {
        return r;
    }

    /**
     * Tells if a given circle is contained on this circle, that means that
     * every point in the circle is contained in this one.
     *
     * @param c the line to check
     * @return {@code true} if the circle is contained and {@code false}
     * otherwise
     * @throws NullPointerException if {@code circle} is {@code null}
     */
    public boolean contains(final GCircle c) {
        if (c == null){
            throw new NullPointerException("The circle can't be null");
        }

        final double cd = Math.hypot(x() - c.x(), y() - c.y());
        final double dr = getRadius() - c.getRadius();

        return cd <= dr;
    }

    /**
     * Tells if a given line segment is contained in the circle, that means
     * that both ends are contained in the circle
     *
     * @param line the line to check
     * @return {@code true} if the line is contained and {@code false}
     * otherwise
     * @throws NullPointerException if {@code line} is {@code null}
     */
    public boolean contains(final GLine line) {
        if (line == null){
            throw new NullPointerException("The line can't be null");
        }

        return contains(line.getStartPoint()) &&
               contains(line.getEndPoint  ());
    }

    /**
     * Tells if a given point is contained in the circle
     *
     * @param point the point to check
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @throws NullPointerException if {@code point} is {@code null}
     */
    public boolean contains(final GPoint point) {
        if (point == null){
            throw new NullPointerException("The point can't be null");
        }

        return contains(point.x(), point.y());
    }

    /**
     * Tells if a given point is contained in the circle
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     */
    public boolean contains(final int x, final int y) {
        return Math.hypot(this.x - x, this.y - y) <= r;
    }

    /**
     * Tells if this circle intersects with another one
     *
     * @param c The circle to check
     * @return {@code true} if the circles intersect and {@code false} otherwise
     * @throws NullPointerException if {@code circle} is {@code null}
     */
    public boolean intersects(final GCircle c) {
        if (c == null){
            throw new NullPointerException("The circle can't be null");
        }

        final double cd = Math.hypot(x() - c.x(), y() - c.y());
        final double sr = getRadius() + c.getRadius();

        return cd <= sr;
    }

    /**
     * Tells if this circle intersects with a line
     *
     * @param line The line to check
     * @return {@code true} if they intersect and {@code false} otherwise
     * @throws NullPointerException if {@code line} is {@code null}
     */
    public boolean intersects(final GLine line) {
        if (line == null){
            throw new NullPointerException("The line can't be null");
        }

        return discriminant(line) >= 0;
    }

    /**
     * Calculates the area of this circle
     *
     * @return area
     */
    public double area() {
        return Math.PI * r * r;
    }

    /**
     * Calculated the perimeter of this circle
     *
     * @return perimeter
     */
    public double perimeter() {
        return Math.PI * d;
    }

    /**
     * Calculates the area of the intersection of this circle and the one passed
     * as an argument
     *
     * @param circle the circle that intersects
     * @return area of the intersection
     * @throws NullPointerException if {@code circle} is {@code null}
     */
    public double intersectionArea(final GCircle circle) {
        if (circle == null){
            throw new NullPointerException("The circle can't be null");
        }
        //@TODO implement me
        return 0;
    }

    private double discriminant(final GLine line){
        //http://stackoverflow.com/questions/6091728/line-segment-circle-intersection
        final GPoint start = line.getStartPoint();
        final GPoint end   = line.getEndPoint();

        final double x1 = start.x();
        final double y1 = start.y();
        final double x2 = end.x();
        final double y2 = end.y();
        final double cx = x();
        final double cy = y();
        final double cr = getRadius();

        final double dx = x2 - x1;
        final double dy = y2 - y1;
        final double a = dx * dx + dy * dy;
        final double b = 2 * (dx * (x1 - cx) + dy * (y1 - cy));
        final double c = cx * cx + cy * cy
                       + x1 * x1 + y1 * y1
                       - 2 * (cx * x1 + cy * y1)
                       - cr * cr;

        return b * b - 4 * a * c;
    }

    @Override
    public void draw(final Graphics2D g) {
        if (fill()){
            g.setPaint(getFillPaint());
            g.fillOval(x, y, d, d);
        }

        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawOval(x, y, d, d);
    }

    @Override
    public void traslate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Moves the center of this circle to the given coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public GCircle clone() {
        return new GCircle(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + x;
        hash = 79 * hash + y;
        hash = 79 * hash + d;
        hash = 79 * hash + r;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GCircle other = (GCircle) obj;
        return !(
            x != other.x |
            y != other.y |
            d != other.d |
            r != other.r
        );
    }

    @Override
    public Area getShape() {
        return new Area(new Ellipse2D.Double(x, y, d, d));
    }
}
