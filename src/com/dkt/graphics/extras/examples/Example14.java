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
import com.dkt.graphics.elements.GPath;
import com.dkt.graphics.extras.GAxis;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example14 implements IExample {
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
        canvas.setRepaintDelay(5);

        final GAxis axis = new GAxis(-250, 250, -250, 250);
        axis.setOrigin(canvas);
        axis.minorTicksH(10);
        axis.mayorTicksH(50);
        axis.minorTicksV(10);
        axis.mayorTicksV(50);
        axis.drawLinesH(true);
        axis.drawLinesV(true);
        canvas.addFixed(axis);

        final GPath path = new GPath();
        path.setPaint(new Color(82, 94, 139));
        canvas.add(path);
        final Timer timer = new Timer(100, null);
        ActionListener al = new ActionListener() {
            double t = 0.001;
            @Override
            public void actionPerformed(ActionEvent e) {
                double c = Math.exp(Math.cos(t))
                         - 2 * Math.cos(4 * t)
                         - Math.pow(Math.sin(t/12.), 5);
                double x = Math.sin(t) * c * 70;
                double y = Math.cos(t) * c * 70;
                path.append((int)x, (int)y);
                t += 0.005;

                if (t > 14 * Math.PI) {
                    timer.stop();
                    canvas.setAutoRepaint(false);
                }
            }
        };
        timer.addActionListener(al);
        timer.setDelay(1);
        timer.start();
    }

    @Override
    public String getName() {
        return "Parametric 1";
    }
}
