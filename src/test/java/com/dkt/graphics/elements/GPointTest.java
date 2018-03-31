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
package com.dkt.graphics.elements;

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPointTest {
    public static double DELTA = 1e-8;

    @Test
    @DisplayName("Copy constructor")
    public void testConstructor() {
        GPoint p1 = new GPoint(0, 0);
        GPoint p2 = new GPoint(p1);
        assertNotNull(p1);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
        p2.traslate(1, 1);
        assertNotEquals(p1, p2);
        assertNotEquals(p1.hashCode(), p2.hashCode());
        p2.traslate(-1, -1);
        assertEquals(p1, p2);
        assertEquals(p1.hashCode(), p2.hashCode());
    }

    @Test
    @DisplayName("Distance to self")
    public void testDistance1() {
        Random r = new Random();
        GPoint p1 = new GPoint(r.nextInt(), r.nextInt());
        assertEquals(0, p1.distance(p1), DELTA);
    }

    @Test
    @DisplayName("Distance to ortho x 1")
    public void testDistance2() {
        GPoint p1 = new GPoint(0, 0);
        GPoint p2 = new GPoint(1, 0);
        assertEquals(1, p1.distance(p2), DELTA);
        assertNotEquals(p1, p2);
    }

    @Test
    @DisplayName("Distance to ortho y 1")
    public void testDistance3() {
        GPoint p1 = new GPoint(4, 4);
        GPoint p2 = new GPoint(4, 5);
        assertEquals(1, p1.distance(p2), DELTA);
        assertNotEquals(p1, p2);
    }

    @Test
    @DisplayName("Distance to diagonal")
    public void testDistance4() {
        GPoint p1 = new GPoint(0, 0);
        GPoint p2 = new GPoint(1, 1);
        assertEquals(Math.sqrt(2), p1.distance(p2), DELTA);
        assertNotEquals(p1, p2);
    }

    @Test
    @DisplayName("Distance to self")
    public void testDistance5() {
        Random r = new Random();
        GPoint p1 = new GPoint(r.nextInt(), r.nextInt());
        assertEquals(0, p1.distance(p1.x(), p1.y()), DELTA);
    }

    @Test
    @DisplayName("Distance to ortho x 1")
    public void testDistance6() {
        GPoint p1 = new GPoint(0, 0);
        assertEquals(1, p1.distance(1, 0), DELTA);
    }

    @Test
    @DisplayName("Distance to ortho y 1")
    public void testDistance7() {
        GPoint p1 = new GPoint(4, 4);
        assertEquals(1, p1.distance(4, 5), DELTA);
    }

    @Test
    @DisplayName("Distance to diagonal")
    public void testDistance8() {
        GPoint p1 = new GPoint(0, 0);
        assertEquals(Math.sqrt(2), p1.distance(1, 1), DELTA);
    }

    @Test
    @DisplayName("Distance to null")
    public void testDistance9() {
        Random r = new Random();
        GPoint p1 = new GPoint(r.nextInt(), r.nextInt());
        assertEquals(0, p1.distance(p1), DELTA);
        assertThrows(IllegalArgumentException.class, () -> {
                p1.distance(null);
            }
        );
    }

    @Test
    @DisplayName("CCW collinear")
    public void testCcw1() {
        GPoint p1 = new GPoint(0, 0);
        GPoint p2 = new GPoint(1, 1);
        GPoint p3 = new GPoint(2, 2);
        assertEquals(0, GPoint.ccw(p1, p2, p3));
    }

    @Test
    @DisplayName("CCW counterclockwise")
    public void testCcw2() {
        GPoint p1 = new GPoint(0, 0);
        GPoint p2 = new GPoint(1, 1);
        GPoint p3 = new GPoint(-1, 2);
        assertEquals(1, GPoint.ccw(p1, p2, p3));
    }

    @Test
    @DisplayName("CCW clockwise")
    public void testCcw3() {
        GPoint p1 = new GPoint(0, 0);
        GPoint p2 = new GPoint(1, 1);
        GPoint p3 = new GPoint(-1, 2);
        assertEquals(-1, GPoint.ccw(p3, p2, p1));
    }

    @Test
    @DisplayName("to String")
    public void testToString() {
        assertEquals("(0, 0)", new GPoint(0,0).toString());
        assertEquals("(-3, 4)", new GPoint(-3,4).toString());
        GPoint p = new GPoint(0,0);
        p.move(30, 30);
        assertEquals("(30, 30)", p.toString());
    }

}
