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
import java.awt.BasicStroke;
import java.awt.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since the graphic creator uses almost all of the other classes, this is the
 * closest thing to integration tests.
 * 
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GraphicCreatorExamplesTest {
    
    @Test
    @DisplayName("Examples->Lines->Lines 1") 
    public void testLines1() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
            "for1 0 10 300 \"stroke 3; linec 0 %1$d %1$d 0\"\n" +
            "for1 0 10 300 \"stroke 3; color 0 %1$d %1$d; linec %1$d 300 300 %1$d\"");
        Graphic g2 = new Graphic();
        Graphic g3 = new Graphic();
        BasicStroke s = new BasicStroke(3f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
        for (int i = 0; i <= 300; i += 10) {
            GLine l = new GLine(0, i, i, 0);
            l.setStroke(s);
            g3.add(l);
        }
        g2.add(g3);
        g3 = new Graphic();
        for (int i = 0; i <= 300; i += 10) {
            GLine l = new GLine(i, 300, 300, i);
            l.setStroke(s);
            l.setPaint(new Color(0, Math.min(i, 255), Math.min(i, 255)));
            g3.add(l);
        }
        g2.add(g3);
        assertEquals(g.getCount(), g2.getCount());
        assertEquals(g, g2);
    }
    
}
