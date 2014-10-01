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
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GPoly;
import com.dkt.graphics.elements.GString;
import com.dkt.graphics.utils.Utils;
import java.awt.Color;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example06 implements IExample {
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

        int[] xs = new int[]{10, -60, -100};
        int[] ys = new int[]{30, 200, -120};

        GPoint p1 = new GPoint(xs[0], ys[0], 2);
        GPoint p2 = new GPoint(xs[1], ys[1], 2);
        GPoint p3 = new GPoint(xs[2], ys[2], 2);

        GString sp1 = new GString(15, 35, "(10, 30)");
        GString sp2 = new GString(-55, 205, "(-60, 200)");
        GString sp3 = new GString(-95, -125, "(-100, -120)");

        canvas.add(p1);
        canvas.add(p2);
        canvas.add(p3);

        canvas.add(sp1);
        canvas.add(sp2);
        canvas.add(sp3);

        GPoly poly = new GPoly(xs, ys);
        poly.setFill(true);
        poly.setPaint(Utils.getColorWithAlpha(Color.ORANGE, 96));
        canvas.add(poly);

        Color[] colores = new Color[]{
            Color.GREEN,
            Color.RED,
            Color.MAGENTA,
            Color.BLUE,
        };

        for (int i = 0; i < 4; i++){
            int[] xxs = new int[3];
            int[] yys = new int[3];

            xxs[0] = Math.abs(xs[0] - xs[1]) / 2 + Math.min(xs[0], xs[1]);
            xxs[1] = Math.abs(xs[1] - xs[2]) / 2 + Math.min(xs[1], xs[2]);
            xxs[2] = Math.abs(xs[2] - xs[0]) / 2 + Math.min(xs[2], xs[0]);

            yys[0] = Math.abs(ys[0] - ys[1]) / 2 + Math.min(ys[0], ys[1]);
            yys[1] = Math.abs(ys[1] - ys[2]) / 2 + Math.min(ys[1], ys[2]);
            yys[2] = Math.abs(ys[2] - ys[0]) / 2 + Math.min(ys[2], ys[0]);

            GPoly poly2 = new GPoly(xxs, yys);
            poly2.setFill(true);
            poly2.setPaint(colores[i]);
            canvas.add(poly2);

            xs = xxs;
            ys = yys;
        }
    }

    @Override
    public String getName() {
        return "Some triangles";
    }
}
