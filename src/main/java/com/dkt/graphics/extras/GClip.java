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
package com.dkt.graphics.extras;

import com.dkt.graphics.elements.GFillableE;
import com.dkt.graphics.elements.GraphicE;
import java.awt.Graphics2D;
import java.awt.geom.Area;
import java.util.ArrayList;

/**
 * This class wraps an {@link Area} in a {@link GraphicE}, in order to create
 * a clipping area from {@link GFillableE}.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GClip extends GraphicE {
    private final ArrayList<Action> elements = new ArrayList<>();

    private Area clip;

    /**
     * Copy constructor
     *
     * @param e {@code GClip} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GClip(GClip e){
        super(e);
        elements.ensureCapacity(e.elements.size());
        for (Action a : e.elements){
            elements.add(new Action(a.e.clone(), a.a));
        }
        updateClip();
    }

    /**
     * Basic {@code GClip} constructor
     */
    public GClip() {

    }

    /**
     * Constructs a {@code GClip} from a given area. Please note that when this
     * constructor is used, then {@link GClip#traslate(int, int)} won't work.
     * <br>If you really need to traslate a {@code GClip}, then you should
     * previously add a {@link GTransform} to the graphic
     *
     * @param area {@link Area} object to be used as clip
     * @throws IllegalArgumentException if {@code area} is {@code null}
     */
    public GClip(Area area) {
        if (area == null) {
            throw new IllegalArgumentException("The area can't be null");
        }
        clip = area;
    }

    @Override
    public void traslate(int x, int y) {
        for (Action action : elements) {
            action.e.traslate(x, y);
        }
        updateClip();
    }

    /**
     * Adds the given element to the the clipping area
     *
     * @param e element to add
     */
    public void add(GFillableE e) {
        elements.add(new Action(e, 0));
        updateClip();
    }

    /**
     * Subtracts the given element from the the clipping area
     *
     * @param e element to subtract
     */
    public void subtract(GFillableE e) {
        elements.add(new Action(e, 1));
        updateClip();
    }

    private void updateClip() {
        if (elements.isEmpty()) {
            return;
        }
        clip = new Area();
        for (Action action : elements) {
            if (action.a == 0) {
                clip.add(action.e.getShape());
            } else if (action.a == 1) {
                clip.subtract(action.e.getShape());
            }
        }
    }

    @Override
    public void draw(Graphics2D g) {
        g.setClip(clip);
    }

    @Override
    public GraphicE clone() {
        return new GClip(this);
    }

    private static class Action {
        private final int a;
        private GFillableE e;

        Action(GFillableE e, int a){
            this.e = e;
            this.a = a;
        }
    }
}
