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
package com.dkt.graphics.extras.formula;

import com.dkt.graphics.elements.GCircle;
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.utils.PThread;
import com.dkt.graphics.utils.TicToc;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class CalculableTimer extends AbstractTimer<Calculable> {

    public CalculableTimer(Calculable calculable) {
        super(calculable);
    }

    @Override
    protected PThread getThread(
            Calculable calculable,
            int threadNumber,
            int threadsTotal,
            boolean drawPen)
    {
        return new CalculatorThread(
                calculable,
                threadNumber,
                threadsTotal,
                drawPen
        );
    }

    private class CalculatorThread extends PThread {
        private final int threadNumber, threadsTotal;
        private final Calculable calculable;
        private final boolean drawPen;
        private final GCircle pen;

        private final double sx, sy, end, step;
        private int lx = Integer.MAX_VALUE;
        private int ly = Integer.MAX_VALUE;
        private double t;

        public CalculatorThread (
                Calculable calculable,
                int threadNumber,
                int threadsTotal,
                boolean drawPen)
        {
            setPriority(Thread.MIN_PRIORITY);

            this.threadNumber = threadNumber;
            this.threadsTotal = threadsTotal;

            this.calculable = calculable;
            this.drawPen    = drawPen;

            final double pstart = calculable.startPoint();
            final double pend   = calculable.endPoint();

            if (threadsTotal != 1){
                double interval = pend - pstart;
                interval /= threadsTotal;
                t   = pstart + interval * threadNumber;
                end = pstart + interval * (threadNumber + 1);
            } else {
                t   = pstart;
                end = pend;
            }

            sx = calculable.scaleX();
            sy = calculable.scaleY();
            step = calculable.step();

            if (drawPen){
                pen = new GCircle(0, 0, 2);
                pen.setFill(true);
                add(pen);
            } else {
                pen = null;
            }
        }

        @Override
        public void run() {
            TicToc tt = new TicToc();
            tt.tic();

            while(!interrupted() & t < end){
                checkPause();

                final int x = (int)(t * sx);
                final int y = (int)(calculable.f(t) * sy);

                if (lx != x | ly != y){
                    if (drawPen){
                        pen.move(x, y);
                    }

                    GPoint p = new GPoint(x, y);
                    p.setPaint(getPaint());
                    add(p);

                    lx = x;
                    ly = y;
                }

                t += step;

            }

            tt.toc();

            if (drawPen){
                remove(pen);
            }

            removeThread(this);
            System.out.format("%s: %d/%d ended in %s%n",
                    calculable.getName(), threadNumber + 1, threadsTotal, tt);
        }
    }
}