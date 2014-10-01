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
import com.dkt.graphics.elements.GArc;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example01 implements IExample {
    @Override
    public void run() {
        CanvasFrame frame = new CanvasFrame(getName());
        frame.setVisible(true);
        frame.setSize(600, 600);
        Canvas canvas = frame.getCanvas();
        canvas.setCenterBounds(true);
        canvas.setCenterOrigin(true);
        canvas.setDrawableSize(550, 550);

        for (int r = 10; r < 400; r += 10){
            canvas.add(new GArc( 0, 0, r, 180,   0));
            canvas.add(new GArc(10, 0, r, 180, 180));
        }
    }

    @Override
    public String getName() {
        return "Spiral";
    }

}
