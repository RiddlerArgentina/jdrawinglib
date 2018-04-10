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

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GRegPoly extends GPoly {
    private int x, y, r, n;
    private double a;

    /**
     * Copy constructor
     *
     * @param e {@code GRegPoly} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
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
        return r * r * n * Math.sin(Math.PI * 2 / n) / 2;
    }

    /**
     * Calculated the perimeter of this polygon
     *
     * @return perimeter
     */
    public double perimeter() {
        return n * 2 * r * Math.sin(Math.PI / n);
    }

    /**
     * Tells if a {@link GPoint} is contained in the polygon
     *
     * @param p {@code GPoint} to evaluate
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public boolean contains(final GPoint p) throws IllegalArgumentException {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
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
        if (xx == x && yy == y) {
            return true;
        }

        //Quick check (point vs outter circle)
        if (Math.hypot(xx - x, xx - y) > r) {
            return false;
        }

        //@TODO Quick check (point vs incircle) would be a nice addition...
        mutex.lock();
        try {
            final int m = n - 1;
            int j;
            for (int i = 0; i <= m; i++){
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
        //https://stackoverflow.com/questions/2049582/
        //how-to-determine-if-a-point-is-in-a-2d-triangle#2049593
        final int asx = xx - x1;
        final int asy = yy - y1;
        final boolean sab = (x2 - x1) * asy - (y2 - y1) * asx > 0;
        final boolean co1 = (x3 - x1) * asy - (y3 - y1) * asx > 0;
        final boolean co2 = (x3 - x2) * (yy - y2) - (y3 - y2) * (xx - x2) > 0;
        return !((co1 == sab) || (co2 != sab));
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
