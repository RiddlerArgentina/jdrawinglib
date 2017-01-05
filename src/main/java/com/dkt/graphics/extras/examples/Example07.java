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
package com.dkt.graphics.extras.examples;

import com.dkt.graphics.canvas.Canvas;
import com.dkt.graphics.canvas.CanvasFrame;
import com.dkt.graphics.elements.GVector;
import com.dkt.graphics.extras.GAxis;
import java.awt.Color;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class Example07 implements IExample {
    @Override
    public void run() {
        CanvasFrame frame = new CanvasFrame(getName());
        frame.setVisible(true);
        frame.setSize(600, 600);
        Canvas canvas = frame.getCanvas();
        canvas.setCenterBounds(true);
        canvas.setCenterOrigin(true);
        canvas.setInvertYAxis(true);
        canvas.setUseFullArea(false);
        canvas.setDrawableSize(500, 500);

        GAxis axis = new GAxis(-250, 250, -250, 250);
        canvas.add(axis);

        GVector v1 = new GVector(0, 0, 100, 0);
        v1.setPaint(Color.RED);
        GVector v2 = new GVector(100, 0, 100, 90);
        v2.setPaint(Color.BLUE);
        GVector v4 = new GVector(0, 0, -100, 45);
        v4.setPaint(Color.BLUE);
        GVector v3 = v1.add(v2, v4);
        v3.setPaint(Color.GREEN);
        canvas.add(v1);
        canvas.add(v2);
        canvas.add(v3);
        canvas.add(v4);
    }

    @Override
    public String getName() {
        return "Vectors";
    }
}
