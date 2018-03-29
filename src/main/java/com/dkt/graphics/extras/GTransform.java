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
package com.dkt.graphics.extras;

import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.util.Objects;

/**
 * This class wraps a {@link AffineTransform} in a {@link GraphicE}, in order to
 * give a bit more "flexibility" to {@link Graphic} objects
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GTransform extends GraphicE {
    private AffineTransform transform;

    /**
     * Copy constructor
     *
     * @param e {@code GTransform} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GTransform(GTransform e) {
        super(e);
        transform = new AffineTransform(e.transform);
    }

    private GTransform() {

    }

    /**
     * Create a scaling {@code GTransform}
     *
     * @param sx horizontal scale
     * @param sy vertical scale
     */
    public GTransform(double sx, double sy) {
        transform = AffineTransform.getScaleInstance(sx, sy);
    }

    /**
     * Create a rotating {@code GTransform}
     *
     * @param x {@code x} coordinate of the pivot point
     * @param y {@code y} coordinate of the pivot point
     * @param phi angle in degrees
     */
    public GTransform(int x, int y, double phi) {
        transform = AffineTransform.getRotateInstance(Math.toRadians(phi), x, y);
    }

    /**
     * Scales the graphics
     *
     * @param sx horizontal scale
     * @param sy vertical scale
     */
    public void scale(double sx, double sy) {
        transform.scale(sx, sy);
    }

    /**
     * Rotates the graphics around the point {@code (x, y)} a given angle
     *
     * @param x {@code x} coordinate of the pivot point
     * @param y {@code y} coordinate of the pivot point
     * @param phi angle in degrees
     */
    public void rotate(int x, int y, double phi) {
        transform.rotate(Math.toRadians(phi), x, y);
    }

    /**
     * Rotates the graphics around the origin of coordinates a given angle
     *
     * @param phi angle in degrees
     */
    public void rotate(double phi) {
        transform.rotate(Math.toRadians(phi));
    }

    /**
     * Retrieves the {@code AffineTransform} used in this {@code GTransform}
     *
     * @return transform
     */
    public AffineTransform getTransform() {
        return new AffineTransform(transform);
    }

    /**
     * Retrieves a new {@code GTransform} that reverses the effects of this one
     *
     * @return inverse transform
     * @throws NoninvertibleTransformException if the transform is not
     * invertible, this usually happens when using sheer, so it's very unlikely
     * here
     */
    public GTransform invert() throws NoninvertibleTransformException {
        final GTransform trans = new GTransform(this);
        trans.transform.invert();
        return trans;
    }

    @Override
    public void traslate(int x, int y) {
        transform.translate(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        g.transform(transform);
    }

    @Override
    public GraphicE clone() {
        return new GTransform(this);
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(transform);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GTransform other = (GTransform) obj;

        return Objects.equals(this.transform, other.transform);
    }

}
