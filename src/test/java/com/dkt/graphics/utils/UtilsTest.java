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

import java.awt.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class UtilsTest {
    @Test
    @DisplayName("Color with alpha") 
    public void testColorWithAlpha() {
        Color c1 = Color.CYAN;
        Color c2 = Utils.getColorWithAlpha(c1, 0);
        assertEquals(c2.getAlpha(), 0);
        assertEquals(c2.getRed(),   c1.getRed());
        assertEquals(c2.getGreen(), c1.getGreen());
        assertEquals(c2.getBlue(),  c1.getBlue());
        c2 = Utils.getColorWithAlpha(c1, 128);
        assertEquals(c2.getAlpha(), 128);
        assertEquals(c2.getRed(),   c1.getRed());
        assertEquals(c2.getGreen(), c1.getGreen());
        assertEquals(c2.getBlue(),  c1.getBlue());
        c2 = Utils.getColorWithAlpha(c1, -127);
        assertEquals(c2.getAlpha(), 0);
        assertEquals(c2.getRed(),   c1.getRed());
        assertEquals(c2.getGreen(), c1.getGreen());
        assertEquals(c2.getBlue(),  c1.getBlue());
        c2 = Utils.getColorWithAlpha(c1, 19284);
        assertEquals(c2.getAlpha(), 255);
        assertEquals(c2.getRed(),   c1.getRed());
        assertEquals(c2.getGreen(), c1.getGreen());
        assertEquals(c2.getBlue(),  c1.getBlue());
    }
}
