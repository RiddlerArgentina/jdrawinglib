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
        BasicStroke s = new BasicStroke(3);
        for (int i = 0; i < 300; i += 10) {
            GLine l = new GLine(0, i, i, 0);
            l.setStroke(s);
            g2.add(l);
        }
        for (int i = 0; i < 300; i += 10) {
            GLine l = new GLine(i, 300, 300, i);
            l.setStroke(s);
            l.setPaint(new Color(0, Math.min(i, 255), Math.min(i, 255)));
            g2.add(l);
        }
        assertEquals(g, g2);
    }
    
    @Test
    @DisplayName("Examples->Lines->Lines 2") 
    public void testLines2() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
            "stroke 0.5\n" +
            "color 128 160 160\n" +
            "linec 0 150 0 -150\n" +
            "linec -150 0 150 0\n" +
            "\n" +
            "# Inner lines\n" +
            "for2 200 -10 0 10 10 200 \"linec 0  %d  %d 0\"\n" +
            "for2 200 -10 0 10 10 200 \"linec 0  %d -%d 0\"\n" +
            "for2 200 -10 0 10 10 200 \"linec 0 -%d  %d 0\"\n" +
            "for2 200 -10 0 10 10 200 \"linec 0 -%d -%d 0\"\n" +
            "\n" +
            "color 100 100 200\n" +
            "# Outter lines\n" +
            "for2 200 -10 0 10 10 200 \"linec  200  %d  %d  200\"\n" +
            "for2 200 -10 0 10 10 200 \"linec -200  %d -%d  200\"\n" +
            "for2 200 -10 0 10 10 200 \"linec  200 -%d  %d -200\"\n" +
            "for2 200 -10 0 10 10 200 \"linec -200 -%d -%d -200\""
        );
        Graphic g2 = new Graphic();
        BasicStroke s = new BasicStroke(0.5f, BasicStroke.CAP_SQUARE,BasicStroke.JOIN_BEVEL);
        Color c = new Color(128, 160, 160);
        GLine l1 = new GLine(0, 150, 0, -150);
        GLine l2 = new GLine(-150, 0, 150, 0);
        l1.setPaint(c);
        l2.setPaint(c);
        l1.setStroke(s);
        l2.setStroke(s);
        g2.add(l1);
        g2.add(l2);
        
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(0, i, i, 0);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(0, i, -i, 0);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(0, -i, i, 0);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(0, -i, -i, 0);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        c = new Color(100, 100, 200);
        
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(200, i, i, 200);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(-200, i, -i, 200);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(200, -i, i, -200);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        for (int i = 200; i >= 0; i -= 10) {
            for (int j = 10; j < 200; j += 10) {
                GLine l = new GLine(-200, -i, -i, -200);
                l.setPaint(c);
                l.setStroke(s);
                g2.add(l);
            }
        }
        assertEquals(g, g2);
    }
}
