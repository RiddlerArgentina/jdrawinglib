/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2016 <dktcoding [at] gmail>
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

import com.dkt.graphics.elements.GRectangle;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.Graphics2D;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * Creates a simple sprite, if you need help creating the pixmaps, you should
 * probably try out PixmapCreator
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GSprite extends GraphicE implements Iterable<GPixMap> {
    private final ArrayList<GPixMap> frames = new ArrayList<>(10);
    private GPixMap current;
    private GRectangle bounds;
    private boolean cyclic = true;
    private int cursor = 0;
    private int skips  = 1;
    private int skipCount = 0;
    private int pixelSize = 1;
    private boolean drawGrid;
    private boolean visible = true;

    public GSprite(GSprite e) {
        super(e);
    }

    /**
     * Creates an empty sprite
     */
    public GSprite() {

    }

    /**
     * Append a new {@link GPixMap} to this {@code GSprite}, this method
     * will skip all {@code GPixMap} that are already in the {@code GSprite}
     *
     * @param map {@code GPixMap} to add
     * @throws IllegalArgumentException if {@code map} is {@code null}
     * @throws InvalidArgumentException if the {@code map} doesn't have
     * the same size.
     */
    public void append(GPixMap map) throws IllegalArgumentException,
                                           InvalidArgumentException {
        if (map == null) {
            throw new IllegalArgumentException("The pixmap can't be null");
        }

        for (final GPixMap frame : frames) {
            if (map.getXSize() != frame.getXSize() |
                map.getYSize() != frame.getYSize()) {
                final String msg = "The pixmaps must have the same size";
                throw new InvalidArgumentException(msg);
            }
        }

        map.setPixelSize(pixelSize);
        map.setDrawLines(drawGrid);
        map.setVisible(visible);

        if (frames.isEmpty()) {
            current = map;
            bounds  = map.getBounds();
        }

        frames.add(map.clone());
    }

    /**
     * Removes a given {@code map} from the sprite. This method is most
     * likely to fail, since {@link GPixMap#equals(java.lang.Object)} relies on
     * the pixel size and the grid, and those attributes are mostly certainly
     * changed by the constructor.
     *
     * @param map Map to remove
     * @return {@code true} if the element was found and removed and
     * {@code false} otherwise
     * @throws IllegalArgumentException if {@code map} is {@code null}
     */
    public boolean remove(GPixMap map) throws IllegalArgumentException {
        if (map == null) {
            throw new IllegalArgumentException("The pixmap can't be null");
        }

        final boolean res = frames.remove(map);

        if (res && frames.isEmpty()) {
            current = null;
            bounds  = null;
        }

        if (current == map) {
            current = frames.get(0);
            bounds  = current.getBounds();
        }

        return res;
    }

    /**
     * Retrieves the current cursor position
     *
     * @return cursor position
     */
    public int getCursor() {
        return cursor;
    }

    /**
     * Sets the first {@link GPixMap} as the current one.
     */
    public void first() {
        if (!frames.isEmpty()) {
            current = frames.get(0);
            bounds  = current.getBounds();
            skipCount = 0;
        }
    }

    /**
     * Passes to the previous {@link GPixMap} in the current sprite.<br>
     * This method depends on the number of skips set, that is: if skips is 3,
     * then this method must be called 3 times in order to make an actual
     * change, apart from it, it also depends on {@link GSprite#isCyclic()}.
     *
     * @return {@code true} the pixmap change and {@code false} otherwise.
     * @see GSprite#setSkips(int)
     * @see GSprite#skips()
     * @see GSprite#isCyclic()
     */
    public boolean prev() {
        if (!isCyclic() && cursor - 1 < 0) {
            return false;
        }

        if (++skipCount % skips == 0) {
            if (--cursor < 0 && isCyclic()) {
                cursor = frames.size() - 1;
            }

            current = frames.get(cursor);
            bounds  = current.getBounds();

            skipCount = 0;

            return true;
        }

        return false;
    }

    /**
     * Passes to the next {@link GPixMap} in the current sprite.<br>
     * This method depends on the number of skips set, that is: if skips is 3,
     * then this method must be called 3 times in order to make an actual
     * change, apart from it, it also depends on {@link GSprite#isCyclic()}.
     *
     * @return {@code true} the pixmap change and {@code false} otherwise.
     * @see GSprite#setSkips(int)
     * @see GSprite#skips()
     * @see GSprite#isCyclic()
     */
    public boolean next() {
        if (!isCyclic() && cursor + 1 >= frames.size()) {
            return false;
        }

        if (++skipCount % skips == 0) {
            if (++cursor >= frames.size() && isCyclic()) {
                cursor = 0;
            }

            current = frames.get(cursor);
            bounds  = current.getBounds();

            skipCount = 0;

            return true;
        }

        return false;
    }

    /**
     * Sets the last {@link GPixMap} as the current one.
     */
    public void last() {
        if (!frames.isEmpty()) {
            current = frames.get(frames.size() - 1);
            bounds  = current.getBounds();
            skipCount = 0;
        }
    }

    /**
     * Tells if the {@code GSprite} should behave as a circular list.<br>
     * The default value is {@code true}.
     *
     * @return {@code true} if the {@code GSprite} is circular, and
     * {@code false} otherwise.
     * @see GSprite#setCyclic(boolean)
     */
    public boolean isCyclic() {
        return cyclic;
    }

    /**
     * Tells the {@code GSprite} to act as if the list of elements is circular
     * (cyclic), that means that when the last {@link GPixMap} is reached,
     * then it will continue with the first one, and viceversa.<br>
     * The default value is {@code true}.
     *
     * @param cyclic {@code true} if the {@code GSprite} should act as circular,
     * and {@code false} otherwise.
     * @see GSprite#isCyclic()
     */
    public void setCyclic(boolean cyclic) {
        this.cyclic = cyclic;
    }

    /**
     * Sets the number of skips, this means how many times should the
     * {@link GSprite#next()} and {@link GSprite#prev()} should be called
     * in order to actually work<br>
     * The default value is 1
     *
     * @param s new number of skips
     * @throws InvalidArgumentException if {@code s} is less than 1
     */
    public void setSkips(int s) throws InvalidArgumentException {
        if (s < 1) {
            final String msg = "The number of skips must be bigger than zero";
            throw new InvalidArgumentException(msg);
        }

        skips = s;
    }

    /**
     * Retrieves the number of skips that are necessary in order for the
     * {@link GSprite#next()} and {@link GSprite#prev()} methods to make an
     * actual change.
     *
     * @return number of skips
     */
    public int skips() {
        return skips;
    }

    /**
     * Sets a new pixel size for all of the {@link GPixMap} of this {@code
     * GSprite}
     *
     * @param px new pixel size
     * @throws InvalidArgumentException if the {@code size <= 0}
     */
    public void setPixelSize(int px) throws InvalidArgumentException {
        if (px < 1) {
            final String msg = "The pixel size must be bigger than 1";
            throw new InvalidArgumentException(msg);
        }

        pixelSize = px;

        for (final GPixMap frame : frames) {
            frame.setPixelSize(pixelSize);
        }

        if (current != null) {
            bounds = current.getBounds();
        }
    }

    /**
     * Retrieves the pixel size of the {@code GSprite}
     *
     * @return pixel size
     */
    public int getPixelSize() {
        return pixelSize;
    }

    /**
     * Tells the {@code GSprite} to draw the inner/outer lines
     *
     * @param grid {@code true} to draw the lines and {@code false} otherwise
     */
    public void setDrawGrid(boolean grid) {
        drawGrid = grid;

        for (final GPixMap map : frames) {
            map.setDrawLines(drawGrid);
        }
    }

    /**
     * Tells if the {@code GSprite} will draw the inner lines or not
     *
     * @return {@code true} if the {@code GSprite} is drawing the inner lines
     * and {@code false} otherwise
     */
    public boolean drawGrid() {
        return drawGrid;
    }

    /**
     * Retrieves the current bounds of this {@code GSprite}
     *
     * @return bounds of this GSprite
     */
    public GRectangle getBounds() {
        return bounds;
    }

    /**
     * Tells the {@code GSprite} if it should draw itself
     *
     * @param visible {@code true} if the {@code GSprite} should be drawn and
     * {@code false} otherwise
     */
    public void setVisible (boolean visible) {
        this.visible = visible;

        for (final GPixMap frame : frames) {
            frame.setVisible(visible);
        }
    }

    /**
     * Tells if the {@code GSprite} is visible
     *
     * @return {@code true} if the {@code GSprite} is visible and {@code false}
     * otherwise
     */
    public boolean isVisible() {
        return visible;
    }

    @Override
    public void draw(Graphics2D g) {
        if (current != null) {
            current.draw(g);
        }
    }

    @Override
    public GSprite clone() {
        return new GSprite(this);
    }

    @Override
    public void traslate(int x, int y) {
        if (bounds != null) {
            bounds.traslate(x, y);
        }

        for (final GPixMap frame : frames) {
            frame.traslate(x, y);
        }
    }

    @Override
    public Iterator<GPixMap> iterator() {
        return frames.iterator();
    }
}
