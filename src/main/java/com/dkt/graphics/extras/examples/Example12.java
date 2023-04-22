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
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GPointArray;
import java.awt.Color;
import java.util.Random;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Example12 implements IExample {
    @Override
    public void run() {
        final CanvasFrame cf = new CanvasFrame(getName());
        cf.setVisible(true);
        cf.setSize(600, 600);

        final Canvas canvas = cf.getCanvas();
        canvas.setDrawableSize(500, 500);
        canvas.setCenterBounds(true);
        canvas.setInvertYAxis(true);
        canvas.setCenterOrigin(true);
        canvas.setAutoRepaint(true);
        canvas.setRepaintDelay(50);

        final GPointArray ar = new GPointArray(100000);
        final Random rand = new Random();
        for (int i = 0; i < 100000; i++){
            final int x = rand.nextInt(502) - 251;
            final int y = rand.nextInt(502) - 251;

            ar.append(x, y);
        }

        canvas.add(ar);
        canvas.add(ar.getBounds());
        GPoint p = ar.leftmostPoint();
        p = new GPoint(p.x(), p.y(), 2);
        p.setPaint(Color.RED);
        canvas.add(p);
        p = ar.highestPoint();
        p = new GPoint(p.x(), p.y(), 2);
        p.setPaint(Color.RED);
        canvas.add(p);
        p = ar.lowestPoint();
        p = new GPoint(p.x(), p.y(), 2);
        p.setPaint(Color.RED);
        canvas.add(p);
        p = ar.rightmostPoint();
        p = new GPoint(p.x(), p.y(), 2);
        p.setPaint(Color.RED);
        canvas.add(p);

        final GPointArray arr2 = ar.pointsInRadius(20, 50, 80);
        ar.removeAll(arr2);
    }

    @Override
    public String getName() {
        return "Random dots extract figure";
    }
}
