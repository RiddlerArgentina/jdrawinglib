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

import java.awt.Paint;
import java.awt.geom.Area;
import java.util.Objects;

/**
 * This represents an abstract fillable element
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public abstract class GFillableE extends GraphicE {
    private boolean fill;
    private Paint fillPaint;

    /**
     * Copy constructor
     *
     * @param e {@code GFillableE} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    protected GFillableE(GFillableE e) {
        super(e);

        fill = e.fill;
        fillPaint = e.fillPaint;
    }

    protected GFillableE() {

    }

    /**
     * Retrieves the {@link Paint} used to render this component
     *
     * @return the {@code Paint}
     */
    public Paint getFillPaint() {
        return fillPaint;
    }

    /**
     * Sets the {@link Paint} that's used to fill this component
     *
     * @param paint The new {@code Paint} used to render this component
     * @throws IllegalArgumentException if {@code paint} is {@code null}
     */
    public void setFillPaint(final Paint paint) {
        if (paint == null){
            throw new IllegalArgumentException("Paint can't be null");
        }

        fillPaint = paint;
    }

    /**
     * Tells if the component must be filled
     * @return {@code true} if the component must be filled and {@code false}
     * otherwise
     */
    public boolean fill() {
        return fill;
    }

    /**
     * Tells if the component needs to be filled
     *
     * @param fill {@code true} if the component must be filled and
     * {@code false} otherwise
     */
    public void setFill(final boolean fill) {
        this.fill = fill;
    }

    /**
     * This method should return a {@link Area} that represents the {@code
     * GFillableE}. Since this is not always possible, and the implementation of
     * {@link Area} isn't always easy, it should return {@code null} otherwise.
     * <br>In {@code jDrawingLib}s implementation we use the default shapes for
     * this. Note that this method is only called to create clips for {@link
     * Graphic} objects
     *
     * @return Shape
     */
    public abstract Area getShape();

    @Override
    public abstract GFillableE clone();

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 79 * hash + (fill ? 1 : 0);
        hash = 79 * hash + Objects.hash(fillPaint);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GFillableE other = (GFillableE) obj;
        
        if (this.fill != other.fill) {
            return false;
        }
        
        return Objects.equals(fillPaint, other.fillPaint);
    }
}
