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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GraphicETest {
    @Test
    @DisplayName("Copy constructor")
    public void testConstructor1() {
        GraphicE ge1 = new GraphicEImpl();
        GraphicE ge2 = new GraphicEImpl(ge1);
        assertNotNull(ge1);
        assertEquals(ge1, ge2);
        assertEquals(ge1.hashCode(), ge2.hashCode());
    }

    @Test
    @DisplayName("Copy constructor null")
    public void testConstructor2() {
        assertThrows(IllegalArgumentException.class, () -> new GraphicEImpl(null)
        );
        assertNotEquals(new GraphicEImpl(), new Object());
    }

    @Test
    @DisplayName("get/set paint")
    public void testPaint() {
        GraphicE ge1 = new GraphicEImpl();
        GraphicE ge2 = new GraphicEImpl(ge1);
        assertNotNull(ge1);
        assertEquals(ge1, ge2);
        assertEquals(ge1.hashCode(), ge2.hashCode());
        assertNotEquals(Color.CYAN, ge1.getPaint());
        ge1.setPaint(Color.CYAN);
        assertEquals(Color.CYAN, ge1.getPaint());
        assertNotEquals(ge1, ge2);
        assertNotEquals(ge1.hashCode(), ge2.hashCode());
        assertThrows(IllegalArgumentException.class, () -> ge1.setPaint(null)
        );
    }

    @Test
    @DisplayName("get/set stroke")
    public void testStroke() {
        GraphicE ge1 = new GraphicEImpl();
        GraphicE ge2 = new GraphicEImpl(ge1);
        assertNotNull(ge1);
        assertEquals(ge1, ge2);
        assertEquals(ge1.hashCode(), ge2.hashCode());
        assertNotEquals(new BasicStroke(2), ge1.getStroke());
        ge1.setStroke(new BasicStroke(2));
        assertEquals(new BasicStroke(2), ge1.getStroke());
        assertNotEquals(ge1, ge2);
        assertNotEquals(ge1.hashCode(), ge2.hashCode());
        assertThrows(IllegalArgumentException.class, () -> ge1.setStroke(null)
        );
    }

    private static class GraphicEImpl extends GraphicE {
        public GraphicEImpl(GraphicE e) throws IllegalArgumentException {
            super(e);
        }

        public GraphicEImpl() {
            super();
        }

        @Override public void traslate(int x, int y) {/*do nothing*/}
        @Override public void draw(Graphics2D g) {/*do nothing*/}
        @Override public GraphicE clone() {return null;/*do nothing*/}
    }
}
