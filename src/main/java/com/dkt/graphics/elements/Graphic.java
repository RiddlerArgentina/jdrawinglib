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
import java.awt.Graphics2D;
import java.awt.Shape;
import java.awt.geom.AffineTransform;
import java.util.Iterator;
import java.util.Objects;

/**
 * This class represents a Graphic made by a collection of {@link GraphicE}
 * components.<br>
 * <i>Note:</i> this class is a {@link GraphicE} on itself, so you can add it to
 * Graphics.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Graphic extends GraphicE implements Iterable<GraphicE> {
    private final UnsafeList components;
    private boolean visible = true;
    private int xOff;
    private int yOff;

    public Graphic() {
        this(20);
    }

    public Graphic(int initialSize) {
        components = new UnsafeList(initialSize);
    }

    /**
     * Copy constructor
     *
     * @param e {@code Graphic} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public Graphic(Graphic e) {
        super(e);

        xOff = e.xOff;
        yOff = e.yOff;

        components = new UnsafeList(e.components.size());
        synchronized (e.components){
            for (final GraphicE ge : e.components){
                components.add(ge.clone());
            }
        }
    }

    /**
     * Retrieves the number of {@link GraphicE} components that compose this
     * {@code Graphic}. This method will not count recursively.
     *
     * @return component count
     */
    public int getCount() {
        return components.size();
    }

    /**
     * Adds a new {@link GraphicE} to this Graphic<br>
     * <i>Note:</i> all the elements that are added will be automatically
     * traslated the same amount as the sum of the traslations up to this
     * moment
     * <i>Note 2:</i>This method doesn't check if the element is already
     * contained on the {@code Graphic}, this mean, that you can add elements
     * twice, the <i>downside</i> (or amazing feature, according to a couple of
     * comments) is that the elements that are added twice will be traslated
     * twice as much as the other elements.
     *
     * @param e element to add
     * @throws IllegalArgumentException if {@code e} is {@code null}
     * @throws InvalidArgumentException if {@code e} is this same object
     * @see Graphic#traslate(int, int)
     */
    public void add(final GraphicE e) {
        if (e == null){
            throw new IllegalArgumentException("The element can't be null");
        }
        if (e == this){
            final String msg = "Graphics can't be added to themselves";
            throw new InvalidArgumentException(msg);
        }

        synchronized (components){
            components.add(e);
            e.traslate(xOff, yOff);
        }
    }

    /**
     * Tells if a given {@link GraphicE} is contained on this {@link Graphic}.
     *
     * @param e element to test
     * @return {@code true} if the element is contained and {@code false}
     * otherwise
     * @throws IllegalArgumentException if {@code e} is {@code null}
     * @see Graphic#flatten()
     */
    public boolean contains(final GraphicE e) {
        if (e == null){
            throw new IllegalArgumentException("The element can't be null");
        }

        synchronized (components){
            return components.contains(e);
        }
    }

    /**
     * Removes a given {@link GraphicE} from this {@link Graphic}
     *
     * @param e element to remove
     * @return {@code true} if the element was contained and {@code false}
     * otherwise
     * @throws IllegalArgumentException if {@code e} is {@code null}
     * @see Graphic#flatten()
     */
    public boolean remove(final GraphicE e) {
        if (e == null){
            throw new IllegalArgumentException("The element can't be null");
        }

        synchronized (components){
            return components.remove(e);
        }
    }

    /**
     * Retrieves the index of a given element on the {@code Graphic}.
     *
     * @param e element
     * @return The index number of the first occurrence of the element, or
     * {@code -1} if the element wasn't found
     * @throws IllegalArgumentException if {@code e} is {@code null}
     * @see Graphic#flatten()
     */
    public int indexOf(final GraphicE e) {
        if (e == null){
            throw new IllegalArgumentException("The element can't be null");
        }

        synchronized (components){
            return components.indexOf(e);
        }
    }

    /**
     * Clears all the components from this {@link Graphic}
     */
    public void removeAll() {
        synchronized (components){
            components.clear();
        }
    }

    @Override
    public void draw(final Graphics2D g) {
        if (!isVisible()) {
            return;
        }

        final AffineTransform at = g.getTransform();
        final Shape clip = g.getClip();

        synchronized (components){
            for (final GraphicE e : components){
                e.draw(g);
            }
        }

        g.setClip(clip);
        g.setTransform(at);
    }

    @Override
    public void traslate(final int x, final int y) {
        synchronized (components){
            for (final GraphicE e : components){
                e.traslate(x, y);
            }

            xOff += x;
        }
    }

    /**
     * Moves all the elements to a given location.<br>
     * This method tends to ruin graphics... use it with care.
     *
     * @param x new X coordinate
     * @param y new Y coordinate
     */
    public void move(final int x, final int y) {
        synchronized (components){
            for (final GraphicE e : components){
                e.traslate(-xOff, -yOff);
                e.traslate(x, y);
            }

            xOff = x;
            yOff = y;
        }
    }

    /**
     * This method adds all of the elements of a given set of {@code Graphic}
     * components into the current element.<br>
     *
     * @param graphics array of graphics to merge
     * @see Graphic#flatten()
     */
    public void addAll(final Graphic... graphics) {
        synchronized (components){
            for (final Graphic graph : graphics){
                components.addAll(graph.components);
            }
        }
    }

    /**
     * This method adds all of the elements of a given set of {@code Graphic}
     * components into the current element.<br>
     *
     * @param graphics array of graphics to merge
     * @see Graphic#flatten()
     */
    public void addCopyOfAll(final Graphic... graphics) {
        synchronized (components){
            for (final Graphic graph : graphics){
                components.addAll(graph.clone().components);
            }
        }
    }

    /**
     * Tells if {@code Graphic} will be drawn in the canvas
     *
     * @return {@code true} if the {@code Graphic} is visible and {@code flase}
     * otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     *
     * @param v {@code true} if the {@code Graphic} will visible and
     * {@code flase} otherwise
     */
    public void setVisible(boolean v) {
        visible = v;
    }

    /**
     * Flattens the structure of a Graphic, this method is very slow, but it will
     * increase the performance of draw(). <br>
     *
     * This method is specially useful when you use the GraphicCreator, or when
     * combining several {@code Graphic}. Consider trying to search for a specific
     * element when having multiple {@code Graphic} objects... basically, you'll
     * never be able to find them if you need to enter each and every
     * {@code Graphic} object to check.
     */
    public void flatten() {
        synchronized (components){
            Graphic foo = new Graphic(this.getCount());
            flatten(this, foo);
            removeAll();
            components.addAll(foo.components);
            foo.removeAll();
        }
    }

    private void flatten(Graphic src, Graphic dest) {
        synchronized (src.components){
            for (GraphicE component : src.components) {
                if (component instanceof Graphic) {
                    flatten((Graphic)component, dest);
                    ((Graphic)component).removeAll();
                } else {
                    dest.add(component);
                }
            }
        }
    }

    @Override
    public Iterator<GraphicE> iterator() {
        return components.iterator();
    }

    @Override
    public Graphic clone() {
        return new Graphic(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        for (int i = 0; i < components.size(); i++) {
            hash = 47 * hash + components.get(i).hashCode();
        }
        hash = 47 * hash + xOff;
        hash = 47 * hash + yOff;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final Graphic other = (Graphic) obj;

        for (int i = 0; i < components.size(); i++) {
            if (!Objects.equals(components.get(i), other.components.get(i))) {
                return false;
            }
        }

        return !(
            xOff != other.xOff |
            yOff != other.yOff
        );
    }

}
