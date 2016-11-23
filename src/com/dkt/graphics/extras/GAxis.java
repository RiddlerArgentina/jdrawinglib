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
package com.dkt.graphics.extras;

import com.dkt.graphics.canvas.Canvas;
import com.dkt.graphics.elements.GLine;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.IntervalException;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.util.Objects;

/**
 * This class represents a pair of Cartesian axis.
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GAxis extends GraphicE {
    private final Graphic axis = new Graphic();
    private int xs, xf, ys, yf;

    /**
     * Copy constructor
     *
     * @param e {@code GAxis} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GAxis(GAxis e){
        super(e);

        drawLinesH = e.drawLinesH;
        drawLinesV = e.drawLinesV;
        gridPaint  = e.gridPaint;
        mTS = e.mTS;
        MTS = e.MTS;
        Mtx = e.Mtx;
        Mty = e.Mty;
        mtx = e.mtx;
        mty = e.mty;
        xs = e.xs;
        xf = e.xf;
        ys = e.ys;
        yf = e.xf;
    }

    /**
     * Main constructor of the Axis.<br>
     * This constructor asks for the dimensions of the axis, but if you want it
     * centered in the drawable area of the canvas, you'll need to set the
     * origin of the canvas in the correct position.
     *
     * @param xs The starting point of the X axis
     * @param xf The ending point of the X axis
     * @param ys The starting point of the Y axis
     * @param yf The ending point of the Y axis
     * @throws IntervalException if either starting point is bigger than the
     * corresponding end point
     * @see GAxis#setOrigin(Canvas)
     */
    public GAxis(
            final int xs,
            final int xf,
            final int ys,
            final int yf)
    {
        if (xf <= xs){
            String msg = "The starting point can't be bigger than the end "
                       + "point (X Axis)";
            throw new IntervalException(msg, xs, xf);
        }

        if (yf <= ys){
            String msg = "The starting point can't be bigger than the end "
                       + "point (Y Axis)";
            throw new IntervalException(msg, ys, yf);
        }

        this.xs = xs;
        this.xf = xf;
        this.ys = ys;
        this.yf = yf;
        calc();
    }

    private int mtx = 10, Mtx = 50, mty = 10, Mty = 50;
    /**
     * Tells the axis to draw the minor ticks of the horizontal axis with a
     * distance of 'h'
     *
     * @param h the vertical
     * @throws InvalidArgumentException if {@code h} is smaller than 1
     */
    public void minorTicksH(final int h) {
        if (h != 0 && h < 1){
            String msg = "The minor tick space must be bigger than 1";
            throw new InvalidArgumentException(msg);
        }

        this.mtx = h;
        calc();
    }

    /**
     * Tells the axis to draw the mayor ticks of the horizontal axis with a
     * distance of 'h'.<br>
     * <i>Note:</i> The mayor ticks MUST correspond to a minor tick. What does
     * this mean? that the minor tick 'h' and the mayor tick 'h' must be
     * perfect integer multiples. <pre>
     *                  MTickH = mTickH * i (i = 2,3,4,5,6,....)</pre>
     *
     * @param h the new distance between mayor horizontal ticks
     * @throws InvalidArgumentException if 'h' is smaller than the minor tick
     * space or if 'h' is not a perfect multiple of mtx
     */
    public void mayorTicksH(final int h) {
        if (h != 0){
            if (h <= mtx){
                String msg = "The mayor tick must be bigger than the minor tick"
                           + " (mtx: " + mtx + ")";
                throw new InvalidArgumentException(msg);
            }
            if (mtx != 0 && h % mtx != 0){
                String msg = "The mayor tick must be a perfect multiple of the "
                           + "minor tick (i.e. " + mtx * 2 + ", " + mtx * 3
                            + ", " + mtx * 4 + ", ...)";
                throw new InvalidArgumentException(msg);
            }
        }

        this.Mtx = h;
        calc();
    }

    /**
     * Tells the axis to draw the minor ticks of the vertical axis with a
     * distance of 'v'
     *
     * @param v the vertical
     * @throws InvalidArgumentException if {@code v} is smaller than 1
     */
    public void minorTicksV(final int v) {
        if (v != 0 && v < 1){
            String msg = "The minor tick space must be bigger than 1";
            throw new InvalidArgumentException(msg);
        }

        this.mty = v;
        calc();
    }

    /**
     * Tells the axis to draw the mayor ticks of the vertical axis with a
     * distance of 'v'.<br>
     * <i>Note:</i> The mayor ticks MUST correspond to a minor tick. What does
     * this mean? that the minor tick 'v' and the mayor tick 'v' must be
     * perfect integer multiples. <pre>
     *                  MTickV = mTickV * i (i = 2,3,4,5,6,....)</pre>
     *
     * @param v the new distance between mayor horizontal ticks
     * @throws InvalidArgumentException if 'v' is smaller than the minor tick
     * space or if 'v' is not a perfect multiple of mty
     */
    public void mayorTicksV(final int v) {
        if (v != 0){
            if (v <= mty){
                String msg = "The mayor tick must be bigger than the minor tick"
                           + " (mtx: " + mty + ")";
                throw new InvalidArgumentException(msg);
            }
            if (mty != 0 && v % mty != 0){
                String msg = "The mayor tick must be a perfect multiple of the "
                           + "minor tick (i.e. " + mty * 2 + ", " + mty * 3
                            + ", " + mty * 4 + ", ...)";
                throw new InvalidArgumentException(msg);
            }
        }

        this.Mty = v;
        calc();
    }

    private boolean drawLinesH;
    /**
     * Tells the Axis to draw the horizontal lines for the grid
     *
     * @param drawLinesH {@code true} if you want to see the horizontal lines
     * and {@code false} otherwise
     */
    public void drawLinesH(final boolean drawLinesH) {
        this.drawLinesH = drawLinesH;
        calc();
    }

    private boolean drawLinesV;
    /**
     * Tells the Axis to draw the vertical lines for the grid
     *
     * @param drawLinesV {@code true} if you want to see the vertical lines
     * and {@code false} otherwise
     */
    public void drawLinesV(final boolean drawLinesV) {
        this.drawLinesV = drawLinesV;
        calc();
    }

    private Paint gridPaint = Color.LIGHT_GRAY;
    /**
     * Sets the color for the grid (if shown).<br>
     * The default value is {@link Color#LIGHT_GRAY}
     *
     * @param paint The new {@link Paint} for the grid
     * @throws NullPointerException if the paint is {@code null}
     */
    public void setGridColor(final Paint paint) {
        if (paint == null){
            throw new NullPointerException("Grid paint can't be null");
        }

        this.gridPaint = paint;
        calc();
    }

    /**
     * This method set's the origin of the {@link Canvas} on the correct place
     * in order for the Axis to be completely displayed.
     *
     * @param canvas canvas you want to set
     * @throws NullPointerException if the canvas is {@code null}
     */
    public void setOrigin(final Canvas canvas) {
        if (canvas == null){
            throw new NullPointerException("Canvas can't be null");
        }

        canvas.setCenterOrigin(false);
        canvas.moveOrigin(-xs, yf);
    }

    private int mTS = 2;
    private int MTS = 5;
    /**
     * Sets the minor tick size<br>
     * <i>Note:</i> This size is used at both sides of the axis so the final
     * length of the tick will be two times this value
     *
     * @param mTS minor tick size
     */
    public void setMinorTickSize(final int mTS) {
        this.mTS = mTS;
        calc();
    }

    /**
     * Retrieves the minor tick size. The default value is 2.
     *
     * @return minor tick size
     */
    public int getMinorTickSize() {
        return mTS;
    }

    /**
     * Sets the mayor tick size<br>
     * <i>Note:</i> This size is used at both sides of the axis so the final
     * length of the tick will be two times this value
     *
     * @param MTS mayor tick size
     */
    public void setMayorTickSize(final int MTS) {
        this.MTS = MTS;
        calc();
    }

    /**
     * Retrieves the mayor tick size. The default value is 5.
     *
     * @return mayor tick size
     */
    public int getMayorTickSize() {
        return MTS;
    }

    private void calc() {
        axis.removeAll();

        //Draw the grid on the background
        if (drawLinesV && Mtx >= 1){
            dLLTZ(Mtx, xs, ys, yf, true, gridPaint);
            dLBTZ(Mtx, xf, ys, yf, true, gridPaint);
        }
        if (drawLinesH && Mty >= 1){
            dLLTZ(Mty, ys, xs, xf, false, gridPaint);
            dLBTZ(Mty, yf, xs, xf, false, gridPaint);
        }

        //Draw the axis lines
        axis.add(new GLine(xs, 0 , xf, 0 ));
        axis.add(new GLine(0 , ys, 0 , yf));

        //Draw tics
        //Minor horizontal ticks
        dLLTZ(mtx, xs, -mTS, mTS, true, getPaint());
        dLBTZ(mtx, xf, -mTS, mTS, true, getPaint());
        //Mayor horizontal ticks
        dLLTZ(Mtx, xs, -MTS, MTS, true, getPaint());
        dLBTZ(Mtx, xf, -MTS, MTS, true, getPaint());
        //Minor vertical ticks
        dLLTZ(mty, ys, -mTS, mTS, false, getPaint());
        dLBTZ(mty, yf, -mTS, mTS, false, getPaint());
        //Mayor vertical ticks
        dLLTZ(Mty, ys, -MTS, MTS, false, getPaint());
        dLBTZ(Mty, yf, -MTS, MTS, false, getPaint());
    }

    private void dLLTZ(
            final int s,
            final int e,
            final int n1,
            final int n2,
            final boolean f,
            final Paint p)
    {
        if (n1 == n2) {
            return;
        }

        for (int xx = -s; xx >= e; xx -= s){
            dLAZ(xx, n1, n2, f, p);
        }
    }

    private void dLBTZ(
            final int s,
            final int e,
            final int n1,
            final int n2,
            final boolean f,
            final Paint p)
    {
        if (n1 == n2) {
            return;
        }

        for (int xx = s; xx < e; xx += s){
            dLAZ(xx, n1, n2, f, p);
        }
    }

    private void dLAZ(
            final int xx,
            final int n1,
            final int n2,
            final boolean f,
            final Paint p)
    {
        final GLine line;

        if (f){
            line = new GLine(xx, n1, xx, n2);
        } else {
            line = new GLine(n1, xx, n2, xx);
        }

        line.setPaint(p);
        axis.add(line);
    }

    @Override
    public GAxis clone() {
        return new GAxis(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 23 * hash + xs;
        hash = 23 * hash + xf;
        hash = 23 * hash + ys;
        hash = 23 * hash + yf;
        hash = 23 * hash + mtx;
        hash = 23 * hash + Mtx;
        hash = 23 * hash + mty;
        hash = 23 * hash + Mty;
        hash = 23 * hash + (drawLinesH ? 1 : 0);
        hash = 23 * hash + (drawLinesV ? 1 : 0);
        hash = 23 * hash + Objects.hashCode(gridPaint);
        hash = 23 * hash + mTS;
        hash = 23 * hash + MTS;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GAxis other = (GAxis) obj;
        if (!Objects.equals(gridPaint, other.gridPaint)) {
            return false;
        }

        return !(
            xs  != other.xs  |
            xf  != other.xf  |
            ys  != other.ys  |
            yf  != other.yf  |
            mtx != other.mtx |
            Mtx != other.Mtx |
            mty != other.mty |
            Mty != other.Mty |
            mTS != other.mTS |
            MTS != other.MTS |
            drawLinesH != other.drawLinesH |
            drawLinesV != other.drawLinesV
        );
    }

    @Override
    public void traslate(int x, int y) {
        axis.traslate(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        axis.draw(g);
    }

}
