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
public class GCircleTest {
    @Test
    @DisplayName("constructors") 
    public void testConstructor1() {
        GCircle c1 = new GCircle(0, 0, 10);
        GCircle c2 = new GCircle(c1);
        assertNotNull(c1);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        c1.move(10, 10);
        assertNotEquals(c1, c2);
        assertNotEquals(c1.hashCode(), c2.hashCode());
        c1.move(0, 0);
        assertEquals(c1, c2);
        assertEquals(c1.hashCode(), c2.hashCode());
        
    }
    
    @Test
    @DisplayName("Copy constructor null") 
    public void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> {
                new GCircle(null);
            }
        );
    }
    
    @Test
    @DisplayName("clone") 
    public void testClone() {
        GCircle c = new GCircle(0, 0, 10);
        assertNotNull(c);
        assertEquals(c, c.clone());
    }
    
    @Test
    @DisplayName("Circle attributes") 
    public void testAttributes() {
        GCircle c1 = new GCircle(0, 0, 10);
        assertNotNull(c1);
        assertEquals(0, c1.x());
        assertEquals(0, c1.y());
        assertEquals(10, c1.getRadius());
        assertEquals(Math.PI * 20, c1.perimeter());
        assertEquals(Math.PI * 100, c1.area());
        c1.traslate(5, 10);
        assertEquals(5, c1.x());
        assertEquals(10, c1.y());
        assertEquals(10, c1.getRadius());
        assertEquals(Math.PI * 20, c1.perimeter());
        assertEquals(Math.PI * 100, c1.area());
    }
    
    @Test
    @DisplayName("Circle contains circle") 
    public void testContains1() {
        GCircle c1 = new GCircle(0, 0, 10);
        GCircle c2 = new GCircle(0, 0, 5);
        assertTrue(c1.contains(c2));
        assertFalse(c2.contains(c1));
        c2.move(20, 20);
        assertFalse(c1.contains(c2));
        assertFalse(c2.contains(c1));
        assertThrows(IllegalArgumentException.class, () -> {
                c1.contains((GCircle)null);
            }
        );
    }
    
    @Test
    @DisplayName("Circle contains point") 
    public void testContains2() {
        GCircle c = new GCircle(0, 0, 10);
        GPoint  p = new GPoint(0, 0);
        assertTrue(c.contains(p));
        p.traslate(20, 20);
        assertFalse(c.contains(p));
        assertThrows(IllegalArgumentException.class, () -> {
                c.contains((GPoint)null);
            }
        );
    }
    
    @Test
    @DisplayName("Circle contains point") 
    public void testContains3() {
        GCircle c = new GCircle(0, 0, 10);
        assertTrue(c.contains(0, 0));
        assertFalse(c.contains(20, 10));
    }
    
    @Test
    @DisplayName("Circle contains line") 
    public void testContains4() {
        GCircle c = new GCircle(0, 0, 10);
        GLine   l = new GLine(0, 0, 5, 5);
        assertTrue(c.contains(l));
        l.traslate(20, 20);
        assertFalse(c.contains(l));
        assertThrows(IllegalArgumentException.class, () -> {
                c.contains((GLine)null);
            }
        );
    }
    
    @Test
    @DisplayName("Circle intersects line") 
    public void testIntersects1() {
        GCircle c = new GCircle(0, 0, 10);
        GLine   l = new GLine(-20, -20, 20, 20);
        assertTrue(c.intersects(l));
        l.traslate(200, 100);
        assertFalse(c.intersects(l));
        assertThrows(IllegalArgumentException.class, () -> {
                c.intersects((GLine)null);
            }
        );
    }
    
    @Test
    @DisplayName("Circle intersects circle") 
    public void testIntersects2() {
        GCircle c1 = new GCircle(0, 0, 100);
        GCircle c2 = new GCircle(0, 0, 50);
        GCircle c3 = new GCircle(100, 0, 50);
        GCircle c4 = new GCircle(100, 100, 40);
        assertTrue(c1.intersects(c2));
        assertTrue(c1.contains(c2));
        assertTrue(c1.intersects(c3));
        assertFalse(c1.contains(c3));
        assertFalse(c1.intersects(c4));
        assertFalse(c1.contains(c4));
        
        assertThrows(IllegalArgumentException.class, () -> {
                c1.intersects((GCircle)null);
            }
        );
    }
    
}
