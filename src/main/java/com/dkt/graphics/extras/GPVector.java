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
package com.dkt.graphics.extras;

import com.dkt.graphics.elements.GVector;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import java.awt.Graphics2D;
import java.awt.Paint;

/**
 * This class represents a vector and it's projections, it's mainly used in
 * physics problems where you need to print how the orthogonal components of
 * a vector changes
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPVector extends GraphicE {
    private final Graphic vector = new Graphic(3);
    private final GVector main;
    private final GVector xPro;
    private final GVector yPro;

    public GPVector(GPVector e){
        super(e);

        main = new GVector(e.main);
        xPro = new GVector(e.xPro);
        yPro = new GVector(e.yPro);
    }

    /**
     * Creates a new {@code GPVector} based on the {@code GVector} passed
     * as argument. <br>
     * <i>Note:</i> this constructor uses a COPY of the vector, so if you need
     * to change some parameters of the vector, you'll need to use:<ul>
     * <li>{@link GPVector#setArgument(double)}
     * <li>{@link GPVector#setModulus(double)}
     * <li>{@link GPVector#setModulus(double, double)}
     * <li>{@link GPVector#setPaint(Paint)}</ul>
     *
     * @param v the vector used as base
     * @throws IllegalArgumentException if {@code v} is {@code null}
     */
    public GPVector(GVector v){
        this(v.x(), v.y(), v.modulus(), v.argument());
    }

    /**
     * Creates a new {@code GPVector}
     *
     * @param x x coordinate of the application point of the vector
     * @param y y coordinate of the application point of the vector
     * @param l vector modulus
     * @param a vector angle (in degrees)
     */
    public GPVector(int x, int y, double l, double a){
        main = new GVector(x, y, l, a);

        xPro = new GVector(x, y, main.getXComponent(), 0. );
        yPro = new GVector(x, y, main.getYComponent(), 90.);

        vector.add(xPro);
        vector.add(yPro);
        vector.add(main);
    }

    /**
     * Sets a new modulus to the main vector (that will also change the
     * components)
     *
     * @param m modulus
     */
    public void setModulus(double m){
        main.setModulus(m);

        xPro.setModulus(main.getXComponent());
        yPro.setModulus(main.getYComponent());
    }

    /**
     * Sets a new modulus and argument to the main vector based on the modulus
     * of the orthogonal components
     *
     * @param x x component
     * @param y y component
     */
    public void setModulus(double x, double y){
        main.setModulus(Math.hypot(x, y));
        main.setRadArgument(Math.atan2(y, x));

        xPro.setModulus(x);
        yPro.setModulus(y);
    }

    /**
     * Changes the argument of the main vector
     *
     * @param a argument in degrees
     */
    public void setArgument(double a){
        main.setArgument(a);

        xPro.setModulus(main.getXComponent());
        yPro.setModulus(main.getYComponent());
    }

    /**
     * Sets the {@link Paint} for the main vector
     *
     * @param paint the paint use to render the main vector
     * @throws IllegalArgumentException if paint is {@code null}
     */
    @Override
    public void setPaint(Paint paint){
        main.setPaint(paint);
    }

    /**
     * Sets the {@link Paint} for the horizontal component of this vector
     *
     * @param paint the paint use to render the horizontal component
     * @throws IllegalArgumentException if paint is {@code null}
     */
    public void setHCompPaint(Paint paint){
        xPro.setPaint(paint);
    }

    /**
     * Sets the {@link Paint} for the vertical component of this vector
     *
     * @param paint the paint use to render the vertical component
     * @throws IllegalArgumentException if paint is {@code null}
     */
    public void setVCompPaint(Paint paint){
        yPro.setPaint(paint);
    }

    /**
     * Moves the start point of this vector to the selected coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(int x, int y){
        main.move(x, y);
        xPro.move(x, y);
        yPro.move(x, y);
    }

    @Override
    public GPVector clone() {
        return new GPVector(this);
    }

    @Override
    public void traslate(int x, int y) {
        vector.traslate(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        vector.draw(g);
    }
 }
