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

import java.util.Arrays;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class creates an unsafe list, actually the only thing this list is
 * unsafe against is {@code null} values and the {@link Iterator} doesn't check
 * anything. But at least it's thread safe.<br>
 * <i>Note:</i> This class does not implement the {@link java.util.List}
 * interface.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
class UnsafeList implements Iterable<GraphicE> {
    private final Object mutex = new Object();
    private GraphicE[] elements;
    private int cursor;

    /**
     * Constructs a new {@code UnsafeList} of a given size
     *
     * @param size initial capacity of the list
     */
    public UnsafeList(int size) {
        elements = new GraphicE[size];
    }

    /**
     * Adds a new element to the list.<br>
     * If the list is full, it's internal size will be incremented by 10%
     *
     * @param elm {@link GraphicE} to add
     * @return {@code true} if the element was added, and {@code false} if the
     * element was omitted (it will be omitted if it's {@code null})
     */
    public boolean add(GraphicE elm) {
        if (elm == null) {
            return false;
        }

        synchronized(mutex) {
            if (cursor == elements.length) {
                final GraphicE[] foo = new GraphicE[(cursor + 1) * 110 / 100];
                System.arraycopy(elements, 0, foo, 0, elements.length);
                elements = foo;
            }

            elements[cursor++] = elm;

            return true;
        }
    }

    /**
     * Reserves more size for elements. This value will be omitted if it's less
     * than the current capacity.
     *
     * @param capacity new capacity
     */
    public void ensureCapacity(int capacity) {
        synchronized(mutex) {
            if (capacity > elements.length) {
                final GraphicE[] foo = new GraphicE[capacity];
                System.arraycopy(elements, 0, foo, 0, elements.length);
                elements = foo;
            }
        }
    }

    /**
     * Retrieves an element from a given index, it will return {@code null} if
     * the index doesn't exist
     *
     * @param idx Element index
     * @return element at a given index or {@code null} otherwise
     */
    public GraphicE get(int idx) {
        synchronized(mutex) {
            try {
                return elements[idx];
            } catch (Exception e){
                return null;
            }
        }
    }

    /**
     * Retrieves the current number of elements of the list
     *
     * @return number of elements
     */
    public int size() {
        return cursor;
    }

    /**
     * Tells if the list is empty
     *
     * @return {@code true} if the list is empty and {@code false} otherwise
     */
    public boolean isEmpty() {
        return cursor == 0;
    }

    /**
     * Clears the list
     */
    public void clear() {
        synchronized(mutex) {
            Arrays.fill(elements, null);
            cursor = 0;
        }
    }

    /**
     * Retrieves the index of the element in the list
     *
     * @param elm element to check
     * @return index of the element or {@code -1} if the element wasn't found
     */
    public int indexOf(GraphicE elm) {
        synchronized(mutex) {
            for (int i = 0; i < cursor; i++) {
                if (Objects.equals(elements[i], elm)) {
                    return i;
                }
                i++;
            }

            return -1;
        }
    }

    /**
     * Removes an element form a given index, if the index doesn't exist then
     * it does nothing.
     *
     * @param idx index of the element to remove
     * @return {@code GraphicE} that was removed, and {@code null} in case
     * nothing was found
     */
    public GraphicE remove(int idx) {
        synchronized(mutex) {
            final GraphicE elm = get(idx);

            if (elm == null) {
                return null;
            }

            final int numMoved = cursor - idx - 1;

            if (numMoved > 0) {
                System.arraycopy(elements, idx + 1, elements, idx, numMoved);
            }

            elements[--cursor] = null;

            return elm;
        }
    }

    /**
     * Removes all the instances of a {@code GraphicE} from the list
     *
     * @param e element to remove
     * @return {@code true} if an element was removed and {@code false} if the
     * element wasn't found
     */
    public boolean remove(GraphicE e) {
        synchronized(mutex) {
            return remove(indexOf(e)) != null;
        }
    }

    /**
     * Tells if a {@code GraphicE} is contained in the list
     *
     * @param e element to check
     * @return {@code true} if the element is contained in the list and {@code
     * false} otherwise
     */
    public boolean contains(GraphicE e) {
        synchronized(mutex) {
            return indexOf(e) != -1;
        }
    }

    /**
     * Adds all the elements in a list to this one
     *
     * @param elmts {@code UnsafeList} to add
     */
    public void addAll(UnsafeList elmts) {
        if (elmts == null || this == elmts) {
            return;
        }

        synchronized(mutex) {
            ensureCapacity(elmts.size() + this.size());
            System.arraycopy(elmts.elements, 0, elements, cursor, elmts.size());
            cursor += elmts.size();
        }
    }

    @Override
    public Iterator<GraphicE> iterator() {
        return new Iterator<GraphicE>() {
            private int idx = -1;
            @Override
            public boolean hasNext() {
                return idx != cursor - 1;
            }

            @Override
            public GraphicE next() {
                try {
                    return elements[++idx];
                } catch (Exception e){
                    return null;
                }
            }

            @Override
            public void remove() {
                //We don't need this in our current implementation
            }
        };
    }

}
