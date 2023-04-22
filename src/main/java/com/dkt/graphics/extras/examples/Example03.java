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
import com.dkt.graphics.elements.GLine;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Example03 implements IExample {
    @Override
    public void run() {
        CanvasFrame frame = new CanvasFrame(getName());
        frame.setVisible(true);
        frame.setSize(600, 600);
        Canvas canvas = frame.getCanvas();
        canvas.setCenterBounds(true);
        canvas.setCenterOrigin(true);
        canvas.setInvertYAxis(true);
        canvas.setDrawableSize(500, 500);

        GLine l1 = new GLine(0, -250, 0, 250);
        GLine l2 = new GLine(-250, 0, 250, 0);

        canvas.add(l1);
        canvas.add(l2);

        for (int x1 = 250, x2 = -10; x1 > 0; x1 -= 10, x2 -= 10){
            GLine l = new GLine(0, x1, x2, 0);
            canvas.add(l);
        }

        for (int x1 = 250, x2 = 10; x1 > 0; x1 -= 10, x2 += 10){
            GLine l = new GLine(0, x1, x2, 0);
            canvas.add(l);
        }

        for (int x1 = -250, x2 = -10; x1 < 0; x1 += 10, x2 -= 10){
            GLine l = new GLine(0, x1, x2, 0);
            canvas.add(l);
        }

        for (int x1 = -250, x2 = 10; x1 < 0; x1 += 10, x2 += 10){
            GLine l = new GLine(0, x1, x2, 0);
            canvas.add(l);
        }
    }

    @Override
    public String getName() {
        return "Playing with lines";
    }

}
