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

import com.dkt.graphics.utils.MathUtils;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class GRegPoly extends GPoly {
    private int x, y, r, n;
    private double a;

    /**
     * Copy constructor
     *
     * @param e {@code GRegPoly} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GRegPoly(GRegPoly e){
        super(e);

        x = e.x;
        y = e.y;
        r = e.r;
        n = e.n;
        a = e.a;
    }

    /**
     * Constructs a regular polygon contained in a circle
     *
     * @param x X coordinate of the center of the circle
     * @param y Y coordinate of the center of the circle
     * @param r radius of the circle
     * @param n number of sides
     * @param a angle of the first point
     */
    public GRegPoly(
            final int x,
            final int y,
            final int r,
            final int n,
            final double a)
    {
        this.x = x;
        this.y = y;
        this.r = r;
        this.n = n;
        this.a = Math.toRadians(a);

        calc();
    }

    private void calc() {
        final double temp = 2 * Math.PI / n;
        final int[] fx = new int[n];
        final int[] fy = new int[n];

        for (int i = 0; i < n; i++){
            fx[i] = ((int)Math.round(x + Math.cos(temp * i + a) * r));
            fy[i] = ((int)Math.round(y + Math.sin(temp * i + a) * r));
        }

        xs = fx;
        ys = fy;
        size = xs.length;
    }

    /**
     * Retrieves the X coordinate of the center of the polygon
     *
     * @return x coordinate of the center
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the Y coordinate of the center of the polygon
     *
     * @return y coordinate of the center
     */
    public int getY() {
        return y;
    }

    /**
     * Returns the radius of the circle used to create
     *
     * @return radius
     */
    public int getRadius() {
        return r;
    }

    /**
     * Calculates the area of this polygon
     *
     * @return area of the polygon
     */
    public double area() {
        if (size <= 1){
            return 0;
        }

        mutex.lock();
        try{
            //The are is calculated as the sum of the areas of the triangles
            //formed between two consecutive points and the center of the circle
            double area = 0;
            for (int i = 0, m = size - 1; i <= m; i++){
                final int j = i == m ? 0 : i + 1;

                area += triangleArea(x, y, xs[i], ys[i], xs[j], ys[j]);
            }

            return area;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Calculated the perimeter of this polygon
     *
     * @return perimeter
     */
    public double perimeter() {
        if (size <= 1){
            return 0;
        }

        mutex.lock();
        try{
            double d0n = Math.hypot(xs[0] - xs[n - 1], ys[0] - ys[n - 1]);

            for (int i = 0, j = 1, m = size - 1; i < m; i++, j++){
                d0n += Math.hypot(xs[i] - xs[j], ys[i] - ys[j]);
            }

            return d0n;
        } finally {
            mutex.unlock();
        }
    }

    private double triangleArea(
            int x1, int y1,
            int x2, int y2,
            int x3, int y3)
    {
        //About this... it works... I honestly can't remember how did I came up
        //with this, it's a mix of the cross product, some weired theorem found
        //on wikipedia and something I read on stackoverflow. Unfortunately I
        //wrote this around 4 years ago, and I can't recall the references...
        final double d12 = Math.hypot(x1 - x2, y1 - y2);
        final double d23 = Math.hypot(x2 - x3, y3 - y3);
        final double d31 = Math.hypot(x3 - x1, y3 - y1);

        final double peri2 = (d12 + d23 + d31) / 2;

        double area = peri2;

        area *= peri2 - d12;
        area *= peri2 - d23;
        area *= peri2 - d31;

        return Math.sqrt(area);
    }

    /**
     * Tells if a {@link GPoint} is contained in the polygon
     *
     * @param p {@code GPoint} to evaluate
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @throws NullPointerException if {@code p} is {@code null}
     */
    public boolean contains(final GPoint p) throws NullPointerException {
        if (p == null){
            throw new NullPointerException("The point can't be null");
        }

        return contains(p.x(), p.y());
    }

    /**
     * Tells if a point is contained in the polygon
     *
     * @param xx X coordinate of the point
     * @param yy Y coordinate of the point
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     */
    public boolean contains(final int xx, final int yy) {
        //Quick check (point vs outter circle)
        if (Math.hypot(xx - x, xx - y) > r) {
            return false;
        }

        //@TODO Quick check (point vs incircle) would be a nice addition...

        mutex.lock();
        try {
            for (int i = 0, j, m = n - 1; i <= m; i++){
                j = i == m ? 0 : i + 1;

                if (triangleContains(xx, yy, xs[i], ys[i], xs[j], ys[j], x, y)){
                    return true;
                }
            }
        } finally {
            mutex.unlock();
        }

        return false;
    }

    private boolean triangleContains(
            int xx, int yy,
            int x1, int y1,
            int x2, int y2,
            int x3, int y3)
    {
        //@TODO Test me
        //http://www.blackpawn.com/texts/pointinpoly/
        // Compute vectors
        final int v0x = x3 - x1;
        final int v0y = y3 - y1;
        final int v1x = x2 - x1;
        final int v1y = y2 - y1;
        final int v2x = xx - x1;
        final int v2y = yy - y1;

        // Compute dot products
        final double d00 = MathUtils.dot(v0x, v0y, v0x, v0y);
        final double d01 = MathUtils.dot(v0x, v0y, v1x, v1y);
        final double d02 = MathUtils.dot(v0x, v0y, v2x, v2y);
        final double d11 = MathUtils.dot(v1x, v1y, v1x, v1y);
        final double d12 = MathUtils.dot(v1x, v1y, v2x, v2y);

        // Compute barycentric coordinates
        final double id = 1 / (d00 * d11 - d01 * d01);
        final double u = (d11 * d02 - d01 * d12) * id;
        final double v = (d00 * d12 - d01 * d02) * id;

        // Check if point is in triangle
        return (u >= 0) & (v >= 0) & (u + v < 1);
    }

    /**
     * Rotates the current {@code GRegPoly} a given angle
     *
     * @param angle angle in degrees
     */
    public void rotate(final double angle) {
        radRotate(Math.toRadians(angle));
    }

    /**
     * Rotates the current {@code GRegPoly} a given angle
     *
     * @param angle angle in radians
     */
    public void radRotate(final double angle) {
        a += angle;
        a %= 2 * Math.PI;
        calc();
    }

    @Override
    public void traslate(final int x, final int y) {
        super.traslate(x, y);
        this.x += x;
        this.y += y;
    }

    @Override
    public GRegPoly clone() {
        return new GRegPoly(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + x;
        hash = 53 * hash + y;
        hash = 53 * hash + r;
        hash = 53 * hash + n;
        hash = 53 * hash + (int) (Double.doubleToLongBits(a)
                               ^ (Double.doubleToLongBits(a) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GRegPoly other = (GRegPoly) obj;
        return !(
            x != other.x |
            y != other.y |
            r != other.r |
            n != other.n |
            a != other.a
        );
    }

}
