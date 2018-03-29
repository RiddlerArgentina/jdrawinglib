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

import com.dkt.graphics.exceptions.InvalidArgumentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GRectangleTest {
    @Test
    @DisplayName("constructors") 
    public void testConstructor1() {
        GRectangle r1 = new GRectangle(10);
        GRectangle r2 = new GRectangle(r1);
        assertNotNull(r1);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        assertThrows(IllegalArgumentException.class, () -> {
                new GRectangle(null);
            }
        );
        assertThrows(InvalidArgumentException.class, () -> {
                new GRectangle(-1);
            }
        );
        assertThrows(InvalidArgumentException.class, () -> {
                new GRectangle(0, 0, 10, -10);
            }
        );
        assertThrows(InvalidArgumentException.class, () -> {
                new GRectangle(0, 0, -10, 10);
            }
        );
    }
    
    @Test
    @DisplayName("clone") 
    public void testClone() {
        GRectangle r = new GRectangle(10, 20, 10, 5);
        assertNotNull(r);
        assertEquals(r, r.clone());
        assertEquals(r.hashCode(), r.clone().hashCode());
    }
    
    @Test
    @DisplayName("attributes") 
    public void testAttributes() {
        GRectangle r = new GRectangle(10, 20, 30, 40);
        assertEquals(10, r.getCX());
        assertEquals(20, r.getCY());
        assertEquals(30, r.getWidth());
        assertEquals(40, r.getHeight());
        assertEquals(-5, r.getLeftL());
        assertEquals(25, r.getRightL());
        assertEquals(40, r.getUpperL());
        assertEquals(0, r.getLowerL());
        assertEquals(1200, r.area());
        assertEquals(140, r.perimeter());
        r.traslate(10, 10);
        assertEquals(20, r.getCX());
        assertEquals(30, r.getCY());
        assertEquals(30, r.getWidth());
        assertEquals(40, r.getHeight());
        assertEquals(5, r.getLeftL());
        assertEquals(35, r.getRightL());
        assertEquals(50, r.getUpperL());
        assertEquals(10, r.getLowerL());
        assertEquals(1200, r.area());
        assertEquals(140, r.perimeter());
    }
    
    @Test
    @DisplayName("contains point") 
    public void testContainsPoint() {
        GRectangle r = new GRectangle(10, 20, 30, 40);
        assertTrue(r.contains(10, 20));
        assertTrue(r.contains(25, 20));
        assertFalse(r.contains(-10, 20));
        assertFalse(r.contains(100, 100));
    }
    
    @Test
    @DisplayName("contains Gpoint") 
    public void testContainsGPoint() {
        GRectangle r = new GRectangle(10, 20, 30, 40);
        assertTrue(r.contains(new GPoint(10, 20)));
        assertTrue(r.contains(new GPoint(25, 20)));
        assertFalse(r.contains(new GPoint(-10, 20)));
        assertFalse(r.contains(new GPoint(100, 100)));
        assertThrows(IllegalArgumentException.class, () -> {
                r.contains((GPoint)null);
            }
        );
    }
    
    @Test
    @DisplayName("contains GLine") 
    public void testContainsGLine() {
        GRectangle r = new GRectangle(50);
        GLine l1 = new GLine(-5, -5, 5, 5);
        GLine l2 = new GLine(0, 0, 50, 45);
        GLine l3 = new GLine(100, 100, 50, 45);
        assertTrue(r.contains(l1));
        assertFalse(r.contains(l2));
        assertFalse(r.contains(l3));
        assertThrows(IllegalArgumentException.class, () -> {
                r.contains((GLine)null);
            }
        );
    }
    
    @Test
    @DisplayName("move") 
    public void testMove() {
        GRectangle r1 = new GRectangle(50);
        GRectangle r2 = new GRectangle(r1);
        assertNotNull(r1);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
        r1.move(20, 20);
        assertNotEquals(r1, r2);
        assertNotEquals(r1.hashCode(), r2.hashCode());
        r2.traslate(20, 0);
        r2.traslate(0, 20);
        assertEquals(r1, r2);
        assertEquals(r1.hashCode(), r2.hashCode());
    }
}
