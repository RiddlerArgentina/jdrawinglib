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
package com.dkt.graphics.extras;

import com.dkt.graphics.elements.GLine;
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;

/**
 * This class creates a simple line grid
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GGrid extends GraphicE {
    private final Graphic grid = new Graphic();

    final private GPoint c;
    final private int w, h;
    final private int hd, vd;

    /**
     * Copy constructor
     *
     * @param e {@code GGrid} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GGrid(GGrid e) {
        c = e.c.clone();
        w = e.w;
        h = e.h;
        hd = e.hd;
        vd = e.vd;
        update();
    }

    /**
     *
     * @param w width of the grid
     * @param h height of the grid
     * @param hd horizontal distance between lines, if {@code 0} there will be
     * no horizontal lines
     * @param vd vertical distance between lines, if {@code 0} there will be no
     * vertical lines.
     * @throws InvalidArgumentException if {@code w <= 0} or {@code h <= 0}
     */
    public GGrid(int w, int h, int hd, int vd) throws InvalidArgumentException {
        this(0, 0, w, h, hd, vd);
    }

    /**
     *
     * @param x {@code x} coordinate of the center
     * @param y {@code y} coordinate of the center
     * @param w width of the grid
     * @param h height of the grid
     * @param hd horizontal distance between lines, if {@code 0} there will be
     * no horizontal lines
     * @param vd vertical distance between lines, if {@code 0} there will be no
     * vertical lines.
     * @throws InvalidArgumentException if {@code w <= 0} or {@code h <= 0}
     */
    public GGrid(
            int x,
            int y,
            int w,
            int h,
            int hd,
            int vd) throws InvalidArgumentException
    {
        if (w <= 0) {
            String msg = "The width must be a positive integer";
            throw new InvalidArgumentException(msg);
        }
        if (h <= 0) {
            String msg = "The height must be a positive integer";
            throw new InvalidArgumentException(msg);
        }

        this.w = w;
        this.h = h;
        this.hd = Math.max(0, hd);
        this.vd = Math.max(0, vd);

        c = new GPoint(x, y);
        update();
    }

    private void update() {
        grid.removeAll();
        final int x  = c.x();
        final int y  = c.y();
        final int xs = c.x() - w / 2;
        final int xf = c.x() + w / 2;
        final int ys = y - h / 2;
        final int yf = y + h / 2;

        //Draw the axis lines
        if (hd >= 1) {
            GLine l1 = new GLine(xs, y, xf, y);
            l1.setStroke(getStroke());
            l1.setPaint(getPaint());
            grid.add(l1);
        }

        if (vd >= 1) {
            GLine l2 = new GLine(x, ys, x, yf);
            l2.setStroke(getStroke());
            l2.setPaint(getPaint());
            grid.add(l2);
        }

        //Draw the grid on the background
        if (hd >= 1){
            for (int i = x + hd, j = x - hd; i < xf; i += hd, j -= hd) {
                GLine l1 = new GLine(i, ys, i, yf);
                GLine l2 = new GLine(j, ys, j, yf);
                l1.setStroke(getStroke());
                l2.setStroke(getStroke());
                l1.setPaint(getPaint());
                l2.setPaint(getPaint());
                grid.add(l1);
                grid.add(l2);
            }
        }
        if (vd >= 1){
            for (int i = y + vd, j = y - vd; i < yf; i += vd, j -= vd) {
                GLine l1 = new GLine(xs, i, xf, i);
                GLine l2 = new GLine(xs, j, xf, j);
                l1.setStroke(getStroke());
                l2.setStroke(getStroke());
                l1.setPaint(getPaint());
                l2.setPaint(getPaint());
                grid.add(l1);
                grid.add(l2);
            }
        }
    }

    @Override
    public void traslate(int x, int y) {
        grid.traslate(x, y);
        c.traslate(x, y);
    }

    @Override
    public void setStroke(Stroke stroke) {
        super.setStroke(stroke);
        update();
    }

    @Override
    public void setPaint(Paint paint) {
        super.setPaint(paint);
        update();
    }

    @Override
    public void draw(Graphics2D g) {
        grid.draw(g);
    }

    @Override
    public GraphicE clone() {
        return new GGrid(this);
    }

}
