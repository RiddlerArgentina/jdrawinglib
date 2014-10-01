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
package com.dkt.graphics.extras;

import com.dkt.graphics.elements.GFillableE;
import com.dkt.graphics.elements.GMultiPoint;
import com.dkt.graphics.elements.GPath;
import com.dkt.graphics.elements.GPointArray;
import com.dkt.graphics.elements.GPoly;
import com.dkt.graphics.elements.GRegPoly;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.IntervalException;
import com.dkt.graphics.extras.formula.Calculable;
import java.awt.Paint;

/**
 * This class represents a basic Formula.<br>
 * It translates a {@link Calculable} into a Graphic.<br>
 * <b>TODO:</b><ul>
 * <li><s>When the formula is scaled there's no real need to evaluate all the
 * points in the domain since most of them will be outside the interval.</s><br>
 * We ended up using the scale to determine the number of points, which means
 * that now we need to play with the interval as well as with the x scale, btw I
 * think this actually makes a lot of sense...
 * <li><s>We must investigate if it's really better to increment the step using
 * {@code xx = xs + i * step} instead of {@code xx += step}.</s><br> It was
 * decided to use {@code xx = xs + i * step}, it's a bit slower but not much,
 * and the interval points are uniformly distributed (very noticeable when using
 * a big X scale.
 * <li><s>Now that the implementation of {@link GMultiPoint} has changed, we
 * should take advantage of it.</s> I'm pretty sure this was taken care of.
 * <li><s>We are adding two {@link GPoly} elements instead of one (when using
 * {@link GFormula#calculateArea(double, double, double)} now that the
 * implementation of {@link GFillableE} has changed we can do the same with only
 * one {@link GPoly}.</s> Done!
 * <li><s>For some odd reason
 * {@link GFormula#calculate(double, double, double, GraphicE)} doesn't work if
 * the {@link GraphicE} is an instance of {@link GRegPoly}...</s><br>Oops, there
 * was a problem with the {@code clone()} implementation.
 * </ul>
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class GFormula extends Graphic {
    private Calculable formula;

    private GFormula(){}

    /**
     * Creates a new {@code GFormula} for the given {@link Calculable} object
     *
     * @param formula the {@link Calculable} object that contains the relation
     * @throws NullPointerException if formula is {@code null}
     */
    public GFormula(Calculable formula){
        if (formula == null){
            throw new NullPointerException("Formula can't be null");
        }

        this.formula = formula;
    }

    /**
     * Calculates the formula, this method must be called before the method is
     * printed in the canvas, otherwise it will print nothing.
     *
     * @param xs The starting point of the interval
     * @param xf The end point of the interval
     * @param step The step used to f
     * @see GFormula#calculate(double, double, double, GraphicE)
     * @see GFormula#calculateArea(double, double, double)
     * @see GFormula#calculatePath(double, double, double)
     * @throws IntervalException if {@code xs < xf} or {@code step < 0} or
     * {@code step > xf - xs}
     */
    public void calculate(double xs, double xf, double step){
        checkValues(xs, xf, step);

        removeAll();

        final double sx  = formula.scaleX();
        final double sy  = formula.scaleY();
        final double ssx = step * sx;
        final double xss = xs / sx;
        final double xfs = xf / sx;

        final int size = (int)((xf - xs) / (sx * step));

        GPointArray array = new GPointArray(size);
        array.setPaint(getPaint());

        int i = 0, lx = Integer.MAX_VALUE, lfx = Integer.MAX_VALUE;
        for (double xx = xss; xx < xfs; xx = xss + i * ssx, i++){
            final int x  = (int)(sx * xx);
            final int fx = (int)(sy * formula.f(xx));

            //if the distance between points is 0px then don't append it
            if (lx != x | lfx != fx){
                array.append(x, fx);
                lfx = fx;
                lx  = x;
            }
        }

        array.trimToSize();
        add(array);
    }

    /**
     * Calculates the formula, this method must be called before the method is
     * printed in the canvas, otherwise it will print nothing.<br>
     * <i>Note:</i> The element must be in the position {@code (0, 0)}
     * otherwise it the graphics will be in the incorrect position. Of course
     * this will come in handy with {@link Graphic} that have strange shapes.
     * <br><i>Note 2:</i> For some reason that I was unable to figure out yet,
     * this method fails when using {@link GRegPoly}.
     *
     * @param xs The starting point of the interval
     * @param xf The end point of the interval
     * @param step The step used to f
     * @param element The element that will be copied and translated in order
     * to generate this value
     * @see GFormula#calculate(double, double, double)
     * @see GFormula#calculateArea(double, double, double)
     * @see GFormula#calculatePath(double, double, double)
     * @throws IntervalException if {@code xs < xf} or {@code step < 0} or
     * {@code step > xf - xs}
     * @throws NullPointerException if {@code element} is {@code null}
     */
    public void calculate(
            double xs,
            double xf,
            double step,
            GraphicE element)
    {
        checkValues(xs, xf, step);
        if (element == null){
            throw new NullPointerException("Element can't be null");
        }

        removeAll();

        final double sx  = formula.scaleX();
        final double sy  = formula.scaleY();
        final double ssx = step * sx;
        final double xss = xs / sx;
        final double xfs = xf / sx;

        int i = 0, lx = Integer.MAX_VALUE, lfx = Integer.MAX_VALUE;
        for (double xx = xss; xx < xfs; xx = xss + i * ssx, i++){
            final int x  = (int)(sx * xx);
            final int fx = (int)(sy * formula.f(xx));

            //if the distance between points is 0px then don't append it
            if (lx != x | lfx != fx){
                final GraphicE e = element.clone();
                e.traslate(x, fx);
                add(e);
                lfx = fx;
                lx  = x;
            }
        }
    }

    /**
     * Calculates the formula, this method must be called before the method is
     * printed in the canvas, otherwise it will print nothing. The drawing will
     * be based on lines.
     *
     * @param xs The starting point of the interval
     * @param xf The end point of the interval
     * @param step The step used to f
     * @see GFormula#calculate(double, double, double)
     * @see GFormula#calculate(double, double, double, GraphicE)
     * @see GFormula#calculateArea(double, double, double)
     * @throws IntervalException if {@code xs < xf} or {@code step < 0} or
     * {@code step > xf - xs}
     */
    public void calculatePath(double xs, double xf, double step){
        checkValues(xs, xf, step);

        removeAll();

        final double sx = formula.scaleX();
        final double sy = formula.scaleY();
        final double ssx = step * sx;
        final double xss = xs / sx;
        final double xfs = xf / sx;

        final int size = (int)((xf - xs) / (sx * step));

        GPath path = new GPath(size);

        int i = 0, lx = Integer.MAX_VALUE, lfx = Integer.MAX_VALUE;
        for (double xx = xss; xx < xfs; xx = xss + i * ssx, i++){
            final int x  = (int)(sx * xx);
            final int fx = (int)(sy * formula.f(xx));

            //if the distance between points is 0px then don't append it
            if (lx != x | lfx != fx){
                path.append(x, fx);

                lfx = fx;
                lx  = x;
            }
        }

        path.setPaint(getPaint());
        path.setStroke(getStroke());
        path.trimToSize();

        add(path);
    }

    private Paint area;
    /**
     * Sets the {@link Paint} used for the area below the curve
     *
     * @param paint The {@link Paint} that will be used to render the area
     * @throws NullPointerException if {@code paint} is {@code null}
     */
    public void setAreaPaint(Paint paint) {
        if (paint == null){
            throw new NullPointerException("Paint can't be null");
        }
        this.area = paint;
    }

    /**
     * Calculates the formula, this method must be called before the method is
     * printed in the canvas, otherwise it will print nothing. The drawing will
     * be based on areas.<br>
     * <i>Note:</i> you must set the area paint first
     *
     * @param xs The starting point of the interval
     * @param xf The end point of the interval
     * @param step The step used to f
     * @see GFormula#calculate(double, double, double)
     * @see GFormula#calculate(double, double, double, GraphicE)
     * @see GFormula#calculateArea(double, double, double)
     * @see GFormula#setAreaPaint(Paint)
     * @throws IntervalException if {@code xs < xf} or {@code step < 0} or
     * {@code step > xf - xs}
     */
    public void calculateArea(double xs, double xf, double step){
        checkValues(xs, xf, step);

        removeAll();

        final double sx = formula.scaleX();
        final double sy = formula.scaleY();
        final double ssx = step * sx;
        final double xss = xs / sx;
        final double xfs = xf / sx;

        final int size = (int)((xf - xs) / (sx * step));

        final GPoly poly = new GPoly(size + 2);

        poly.append((int)(sx * xs), 0);

        int i = 0, lx = Integer.MAX_VALUE, lfx = Integer.MAX_VALUE;
        for (double xx = xss; xx < xfs; xx = xss + i * ssx, i++){
            final int x  = (int)(sx * xx);
            final int fx = (int)(sy * formula.f(xx));

            //if the distance between points is 0px then don't append it
            if (lx != x | lfx != fx){
                poly.append(x, fx);

                lfx = fx;
                lx  = x;
            }

        }

        poly.append(lx, 0);
        poly.trimToSize();

        poly.setPaint(getPaint());
        poly.setFillPaint(area == null ? getPaint() : area);
        poly.setFill(true);

        add(poly);
    }

    private static void checkValues(double xs, double xf, double step){
        if (xs >= xf){
            String msg = "The final point must be bigger than the initial point";
            throw new IntervalException(msg, xs, xf, step);
        }

        if (step <= 0.0){
            String msg = "The step must be a non-zero positive real!";
            throw new IntervalException(msg, xs, xf, step);
        }

        if (step > xf - xs){
            String msg = "The step can't be bigger than the interval!";
            throw new IntervalException(msg, xs, xf, step);
        }
    }

}
