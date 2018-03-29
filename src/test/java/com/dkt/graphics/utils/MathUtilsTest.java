/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2018 <fede@riddler.com.ar>
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
package com.dkt.graphics.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * // @TODO should also test min and max with negative values // @TODO test min
 * and max with Integer.MAX_VALUE and Integer.MIN_VALUE
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class MathUtilsTest {

    @Test
    @DisplayName("heavyside")
    public void testHs() {
        assertEquals(0, MathUtils.hs(-10));
        assertEquals(1, MathUtils.hs(  0));
        assertEquals(1, MathUtils.hs( 10));
    }
    
    @Test
    @DisplayName("boxcar")
    public void testBoxCar() {
        double a = -1;
        double b = 1;
        assertEquals(0, MathUtils.boxcar(a-1, a, b));
        assertEquals(1, MathUtils.boxcar((b-a)/2, a, b));
        assertEquals(0, MathUtils.boxcar(b+1, a, b));
    }
    
    @Test
    @DisplayName("a < b")
    public void testMin1() {
        int a = 10;
        int b = 20;
        int result = MathUtils.min(a, b);
        int expected = Math.min(a, b);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("a > b")
    public void testMin2() {
        int a = 20;
        int b = 10;
        int result = MathUtils.min(a, b);
        int expected = Math.min(a, b);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("a = b")
    public void testMin3() {
        int a = 0;
        int b = 0;
        int result = MathUtils.min(a, b);
        int expected = Math.min(a, b);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("a > b")
    public void testMax1() {
        int a = 10;
        int b = 20;
        int result = MathUtils.max(a, b);
        int expected = Math.max(a, b);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("a < b")
    public void testMax2() {
        int a = 20;
        int b = 10;
        int result = MathUtils.max(a, b);
        int expected = Math.max(a, b);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("a = b")
    public void testMax3() {
        int a = 0;
        int b = 0;
        int result = MathUtils.max(a, b);
        int expected = Math.max(a, b);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("abs(0)")
    public void testAbs1() {
        int a = 0;
        int result = MathUtils.abs(a);
        int expected = Math.abs(a);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("abs(>0)")
    public void testAbs2() {
        int a = 10;
        int result = MathUtils.abs(a);
        int expected = Math.abs(a);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("abs(<0)")
    public void testAbs3() {
        int a = -10;
        int result = MathUtils.abs(a);
        int expected = Math.abs(a);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("abs(+Inf)")
    public void testAbs4() {
        int a = Integer.MAX_VALUE;
        int result = MathUtils.abs(a);
        int expected = Math.abs(a);
        assertEquals(expected, result);
    }

    @Test
    @DisplayName("abs(-Inf)")
    public void testAbs5() {
        int a = Integer.MIN_VALUE;
        int result = MathUtils.abs(a);
        int expected = Math.abs(a);
        assertEquals(expected, result);
    }

}
