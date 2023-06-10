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
package com.dkt.graphics.elements;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GFillableETest {
    @Test
    @DisplayName("Copy constructor")
    public void testConstructor1() {
        GFillableE ge1 = new GFillableEImpl();
        GFillableE ge2 = new GFillableEImpl(ge1);
        assertNotNull(ge1);
        assertEquals(ge1, ge2);
        assertEquals(ge1.hashCode(), ge2.hashCode());
    }

    @Test
    @DisplayName("Copy constructor null")
    public void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> new GFillableEImpl(null)
        );
        assertNotEquals(new GFillableEImpl(), new Object());
    }

    @Test
    @DisplayName("get/set fillable")
    public void testFillPaint() {
        GFillableE ge1 = new GFillableEImpl();
        GFillableE ge2 = new GFillableEImpl(ge1);
        assertNotNull(ge1);
        assertEquals(ge1, ge2);
        assertEquals(ge1.hashCode(), ge2.hashCode());
        assertNotEquals(Color.CYAN, ge1.getFillPaint());
        ge1.setFillPaint(Color.CYAN);
        assertEquals(Color.CYAN, ge1.getFillPaint());
        assertNotEquals(ge1, ge2);
        assertNotEquals(ge1.hashCode(), ge2.hashCode());
        assertThrows(IllegalArgumentException.class, () -> ge1.setFillPaint(null)
        );
    }

    @Test
    @DisplayName("get/set fill")
    public void testFill() {
        GFillableE ge1 = new GFillableEImpl();
        GFillableE ge2 = new GFillableEImpl(ge1);
        assertNotNull(ge1);
        assertEquals(ge1, ge2);
        assertEquals(ge1.hashCode(), ge2.hashCode());
        assertFalse(ge1.fill());
        ge1.setFill(true);
        assertTrue(ge1.fill());
        assertNotEquals(ge1, ge2);
        assertNotEquals(ge1.hashCode(), ge2.hashCode());
    }

    private static class GFillableEImpl extends GFillableE {
        public GFillableEImpl(GFillableE e) throws IllegalArgumentException {
            super(e);
        }

        public GFillableEImpl() {
            super();
        }

        @Override public void traslate(int x, int y) {/*do nothing*/}
        @Override public void draw(Graphics2D g) {/*do nothing*/}
        @Override public Area getShape()  {return null;/*do nothing*/}
        @Override public GFillableE clone()  {return null;/*do nothing*/}
    }
}
