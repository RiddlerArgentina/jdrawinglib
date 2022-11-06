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

import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.util.Iterator;
import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPointArrayTest {
    public static double DELTA = 1e-8;


    @Test
    @DisplayName("Test constructor")
    public void testConstructor1() {
        GPointArray mp = new GPointArray(10);
        assertNotNull(mp);
    }

    @Test
    @DisplayName("Test constructor < 0")
    public void testConstructor2() {
        assertThrows(NegativeArraySizeException.class, () -> {
            GMultiPoint mp = new GPointArray(-10);
            mp.clear();
        });
    }

    @Test
    @DisplayName("Test constructor different lengths")
    public void testConstructor3() {
        int[] XX = {0, 1, 2, 3, 4, 5};
        int[] YY = {0, 1, 2, 3, 4, 5, 6};
        assertThrows(InvalidArgumentException.class, () -> {
            new GPointArray(XX, YY);
        });
    }

    @Test
    @DisplayName("Test constructor different lengths")
    public void testConstructor4() {
        int[] XX = {0, 1, 2, 3, 4, 5, 6};
        int[] YY = {0, 1, 2, 3, 4, 5};
        assertThrows(InvalidArgumentException.class, () -> {
                new GPointArray(XX, YY);
            }
        );
    }

    @Test
    @DisplayName("Test constructor data")
    public void testConstructor5() {
        int[] XX = {0, 1, 2, 3, 4};
        int[] YY = {5, 6, 7, 8, 9};
        GMultiPoint mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(5, mp.size());
        XX[0] = 10000;
        assertEquals(0, mp.getPointAt(0).x());
    }

    @Test
    @DisplayName("Copy constructor")
    public void testConstructor6() {
        int[] XX = {0, 1, 2, 3, 4};
        int[] YY = {5, 6, 7, 8, 9};
        GPointArray mp = new GPointArray(XX, YY);
        GPointArray mp2 = new GPointArray(mp);

        assertNotNull(mp);
        assertNotNull(mp2);
        assertEquals(mp.hashCode(), mp2.hashCode());
    }

    @Test
    @DisplayName("Constructor points")
    public void testConstructor7() {
        GPoint[] ar = {
            new GPoint(0, 4),
            new GPoint(1, 5),
            new GPoint(2, 6),
            new GPoint(3, 7),
        };

        GPointArray mp = new GPointArray(ar);

        assertNotNull(mp);
        assertArrayEquals(ar, mp.getPoints());
    }

    @Test
    @DisplayName("Index of empty")
    public void testIndexOf1() {
        GPointArray mp = new GPointArray(10);

        assertNotNull(mp);
        assertEquals(-1, mp.indexOf(new GPoint(0, 0)));
    }

    @Test
    @DisplayName("Index of first")
    public void testIndexOf2() {
        int[] XX = {0, 1, 2, 3, 4};
        int[] YY = {5, 6, 7, 8, 9};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(5, mp.size());
        assertEquals(0, mp.indexOf(new GPoint(0, 5)));
    }

    @Test
    @DisplayName("Index of last")
    public void testIndexOf3() {
        int[] XX = {0, 1, 2, 3, 4};
        int[] YY = {5, 6, 7, 8, 9};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());
        assertEquals(4, mp.indexOf(new GPoint(4, 9)));
        mp.clear();
        assertEquals(0, mp.size());
        assertTrue(mp.isEmpty());
    }

    @Test
    @DisplayName("Index with initial search")
    public void testIndexOf4() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        assertEquals(5, mp.indexOf(new GPoint(3, 8), 4));
        assertEquals(3, mp.indexOf(new GPoint(3, 8)));
    }

    @Test
    @DisplayName("Remove out of bounds")
    public void testRemove1() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mp.remove(-1);
        });
    }

    @Test
    @DisplayName("Remove out of bounds")
    public void testRemove2() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mp.remove(10);
        });
    }

    @Test
    @DisplayName("Remove out of bounds")
    public void testRemove3() {
        GPointArray mp = new GPointArray(4);

        assertNotNull(mp);
        assertEquals(0, mp.size());
        assertTrue(mp.isEmpty());

        assertThrows(IndexOutOfBoundsException.class, () -> {
            mp.remove(1);
        });
    }

    @Test
    @DisplayName("Remove first")
    public void testRemove4() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(0);

        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        for (int i = 1; i < YY.length; i++) {
            GPoint p1 = mp.getPointAt(i - 1);
            GPoint p2 = new GPoint(XX[i], YY[i]);
            assertEquals(p1, p2);
        }
    }

    @Test
    @DisplayName("Remove last")
    public void testRemove5() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(5);

        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        for (int i = 0; i < YY.length - 1; i++) {
            GPoint p1 = mp.getPointAt(i);
            GPoint p2 = new GPoint(XX[i], YY[i]);
            assertEquals(p1, p2);
        }
    }

    @Test
    @DisplayName("Remove middle")
    public void testRemove6() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(3);

        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(XX[0], YY[0]), mp.getPointAt(0));
        assertEquals(new GPoint(XX[1], YY[1]), mp.getPointAt(1));
        assertEquals(new GPoint(XX[2], YY[2]), mp.getPointAt(2));
        assertEquals(new GPoint(XX[4], YY[4]), mp.getPointAt(3));
        assertEquals(new GPoint(XX[5], YY[5]), mp.getPointAt(4));
    }

    @Test
    @DisplayName("Remove first")
    public void testRemove7() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(0, 5);

        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        for (int i = 1; i < YY.length; i++) {
            GPoint p1 = mp.getPointAt(i - 1);
            GPoint p2 = new GPoint(XX[i], YY[i]);
            assertEquals(p1, p2);
        }
    }

    @Test
    @DisplayName("Remove last")
    public void testRemove8() {
        int[] XX = {0, 1, 2, 3, 4};
        int[] YY = {5, 6, 7, 8, 9};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(4, 9);

        assertEquals(4, mp.size());
        assertFalse(mp.isEmpty());

        for (int i = 0; i < YY.length - 1; i++) {
            GPoint p1 = mp.getPointAt(i);
            GPoint p2 = new GPoint(XX[i], YY[i]);
            assertEquals(p1, p2);
        }
    }

    @Test
    @DisplayName("Remove middle")
    public void testRemove9() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(2, 7);

        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(XX[0], YY[0]), mp.getPointAt(0));
        assertEquals(new GPoint(XX[1], YY[1]), mp.getPointAt(1));
        assertEquals(new GPoint(XX[3], YY[3]), mp.getPointAt(2));
        assertEquals(new GPoint(XX[4], YY[4]), mp.getPointAt(3));
        assertEquals(new GPoint(XX[5], YY[5]), mp.getPointAt(4));
    }

    @Test
    @DisplayName("Remove inexistent")
    public void testRemove10() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertFalse(mp.remove(2, 8));

        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(XX[0], YY[0]), mp.getPointAt(0));
        assertEquals(new GPoint(XX[1], YY[1]), mp.getPointAt(1));
        assertEquals(new GPoint(XX[2], YY[2]), mp.getPointAt(2));
        assertEquals(new GPoint(XX[3], YY[3]), mp.getPointAt(3));
        assertEquals(new GPoint(XX[4], YY[4]), mp.getPointAt(4));
        assertEquals(new GPoint(XX[5], YY[5]), mp.getPointAt(5));
    }

    @Test
    @DisplayName("Remove by point")
    public void testRemove11() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        mp.remove(new GPoint(2, 7));

        assertEquals(5, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(XX[0], YY[0]), mp.getPointAt(0));
        assertEquals(new GPoint(XX[1], YY[1]), mp.getPointAt(1));
        assertEquals(new GPoint(XX[3], YY[3]), mp.getPointAt(2));
        assertEquals(new GPoint(XX[4], YY[4]), mp.getPointAt(3));
        assertEquals(new GPoint(XX[5], YY[5]), mp.getPointAt(4));
    }

    @Test
    @DisplayName("Remove inexistent")
    public void testRemove12() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertFalse(mp.remove(new GPoint(2, 8)));

        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(XX[0], YY[0]), mp.getPointAt(0));
        assertEquals(new GPoint(XX[1], YY[1]), mp.getPointAt(1));
        assertEquals(new GPoint(XX[2], YY[2]), mp.getPointAt(2));
        assertEquals(new GPoint(XX[3], YY[3]), mp.getPointAt(3));
        assertEquals(new GPoint(XX[4], YY[4]), mp.getPointAt(4));
        assertEquals(new GPoint(XX[5], YY[5]), mp.getPointAt(5));
    }

    @Test
    @DisplayName("Remove null")
    public void testRemove13() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());

        assertThrows(IllegalArgumentException.class, () -> {
            mp.remove(null);
        });
    }

    @Test
    @DisplayName("Append to empty")
    public void testAppend1() {
        GPointArray mp = new GPointArray(0);

        assertNotNull(mp);
        assertEquals(0, mp.size());
        assertTrue(mp.isEmpty());
        mp.append(1,2);
        assertEquals(1, mp.size());
        assertFalse(mp.isEmpty());
        mp.append(3,4);
        assertEquals(2, mp.size());
        assertFalse(mp.isEmpty());
        mp.append(3,4);
        assertEquals(3, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(1, 2), mp.getPointAt(0));
        assertEquals(new GPoint(3, 4), mp.getPointAt(1));
        assertEquals(new GPoint(3, 4), mp.getPointAt(2));

    }

    @Test
    @DisplayName("Append to data")
    public void testAppend2() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        mp.append(1,2);
        assertEquals(7, mp.size());
        assertFalse(mp.isEmpty());
        mp.append(3,4);
        assertEquals(8, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(1, 2), mp.getPointAt(6));
        assertEquals(new GPoint(3, 4), mp.getPointAt(7));

    }

    @Test
    @DisplayName("AppendNR to empty")
    public void testAppendNR1() {
        GPointArray mp = new GPointArray(0);

        assertNotNull(mp);
        assertEquals(0, mp.size());
        assertTrue(mp.isEmpty());
        mp.appendNR(1,2);
        assertEquals(1, mp.size());
        assertFalse(mp.isEmpty());
        mp.appendNR(3,4);
        assertEquals(2, mp.size());
        assertFalse(mp.isEmpty());
        mp.appendNR(3,4);
        assertEquals(2, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(1, 2), mp.getPointAt(0));
        assertEquals(new GPoint(3, 4), mp.getPointAt(1));

    }

    @Test
    @DisplayName("AppendNR to data")
    public void testAppendNR2() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        mp.appendNR(1,2);
        assertEquals(7, mp.size());
        assertFalse(mp.isEmpty());
        mp.appendNR(1,6);
        assertEquals(7, mp.size());
        assertFalse(mp.isEmpty());

        assertEquals(new GPoint(1, 2), mp.getPointAt(6));
    }

    @Test
    @DisplayName("Sort by X")
    public void testSortByX() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        mp.sortByX();
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        assertEquals(0, mp.indexOf(0, 5));
        assertEquals(1, mp.indexOf(1, 6));
        assertEquals(2, mp.indexOf(2, 7));
        assertEquals(3, mp.indexOf(3, 8));
        assertEquals(4, mp.indexOf(3, 8), 4);
        assertEquals(5, mp.indexOf(4, 9));
        mp.removeDuplicates();
        assertEquals(4, mp.size());
        assertFalse(mp.isEmpty());
    }

    @Test
    @DisplayName("Sort by Y")
    public void testSortByY() {
        int[] XX = {5, 6, 7, 8, 9, 8};
        int[] YY = {0, 1, 2, 3, 4, 3};
        GPointArray mp = new GPointArray(XX, YY);

        assertNotNull(mp);
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        mp.sortByY();
        assertEquals(6, mp.size());
        assertFalse(mp.isEmpty());
        assertEquals(0, mp.indexOf(5, 0));
        assertEquals(1, mp.indexOf(6, 1));
        assertEquals(2, mp.indexOf(7, 2));
        assertEquals(3, mp.indexOf(8, 3));
        assertEquals(4, mp.indexOf(8, 3), 4);
        assertEquals(5, mp.indexOf(9, 4));
        mp.removeDuplicates();
        assertEquals(4, mp.size());
        assertFalse(mp.isEmpty());
    }

    @Test
    @DisplayName("Add to hashmap")
    public void testHashCode() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);
        GPointArray mp2 = new GPointArray(mp);
        assertNotNull(mp);
        assertNotNull(mp2);
        assertEquals(mp, mp2);
        assertEquals(mp.hashCode(), mp2.hashCode());
        mp2.remove(5);
        assertNotEquals(mp, mp2);
        assertNotEquals(mp.hashCode(), mp2.hashCode());
        mp2.append(3, 8);
        assertEquals(mp, mp2);
        assertEquals(mp.hashCode(), mp2.hashCode());
    }

    @Test
    @DisplayName("Traslate 0")
    public void testTraslate1() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);
        GPointArray mp2 = new GPointArray(mp);
        assertNotNull(mp);
        assertNotNull(mp2);
        assertEquals(mp, mp2);
        mp.traslate(0, 0);
        assertEquals(mp, mp2);
    }

    @Test
    @DisplayName("Traslate step")
    public void testTraslate2() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);
        GPointArray mp2 = new GPointArray(mp);
        assertNotNull(mp);
        assertNotNull(mp2);
        assertEquals(mp, mp2);
        mp.traslate(1, 1);
        assertNotEquals(mp, mp2);
        int i = 0;
        Iterator<GPoint> it = mp.iterator();
        while (it.hasNext()) {
            assertEquals(new GPoint(XX[i] +1, YY[i] +1), it.next());
            i++;
        }
    }

    @Test
    @DisplayName("get Points")
    public void testGetPoints() {
        int[] XX = {0, 1, 2, 3, 4, 3};
        int[] YY = {5, 6, 7, 8, 9, 8};
        GPointArray mp = new GPointArray(XX, YY);
        assertNotNull(mp);

        GPoint[] points = mp.getPoints();

        assertEquals(points.length, mp.size());

        for (int i = 0; i < YY.length; i++) {
            assertEquals(mp.getPointAt(i), points[i]);
        }
    }

    @Test
    @DisplayName("Highest point")
    public void testHighestPoint(){
        GPointArray pa = populate();
        GPoint p = pa.highestPoint();
        assertFalse(pa.isEmpty());
        for (GPoint pp : pa) {
            assertTrue(p.y() >= pp.y());
        }
        pa.clear();
        assertTrue(pa.isEmpty());
        assertNull(pa.highestPoint());
    }

    @Test
    @DisplayName("Lowest point")
    public void testLowestPoint(){
        GPointArray pa = populate();
        GPoint p = pa.lowestPoint();
        assertFalse(pa.isEmpty());
        for (GPoint pp : pa) {
            assertTrue(p.y() <= pp.y());
        }
        pa.clear();
        assertTrue(pa.isEmpty());
        assertNull(pa.lowestPoint());
    }

    @Test
    @DisplayName("Leftmost point")
    public void testLeftmostPoint(){
        GPointArray pa = populate();
        GPoint p = pa.leftmostPoint();
        assertFalse(pa.isEmpty());
        for (GPoint pp : pa) {
            assertTrue(p.x() <= pp.x());
        }
        pa.clear();
        assertTrue(pa.isEmpty());
        assertNull(pa.leftmostPoint());
    }

    @Test
    @DisplayName("Rightmost point")
    public void testRightmostPoint(){
        GPointArray pa = populate();
        GPoint p = pa.rightmostPoint();
        assertFalse(pa.isEmpty());
        for (GPoint pp : pa) {
            assertTrue(p.x() >= pp.x());
        }
        pa.clear();
        assertTrue(pa.isEmpty());
        assertNull(pa.rightmostPoint());
    }

    @Test
    @DisplayName("Bounds")
    public void testGetBounds(){
        GPointArray pa = populate();
        GRectangle rec = pa.getBounds();
        assertNotNull(rec);

        GPoint lp = pa.lowestPoint();
        GPoint hp = pa.highestPoint();
        GPoint ll = pa.leftmostPoint();
        GPoint rp = pa.rightmostPoint();
        lp.traslate( 0,  1);
        hp.traslate( 0, -1);
        ll.traslate( 1,  0);
        rp.traslate(-1,  0);

        assertTrue(rec.contains(lp));
        assertTrue(rec.contains(hp));
        assertTrue(rec.contains(ll));
        assertTrue(rec.contains(rp));

        pa.clear();
        assertTrue(pa.isEmpty());
        assertNull(pa.getBounds());
    }

    @Test
    @DisplayName("Higher than point")
    public void testHigherThanPoint(){
        GPointArray pa1 = populate();
        GPointArray pa2 = pa1.higherThan(new GPoint(0, 0));
        assertFalse(pa1.isEmpty());
        assertFalse(pa2.isEmpty());
        for (GPoint pp : pa2) {
            assertTrue(pp.y() > 0);
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.higherThan(null);
        });
        pa1.clear();
        assertEquals(0, pa1.size());
        assertEquals(pa1, pa1.higherThan(new GPoint(0, 0)));
    }

    @Test
    @DisplayName("Lower than point")
    public void testLowerThanPoint(){
        GPointArray pa1 = populate();
        GPointArray pa2 = pa1.lowerThan(new GPoint(0, 0));
        assertFalse(pa1.isEmpty());
        assertFalse(pa2.isEmpty());
        for (GPoint pp : pa2) {
            assertTrue(pp.y() < 0);
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.lowerThan(null);
        });
        pa1.clear();
        assertEquals(0, pa1.size());
        assertEquals(pa1, pa1.lowerThan(new GPoint(0, 0)));
    }

    @Test
    @DisplayName("Left than point")
    public void testLeftThanPoint(){
        GPointArray pa1 = populate();
        GPointArray pa2 = pa1.leftThan(new GPoint(0, 0));
        assertFalse(pa1.isEmpty());
        assertFalse(pa2.isEmpty());
        for (GPoint pp : pa2) {
            assertTrue(pp.x() < 0);
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.leftThan(null);
        });
        pa1.clear();
        assertEquals(0, pa1.size());
        assertEquals(pa1, pa1.leftThan(new GPoint(0, 0)));
    }

    @Test
    @DisplayName("Right than point")
    public void testRightThanPoint(){
        GPointArray pa1 = populate();
        GPointArray pa2 = pa1.rightThan(new GPoint(0, 0));
        assertFalse(pa1.isEmpty());
        assertFalse(pa2.isEmpty());
        for (GPoint pp : pa2) {
            assertTrue(pp.x() > 0);
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.rightThan(null);
        });
        pa1.clear();
        assertEquals(0, pa1.size());
        assertEquals(pa1, pa1.rightThan(new GPoint(0, 0)));
    }

    @Test
    @DisplayName("Remove all")
    public void testRemoveAll1(){
        GPointArray pa1 = populate();
        GPointArray pa2 = pa1.rightThan(new GPoint(0, 0));
        assertFalse(pa1.isEmpty());
        assertFalse(pa2.isEmpty());
        pa1.removeAll(pa2);
        assertEquals(pa1, pa1.leftThan(new GPoint(1, 0)));
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.removeAll(null);
        });
    }

    @Test
    @DisplayName("Remove all")
    public void testRemoveAll2(){
        GPointArray pa1 = populate();
        assertFalse(pa1.isEmpty());
        pa1.removeAll(pa1);
        assertTrue(pa1.isEmpty());
    }

    @Test
    @DisplayName("Intersects line")
    public void testIntersects1(){
        GPointArray pa1 = populate();
        GPointArray pa2;
        GLine l1 = new GLine(1000, 1000, 10., 0.);
        GLine l2 = new GLine(0, 0, 100., 45.);
        assertFalse(pa1.isEmpty());
        assertTrue(pa1.intersection(l1).isEmpty());
        pa2 = pa1.intersection(l2);
        assertFalse(pa2.isEmpty());
        for (GPoint p : pa2) {
            assertTrue(l2.contains(p));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.intersection((GLine)null);
        });
    }

    @Test
    @DisplayName("Intersects rectangle")
    public void testIntersects2(){
        GPointArray pa1 = populate();
        GPointArray pa2;
        GRectangle r1 = new GRectangle(1000, 1000, 10, 10);
        GRectangle r2 = new GRectangle(0, 0, 100, 100);
        assertFalse(pa1.isEmpty());
        assertTrue(pa1.intersection(r1).isEmpty());
        pa2 = pa1.intersection(r2);
        assertFalse(pa2.isEmpty());
        for (GPoint p : pa2) {
            assertTrue(r2.contains(p));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.intersection((GRectangle)null);
        });
    }

    @Test
    @DisplayName("Intersects circle")
    public void testIntersects3(){
        GPointArray pa1 = populate();
        GPointArray pa2;
        GCircle c1 = new GCircle(1000, 1000, 10);
        GCircle c2 = new GCircle(0, 0, 50);
        assertFalse(pa1.isEmpty());
        assertTrue(pa1.intersection(c1).isEmpty());
        pa2 = pa1.intersection(c2);
        assertFalse(pa2.isEmpty());
        for (GPoint p : pa2) {
            assertTrue(c2.contains(p));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.intersection((GCircle)null);
        });
    }

    @Test
    @DisplayName("Clone")
    public void testClone(){
        GPointArray pa1 = populate();
        assertFalse(pa1.isEmpty());
        assertEquals(pa1, pa1.clone());
        pa1.setCrossSize(3);
        assertEquals(pa1, pa1.clone());
    }

    @Test
    @DisplayName("Closest point")
    public void testClosestPoint(){
        GPointArray pa1 = populate();
        assertNotNull(pa1.closestPoint(new GPoint(0, 0)));
        assertFalse(pa1.isEmpty());
        pa1.append(new GPoint(1000, 10000));
        assertEquals(new GPoint(1000, 10000), pa1.closestPoint(new GPoint(1000, 10000)));
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.closestPoint((GPoint)null);
        });
    }

    @Test
    @DisplayName("Point in radius")
    public void testPointInRadius(){
        GPointArray pa1 = populate();
        GPointArray pa2 = pa1.pointsInRadius(new GPoint(0, 0), 40);
        assertNotNull(pa2);
        assertFalse(pa1.isEmpty());
        assertFalse(pa2.isEmpty());
        GCircle c = new GCircle(0, 0, 41);
        for (GPoint p : pa2) {
            assertTrue(c.contains(p));
        }
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.pointsInRadius((GPoint)null, 10);
        });
        pa1.clear();
        assertEquals(pa1, pa1.pointsInRadius(new GPoint(0, 0) , 10));
    }

    @Test
    @DisplayName("Append")
    public void testAppend(){
        GPointArray pa1 = populate();
        GPointArray pa2 = populate();
        GPointArray pa3 = new GPointArray(pa1);

        assertFalse(pa1.isEmpty());
        assertEquals(400, pa1.size());
        assertFalse(pa2.isEmpty());
        assertEquals(400, pa2.size());
        assertFalse(pa3.isEmpty());
        assertEquals(400, pa2.size());
        assertEquals(pa1, pa3);
        pa1.append(pa2);
        assertFalse(pa1.isEmpty());
        assertEquals(800, pa1.size());
        assertFalse(pa2.isEmpty());
        assertEquals(400, pa2.size());
        assertFalse(pa3.isEmpty());
        assertEquals(400, pa2.size());
        assertNotEquals(pa1, pa3);
        assertThrows(IllegalArgumentException.class, () -> {
            pa1.append((GPointArray)null);
        });
    }

    private GPointArray populate() {
        Random r = new Random();
        GPointArray pa = new GPointArray();
        for (int i = 0; i < 400; i++) {
            pa.append(new GPoint(
                    r.nextInt(500) - 250,
                    r.nextInt(500) - 250)
            );
        }
        return pa;
    }

}