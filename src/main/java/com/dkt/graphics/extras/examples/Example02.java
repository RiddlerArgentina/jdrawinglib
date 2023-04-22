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
public class Example02 implements IExample {
    @Override
    public void run() {
        CanvasFrame frame = new CanvasFrame(getName());
        frame.setVisible(true);
        frame.setSize(600, 600);
        Canvas canvas = frame.getCanvas();
        canvas.setCenterBounds(true);
        canvas.setUseFullArea(false);
        canvas.setDrawableSize(550, 550);

        GLine l1 = new GLine(0, 0, 0, 550);
        GLine l2 = new GLine(0, 550, 550, 550);
        canvas.add(l1);
        canvas.add(l2);

        for (int x = 0; x < 1100; x += 10){
            GLine l = new GLine(0, x, x, 0);
            canvas.add(l);
        }
    }

    @Override
    public String getName() {
        return "Parallel lines";
    }

}
