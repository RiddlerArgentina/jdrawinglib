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
package com.dkt.graphics.extras;

import com.dkt.graphics.exceptions.InvalidArgumentException;
import com.dkt.graphics.utils.Utils;
import java.awt.Color;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPixMapTest {
    private static final Color ALPHA_W = Utils.getColorWithAlpha(Color.WHITE, 0);
    @Test
    @DisplayName("Constructor")
    public void testConstructor() {
        GPixMap pm = new GPixMap(2, 3);
        assertNotNull(pm);
        assertEquals(ALPHA_W, pm.colorAt(0, 0));
        assertEquals(ALPHA_W, pm.colorAt(0, 0));
        pm.setColorAt(0, 0, Color.RED);
        pm.setColorAt(1, 1, Color.BLUE);
        assertEquals(Color.RED, pm.colorAt(0, 0));
        assertEquals(Color.BLUE, pm.colorAt(1, 1));

        assertThrows(InvalidArgumentException.class, () -> {
                new GPixMap(-2, 3);
            }
        );
        assertThrows(InvalidArgumentException.class, () -> {
                new GPixMap(2, -3);
            }
        );
    }

    @Test
    @DisplayName("Constructor")
    public void testConstructor2() {
        Color[][] data = new Color[][]{
            {new Color(0xffffff00, true), new Color(0xffffff, true), new Color(0xffffff, true)},
            {new Color(0xffffff, true), new Color(0xffffff00, true), new Color(0xffffff, true)},
            {new Color(0xffffff, true), new Color(0xffffff, true), new Color(0xffffff00, true)}
        };
        GPixMap pm = new GPixMap(data);
        assertNotNull(pm);
        assertEquals(3, pm.getXSize());
        assertEquals(3, pm.getYSize());
        assertEquals(Color.YELLOW, pm.colorAt(0, 0));
        assertEquals(Color.YELLOW, pm.colorAt(1, 1));
        assertEquals(Color.YELLOW, pm.colorAt(2, 2));
        assertThrows(IllegalArgumentException.class, () -> {
                new GPixMap((Color[][])null);
            }
        );
        assertThrows(InvalidArgumentException.class, () -> {
                new GPixMap(new Color[0][0]);
            }
        );
    }

    @Test
    @DisplayName("Constructor")
    public void testConstructor3() {
        int[][] data = new int[][]{
            {0xffffff00, 0xffffff, 0xffffff},
            {0xffffff, 0xffffff00, 0xffffff},
            {0xffffff, 0xffffff, 0xffffff00}
        };
        GPixMap pm = new GPixMap(data, false);
        assertNotNull(pm);
        assertEquals(3, pm.getXSize());
        assertEquals(3, pm.getYSize());
        assertEquals(Color.YELLOW, pm.colorAt(0, 0));
        assertEquals(Color.YELLOW.getRGB(), pm.valueAt(1, 1));
        assertEquals(Color.YELLOW, pm.colorAt(2, 2));
    }

    @Test
    @DisplayName("Copy Constructor")
    public void testConstructor4() {
        int[][] data = new int[][]{
            {0xffffff00, 0xffffff, 0xffffff},
            {0xffffff, 0xffffff00, 0xffffff},
            {0xffffff, 0xffffff, 0xffffff00}
        };
        GPixMap pm1 = new GPixMap(data, false);
        GPixMap pm2 = new GPixMap(pm1);
        GPixMap pm3 = pm2.clone();
        assertNotNull(pm1);
        assertEquals(pm1, pm2);
        assertEquals(pm1.hashCode(), pm2.hashCode());
        assertEquals(pm3, pm3);
        assertEquals(pm3.hashCode(), pm3.hashCode());
    }

    @Test
    @DisplayName("equality")
    public void testEquality() {
        int[][] data1 = new int[][]{
            {0xffffff00, 0xffffff, 0xffffff},
            {0xffffff, 0xffffff00, 0xffffff},
            {0xffffff, 0xffffff, 0xffffff00}
        };
        Color[][] data2 = new Color[][]{
            {new Color(0xffffff00, true), new Color(0xffffff, true), new Color(0xffffff, true)},
            {new Color(0xffffff, true), new Color(0xffffff00, true), new Color(0xffffff, true)},
            {new Color(0xffffff, true), new Color(0xffffff, true), new Color(0xffffff00, true)}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data2);
        GPixMap pm3 = new GPixMap(data1, false);
        assertNotNull(pm1);
        assertEquals(pm1, pm2);
        assertEquals(pm1.hashCode(), pm2.hashCode());
        assertNotEquals(pm1, pm3);
        assertNotEquals(pm1.hashCode(), pm3.hashCode());
        pm1.setColorAt(0, 0, ALPHA_W);
        assertNotEquals(pm1, pm2);
        assertNotEquals(pm1.hashCode(), pm2.hashCode());
    }

    @Test
    @DisplayName("mirror horizontal")
    public void testMirrorHorizontal() {
        int[][] data1 = new int[][]{
            {0xff000000, 0xff000000, 0xffffff},
            {0xffffff, 0xff000000, 0xff000000},
            {0xffffff, 0xffffff, 0xff000000}
        };
        int[][] data2 = new int[][]{
            {0xffffff, 0xff000000, 0xff000000},
            {0xff000000, 0xff000000, 0xffffff},
            {0xff000000, 0xffffff, 0xffffff}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data2, true);
        assertNotNull(pm1);
        assertNotEquals(pm1, pm2);
        assertNotEquals(pm1, pm1.mirrorHorizontal());
        assertEquals(pm1.mirrorHorizontal(), pm2);
        assertEquals(pm1, pm1.mirrorHorizontal().mirrorHorizontal());
        assertEquals(pm1, pm2.mirrorHorizontal());
    }

    @Test
    @DisplayName("mirror vertical")
    public void testMirrorVertical() {
        int[][] data1 = new int[][]{
            {0xff000000, 0xff000000, 0xffffff},
            {0xffffff, 0xff000000, 0xff000000},
            {0xffffff, 0xffffff, 0xff000000}
        };
        int[][] data2 = new int[][]{
            {0xffffff, 0xffffff, 0xff000000},
            {0xffffff, 0xff000000, 0xff000000},
            {0xff000000, 0xff000000, 0xffffff}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data2, true);
        assertNotNull(pm1);
        assertNotEquals(pm1, pm2);
        assertNotEquals(pm1, pm1.mirrorVertical());
        assertEquals(pm1.mirrorVertical(), pm2);
        assertEquals(pm1, pm1.mirrorVertical().mirrorVertical());
        assertEquals(pm1, pm2.mirrorVertical());
    }

    @Test
    @DisplayName("Rotate Clockwise")
    public void testRotateClockwise() {
        int[][] data1 = new int[][]{
            {0xff0000ff, 0xffffff, 0xff000000},
            {0xffffff, 0xff000000, 0xff000000},
            {0xff000000, 0xff000000, 0xffffff}
        };
        int[][] data2 = new int[][]{
            {0xff000000, 0xffffff, 0xff0000ff},
            {0xff000000, 0xff000000, 0xffffff},
            {0xffffff, 0xff000000, 0xff000000}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data2, true);
        assertNotNull(pm1);
        assertNotEquals(pm1, pm2);
        assertNotEquals(pm1, pm1.rotateCW());
        assertEquals(pm1.rotateCW(), pm2);
        assertEquals(pm1, pm1.rotateCW().rotateCW().rotateCW().rotateCW());
        assertEquals(pm1, pm2.rotateCCW());
    }

    @Test
    @DisplayName("Rotate Counter Clockwise")
    public void testRotateCounterClockwise() {
        int[][] data1 = new int[][]{
            {0xff000000, 0xffffff, 0xff0000ff},
            {0xff000000, 0xff000000, 0xffffff},
            {0xffffff, 0xff000000, 0xff000000}
        };
        int[][] data2 = new int[][]{
            {0xff0000ff, 0xffffff, 0xff000000},
            {0xffffff, 0xff000000, 0xff000000},
            {0xff000000, 0xff000000, 0xffffff}
        };
        int[][] data3 = new int[][]{
            {0xff0000ff, 0xffffff, 0xff000000},
            {0xff000000, 0xff000000, 0xffffff}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data2, true);
        GPixMap pm3 = new GPixMap(data3, true);
        assertNotNull(pm1);
        assertNotEquals(pm1, pm2);
        assertNotEquals(pm1, pm1.rotateCCW());
        assertEquals(pm1.rotateCCW(), pm2);
        assertEquals(pm1, pm1.rotateCW().rotateCCW().rotateCCW().rotateCW());
        assertEquals(pm1, pm2.rotateCW());
        assertNotEquals(pm3.getXSize(), pm3.getYSize());
        assertEquals(pm3.getXSize(), pm3.rotateCCW().getYSize());
        assertEquals(pm3.getYSize(), pm3.rotateCCW().getXSize());
    }

    @Test
    @DisplayName("Pixel Size")
    public void testSetPixelSize() {
        int[][] data1 = new int[][]{
            {0xff000000, 0xffffff, 0xff0000ff},
            {0xff000000, 0xff000000, 0xffffff},
            {0xffffff, 0xff000000, 0xff000000}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data1, true);
        assertNotNull(pm1);
        assertEquals(pm1, pm2);
        pm2.setPixelSize(10);
        assertNotEquals(pm1, pm2);
        pm2.setPixelSize(pm1.pixelSize());
        assertEquals(pm1, pm2);
        assertThrows(InvalidArgumentException.class, () -> {
                pm2.setPixelSize(-10);
            }
        );
    }



    @Test
    @DisplayName("getData")
    public void testGetData() {
        int[][] data1 = new int[][]{
            {0xffffff, 0xff000000, 0xff000000},
            {0xff000000, 0xff000000, 0xffffff},
            {0xff000000, 0xffffff, 0xff0000ff}
        };
        Color[][] data2 = new Color[][]{
            {new Color(0xffffff, true), new Color(0xff000000, true), new Color(0xff000000, true)},
            {new Color(0xff000000, true), new Color(0xff000000, true), new Color(0xffffff, true)},
            {new Color(0xff000000, true), new Color(0xffffff, true), new Color(0xff0000ff, true)}
        };
        GPixMap pm1 = new GPixMap(data1, true);
        GPixMap pm2 = new GPixMap(data2);
        assertNotNull(pm1);
        assertEquals(pm1, pm2);
        assertArrayEquals(data1, pm1.getData());
        assertArrayEquals(data1, pm2.getData());
        assertArrayEquals(data2, pm1.getColorData());
        assertArrayEquals(data2, pm2.getColorData());
    }


}
