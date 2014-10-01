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
import com.dkt.graphics.exceptions.DomainException;
import com.dkt.graphics.extras.GAxis;
import com.dkt.graphics.extras.GFormula;
import com.dkt.graphics.extras.formula.Calculable;
import com.dkt.graphics.utils.Utils;
import java.awt.Color;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Example10 implements IExample {
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
        canvas.setRepaintDelay(50);

        Calculable calc = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                if (x == 0) {
                    return 1;
                }
                return Math.sin(x) / x;
            }
        };

        calc.setScaleX(15);
        calc.setScaleY(100);

        GAxis axis = new GAxis(-250, 250, -250, 250);
        axis.setOrigin(canvas);
        axis.drawLinesH(true);
        axis.drawLinesV(true);
        canvas.addFixed(axis);

        GFormula f = new GFormula(calc);
        f.setAreaPaint(Utils.getColorWithAlpha(Color.GREEN, 96));
        f.calculateArea(-250, 250, 0.0005);
        canvas.add(f);
    }

    @Override
    public String getName() {
        return "Sinc plot";
    }
}
