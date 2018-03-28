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

import java.util.Random;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Disabled;
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
			UnsafeList list = new UnsafeList(-1);
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
		list.clear();
		assertTrue(list.isEmpty());
		assertEquals(0, list.size());
	}

	@Test
	@DisplayName("IndexOf empty")
	public void testIndexOf1() {
		UnsafeList list = new UnsafeList(10);
		int res = list.indexOf(new GPoint(0,0));
		assertEquals(-1, res);
	}

	@Test
	@DisplayName("IndexOf 1")
	public void testIndexOf2() {
		UnsafeList list = new UnsafeList(10);
		list.add(new GPoint(0,0));
		int res = list.indexOf(new GPoint(0,0));
		assertEquals(0, res);
	}

	@Test
	@DisplayName("IndexOf repeated element")
	public void testIndexOf3() {
		UnsafeList list = new UnsafeList(10);
		list.add(new GPoint(0,0));
		list.add(new GPoint(0,0));
		int res = list.indexOf(new GPoint(0,0));
		assertEquals(0, res);
	}



}