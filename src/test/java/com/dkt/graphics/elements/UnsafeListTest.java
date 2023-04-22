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

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class UnsafeListTest {

    @Test
    @DisplayName("Constructor")
    public void testConstructor() {
        UnsafeList list = new UnsafeList(10);
        assertNotNull(list);
    }

    @Test
    @DisplayName("Constructor < 0")
    public void testConstructor2() {
        assertThrows(NegativeArraySizeException.class, () -> {
            new UnsafeList(-1);
        });
    }

    @Test
    @DisplayName("Add element with extra capacity")
    public void testAdd1() {
        UnsafeList list = new UnsafeList(10);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        list.add(new GPoint(0, 0));
        assertEquals(1, list.size());
        assertFalse(list.isEmpty());
        list.add(new GPoint(0, 0));
        assertEquals(2, list.size());
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("Add element with not enough capacity")
    public void testAdd2() {
        UnsafeList list = new UnsafeList(1);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        for (int i = 1; i < 20; i++) {
            boolean status = list.add(new GPoint(0, 0));
            assertEquals(i, list.size());
            assertTrue(status);
        }
        assertFalse(list.isEmpty());
    }

    @Test
    @DisplayName("Add null element")
    public void testAdd3() {
        UnsafeList list = new UnsafeList(10);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        list.add(null);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Ensure capacity")
    public void testEnsureCapacity() {
        UnsafeList list = new UnsafeList(0);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        list.ensureCapacity(-10);
        list.ensureCapacity(0);
        list.ensureCapacity(10);
        list.ensureCapacity(5);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
    }

    @Test
    @DisplayName("Get out of bounds")
    public void testGet1() {
        UnsafeList list = new UnsafeList(10);
        assertEquals(0, list.size());
        assertTrue(list.isEmpty());
        GraphicE e = list.get(-10);
        assertNull(e);
        e = list.get(5);
        assertNull(e);
    }

    @Test
    @DisplayName("Get element")
    public void testGet2() {
        UnsafeList list = new UnsafeList(10);
        GPoint point = new GPoint(10, 10);
        assertNull(list.get(0));
        list.add(point);
        assertNotNull(list.get(0));
        assertEquals(point, list.get(0));
    }

    @Test
    @DisplayName("Clear empty")
    public void testClear1() {
        UnsafeList list = new UnsafeList(10);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Clear with one element")
    public void testClear2() {
        UnsafeList list = new UnsafeList(10);
        GPoint point = new GPoint(10, 10);
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        list.add(point);
        assertFalse(list.isEmpty());
        assertEquals(1, list.size());
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("Clear with many elements")
    public void testClear3() {
        UnsafeList list = new UnsafeList(10);
        Random rand = new Random();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
        for (int i = 1; i < 20; i++) {
            list.add(new GPoint(rand.nextInt(), rand.nextInt()));
            assertFalse(list.isEmpty());
            assertEquals(i, list.size());
        }
        UnsafeList l2 = new UnsafeList(10);
        assertEquals(0, l2.size());
        l2.addAll(list);
        assertEquals(19, l2.size());
        assertNotEquals(list, null);
        assertNotEquals(list, rand);
        assertEquals(list, list);
        assertEquals(list, l2);
        assertEquals(list.hashCode(), l2.hashCode());
        l2.add(new GPoint(0, 0));
        assertEquals(20, l2.size());
        assertNotEquals(list, l2);
        assertNotEquals(list.hashCode(), l2.hashCode());
        assertNotNull(l2.remove(l2.size() - 1));
        assertEquals(19, l2.size());
        assertEquals(list, l2);
        assertEquals(list.hashCode(), l2.hashCode());
        assertNotNull(l2.remove(l2.size() - 1));
        assertEquals(18, l2.size());
        assertNotEquals(list, l2);
        assertNotEquals(list.hashCode(), l2.hashCode());
        l2.add(new GLine(0, 0, 10, 10));
        assertEquals(19, l2.size());
        assertNotEquals(list, l2);
        assertNotEquals(list.hashCode(), l2.hashCode());
        list.clear();
        assertTrue(list.isEmpty());
        assertEquals(0, list.size());
    }

    @Test
    @DisplayName("IndexOf empty")
    public void testIndexOf1() {
        UnsafeList list = new UnsafeList(10);
        int res = list.indexOf(new GPoint(0, 0));
        assertEquals(-1, res);
    }

    @Test
    @DisplayName("IndexOf 1")
    public void testIndexOf2() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        int res = list.indexOf(new GPoint(0, 0));
        assertEquals(0, res);
    }

    @Test
    @DisplayName("IndexOf repeated element")
    public void testIndexOf3() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 0));
        int res = list.indexOf(new GPoint(0, 0));
        assertEquals(0, res);
    }

    @Test
    @DisplayName("IndexOf second element")
    public void testIndexOf4() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        int res = list.indexOf(new GPoint(0, 1));
        assertEquals(1, res);
    }

    @Test
    @DisplayName("Remove element")
    public void testRemove1() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(3, list.size());
        GraphicE foo = list.remove(1);
        assertNotNull(foo);
        assertEquals(new GPoint(0, 0), list.get(0));
        assertEquals(new GPoint(0, 1), foo);
        assertEquals(new GPoint(0, 2), list.get(1));
    }

    @Test
    @DisplayName("Remove null")
    public void testRemove2() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(null);
        list.add(new GPoint(0, 2));
        assertEquals(3, list.size());
        GraphicE foo = list.remove(1);
        assertNotNull(foo);
        assertEquals(new GPoint(0, 0), list.get(0));
        assertEquals(new GPoint(0, 1), foo);
        assertEquals(new GPoint(0, 2), list.get(1));
    }

    @Test
    @DisplayName("Remove last element")
    public void testRemove3() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(3, list.size());
        GraphicE foo = list.remove(list.size() - 1);
        assertNotNull(foo);
        assertEquals(new GPoint(0, 0), list.get(0));
        assertEquals(new GPoint(0, 1), list.get(1));
        assertEquals(new GPoint(0, 2), foo);
    }

    @Test
    @DisplayName("Remove non existent element")
    public void testRemoveElement1() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(3, list.size());
        boolean status = list.remove(new GPoint(0, 3));
        assertFalse(status);
        assertEquals(3, list.size());
        assertEquals(new GPoint(0, 0), list.get(0));
        assertEquals(new GPoint(0, 1), list.get(1));
        assertEquals(new GPoint(0, 2), list.get(2));
    }

    @Test
    @DisplayName("Remove repeated element")
    public void testRemoveElement2() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 3));
        assertEquals(5, list.size());
        boolean status = list.remove(new GPoint(0, 1));
        assertTrue(status);
        assertEquals(3, list.size());
        assertEquals(new GPoint(0, 0), list.get(0));
        assertEquals(new GPoint(0, 2), list.get(1));
        assertEquals(new GPoint(0, 3), list.get(2));
    }

    @Test
    @DisplayName("Remove repeated element (on a row)")
    public void testRemoveElement3() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(6, list.size());
        boolean status = list.remove(new GPoint(0, 1));
        assertTrue(status);
        assertEquals(2, list.size());
        assertEquals(new GPoint(0, 0), list.get(0));
        assertEquals(new GPoint(0, 2), list.get(1));
    }

    @Test
    @DisplayName("Contains null")
    public void testContains1() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(6, list.size());
        assertFalse(list.contains(null));
    }

    @Test
    @DisplayName("Contains one")
    public void testContains2() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(6, list.size());
        assertTrue(list.contains(new GPoint(0, 0)));
    }

    @Test
    @DisplayName("Contains none")
    public void testContains3() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(6, list.size());
        assertFalse(list.contains(new GPoint(0, 4)));
    }

    @Test
    @DisplayName("Contains last")
    public void testContains4() {
        UnsafeList list = new UnsafeList(10);
        list.add(new GPoint(0, 0));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 1));
        list.add(new GPoint(0, 2));
        assertEquals(6, list.size());
        assertTrue(list.contains(new GPoint(0, 2)));
    }

    @Test
    @DisplayName("Add all empty")
    public void testAddAll1() {
        UnsafeList l1 = new UnsafeList(10);
        UnsafeList l2 = new UnsafeList(10);
        l1.add(new GPoint(0, 0));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 2));
        assertEquals(6, l1.size());
        l1.addAll(l2);
        assertEquals(6, l1.size());
    }

    @Test
    @DisplayName("Add all null")
    public void testAddAll2() {
        UnsafeList l1 = new UnsafeList(10);
        l1.add(new GPoint(0, 0));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 2));
        assertEquals(6, l1.size());
        l1.addAll(null);
        assertEquals(6, l1.size());
    }

    @Test
    @DisplayName("Add all this")
    public void testAddAll3() {
        UnsafeList l1 = new UnsafeList(10);
        l1.add(new GPoint(0, 0));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 2));
        assertEquals(6, l1.size());
        l1.addAll(l1);
        assertEquals(6, l1.size());
    }

    @Test
    @DisplayName("Add all one")
    public void testAddAll4() {
        UnsafeList l1 = new UnsafeList(10);
        UnsafeList l2 = new UnsafeList(10);
        l1.add(new GPoint(0, 0));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 2));
        l2.add(new GPoint(1, 0));
        assertEquals(6, l1.size());
        assertEquals(1, l2.size());
        l1.addAll(l2);
        assertEquals(7, l1.size());
        assertEquals(1, l2.size());
    }

    @Test
    @DisplayName("Add all many")
    public void testAddAll5() {
        UnsafeList l1 = new UnsafeList(10);
        UnsafeList l2 = new UnsafeList(10);
        l1.add(new GPoint(0, 0));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 1));
        l1.add(new GPoint(0, 2));
        l2.add(new GPoint(1, 0));
        l2.add(new GPoint(1, 1));
        l2.add(new GPoint(1, 2));
        l2.add(new GPoint(1, 3));
        l2.add(new GPoint(1, 4));
        assertEquals(6, l1.size());
        assertEquals(5, l2.size());
        l1.addAll(l2);
        assertEquals(11, l1.size());
        assertEquals(5,  l2.size());
        assertEquals(new GPoint(0, 0), l1.get(0));
        assertEquals(new GPoint(0, 1), l1.get(1));
        assertEquals(new GPoint(0, 1), l1.get(2));
        assertEquals(new GPoint(0, 1), l1.get(3));
        assertEquals(new GPoint(0, 1), l1.get(4));
        assertEquals(new GPoint(0, 2), l1.get(5));
        assertEquals(l2.get(0), l1.get(6));
        assertEquals(l2.get(1), l1.get(7));
        assertEquals(l2.get(2), l1.get(8));
        assertEquals(l2.get(3), l1.get(9));
        assertEquals(l2.get(4), l1.get(10));
    }

    @Test
    @DisplayName("Iterator empty")
    public void testIterator1() {
        UnsafeList list = new UnsafeList(10);
        for (GraphicE e : list) {
            fail("List not empty! " + e);
        }
    }

    @Test
    @DisplayName("Iterator elements")
    public void testIterator2() {
        UnsafeList list = new UnsafeList(10);
        for (int i = 0; i < 10; i++) {
            list.add(new GPoint(i, i));
        }
        assertEquals(10, list.size());
        int i = 0;
        for (GraphicE e : list) {
            assertEquals(new GPoint(i, i), e);
            i++;
        }
        assertEquals(10, i);
    }

}
