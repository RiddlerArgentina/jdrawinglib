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

import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Area;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GRectangle extends GFillableE {
    protected int x, y, w, h, cx, cy;

    /**
     * Copy constructor
     *
     * @param e {@code GRectangle} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GRectangle(GRectangle e) {
        super(e);

        cx = e.cx;
        cy = e.cy;
        x = e.x;
        y = e.y;
        h = e.h;
        w = e.w;
    }

    /**
     * Creates a new rectangle (square) on with center in {@code (0, 0)}
     *
     * @param s side of the square
     * @throws InvalidArgumentException if {@code s} is less or equal than 0
     */
    public GRectangle(final int s) {
        this(0, 0, s);
    }

    /**
     * Creates a new rectangle (square) given the coordinates of the center,
     * and the size of it's side
     *
     * @param x x coordinate of the center of the rectangle
     * @param y y coordinate of the center of the rectangle
     * @param s side of the square
     * @throws InvalidArgumentException if {@code s} is less or equal than 0
     */
    public GRectangle(final int x, final int y, final int s) {
        this(x, y, s, s);
    }

    /**
     * Creates a new rectangle given the coordinates of the center, it's width
     * and height
     *
     * @param x x coordinate of the center of the rectangle
     * @param y y coordinate of the center of the rectangle
     * @param w width of the rectangle
     * @param h height of the rectangle
     * @throws InvalidArgumentException if either {@code w} or {@code h} are
     * less or equal than 0
     */
    public GRectangle(
            final int x,
            final int y,
            final int w,
            final int h)
    {
        if (w <= 0 || h <= 0){
            String msg = "The size must be a non-zero positive integer";
            throw new InvalidArgumentException(msg);
        }

        this.cx = x;
        this.cy = y;
        this.x = x - w / 2;
        this.y = y - h / 2;
        this.w = w;
        this.h = h;
    }

    /**
     * Tells whether a rectangle is intersecting this rectangle
     *
     * @param r {@link GRectangle} to check
     * @return {@code true} if the rectangles intersect and {@code false}
     * otherwise
     * @throws NullPointerException if {@code r} is {@code null}
     */
    public boolean intersects(final GRectangle r) {
        if (r == null){
            throw new NullPointerException("The rectangle can't be null");
        }

        return !((x + w < r.x) | (r.x + r.w < x) |
                 (y + h < r.y) | (r.y + r.h < y));
    }

    /**
     * Tells whether a rectangle is contained on this rectangle
     *
     * @param r {@link GRectangle} to check
     * @return {@code true} if the rectangle is contained and {@code false}
     * otherwise
     * @throws NullPointerException if {@code r} is {@code null}
     */
    public boolean contains(final GRectangle r) {
        if (r == null){
            throw new NullPointerException("The rectangle can't be null");
        }

        return contains(   r.x   ,    r.y   ) & contains(r.x + r.w,    r.y   ) &
               contains(r.x + r.w, r.y + r.h) & contains(   r.x   , r.y + r.h);
    }

    /**
     * Tells whether a line is contained on this rectangle
     *
     * @param l {@link GLine} to check
     * @return {@code true} if the line is contained and {@code false} otherwise
     * @throws NullPointerException if {@code l} is {@code null}
     */
    public boolean contains(final GLine l) {
        if (l == null){
            throw new NullPointerException("The line can't be null");
        }

        return contains(l.getStartPoint()) && contains(l.getEndPoint());
    }

    /**
     * Tells whether a point is contained on this rectangle
     *
     * @param p {@link GPoint} to check
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public boolean contains(final GPoint p) {
        if (p == null){
            throw new NullPointerException("The point can't be null");
        }

        return contains(p.x(), p.y());
    }

    /**
     * Tells whether a point is contained on this rectangle
     *
     * @param xx x coordinate of the point
     * @param yy y coordinate of the point
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     */
    public boolean contains(final int xx, final int yy) {
        return (x <= xx) & (xx <= x + w) & (y <= yy) & (yy <= y + h);
    }

    /**
     * Retrieves the X coordinate of the center of this rectangle
     *
     * @return x coordinate
     */
    public int getCX() {
        return cx;
    }

    /**
     * Retrieves the Y coordinate of the center of this rectangle
     *
     * @return y coordinate
     */
    public int getCY() {
        return cy;
    }

    /**
     * Retrieves x coordinate of the right limit of this rectangle
     *
     * @return x coordinate of the right limit
     */
    public int getRightL() {
        return x + w;
    }

    /**
     * Retrieves x coordinate of the left limit of this rectangle
     *
     * @return x coordinate of the left limit
     */
    public int getLeftL() {
        return x;
    }

    /**
     * Retrieves y coordinate of the lower limit of this rectangle
     *
     * @return y coordinate of the lower limit
     */
    public int getLowerL() {
        return y;
    }

    /**
     * Retrieves y coordinate of the upper limit of this rectangle
     *
     * @return y coordinate of the upper limit
     */
    public int getUpperL() {
        return y + h;
    }

    /**
     * Calculates the area of this rectangle
     *
     * @return area
     */
    public double area() {
        return h * w;
    }

    /**
     * Calculated the perimeter of this rectangle
     *
     * @return perimeter
     */
    public double perimeter() {
        return h + h + w + w;
    }

    /**
     * Retrieves the height of this rectangle
     *
     * @return height
     */
    public int getHeight() {
        return h;
    }

    /**
     * Retrieves the width of this rectangle
     *
     * @return width
     */
    public int getWidth() {
        return w;
    }

    @Override
    public void traslate(final int x, final int y) {
        this.cx += x;
        this.cy += y;
        this.x += x;
        this.y += y;
    }

    /**
     * Moves the center of this rectangle to the given coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(final int x, final int y) {
        this.cx = x;
        this.cy = y;
        this.x  = x - w / 2;
        this.y  = y - h / 2;
    }

    @Override
    public void draw(final Graphics2D g) {
        g.setStroke(getStroke());

        if (fill()){
            g.setPaint(getFillPaint());
            g.fillRect(x, y, w, h);
        }

        g.setPaint(getPaint());
        g.drawRect(x, y, w, h);
    }

    @Override
    public GRectangle clone() {
        return new GRectangle(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 83 * hash + x;
        hash = 83 * hash + y;
        hash = 83 * hash + w;
        hash = 83 * hash + h;
        hash = 83 * hash + cx;
        hash = 83 * hash + cy;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GRectangle other = (GRectangle) obj;
        return !(
            x  != other.x |
            y  != other.y |
            w  != other.w |
            h  != other.h |
            cx != other.cx |
            cy != other.cy
        );
    }

    @Override
    public Area getShape() {
        return new Area(new Rectangle(x, y, w, h));
    }

}
