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

import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.Graphics2D;
import java.util.Arrays;
import java.util.Comparator;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GPointArray extends GMultiPoint {
    private int cs;

    /**
     * Copy constructor
     *
     * @param e {@code GPointArray} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GPointArray(GPointArray e) {
        super(e);

        cs = e.cs;
    }

    /**
     * @param points {@link GPoint} array containing all the points
     * @throws IllegalArgumentException if the array is {@code null}
     */
    public GPointArray(final GPoint[] points) {
        this(points, 0);
    }

    /**
     * @param points {@link GPoint} array containing all the points
     * @param cs cross size
     * @throws IllegalArgumentException if the array is {@code null}
     */
    public GPointArray(final GPoint[] points, final int cs) {
        this(points.length);
        for (final GPoint p : points){
            append(new GPoint(p.x(), p.y(), cs));
        }
    }

    /**
     * Generates a new empty {@code GPointArray}
     */
    public GPointArray() {
        this(new int[0], new int[0]);
    }

    /**
     * @param initial initial reserved size
     * @throws NegativeArraySizeException if the size is less than zero
     */
    public GPointArray(final int initial) {
        super(initial);
    }

    /**
     * @param xs array containing all the x coordinates
     * @param ys array containing all the y coordinates
     * @throws IllegalArgumentException if either array is {@code null}
     * @throws InvalidArgumentException if the array size doesn't match
     */
    public GPointArray(final int[] xs, final int[] ys) {
        this(xs, ys, 0);
    }

    /**
     * @param xs array containing all the x coordinates
     * @param ys array containing all the y coordinates
     * @param cs the cross size of the point
     * @throws IllegalArgumentException if either array is {@code null}
     * @throws InvalidArgumentException if the array size doesn't match
     */
    public GPointArray(
            final int[] xs,
            final int[] ys,
            final int cs)
    {
        super(xs, ys);
        this.cs = cs;
    }

    /**
     * Changes the cross size.<br>
     * We treat points as crosses on the canvas, this method changes the size
     * of that cross.<br>
     * <i>Note:</i> The default value is 0
     *
     * @param cs new cross size
     */
    public void setCrossSize(final int cs){
        this.cs = cs;
    }

    /**
     * Retrieves a new {@code GPointArray} with all the points that are
     * contained in the circle with center {@code p} and radius {@code r}.
     *
     * @param p {@link GPoint} of the center
     * @param r radius
     * @return a new {@code GPointArray} with the points of the intersection
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public GPointArray pointsInRadius(
            final GPoint p,
            final double r)
    {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        return pointsInRadius(p.x(), p.y(), r);
    }

    /**
     * Retrieves a new {@code GPointArray} with all the points that are
     * contained in the circle with center in {@code (x, y)} and radius
     * {@code r}.
     *
     * @param x X coordinate of the center
     * @param y Y coordinate of the center
     * @param r radius
     * @return a new {@code GPointArray} with the points of the intersection
     */
    public GPointArray pointsInRadius(
            final int x,
            final int y,
            final double r)
    {
        final GPointArray array = new GPointArray();
        if (isEmpty()){
            return array;
        }

        mutex.lock();
        try{
            array.ensureCapacity(size);

            final double r2 = r * r;

            for (int i = 0; i < size; i++){
                final int xd = xs[i] - x;
                final int yd = ys[i] - y;

                final double nd = xd * xd + yd * yd;

                if (nd < r2){
                    array.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        array.trimToSize();
        return array;
    }

    /**
     * Retrieves a new {@code GPoint} representing the coordinates of the
     * closest point in this array
     *
     * @param p {@link GPoint} that will be used to compare
     * @return a new {@code GPoint} representing the closest to {@code p}
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public GPoint closestPoint(final GPoint p) {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        return closestPoint(p.x(), p.y());
    }

    /**
     * Retrieves a new {@code GPoint} representing the coordinates of the
     * closest point in this array
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return a new {@code GPoint} representing the closest to {@code (x, y)}
     * or {@code null} if none is found.
     */
    public GPoint closestPoint(final int x, final int y) {
        if (isEmpty()){
            return null;
        }

        double distance = Double.MAX_VALUE;
        int nx = 0;
        int ny = 0;

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                final double ndistance = Math.hypot(xs[i] - x, ys[i] - y);

                if (ndistance < distance){
                    distance = ndistance;
                    nx = xs[i];
                    ny = ys[i];

                    if (x == nx && y == ny){
                        break;
                    }
                }
            }
        } finally {
            mutex.unlock();
        }

        return new GPoint(nx, ny);
    }

    /**
     * Retrieves a copy of the highest point in this array at the moment
     *
     * @return highest point
     */
    public GPoint highestPoint() {
        if (isEmpty()){
            return null;
        }

        int nx = 0;
        int ny = Integer.MIN_VALUE;

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                if (ny < ys[i]){
                    nx = xs[i];
                    ny = ys[i];
                }
            }
        } finally {
            mutex.unlock();
        }

        return new GPoint(nx, ny);
    }

    /**
     * Retrieves a copy of the lowest point in this array at the moment
     *
     * @return lowest point
     */
    public GPoint lowestPoint() {
        if (isEmpty()){
            return null;
        }

        int nx = 0;
        int ny = Integer.MAX_VALUE;

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                if (ny > ys[i]){
                    nx = xs[i];
                    ny = ys[i];
                }
            }
        } finally {
            mutex.unlock();
        }

        return new GPoint(nx, ny);
    }

    /**
     * Retrieves a copy of the leftmost point in this array at the moment
     *
     * @return leftmost point or {@code null} if none is found.
     */
    public GPoint leftmostPoint() {
        if (isEmpty()){
            return null;
        }

        int nx = Integer.MAX_VALUE;
        int ny = 0;

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                if (nx > xs[i]){
                    nx = xs[i];
                    ny = ys[i];
                }
            }
        } finally {
            mutex.unlock();
        }

        return new GPoint(nx, ny);
    }

    /**
     * Retrieves a copy of the rightmost point in this array at the moment
     *
     * @return rightmost point or {@code null} if none is found.
     */
    public GPoint rightmostPoint() {
        if (isEmpty()){
            return null;
        }

        int nx = Integer.MIN_VALUE;
        int ny = 0;

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                if (nx < xs[i]){
                    nx = xs[i];
                    ny = ys[i];
                }
            }
        } finally {
            mutex.unlock();
        }

        return new GPoint(nx, ny);
    }

    /**
     * Retrieves {@link GRectangle} that contains all the points in this
     * array
     *
     * @return rectangle containing all the points
     */
    public GRectangle getBounds() {
        if (isEmpty()){
            return null;
        }

        int le = Integer.MAX_VALUE;
        int ri = Integer.MIN_VALUE;
        int up = Integer.MIN_VALUE;
        int lo = Integer.MAX_VALUE;

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                le = Math.min(le, xs[i]);
                ri = Math.max(ri, xs[i]);
                up = Math.max(up, ys[i]);
                lo = Math.min(lo, ys[i]);
            }
        } finally {
            mutex.unlock();
        }

        final int w = ri - le;
        final int h = up - lo;

        return new GRectangle(le + w / 2, lo + h / 2 + 1, w, h);
    }

    /**
     * Retrieves a new {@code GPointArray} representing all the points that
     * are above {@code p}.<br>
     * Please note that this is done considering the Y axis, graphically it
     * means the higher if and only if you are inverting the Y axis
     *
     * @param p {@link GPoint} that will be used to compare
     * @return {@code GPointArray} with all the higher points
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public GPointArray higherThan(final GPoint p) {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        GPointArray array = new GPointArray();
        if (isEmpty()){
            return array;
        }

        final int y = p.y();
        mutex.lock();
        try{
            array.ensureCapacity(size);

            for (int i = 0; i < size; i++){
                if (y < ys[i]){
                    array.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        array.trimToSize();
        return array;
    }

    /**
     * Retrieves a new {@code GPointArray} representing all the points that
     * are below {@code p}.<br>
     * Please note that this is done considering the Y axis, graphically it
     * means the lower if and only if you are inverting the Y axis
     *
     * @param p {@link GPoint} that will be used to compare
     * @return {@code GPointArray} with all the lower points
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public GPointArray lowerThan(final GPoint p) {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        GPointArray array = new GPointArray();
        if (isEmpty()){
            return array;
        }

        final int y = p.y();

        mutex.lock();
        try{
            array.ensureCapacity(size);

            for (int i = 0; i < size; i++){
                if (y > ys[i]){
                    array.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        array.trimToSize();
        return array;
    }

    /**
     * Retrieves a new {@code GPointArray} representing all the points that
     * are located to the left of {@code p}.
     *
     * @param p {@link GPoint} that will be used to compare
     * @return {@code GPointArray} with all the left points
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public GPointArray leftThan(final GPoint p) {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        GPointArray array = new GPointArray();
        if (isEmpty()){
            return array;
        }

        final int x = p.x();

        mutex.lock();
        try{
            array.ensureCapacity(size);

            for (int i = 0; i < size; i++){
                if (x > xs[i]){
                    array.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        array.trimToSize();
        return array;
    }

    /**
     * Retrieves a new {@code GPointArray} representing all the points that
     * are located to the right of {@code p}.
     *
     * @param p {@link GPoint} that will be used to compare
     * @return {@code GPointArray} with all the right points
     * @throws IllegalArgumentException if {@code p} is {@code null}
     */
    public GPointArray rightThan(final GPoint p) {
        if (p == null){
            throw new IllegalArgumentException("The point can't be null");
        }

        GPointArray array = new GPointArray();
        if (isEmpty()){
            return array;
        }

        final int x = p.x();

        mutex.lock();
        try{
            array.ensureCapacity(size);

            for (int i = 0; i < size; i++){
                if (x < xs[i]){
                    array.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        array.trimToSize();
        return array;
    }

    /**
     * Removes all the points that are contained both in this array and in
     * the array passed as an argument
     *
     * @param arr points to remove
     * @throws IllegalArgumentException if {@code array} is {@code null}
     */
    public void removeAll(final GPointArray arr) {
        if (arr == null){
            throw new IllegalArgumentException("The array can't be null");
        }

        mutex.lock();
        try {
            //@FIXME this should be done more gracefully.
            for (int i = 0; i < arr.size; i++){
                int idx = 0;

                while ((idx = indexOf(arr.xs[i], arr.ys[i], idx)) >= 0){
                    remove(idx);
                }
            }
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Appends all the point to this array, regardless if they are already
     * contained
     *
     * @param arr points to add
     * @throws IllegalArgumentException if {@code array} is {@code null}
     */
    public void append(final GPointArray arr) {
        if (arr == null){
            throw new IllegalArgumentException("The array can't be null");
        }

        mutex.lock();
        ensureCapacity(size + arr.size);

        try{
            System.arraycopy(arr.xs, 0, xs, size, arr.size);
            System.arraycopy(arr.ys, 0, ys, size, arr.size);
            size += arr.size;
            modCount.incrementAndGet();
        } finally {
            mutex.unlock();
        }
    }

    /**
     * Retrieves a new {@code GPointArray} containing all the points that are
     * both in this array and in the one passed as an argument
     *
     * @param array points to intersect
     * @return a new {@code GPointArray} with all the points of the intersection
     * @throws IllegalArgumentException if {@code array} is {@code null}
     */
    public GPointArray intersection(final GPointArray array) {
        if (array == null){
            throw new IllegalArgumentException("The array can't be null");
        }

        //@FIXME this should be done more gracefully.
        final GPointArray arr = new GPointArray();
        arr.ensureCapacity(size + array.size);

        mutex.lock();
        try{
            for (int i = 0; i < array.size; i++){
                if (indexOf(array.xs[i], array.ys[i]) >= 0){
                    arr.append(array.xs[i], array.ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        arr.trimToSize();
        return arr;
    }

    /**
     * Retrieves a new {@code GPointArray} containing all the points that are
     * contained both in this array and in the {@link GRectangle} passed as an
     * argument
     *
     * @param r rectangle to intersect
     * @return a new {@code GPointArray} with all the points of the intersection
     * @throws IllegalArgumentException if {@code r} is {@code null}
     */
    public GPointArray intersection(final GRectangle r) {
        if (r == null){
            throw new IllegalArgumentException("The rectangle can't be null");
        }

        final GPointArray arr = new GPointArray();
        arr.ensureCapacity(size);

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                if (r.contains(xs[i], ys[i])){
                    arr.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        arr.trimToSize();
        return arr;
    }

    /**
     * Retrieves a new {@code GPointArray} containing all the points that are
     * contained both in this array and in the {@link GLine} passed as an
     * argument
     *
     * @param l the line to intersect
     * @return a new {@code GPointArray} with all the points of the intersection
     * @throws IllegalArgumentException if {@code l} is {@code null}
     */
    public GPointArray intersection(final GLine l) {
        if (l == null){
            throw new IllegalArgumentException("The line can't be null");
        }

        final GPointArray arr = new GPointArray();
        arr.ensureCapacity(size);

        mutex.lock();
        try{
            for (int i = 0; i < size; i++){
                if (l.contains(xs[i], ys[i])){
                    arr.append(xs[i], ys[i]);
                }
            }
        } finally {
            mutex.unlock();
        }

        arr.trimToSize();
        return arr;
    }

    /**
     * Retrieves a new {@code GPointArray} containing all the points that are
     * contained both in this array and in the {@link GCircle} passed as an
     * argument
     *
     * @param c the circle to intersect
     * @return a new {@code GPointArray} with all the points of the intersection
     * @throws IllegalArgumentException if {@code c} is {@code null}
     */
    public GPointArray intersection(final GCircle c) {
        if (c == null){
            throw new IllegalArgumentException("The circle can't be null");
        }

        return pointsInRadius(c.x(), c.y(), c.getRadius());
    }

    /**
     * Sorts all the points in this array by it's {@code X} value, and breaks
     * ties with the {@code Y} value
     */
    @Override
    public void sortByX() {
        sort((n1, n2) -> {
            final int status = Integer.compare(xs[n1], xs[n2]);
            return status == 0 ? Integer.compare(ys[n1], ys[n2]) : status;
        });
    }

    /**
     * Sorts all the points in this array by it's {@code Y} value, and breaks
     * ties with the {@code X} value
     */
    public void sortByY() {
        sort((n1, n2) -> {
            final int status = Integer.compare(ys[n1], ys[n2]);
            return status == 0 ? Integer.compare(xs[n1], xs[n2]) : status;
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

    /**
     * Removes <b>ALL</b> duplicate entries in the array.
     */
    public void removeDuplicates() {
        //I guess this can be done more efficiently... but I have no idea how...
        final Integer[] idxs = new Integer[size];
        for( int i = 0 ; i < size; i++ ) {
            idxs[i] = i;
        }

        Arrays.sort(idxs, (n1, n2) -> Integer.compare(xs[n1], xs[n2]));

        final GPointArray parr = new GPointArray();

        mutex.lock();
        try {
            //Worst case scenario is that the array is full of duplicates
            //perhaps this should be changed if size is very big since we'll
            //need a lot of memory
            parr.ensureCapacity(size);

            for (int i = 0; i < size; i++){
                final int ii = idxs[i];
                final int xi = xs[ii];
                final int yi = ys[ii];

                for (int j = i + 1; j < size && xi == xs[idxs[j]]; j++){
                    final int ij = idxs[j];

                    if (xi == xs[ij] & yi == ys[ij]){
                        parr.append(xs[ij], ys[ij]);
                    }
                }
            }
        } finally {
            mutex.unlock();
        }

        removeAll(parr);
    }

    @Override
    public void draw(Graphics2D g) {
        g.setPaint(getPaint());

        mutex.lock();
        try{
            if (cs != 0) {
                g.setStroke(getStroke());
                for (int i = 0; i < size; i++){
                    final int x = xs[i];
                    final int y = ys[i];

                    g.drawLine(x - cs, y     , x + cs, y     );
                    g.drawLine(x     , y - cs, x     , y + cs);
                }
            } else {
                for (int i = 0; i < size; i++){
                    g.drawRect(xs[i], ys[i], 0, 0);
                }
            }
        } finally {
            mutex.unlock();
        }
    }

    @Override
    public GPointArray clone() {
        return new GPointArray(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 73 * hash + cs;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GPointArray other = (GPointArray) obj;
        return this.cs == other.cs;
    }

}
