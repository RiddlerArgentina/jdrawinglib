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
package com.dkt.graphics.extras.examples;

import com.dkt.graphics.canvas.Canvas;
import com.dkt.graphics.canvas.CanvasFrame;
import com.dkt.graphics.elements.GVector;
import com.dkt.graphics.extras.GVectorPolygon;
import java.awt.BasicStroke;
import java.awt.Color;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Example08 implements IExample {
    @Override
    public void run() {
        CanvasFrame frame = new CanvasFrame(getName());
        frame.setVisible(true);
        frame.setSize(600, 600);
        Canvas canvas = frame.getCanvas();
        canvas.setCenterBounds(true);
        canvas.setUseFullArea(false);
        canvas.setDrawableSize(500, 500);

        BasicStroke bs = new BasicStroke(3);
        GVector v1 = new GVector(0, 0, 12, 45);
        v1.setPaint(Color.RED);
        v1.setStroke(bs);

        GVectorPolygon poly = new GVectorPolygon(v1);
        v1 = new GVector(0, 0, 25, 0);
        v1.setPaint(Color.BLUE);
        v1.setStroke(bs);
        poly.append(v1);
        v1 = new GVector(0, 0, 32, 45);
        v1.setPaint(Color.CYAN);
        v1.setStroke(bs);
        poly.append(v1);
        v1 = new GVector(0, 0, 56, 90);
        v1.setPaint(Color.GREEN);
        v1.setStroke(bs);
        poly.append(v1);
        v1 = new GVector(0, 0, 12, 135);
        v1.setPaint(Color.YELLOW);
        v1.setStroke(bs);
        poly.append(v1);

        canvas.add(poly);
    }

    @Override
    public String getName() {
        return "Vector polygon";
    }
}
