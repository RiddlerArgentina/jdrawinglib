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
import com.dkt.graphics.elements.GraphicE;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Objects;

/**
 * This class creates a Resultant Vector as a result of the sum of a finite
 * number of vectors.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GVectorPolygon extends GraphicE {
    private final ArrayList<GVector> vectors = new ArrayList<>(5);
    private GVector resultant;

    public GVectorPolygon(GVectorPolygon e) {
        super(e);

        resultant = new GVector(resultant);
        for (GVector v : e.vectors) {
            vectors.add(new GVector(v));
        }

        calc();
    }

    /**
     * Creates a new {@code GVectorPolygon} based on a {@link GVector} array
     *
     * @param vectors that form the vector polygon
     * @throws IllegalArgumentException if {@code vectors} is {@code null}
     */
    public GVectorPolygon(GVector... vectors) throws IllegalArgumentException {
        append(vectors);
    }

    /**
     * Appends a finite number of vectors to the Polygon, let it be noted that
     * the order in which the vectors are appended is the same order in which
     * they will be shown
     *
     * @param vector {@code GVector} containing the vectors to be added.
     * @throws IllegalArgumentException if {@code vector} is {@code null}
     */
    public void append(GVector... vector) throws IllegalArgumentException {
        if (vector == null){
            throw new IllegalArgumentException("You must add at least ONE vector");
        }

        vectors.ensureCapacity(vectors.size() + vector.length);

        for (GVector v : vector){
            vectors.add(v);
            v.setArrowWeight(-10);
        }

        calc();
    }

    private void calc() {
        if (vectors.isEmpty()){
            resultant = new GVector(0, 0, 0, 0);
        } else if (vectors.size() == 1){
            GVector v = vectors.get(0);
            v.traslate(-v.x(), -v.y());
            resultant = v;
        } else {
            //Reference all vectors for easy adding
            GVector[] array = new GVector[vectors.size()];
            array = vectors.toArray(array);

            //Add all the vectors to a zero-modulus vector
            GVector nullVector = new GVector(0, 0, 0, 0);
            resultant = nullVector.add(array);

            //Traslate all the vectors to the "point" of the previous one
            GVector prev = vectors.get(0);
            for (GVector v : vectors){
                if (Objects.equals(v, prev)){
                    //The first vector is on the origin
                    v.traslate(-v.x(), -v.y());
                    continue;
                }

                v.traslate(-v.x(), -v.y());
                v.traslate(prev.xf(), prev.yf());

                prev = v;
            }
        }

    }

    @Override
    public void traslate(int x, int y) {
        for (GVector v : vectors){
            v.traslate(x, y);
        }

        resultant.traslate(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        for (GVector v : vectors){
            v.draw(g);
        }
        resultant.draw(g);
    }

    @Override
    public GVectorPolygon clone() {
        return new GVectorPolygon(this);
    }
}
