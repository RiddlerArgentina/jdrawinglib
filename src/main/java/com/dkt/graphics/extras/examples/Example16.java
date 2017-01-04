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
import com.dkt.graphics.elements.GCircle;
import com.dkt.graphics.elements.GPath;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class Example16 implements IExample {
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

        final GCircle circle = new GCircle(0, 0, 2);
        circle.setFill(true);
        final GPath path = new GPath(1000);

        canvas.add(circle);
        canvas.add(path);

        final Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            private double t = 0;
            double i = 1, j = 2;
            double a = 1, b = 1, c = 60, d = 58, e = 60;
//            double a = 1, b = 1, c = 60, d = 1, e = 60;
//            double a = 1, b = 60, c = 1, d = 1, e = 60;
//            double a = 80, b = 1, c = 2, d = 1, e = 80;
            private int xx = 0, yy = 0;
            @Override
            public void actionPerformed(ActionEvent evt) {
                double x = (i * Math.cos(a*t) - Math.cos(b*t) * Math.sin(c * t)) * 50;
                double y = (j * Math.sin(d*t) - Math.sin(e * t)) * 50;


                circle.traslate(-xx, -yy);

                xx = (int)x;
                yy = (int)y;

                circle.traslate(xx, yy);
                path.append(xx, yy);

                t += 0.002;

                if (t > 1000){
                    t = 0;
                    timer.stop();
                    canvas.setAutoRepaint(false);
                }

                canvas.repaint();
            }
        });

        timer.setDelay(1);
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public String getName() {
        return "Parametric 3";
    }
}
