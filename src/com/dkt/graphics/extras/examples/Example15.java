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
import com.dkt.graphics.elements.GPath;
import com.dkt.graphics.extras.GAxis;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example15 implements IExample {
    @Override
    public void run() {
        final CanvasFrame cf = new CanvasFrame(getName());
        cf.setVisible(true);
        cf.setSize(600, 600);

        final Canvas canvas = cf.getCanvas();
        canvas.setUseFullArea(false);
        canvas.setDrawableSize(500, 500);
        canvas.setCenterBounds(true);
        canvas.setInvertYAxis(true);
        canvas.setCenterOrigin(true);
        canvas.setAutoRepaint(true);
        canvas.setRepaintDelay(1);

        final GAxis axis = new GAxis(-250, 250, -250, 250);
        axis.setOrigin(canvas);
        axis.minorTicksH(10);
        axis.mayorTicksH(50);
        axis.minorTicksV(10);
        axis.mayorTicksV(50);
        axis.drawLinesH(true);
        axis.drawLinesV(true);
        canvas.addFixed(axis);

        final GCircle circle = new GCircle(0,0,2);
        final GPath path = new GPath(1000);
        canvas.add(circle);
        canvas.add(path);

        final Timer timer = new Timer(200, null);

        timer.addActionListener(new ActionListener() {
            //http://upload.wikimedia.org/wikipedia/commons/f/f5/Param_02.jpg
            private double t = 0;
            private double k = 6.5;
            private int xx = 0, yy = 0;
            @Override
            public void actionPerformed(ActionEvent e) {
                double a = 1, b = a / k;
                double scale = 100;

                double x = (a - b) * Math.cos(t) + b*Math.cos(t*(k-1));
                double y = (a - b) * Math.sin(t) - b*Math.sin(t*(k-1));

                x *= scale;
                y *= scale;

                circle.traslate(-xx, -yy);

                xx = (int)x;
                yy = (int)y;

                circle.traslate(xx, yy);

                path.append(xx, yy);

                t += 0.01;

                if (t > 1000){
                    t = 0;
                    timer.stop();
                    canvas.setAutoRepaint(false);
                }

            }
        });

        timer.setDelay(1);
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public String getName() {
        return "Parametric 2";
    }
}
