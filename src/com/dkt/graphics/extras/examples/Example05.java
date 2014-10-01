/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2014 <dktcoding [at] gmail>
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
import com.dkt.graphics.elements.GCircle;
import com.dkt.graphics.elements.GLine;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example05 implements IExample {
    @Override
    public void run() {
        CanvasFrame frame = new CanvasFrame(getName());
        frame.setVisible(true);
        frame.setSize(600, 600);
        Canvas canvas = frame.getCanvas();
        canvas.setCenterBounds(true);
        canvas.setCenterOrigin(true);
        canvas.setDrawableSize(500, 500);

        GCircle circ = new GCircle(0, 0, 250);
        canvas.add(circ);

        double ang0 = Math.PI / 2;
        double ang1 = 0;
        double step = Math.PI / 40;
        double r = 250;
        for (;ang1 < Math.PI * 2; ang0 += step, ang1 += step){
            GLine l = new GLine(
                    (int)(Math.cos(ang0) * r),
                    (int)(Math.sin(ang0) * r),
                    (int)(Math.cos(ang1) * r),
                    (int)(Math.sin(ang1) * r)
            );
            canvas.add(l);
        }
    }

    @Override
    public String getName() {
        return "Playing with lines 3";
    }
}
