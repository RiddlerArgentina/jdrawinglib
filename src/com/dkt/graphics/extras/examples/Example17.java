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
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GString;
import com.dkt.graphics.utils.GuiUtils;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import javax.swing.Timer;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example17  implements IExample {
    @Override
    public void run() {
        final CanvasFrame cf = new CanvasFrame(getName());
        cf.setVisible(true);
        cf.setSize(600, 600);

        GuiUtils.centerFrame(cf);

        final Canvas canvas = cf.getCanvas();
        canvas.setDrawableSize(500, 500);
        canvas.setCenterBounds(true);
        canvas.setCenterOrigin(true);
        canvas.setUseAntiAliasing(true);
        canvas.setAutoRepaint(true);
        canvas.setRepaintDelay(25);

        final int nPoints = 35;
        final GPoint[] points = new GPoint[nPoints];

        final Random rand = new Random();
        for (int i = 0; i < nPoints; i++) {
            final int x = rand.nextInt(500) - 250;
            final int y = rand.nextInt(500) - 250;
            points[i] = new GPoint(x, y);
        }

        final Timer timer = new Timer(200, null);
        timer.addActionListener(new ActionListener() {
            private GPoint[] pts = points;
            private int a;
            //If you set this to true you'll see all the transformations
            //otherwise it will skip one frame and the animation will be
            //smoother
            private final boolean alternate = true;
            @Override
            public void actionPerformed(ActionEvent evt) {
                if (alternate && a % 2 == 0) {
                    canvas.removeAll();
                }

                GLine[] lines = getLines(pts);

                if (alternate && a % 2 == 0) {
                    for (GLine line : lines) {
                        canvas.add(line);
                    }
                    showPointsAsCircles(canvas, pts, Color.WHITE);
                }

                pts = new GPoint[lines.length];

                int i = 0;
                for (GLine line : lines) {
                    pts[i++] = line.getMiddlePoint();
                }

                if (alternate && a % 2 == 0) {
                    canvas.add(S1);
                    canvas.add(S2);
                }

                transform(pts);
                a++;
            }
        });

        timer.setDelay(50);
        timer.setRepeats(true);
        timer.start();
    }

    private final GString S1 = new GString(-240 , 230, "The theory behind this can be found at:");
    private final GString S2 = new GString(-240 , 245, "http://www.jasondavies.com/random-polygon-ellipse/");

    @Override
    public String getName() {
        return "Random point elipse";
    }

    private void showPointsAsCircles(Canvas canvas, GPoint[] points, Color col) {
        for (GPoint p : points) {
            GCircle c = new GCircle(p.x(), p.y(), 3);
            c.setFillPaint(col);
            c.setFill(true);
            canvas.add(c);
        }
    }

    private GLine[] getLines(GPoint[] points) {
        GLine[] lines = new GLine[points.length];

        for (int i = 0; i < points.length - 1; i++) {
            lines[i] = new GLine(points[i], points[i + 1]);
        }

        lines[points.length - 1] = new GLine(points[0], points[points.length - 1]);
        return lines;
    }

    private void transform(GPoint[] pts) {
        //We avoid using an affine transform since it also increases the size
        //of the stroke
        int l = 250, r = -250, u = 250, d = -250;

        for (final GPoint p : pts) {
            l = Math.min(l, p.x());
            r = Math.max(r, p.x());
            u = Math.min(u, p.y());
            d = Math.max(d, p.y());
        }

        final double sx = 500. / (r - l);
        final double sy = 500. / (d - u);

        int i = 0;
        for (final GPoint p : pts) {
            final int x = (int)(p.x() * sx) - r + (r - l) / 2;
            final int y = (int)(p.y() * sy) - d + (d - u) / 2;

            pts[i++] = new GPoint(x, y);
        }
    }
}