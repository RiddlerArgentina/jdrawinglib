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
package com.dkt.graphics.elements.extras;

import com.dkt.graphics.elements.GPath;
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GPointArray;
import com.dkt.graphics.elements.GPoly;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.DomainException;
import com.dkt.graphics.exceptions.IntervalException;
import com.dkt.graphics.extras.GFormula;
import com.dkt.graphics.extras.formula.Calculable;
import java.awt.Color;
import java.util.Iterator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GFormulaTest {
    @Test
    @DisplayName("Test calculate")
    public void testFormula1() {
        Calculable eye = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                return x;
            }
        };
        GFormula formula = new GFormula(eye);
        formula.calculate(-5, 5, 1);
        Iterator<GraphicE> iter = formula.iterator();
        boolean status = false;
        GPointArray exp = new GPointArray();
        for (int i = -5; i < 5; i++) {
            exp.append(i, i);
        }
        while (iter.hasNext()) {
            GraphicE e = iter.next();
            if (e instanceof GPointArray) {
                status = true;
                GPointArray o = (GPointArray)e;
                assertEquals(exp, o);
            }
        }
        assertTrue(status);
    }

    @Test
    @DisplayName("Test calculate element")
    public void testFormula2() {
        Calculable eye = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                return x;
            }
        };
        GFormula formula = new GFormula(eye);
        formula.calculate(-5, 5, 1, new GPoint(0, 0));
        Iterator<GraphicE> iter = formula.iterator();
        boolean status = false;

        int i = -5;
        while (iter.hasNext()) {
            GraphicE e = iter.next();
            if (e instanceof GPoint) {
                status = true;
                GPoint o = (GPoint)e;
                assertEquals(new GPoint(i, i), o);
                i++;
            }
        }
        assertTrue(status);
    }

    @Test
    @DisplayName("Test calculate path")
    public void testFormula3() {
        Calculable eye = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                return x;
            }
        };
        GFormula formula = new GFormula(eye);
        formula.calculatePath(-5, 5, 1);
        Iterator<GraphicE> iter = formula.iterator();
        boolean status = false;
        GPath exp = new GPath();
        for (int i = -5; i < 5; i++) {
            exp.append(i, i);
        }
        while (iter.hasNext()) {
            GraphicE e = iter.next();
            if (e instanceof GPath) {
                status = true;
                GPath o = (GPath)e;
                assertEquals(exp, o);
            }
        }
        assertTrue(status);

    }

    @Test
    @DisplayName("Test calculate area")
    public void testFormula4() {
        Calculable eye = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                return x;
            }
        };
        GFormula formula = new GFormula(eye);
        formula.calculateArea(-5, 5, 1);
        Iterator<GraphicE> iter = formula.iterator();
        boolean status = false;
        GPoly exp = new GPoly();
        exp.setFill(true);
        exp.setFillPaint(formula.getPaint());
        exp.append(-5, 0);
        for (int i = -5; i < 5; i++) {
            exp.append(i, i);
        }
        exp.append(4, 0);
        while (iter.hasNext()) {
            GraphicE e = iter.next();
            if (e instanceof GPoly) {
                status = true;
                GPoly o = (GPoly)e;
                assertEquals(exp, o);
            }
        }
        assertTrue(status);
    }

    @Test
    @DisplayName("Test calculate null formula")
    public void testFormula5() {
        Calculable eye = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                return x;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> {
            new GFormula(null);
        });
        assertThrows(IllegalArgumentException.class, () -> {
            GFormula formula = new GFormula(eye);
            formula.calculate(0, 10, 1, null);
        });
        assertThrows(IntervalException.class, () -> {
            GFormula formula = new GFormula(eye);
            formula.calculate(0, 10, -10);
        });
        assertThrows(IntervalException.class, () -> {
            GFormula formula = new GFormula(eye);
            formula.calculate(100, 10, -10);
        });
        assertThrows(IntervalException.class, () -> {
            GFormula formula = new GFormula(eye);
            formula.calculate(0, 10, 20);
        });
    }

    @Test
    @DisplayName("Test set Fill Paint")
    public void testFormula6() {
        Calculable eye = new Calculable() {
            @Override
            public double f(double x) throws DomainException {
                return x;
            }
        };
        assertThrows(IllegalArgumentException.class, () -> {
            GFormula formula = new GFormula(eye);
            formula.setAreaPaint(null);
        });
        GFormula formula = new GFormula(eye);
        formula.setAreaPaint(Color.RED);
    }
}
