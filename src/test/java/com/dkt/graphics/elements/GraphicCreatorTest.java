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
import java.util.Iterator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since the graphic creator uses almost all of the other classes, this is the
 * closest thing to integration tests.
 * 
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GraphicCreatorTest {
    @Test
    @DisplayName("point") 
    public void testPoint1() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse("point 0 0");
        Graphic g2 = new Graphic();
        g2.add(new GPoint(0, 0));
        assertNotNull(g);
        assertEquals(g, g2);
        assertEquals(1, g.getCount());
        assertEquals(0, gc.getErrorCount());
        assertEquals(1, gc.getTotalLineCount());
        Iterator<GraphicE> it = g.iterator();
        GraphicE e = it.next();
        assertTrue(e instanceof GPoint);
        GPoint p = (GPoint)e;
        assertEquals(0, p.x());
        assertEquals(0, p.y());
    }
    
    @Test
    @DisplayName("point cross") 
    public void testPoint2() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse("point 0 0 3");
        Graphic g2 = new Graphic();
        g2.add(new GPoint(0, 0, 3));
        assertEquals(1, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(1, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("points") 
    public void testPoint3() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                "point 0 0\n"
              + "point 0 0 3"
        );
        Graphic g2 = new Graphic();
        g2.add(new GPoint(0, 0));
        g2.add(new GPoint(0, 0, 3));
        assertEquals(2, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(2, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("points one commented") 
    public void testPoint4() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                "#point 0 0\n"
              + "point 0 0 3"
        );
        Graphic g2 = new Graphic();
        g2.add(new GPoint(0, 0, 3));
        assertEquals(1, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(2, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Lines") 
    public void testLines() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "linec 0 0 20 20\n"
                + "linec 1 2 3 4\n"
                + "linep 30 40 50 0\n"
                + "linep -3 40 50 45"
        );
        Graphic g2 = new Graphic();
        g2.add(new GLine(0, 0, 20, 20));
        g2.add(new GLine(1, 2, 3, 4));
        g2.add(new GLine(30, 40, 50., 0.));
        g2.add(new GLine(-3, 40, 50., 45.));
        assertEquals(4, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(4, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Path") 
    public void testLinePath() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "lpath 0 10 10 10 10 20 20 20 20 30 30 30"
        );
        Graphic g2 = new Graphic();
        GPath path = new GPath();
        path.append(0, 10);
        path.append(10, 10);
        path.append(10, 20);
        path.append(20, 20);
        path.append(20, 30);
        path.append(30, 30);
        g2.add(path);
        assertEquals(1, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(1, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Polygon") 
    public void testLinePoly() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "polyp 0 10 10 10 10 20 20 20 20 30 30 30"
        );
        Graphic g2 = new Graphic();
        GPoly poly = new GPoly();
        poly.append(0, 10);
        poly.append(10, 10);
        poly.append(10, 20);
        poly.append(20, 20);
        poly.append(20, 30);
        poly.append(30, 30);
        g2.add(poly);
        assertEquals(1, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(1, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Rectangles") 
    public void testLineRectangles() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "rectf -10 -10 10 10\n" +
                  "rectc 0 0 20 20"
        );
        Graphic g2 = new Graphic();
        g2.add(new GRectangle(0, 0, 20, 20));
        g2.add(new GRectangle(0, 0, 20, 20));
        assertEquals(2, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(2, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Regular Polygon") 
    public void testRegPoly() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "polyn 10 10 60 5\n" +
                  "polyn 10 10 60 5 20\n" +
                  "polyn 10 10 60 5 40\n" +
                  "polyn 10 10 60 5 60"
        );
        Graphic g2 = new Graphic();
        g2.add(new GRegPoly(10, 10, 60, 5, 0));
        g2.add(new GRegPoly(10, 10, 60, 5, 20));
        g2.add(new GRegPoly(10, 10, 60, 5, 40));
        g2.add(new GRegPoly(10, 10, 60, 5, 60));
        assertEquals(4, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(4, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Vectors") 
    public void testVectors() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "vectc 0 0 20 20\n"
                + "vectc 1 2 3 4\n"
                + "vectp 30 40 50 0\n"
                + "vectp -3 40 50 45"
        );
        Graphic g2 = new Graphic();
        g2.add(new GVector(0, 0, 20, 20));
        g2.add(new GVector(1, 2, 3, 4));
        g2.add(new GVector(30, 40, 50., 0.));
        g2.add(new GVector(-3, 40, 50., 45.));
        assertEquals(4, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(4, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Circles") 
    public void testCircles() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "circle  0 0 20\n"
                + "circle 10 40 20\n"
                + "oval 1 2 3 4"
        );
        Graphic g2 = new Graphic();
        g2.add(new GCircle(0, 0, 20));
        g2.add(new GCircle(10, 40, 20));
        g2.add(new GOval(1, 2, 3, 4));
        assertEquals(3, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(3, gc.getTotalLineCount());
    }
    
    @Test
    @DisplayName("Arcs") 
    public void testArcs() {
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(
                  "arcc   0  0 75  90  90\n" +
                  "arcc   0  0 75  90 -90\n" +
                  "arcc   0  0 50  90   0\n" +
                  "arcc   0  0 50  90 180\n" +
                  "arcc   0  0 25  90  90\n" +
                  "arcc   0  0 25  90 -90\n" +
                  "arcc -70 70 30 280  80"
        );
        Graphic g2 = new Graphic();
        g2.add(new GArc(0, 0, 75, 90, 90));
        g2.add(new GArc(0, 0, 75, 90, -90));
        g2.add(new GArc(0, 0, 50, 90, 0));
        g2.add(new GArc(0, 0, 50, 90, 180));
        g2.add(new GArc(0, 0, 25, 90, 90));
        g2.add(new GArc(0, 0, 25, 90, -90));
        g2.add(new GArc(-70, 70, 30, 280, 80));
        assertEquals(7, g.getCount());
        assertEquals(g, g2);
        assertEquals(0, gc.getErrorCount());
        assertEquals(7, gc.getTotalLineCount());
    }
}
