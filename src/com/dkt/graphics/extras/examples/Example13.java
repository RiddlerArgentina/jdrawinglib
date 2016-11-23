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
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.extras.GAxis;
import com.dkt.graphics.extras.GPVector;
import com.dkt.graphics.utils.Utils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.Timer;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class Example13 implements IExample {
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
        canvas.setRepaintDelay(5);

        final GAxis axis = new GAxis(-250, 250, -50, 450);
        axis.setOrigin(canvas);
        axis.minorTicksH(10);
        axis.mayorTicksH(40);
        axis.minorTicksV(10);
        axis.mayorTicksV(40);
        axis.drawLinesH(true);
        axis.drawLinesV(true);
        canvas.addFixed(axis);

        final Graphic mass = new Graphic();
        final GPVector ov = new GPVector(0, 0, 100, 0);
        final GCircle c1 = new GCircle(0, 0, 5);
        final GPath p1 = new GPath();
        p1.setPaint(Color.GREEN);
        p1.ensureCapacity(1500);
        c1.setFill(true);
        c1.setPaint(Color.CYAN);
        c1.setFillPaint(Utils.getColorWithAlpha(Color.CYAN, 96));
        ov.setPaint(Color.RED);
        ov.setHCompPaint(Color.BLUE);
        ov.setVCompPaint(Color.MAGENTA);
        canvas.add(p1);
        mass.add(c1);
        mass.add(ov);
        canvas.add(mass);

        final Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            private final double g   = 9.8;
            private final double phi = Math.toRadians(65);
            private final double vi  = 70;
            private final double x0  = -190;
            private final double y0  = 0;
            private double t = 0;

            @Override
            public void actionPerformed(ActionEvent e) {
                t += 1./100;
                final double vx = vi * Math.cos(phi);
                double vy = vi * Math.sin(phi);

                final int x = (int)(x0 + vx * t);
                final int y = (int)(y0 + vy * t - .5 * g * t * t);

                mass.move(x, y);

                vy -= g * t;
                ov.setModulus(vx, vy);

                p1.append(x, y);

                if (y < 0){
                    t = 0;
                    timer.stop();
                }

                canvas.repaint();
            }
        });

        timer.setDelay(5);
        timer.setCoalesce(true);
        timer.setRepeats(true);
        timer.start();
    }

    @Override
    public String getName() {
        return "Trajectory plot";
    }
}
