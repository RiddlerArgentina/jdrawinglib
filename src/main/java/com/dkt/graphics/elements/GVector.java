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
import java.util.Arrays;

/**
 * This class represents a Vector, basically a line with a triangular tip.<br>
 * If the modulus of the vector is {@code 0} then not even the triangle is
 * drawn.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GVector extends GraphicE {
    private int[] xs = new int[3];
    private int[] ys = new int[3];

    //It would be awesome if this could handle arrow angles >= 45°
    private double aa = Math.toRadians(25);
    private int x1;
    private int y1;
    private int x2;
    private int y2;

    private double l;
    private double a;

    private int aw = 10;
    private int n = 3;

    /**
     * Copy constructor
     *
     * @param e {@code GVector} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GVector(GVector e) {
        super(e);

        aa = e.aa;
        aw = e.aw;
        x1 = e.x1;
        x2 = e.x2;
        y1 = e.y1;
        y2 = e.y2;
        a = e.a;
        l = e.l;
        n = e.n;

        xs = new int[xs.length];
        ys = new int[ys.length];

        System.arraycopy(e.xs, 0, xs, 0, xs.length);
        System.arraycopy(e.ys, 0, ys, 0, ys.length);
    }

    /**
     * Basic Vector constructor
     *
     * @param x x coordinate of the application point of the vector
     * @param y y coordinate of the application point of the vector
     * @param l vector modulus
     * @param a vector angle (in degrees)
     */
    public GVector(
            final int x,
            final int y,
            final double l,
            final double a)
    {
        this.a = Math.toRadians(a % 360);
        this.l = l;

        this.x1 = x;
        this.y1 = y;

        calc();
    }

    /**
     * Creates a new vector from a given {@link GLine}, using the start point as
     * the application point.
     *
     * @param line the line to use as base for this {@code GVector}
     * @throws IllegalArgumentException if {@code line} is {@code null}
     */
    public GVector(final GLine line) throws IllegalArgumentException {
        if (line == null) {
            throw new IllegalArgumentException("The line can't be null");
        }

        this.a = line.getRadArgument();
        this.l = line.modulus();

        final GPoint start = line.getStartPoint();
        this.x1 = start.x();
        this.y1 = start.y();

        calc();
    }

    /**
     * Basic Vector constructor
     *
     * @param x1 x coordinate of the application point of the vector
     * @param y1 y coordinate of the application point of the vector
     * @param x2 x coordinate of the end of the vector
     * @param y2 y coordinate of the end of the vector
     *
     */
    public GVector(
            final int x1,
            final int y1,
            final int x2,
            final int y2)
    {
        this.x1 = x1;
        this.y1 = y1;

        this.a = Math.atan2(y2 - y1, x2 - x1);
        this.l = Math.hypot(x2 - x1, y2 - y1);

        calc();
    }

    /**
     * Multiplies this vector by a scalar
     *
     * @param scalar scale
     */
    public void scalarMultiplication(final double scalar) {
        l *= scalar;
        calc();
    }

    /**
     * Calculates the dot product between this vector and the one passed as an
     * argument
     *
     * @param v other vector
     * @return dot product
     * @throws IllegalArgumentException if {@code v} is {@code null}
     */
    public double dot(final GVector v) {
        if (v == null){
            throw new IllegalArgumentException("The vector can't be null");
        }

        final int xx1 =   getXComponent();
        final int yy1 =   getYComponent();
        final int xx2 = v.getXComponent();
        final int yy2 = v.getYComponent();

        return (xx1 * xx2) + (yy1 * yy2);
    }

    /**
     * Calculates the module of the orthogonal vector resulting of the cross
     * product between this vector and the one passed as an argument,
     * considering the {@code z} coordinate of both vectors equal to zero
     *
     * @param v other vector
     * @return module of the cross product vector
     * @throws IllegalArgumentException if {@code v} is {@code null}
     */
    public double cross(final GVector v) {
        if (v == null){
            throw new IllegalArgumentException("The vector can't be null");
        }

        final int xx1 =   getXComponent();
        final int yy1 =   getYComponent();
        final int xx2 = v.getXComponent();
        final int yy2 = v.getYComponent();

        return (xx1 * yy2) - (yy1 * xx2);
    }

    /**
     * Retrieves the angle between this vector and the one passed as an argument
     *
     * @param v other vector
     * @return Angle between vectors (in degrees)
     * @throws IllegalArgumentException if {@code v} is {@code null}
     */
    public double angleBetween(final GVector v) {
        if (v == null){
            throw new IllegalArgumentException("The vector can't be null");
        }

        return Math.toDegrees(radAngleBetween(v));
    }

    /**
     * Retrieves the angle between this vector and the one passed as an argument
     *
     * @param v other vector
     * @return Angle between vectors (in radians)
     * @throws IllegalArgumentException if {@code v} is {@code null}
     */
    public double radAngleBetween(final GVector v) {
        if (v == null){
            throw new IllegalArgumentException("The vector can't be null");
        }

        return Math.asin(cross(v) / (modulus() * v.modulus()));
    }

    /**
     * Retrieves a vector with the same direction and application point but
     * with a modulus equal to 1
     *
     * @return normalized vector
     */
    public GVector normalized() {
        final GVector v = new GVector(this);
        v.scalarMultiplication(1. / modulus());
        return v;
    }

    /**
     * Changes the modulus of this vector
     *
     * @param modulus new modulus of the vector
     */
    public void setModulus(final double modulus) {
        l = modulus;
        calc();
    }

    /**
     * Changes the argument of this vector (in radians)
     *
     * @param arg new argument of the vector
     */
    public void setRadArgument(final double arg) {
        a = arg % (2 * Math.PI);
        calc();
    }

    /**
     * Changes the argument of this vector (in degrees)
     *
     * @param arg new argument of the vector
     */
    public void setArgument(final double arg) {
        a = Math.toRadians(arg % 360);
        calc();
    }

    /**
     * Changes the arrow weight. <br>
     * The arrow weight is defined as the length of the arrow, by default the
     * arrow will be appended to the line, but if {@code arrowWeight} is
     * negative then the arrow will end at the end of the line.
     *
     * @param arrowWeight new arrow weight
     */
    public void setArrowWeight(final int arrowWeight) {
        aw = arrowWeight;
        calc();
    }

    /**
     * Sets the new angle for the tip of the arrow (in degrees).
     *
     * @param arrowAngle angle in degrees.
     */
    public void setArrowTipAngle(final double arrowAngle) {
        aa = Math.toRadians(arrowAngle / 2);
        calc();
    }

    /**
     * Retrieves the X coordinate of the point of application of the vector
     *
     * @return x coordinate
     */
    public int x() {
        return x1;
    }

    /**
     * Retrieves the Y coordinate of the point of application of the vector
     *
     * @return y coordinate
     */
    public int y() {
        return y1;
    }

    /**
     * Retrieves the X coordinate of the tip of the vector
     *
     * @return X coordinate
     */
    public int xf() {
        return x2;
    }

    /**
     * Retrieves the Y coordinate of the tip of the vector
     *
     * @return Y coordinate
     */
    public int yf() {
        return y2;
    }

    /**
     * Retrieves the X projection of this vector
     *
     * @return x component
     */
    public int getXComponent() {
        return x2 - x1;
    }

    /**
     * Retrieves the Y projection of this vector
     *
     * @return y component
     */
    public int getYComponent() {
        return y2 - y1;
    }

    /**
     * Retrieves the modulus of the vector
     *
     * @return modulus
     */
    public double modulus() {
        return l;
    }

    /**
     * Retrieves the argument of the vector (in degrees)
     *
     * @return argument
     */
    public double argument() {
        return Math.toDegrees(a);
    }

    /**
     * Retrieves the argument of the vector (in radians)
     *
     * @return argument
     */
    public double radArgument() {
        return a;
    }

    public void rotate(final double a) {
        radRotate(Math.toRadians(a));
    }

    public void radRotate(final double a) {
        this.a += a;
        this.a %= 2 * Math.PI;

        calc();
    }

    private void calc() {
        final double sl =-Math.signum(l) * Math.abs(aw);
        final double ca = Math.cos(a);
        final double sa = Math.sin(a);

        //Ensure the arrow position when l is negative
        //final double ll = aw > 0 ? (l < 0 ? -aw : aw) : 0;

        //Last point of the line
        x2 = x1 + (int)(l * ca);
        y2 = y1 + (int)(l * sa);

        //Calculate triangle
        final int xx = x2;// - (int)(ll * ca);
        final int yy = y2;// - (int)(ll * sa);

        xs[0] = xx;
        xs[1] = xx + (int)(sl * Math.cos(a -  aa));
        xs[2] = xx + (int)(sl * Math.cos(a +  aa));
        ys[0] = yy;
        ys[1] = yy + (int)(sl * Math.sin(a -  aa));
        ys[2] = yy + (int)(sl * Math.sin(a +  aa));

        n = Math.abs(l) < 1e-10 ? 0 : 3;
    }

    /**
     * Adds a finite number of vectors with <b>no</b> overflow check
     *
     * @param vectors the vectors to add
     * @return A new {@link GVector} equal to the resultant
     * @throws IllegalArgumentException if no vector is passed
     */
    public GVector add(final GVector... vectors) {
        if (vectors == null){
            throw new IllegalArgumentException("You must add at least ONE vector");
        }

        int x = x2;
        int y = y2;
        for (GVector vector : vectors){
            x += vector.x2 - vector.x1;
            y += vector.y2 - vector.y1;
        }

        final double mod = Math.hypot(x, y);
        final double arg = Math.toDegrees(Math.atan2(y, x));

        return new GVector(x1, y1, mod, arg);
    }

    public GVector subtract(final GVector... vectors) {
        if (vectors == null){
            throw new IllegalArgumentException("You must add at least ONE vector");
        }

        int x = x2;
        int y = y2;
        for (final GVector vector : vectors){
            x -= vector.x2 - vector.x1;
            y -= vector.y2 - vector.y1;
        }

        final double mod = Math.hypot(x, y);
        final double arg = Math.toDegrees(Math.atan2(y, x));

        return new GVector(x1, y1, mod, arg);
    }

    public double distance(final GVector v) {
        if (v == null){
            throw new IllegalArgumentException("The vector can't be null");
        }
        //@TODO test me
        return subtract(v).modulus();
    }

    @Override
    public void draw(Graphics2D g) {
        g.setPaint(getPaint());
        g.setStroke(getStroke());
        g.drawLine(x1, y1, x2, y2);
        g.drawPolygon(xs, ys, n);
        g.fillPolygon(xs, ys, n);
    }

    @Override
    public void traslate(final int x, final int y) {
        x1 += x;
        y1 += y;
        calc();
    }

    /**
     * Moves the application point of this vector to the given coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(final int x, final int y) {
        x1 = x;
        y1 = y;
        calc();
    }

    @Override
    public GVector clone() {
        return new GVector(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 89 * hash + Arrays.hashCode(xs);
        hash = 89 * hash + Arrays.hashCode(ys);
        hash = 89 * hash + x1;
        hash = 89 * hash + y1;
        hash = 89 * hash + x2;
        hash = 89 * hash + y2;
        hash = 89 * hash + aw;
        hash = 89 * hash + n;
        hash = 89 * hash + (int) (Double.doubleToLongBits(l)
                               ^ (Double.doubleToLongBits(l) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(a)
                               ^ (Double.doubleToLongBits(a) >>> 32));
        hash = 89 * hash + (int) (Double.doubleToLongBits(aa)
                               ^ (Double.doubleToLongBits(aa) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GVector other = (GVector) obj;
        if (!Arrays.equals(this.xs, other.xs)) {
            return false;
        }
        if (!Arrays.equals(this.ys, other.ys)) {
            return false;
        }

        return !(
            aa != other.aa |
            aw != other.aw |
            x1 != other.x1 |
            y1 != other.y1 |
            x2 != other.x2 |
            y2 != other.y2 |
            n  != other.n  |
            l  != other.l  |
            a  != other.a
        );
    }

}
