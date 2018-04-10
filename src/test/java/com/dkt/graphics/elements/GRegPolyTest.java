/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2018 <fede@riddler.com.ar>
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
package com.dkt.graphics.elements;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GRegPolyTest {
    @Test
    @DisplayName("test Constructor")
    public void testConstructor() {
        GRegPoly gp = new GRegPoly(1, 2, 20, 5, 0);
        assertNotNull(gp);
        assertEquals(1, gp.getX());
        assertEquals(2, gp.getY());
        assertEquals(20, gp.getRadius());
    }

    @Test
    @DisplayName("test copy constructor")
    public void testCopyConstructor() {
        GRegPoly rp1 = new GRegPoly(1, 2, 20, 5, 0);
        GRegPoly rp2 = new GRegPoly(rp1);
        assertNotNull(rp1);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
        rp1.traslate(10, 20);
        assertNotEquals(rp1, rp2);
        assertNotEquals(rp1.hashCode(), rp2.hashCode());
        rp1.traslate(-10, -20);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
    }

    @Test
    @DisplayName("test clone")
    public void testClone() {
        GRegPoly rp1 = new GRegPoly(1, 2, 20, 5, 0);
        GRegPoly rp2 = rp1.clone();
        assertNotNull(rp1);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
        rp1.traslate(10, 20);
        assertNotEquals(rp1, rp2);
        assertNotEquals(rp1.hashCode(), rp2.hashCode());
        rp1.traslate(-10, -20);
        assertEquals(rp1, rp2);
        assertEquals(rp1.hashCode(), rp2.hashCode());
    }

    @Test
    @DisplayName("test zero")
    public void testArea1() {
        GRegPoly rp1 = new GRegPoly(1, 2, 0, 5, 0);
        assertNotNull(rp1);
        assertEquals(0, rp1.area());
    }

    @Test
    @DisplayName("area known")
    public void testArea2() {
        GRegPoly rp1 = new GRegPoly(1, 2, 6, 6, 0);
        assertNotNull(rp1);
        assertEquals(93.53074361, rp1.area(), 1e-6);
        GRegPoly rp2 = new GRegPoly(1, 2, 10, 7, 0);
        assertNotNull(rp2);
        assertEquals(273.64101886, rp2.area(), 1e-6);
        GRegPoly rp3 = new GRegPoly(1, 2, 10, 7, 90);
        assertEquals(rp2.area(), rp3.area(), 1e-6);
    }

    @Test
    @DisplayName("perimeter zero")
    public void testPerimeter() {
        GRegPoly rp1 = new GRegPoly(1, 2, 0, 6, 0);
        assertNotNull(rp1);
        assertEquals(0, rp1.perimeter());
    }

    @Test
    @DisplayName("perimeter zero")
    public void testPerimeter2() {
        GRegPoly rp1 = new GRegPoly(1, 2, 10, 6, 0);
        assertNotNull(rp1);
        assertEquals(60, rp1.perimeter(), 1e-6);
        GRegPoly rp2 = new GRegPoly(1, 2, 10, 6, 84);
        assertNotNull(rp2);
        assertEquals(rp1.perimeter(), rp2.perimeter());
        GRegPoly rp3 = new GRegPoly(14, -42, 10, 6, 84);
        assertNotNull(rp3);
        assertEquals(rp1.perimeter(), rp3.perimeter());
    }

    @Test
    @DisplayName("contains")
    public void testContains() {
        GRegPoly rp1 = new GRegPoly(1, 2, 10, 6, 0);
        assertNotNull(rp1);
        assertTrue(rp1.contains(new GPoint(1, 2)));
        assertTrue(rp1.contains(new GPoint(2, 3)));
        assertFalse(rp1.contains(new GPoint(11, 2)));
        assertFalse(rp1.contains(new GPoint(20, 2)));
        //Needs more tests
        assertThrows(IllegalArgumentException.class, () -> {
                rp1.contains(null);
            }
        );
    }

    @Test
    @DisplayName("rotate")
    public void testRotate() {
        GRegPoly rp1 = new GRegPoly(1, 2, 10, 6, 0);
        GRegPoly rp2 = rp1.clone();
        GRegPoly rp3 = new GRegPoly(1, 2, 10, 6, 90);
        assertNotNull(rp1);
        assertEquals(rp1, rp2);
        assertNotEquals(rp2, rp3);
        rp2.rotate(90);
        assertEquals(rp2, rp3);
        assertEquals(rp1.area(), rp3.area());
        assertEquals(rp1.perimeter(), rp3.perimeter());
    }

}
