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

import com.dkt.graphics.extras.GraphicCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GraphicTest {
    @Test
    @DisplayName("Flatten")
    public void testFlatten() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g1 = gc.parse(
            "color 128 128 128\n" +
            "for2 0 10 145 140 10 0 \"linec  %1$d  150  150  %2$d\"\n" +
            "for2 0 10 145 140 10 0 \"linec  %1$d -150  150 -%2$d\"\n" +
            "for2 0 10 145 140 10 0 \"linec -%1$d -150 -150 -%2$d\"\n" +
            "for2 0 10 145 140 10 0 \"linec -%1$d  150 -150  %2$d\"\n" +
            "\n" +
            "color 0 0 0\n" +
            "clipadd circle 0 0 156\n" +
            "linec -150 -150  150  150\n" +
            "linec -150  150  150 -150"
        );
        Graphic g2 = new Graphic(g1);
        assertNotNull(g1);
        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
        int graphicCount = 0;
        //There should be no Graphics after flattening
        for (GraphicE e : g1) {
            if (e instanceof Graphic) {
                graphicCount++;
            }
        }
        // There should be 4 Graphics (one for each for) a GClip and two GLine
        assertEquals(4, graphicCount);
        assertEquals(7, g1.getCount());
        g1.flatten();
        assertEquals(63, g1.getCount());

        assertNotEquals(g1, g2);
        assertNotEquals(g1.hashCode(), g2.hashCode());

        //There should be no Graphics after flattening
        for (GraphicE e : g1) {
            assertFalse(e instanceof Graphic);
        }
        g2.flatten();

        assertEquals(g1, g2);
        assertEquals(g1.hashCode(), g2.hashCode());
    }
}
