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

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GLineTest {
    @Test
    @DisplayName("Copy constructor")
    public void testConstructor1() {
        GLine l1 = new GLine(0, 0, 1, 1);
        GLine l2 = new GLine(l1);
        assertNotNull(l1);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.traslate(10, 10);
        assertNotEquals(l1, l2);
        assertNotEquals(l1.hashCode(), l2.hashCode());
        l2.traslate(-10, -10);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }
    
    @Test
    @DisplayName("Copy constructor with point")
    public void testConstructor2() {
        GLine l1 = new GLine(new GPoint(0, 0), new GPoint(1, 1));
        GLine l2 = new GLine(l1);
        assertNotNull(l1);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.traslate(10, 10);
        assertNotEquals(l1, l2);
        assertNotEquals(l1.hashCode(), l2.hashCode());
        l2.traslate(-10, -10);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }
    
    @Test
    @DisplayName("Clone")
    public void testClone() {
        GLine l1 = new GLine(new GPoint(0, 0), new GPoint(1, 1));
        GLine l2 = l1.clone();
        assertNotNull(l1);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
        l2.traslate(10, 10);
        assertNotEquals(l1, l2);
        assertNotEquals(l1.hashCode(), l2.hashCode());
        l2.traslate(-10, -10);
        assertEquals(l1, l2);
        assertEquals(l1.hashCode(), l2.hashCode());
    }
    
    @Test
    @DisplayName("Middle Point on zero lenght")
    public void testMiddlePoint1() {
        GLine l = new GLine(0, 0, 0, 0);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(s, m);
        assertEquals(m, e);
        assertEquals(s, e);
    }

    @Test
    @DisplayName("Middle Point on x axis")
    public void testMiddlePoint2() {
        GLine l = new GLine(0, 10, 0, 0);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(new GPoint(0, 10), s);
        assertEquals(new GPoint(0, 5), m);
        assertEquals(new GPoint(0, 0), e);
    }

    @Test
    @DisplayName("Middle Point on y axis")
    public void testMiddlePoint3() {
        GLine l = new GLine(0, 0, 0, 10);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(new GPoint(0, 0), s);
        assertEquals(new GPoint(0, 5), m);
        assertEquals(new GPoint(0, 10), e);
    }

    @Test
    @DisplayName("Middle Point on diagonal")
    public void testMiddlePoint4() {
        GLine l = new GLine(10, 10, 20, 20);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(new GPoint(10, 10), s);
        assertEquals(new GPoint(15, 15), m);
        assertEquals(new GPoint(20, 20), e);
    }

    @Test
    @DisplayName("Constructor x axis")
    public void testConstructByAngle1() {
        GLine l = new GLine(0, 0, 10.0, 0.0);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(new GPoint(0, 0), s);
        assertEquals(new GPoint(5, 0), m);
        assertEquals(new GPoint(10, 0), e);
    }

    @Test
    @DisplayName("Constructor y axis")
    public void testConstructByAngle2() {
        GLine l = new GLine(0, 0, 10.0, 90.);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(new GPoint(0, 0), s);
        assertEquals(new GPoint(0, 5), m);
        assertEquals(new GPoint(0, 10), e);
    }

    @Test
    @DisplayName("Constructor diagonal")
    public void testConstructByAngle3() {
        GLine l = new GLine(0, 0, Math.sqrt(8), 45);
        GPoint s = l.getStartPoint();
        GPoint m = l.getMiddlePoint();
        GPoint e = l.getEndPoint();
        assertEquals(new GPoint(0, 0), s);
        assertEquals(new GPoint(1, 1), m);
        assertEquals(new GPoint(2, 2), e);
    }

    @Test
    @DisplayName("Orthogonal x axis")
    public void testOrthogonal1() {
        GLine l = new GLine(0, 0, 10, 0);
        GLine o = l.getOrthogal(5, 0);
        assertEquals(l.getArgument() + 90, o.getArgument(), 1e-9);
        o.contains(5, 0);
    }

    @Test
    @DisplayName("Orthogonal y axis")
    public void testOrthogonal2() {
        GLine l = new GLine(0, 0, 0, 10);
        GLine o = l.getOrthogal(0, 5);
        assertEquals(l.getArgument() + 90, o.getArgument(), 1e-9);
        assertTrue(o.contains(0, 5));
    }

    @Test
    @DisplayName("Orthogonal random")
    public void testOrthogonal3() {
        GLine l = new GLine(13, 20, 10, 10);
        GLine o = l.getOrthogal(0, 5);
        assertEquals(l.getArgument() + 90, o.getArgument(), 1e-9);
        assertTrue(o.contains(0, 5));
    }

    @Test
    @DisplayName("Parallel x axis")
    public void testParallel1() {
        GLine l = new GLine(0, 0, 10, 0);
        GLine o = l.getParallel(5, 0);
        assertEquals(l.getArgument(), o.getArgument(), 1e-9);
        assertTrue(l.contains(5, 0));
        assertTrue(o.contains(5, 0));
    }

    @Test
    @DisplayName("Parallel y axis")
    public void testParallel2() {
        GLine l = new GLine(0, 0, 0, 10);
        GLine o = l.getParallel(0, 5);
        assertEquals(l.getArgument(), o.getArgument(), 1e-9);
        assertTrue(o.contains(0, 5));
    }

//    @Test
//    @DisplayName("Parallel random")
//    public void testParallel3() {
//        GLine l = new GLine(13, 20, 10, 10);
//        GLine o = l.getParallel(0, 5);
//        assertEquals(l.getArgument(), o.getArgument(), 1e-9);
//        assertTrue(o.contains(0, 5));
//    }
}
