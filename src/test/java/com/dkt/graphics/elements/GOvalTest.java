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
public class GOvalTest {
    @Test
    @DisplayName("constructors")
    public void testConstructor1() {
        GOval o1 = new GOval(0, 0, 10, 20);
        GOval o2 = new GOval(o1);
        GOval o3 = o1.clone();
        assertNotNull(o1);
        assertEquals(o1, o2);
        assertEquals(o2, o3);
        assertEquals(o1, new GOval(new GRectangle(0, 0, 10, 20)));
        assertEquals(o1.hashCode(), o2.hashCode());
        assertEquals(o2.hashCode(), o3.hashCode());
        o1.traslate(10, 10);
        assertNotEquals(o1, new GPoint(0, 0));
        assertNotEquals(o1, o2);
        assertNotEquals(o1.hashCode(), o2.hashCode());
        o1.traslate(-10, -10);
        assertEquals(o1, o2);
        assertEquals(o1.hashCode(), o2.hashCode());
        assertThrows(IllegalArgumentException.class, () -> {
                new GOval((GOval)null);
            }
        );
        assertThrows(IllegalArgumentException.class, () -> {
                new GOval((GRectangle)null);
            }
        );
    }
}
