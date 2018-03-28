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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.util.Objects;

/**
 * This class represents an abstract Graphical Element
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public abstract class GraphicE implements Cloneable {
    /**
     * Size 1 stroke
     */
    public static final Stroke STROKE = new BasicStroke();
    private Paint  paint;
    private Stroke stroke;

    /**
     * Copy constructor
     *
     * @param e {@code GraphicE} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GraphicE(GraphicE e) throws IllegalArgumentException {
        if (e == null) {
            final String msg = "Copy constructor argument can't be null";
            throw new IllegalArgumentException(msg);
        }

        paint  = e.paint;
        stroke = e.stroke;
    }

    protected GraphicE() {
        paint  = Color.BLACK;
        stroke = STROKE;
    }

    /**
     * The {@link Paint} of this component
     *
     * @return Paint the {@link Paint} used to render this {@link GraphicE}
     */
    public Paint getPaint() {
        return paint;
    }

    /**
     * Sets the {@link Paint} for this component
     *
     * @param paint the paint use to render this {@link GraphicE}
     * @throws IllegalArgumentException if paint is {@code null}
     */
    public void setPaint(final Paint paint) {
        if (paint == null){
            throw new IllegalArgumentException("Paint can't be null");
        }

        this.paint = paint;
    }

    /**
     * Gets the {@link Stroke} used to draw this component
     *
     * @return stroke used for this {@link GraphicE}
     */
    public Stroke getStroke() {
        return stroke;
    }

    /**
     * Sets a new {@link Stroke} for this component
     *
     * @param stroke the {@link Stroke} to use
     * @throws IllegalArgumentException if stroke is {@code null}
     */
    public void setStroke(final Stroke stroke) {
        if (stroke == null){
            throw new IllegalArgumentException("The stroke can't be null");
        }

        this.stroke = stroke;
    }

    /**
     * Traslates this element on X and Y
     *
     * @param x size in {@code px} of the horizontal translation
     * @param y size in {@code px} of the vertical translation
     */
    public abstract void traslate(final int x, final int y);

    /**
     * Draws the component on the given graphics
     *
     * @param g where to draw
     */
    public abstract void draw(final Graphics2D g);

    @Override
    public abstract GraphicE clone();

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 47 * hash + Objects.hashCode(paint);
        hash = 47 * hash + Objects.hashCode(stroke);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        
        if (this == obj) {
            return true;
        }

        if (getClass() != obj.getClass()) {
            return false;
        }

        final GraphicE other = (GraphicE) obj;
        if (!Objects.equals(paint, other.paint)) {
            return false;
        }

        return Objects.equals(stroke, other.stroke);
    }
}
