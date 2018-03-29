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
import java.awt.Polygon;
import java.awt.geom.Area;
import java.util.Arrays;
import java.util.Comparator;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Represents an abstract array of (x, y) coordinates.<br>
 * <i>Note:</i> even though this class extends from {@link GFillableE} it's the
 * subclass option to enforce the fill methods
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public abstract class GMultiPoint extends GFillableE
                               implements Iterable<GPoint> {
    protected final AtomicInteger modCount = new AtomicInteger(0);
    protected final Lock mutex = new ReentrantLock();
    protected int size;
    protected int[] xs;
    protected int[] ys;

    /**
     * Copy constructor
     *
     * @param e {@code GMultiPoint} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    protected GMultiPoint(GMultiPoint e) {
        super(e);

        size = e.size;
        xs   = new int[e.xs.length];
        ys   = new int[e.ys.length];

        System.arraycopy(e.xs, 0, xs, 0, size);
        System.arraycopy(e.ys, 0, ys, 0, size);
    }

    /**
     * @param initial initial reserved size
     * @throws NegativeArraySizeException if the size is less than zero
     */
    protected GMultiPoint(final int initial) {
        this(new int[initial], new int[initial]);
        size = 0;
    }

    /**
     * @param xs list of X coordinates
     * @param ys list of Y coordinates
     * @throws IllegalArgumentException if either array is {@code null} or if
     * the array size doesn't match
     */
    protected GMultiPoint(final int[] xs, final int[] ys) {
        if (xs == null || ys == null){
            throw new IllegalArgumentException("Neither array can be null");
        }

        if (xs.length != ys.length){
            String msg = "Both arrays MUST have the same size";
            throw new InvalidArgumentException(msg);
        }

        this.size = xs.length;
        this.xs = new int[size];
        this.ys = new int[size];

        System.arraycopy(xs, 0, this.xs, 0, size);
        System.arraycopy(ys, 0, this.ys, 0, size);
    }

    /**
     * Retrieves the number of vertices
     *
     * @return number of vertices
     */
    public int size() {
        mutex.lock();
        try{
            return size;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Retrieves the index of the first occurrence of this point
     *
     * @param point point to check
     * @return first index of the point or -1 if the point is not one of the
     * paths 's vertices
     * @throws IllegalArgumentException if {@code point} is {@code null}
     */
    public int indexOf(final GPoint point) {
        if (point == null){
            throw new IllegalArgumentException("Point can't be null");
        }

        return indexOf(point.x(), point.y(), 0);
    }

    /**
     * Retrieves the index of the first occurrence of this point after the
     * starting point
     *
     * @param point point to check
     * @param start starting point of the search
     * @return first index of the point or -1 if the point is not one of the
     * paths 's vertices
     * @throws IllegalArgumentException if {@code point} is {@code null}
     */
    public int indexOf(final GPoint point, final int start) {
        if (point == null){
            throw new IllegalArgumentException("Point can't be null");
        }

        return indexOf(point.x(), point.y(), start);
    }

    /**
     * Retrieves the index of the first occurrence of this point
     *
     * @param x y coordinate of the point
     * @param y y coordinate of the point
     * @return first index of the point or -1 if the point is not one of the
     * paths 's vertices
     */
    public int indexOf(final int x, final int y) {
        return indexOf(x, y, 0);
    }

    /**
     * Retrieves the index of the first occurrence of this point after the
     * starting point
     *
     * @param x y coordinate of the point
     * @param y y coordinate of the point
     * @param start starting point of the search
     * @return first index of the point or -1 if the point is not one of the
     * paths 's vertices
     */
    public int indexOf(
            final int x,
            final int y,
            final int start)
    {
        mutex.lock();
        try{
            for (int i = start, n = size; i < n; i++){
                if (xs[i] == x & ys[i] == y){
                    return i;
                }
            }

            return -1;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Clears the object
     */
    public void clear() {
        size = 0;
    }

    /**
     * Removes the first occurrence of this point in the array
     *
     * @param point the point to check
     * @return {@code true} if the point was contained and {@code false}
     * otherwise
     * @throws IllegalArgumentException if {@code point} is {@code null}
     */
    public boolean remove(final GPoint point) {
        if (point == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        return remove(point.x(), point.y());
    }

    /**
     * Removes the element at the specified index
     *
     * @param idx Index of the element
     * @return {@code true} if the point was contained and {@code false}
     * otherwise
     * @throws IndexOutOfBoundsException if {@code idx < 0 | idx >= size}
     */
    public boolean remove(final int idx) {
        if (idx < 0 || idx >= size){
            String msg = "The index must be contained in [0, %d) current '%d'";
            msg = String.format(msg, size, idx);
            throw new IndexOutOfBoundsException(msg);
        }

        mutex.lock();
        try {
            final int nm = size - idx - 1;

            System.arraycopy(xs, idx + 1, xs, idx, nm);
            System.arraycopy(ys, idx + 1, ys, idx, nm);

            size--;

            modCount.incrementAndGet();

            return true;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Removes the first occurrence of this point in the array
     *
     * @param x x coordinate of the point
     * @param y y coordinate of the point
     * @return {@code true} if the point was contained and {@code false}
     * otherwise
     */
    public boolean remove(final int x, final int y) {
        mutex.lock();
        try{
            final int idx = indexOf(x, y);

            if (idx < 0){
                return false;
            }

            modCount.incrementAndGet();
            return remove(idx);
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Retrieves a given {@link GPoint} from the path
     *
     * @param idx the index of the point to retrieve
     * @return {@link GPoint} at the specified position
     * @throws ArrayIndexOutOfBoundsException if
     * {@code (idx < 0 | idx > numberOfVertices)}
     */
    public GPoint getPointAt(final int idx) {
        if (idx < 0 || idx >= size){
            throw new ArrayIndexOutOfBoundsException(idx);
        }

        return new GPoint(xs[idx], ys[idx]);
    }

    /**
     * Retrieves a COPY of the {@link GPoint}'s in the path
     *
     * @return array of points
     */
    public GPoint[] getPoints() {
        mutex.lock();
        try{
            final GPoint[] points = new GPoint[size];

            for (int i = 0, n = size; i < n; i++){
                points[i] = new GPoint(xs[i], ys[i]);
            }

            return points;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Appends a new point to the path. If the path has run out of space then
     * it will call {@code ensureCapacity(size + 5)}
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @see GMultiPoint#ensureCapacity(int)
     */
    public void append(final int x, final int y) {
        mutex.lock();
        if (size == xs.length){
            ensureCapacity(size + 5);
        }

        try{
            xs[size] = x;
            ys[size] = y;

            size++;

            modCount.incrementAndGet();
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Appends a new point to the path if and only if this point doesn't exist
     * within the array. If the path has run out of space then
     * it will call {@code ensureCapacity(size + 5)}
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @see GMultiPoint#ensureCapacity(int)
     */
    public void appendNR(final int x, final int y) {
        if (indexOf(x, y) >= 0){
            return;
        }

        mutex.lock();
        if (size == xs.length){
            ensureCapacity(size + 5);
        }

        try{
            xs[size] = x;
            ys[size] = y;

            size++;

            modCount.incrementAndGet();
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Appends a new point to the path
     *
     * @param p the {@link GPoint} to append
     * @throws IllegalArgumentException if {@code p} is {@code null}
     * @see GMultiPoint#ensureCapacity(int)
     */
    public void append(final GPoint p) {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        append(p.x(), p.y());
    }

    /**
     * Allocates enough space for the specified number of elements
     *
     * @param nElements the capacity the array must be able to hold
     * @throws InvalidArgumentException if {@code nElements} is less than 0
     */
    public void ensureCapacity(final int nElements) {
        if (nElements < 0){
            throw new InvalidArgumentException("The size must be positive");
        }
        if (nElements < xs.length){
            return;
        }

        mutex.lock();
        try{
            final int[] fx = new int[nElements];
            final int[] fy = new int[nElements];

            System.arraycopy(xs, 0, fx, 0, size);
            System.arraycopy(ys, 0, fy, 0, size);

            xs = fx;
            ys = fy;
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Tells if the object doesn't contain any points
     *
     * @return {@code true} if the object is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Removes all the extra space used by the object
     */
    public void trimToSize() {
        mutex.lock();
        try{
            if (size != xs.length){
                final int[] fx = new int[size];
                final int[] fy = new int[size];

                System.arraycopy(xs, 0, fx, 0, size);
                System.arraycopy(ys, 0, fy, 0, size);

                xs = fx;
                ys = fy;
            }
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public void traslate(final int x, final int y) {
        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                xs[i] += x;
                ys[i] += y;
            }

            modCount.incrementAndGet();
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Sorts all the points in this array by it's {@code X} value, and breaks
     * ties with the {@code Y} value
     */
    public void sortByX() {
        sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer n1, Integer n2) {
                final int status = Integer.compare(xs[n1], xs[n2]);
                return status == 0 ? Integer.compare(ys[n1], ys[n2]) : status;
            }
        });
    }
 
    private void sort(final Comparator<Integer> comparator) {
        final Integer[] idxs = new Integer[size];
        for( int i = 0 ; i < size; i++ ) {
            idxs[i] = i;
        }

        mutex.lock();
        try{
            Arrays.sort(idxs, comparator);

            //@FIXME this can be done without generating a new array
            final int[] xxs = new int[xs.length];
            final int[] yys = new int[xs.length];

            for (int i = 0; i < size; i++){
                xxs[i] = xs[idxs[i]];
                yys[i] = ys[idxs[i]];
            }

            xs = xxs;
            ys = yys;
        } finally {
            mutex.unlock();
        }

        modCount.incrementAndGet();
    }

    @Override
    public Iterator<GPoint> iterator() {
        return new Iterator<GPoint>() {
            private final int modifications = modCount.get();

            //The cursor must be BEFORE the first item
            private int idx = -1;
            @Override
            public boolean hasNext() {
                if (modifications != modCount.get()){
                    throw new ConcurrentModificationException();
                }

                return idx < size - 1;
            }

            @Override
            public GPoint next() {
                if (idx >= size - 1){
                    throw new NoSuchElementException();
                }

                if (modifications != modCount.get()){
                    throw new ConcurrentModificationException();
                }

                return getPointAt(++idx);
            }

            @Override
            public void remove() {
                //Unless this is the last item on the loop, this will always
                //end in a ConcurrentModificationException
                //Should we change modifications to modCount?
                if (modifications != modCount.get()){
                    throw new ConcurrentModificationException();
                }

                GMultiPoint.this.remove(getPointAt(idx));
            };
        };
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 53 * hash + size;
        hash = 53 * hash + Arrays.hashCode(xs);
        hash = 53 * hash + Arrays.hashCode(ys);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GMultiPoint other = (GMultiPoint) obj;
        if (size != other.size) {
            return false;
        }
        if (!Arrays.equals(xs, other.xs)) {
            return false;
        }

        return Arrays.equals(ys, other.ys);
    }

    @Override
    public Area getShape() {
        return new Area(new Polygon(xs, ys, size()));
    }
}
