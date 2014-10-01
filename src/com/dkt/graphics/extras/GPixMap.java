/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2014 <dktcoding [at] gmail>
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

import com.dkt.graphics.elements.GLine;
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GRectangle;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.IntervalException;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import com.dkt.graphics.utils.Utils;
import java.awt.Color;
import java.awt.Graphics2D;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.Arrays;
import java.util.Objects;

/**
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public final class GPixMap extends GraphicE {
    private GPoint p = new GPoint(0, 0);
    private final Color[][] data;
    private GRectangle bounds;
    private boolean drawLine;
    private boolean visible = true;
    private int ps;

    /**
     * Copy constructor
     *
     * @param e {@code GPixMap} to copy
     * @throws NullPointerException if {@code e} is {@code null}
     */
    public GPixMap(GPixMap e) {
        super(e);

        data = new Color[e.data.length][e.data[0].length];
        for (int i = 0, n = data.length; i < n; i++) {
            System.arraycopy(e.data[i], 0, data[i], 0, data[0].length);
        }

        e.copy(this);
    }

    /**
     * Creates a new {@code GPixMap} of a given size
     *
     * @param x horizontal size in pixels
     * @param y vertical size in pixels
     * @throws InvalidArgumentException if either {@code x} or {@code y} are
     * smaller or equal to zero
     */
    public GPixMap(int x, int y) throws InvalidArgumentException {
        if (x <= 0 || y <= 0) {
            throw new InvalidArgumentException("Size must be positive");
        }

        data = new Color[y][x];
        Color awhite = Utils.getColorWithAlpha(Color.WHITE, 0);
        for (Color[] row : data) {
            Arrays.fill(row, awhite);
        }

        setPixelSize(8);
    }

    /**
     * Constructs a new {@code GPixMap} from a given {@link Color} matrix
     *
     * @param data The data that will form the {@code GPixMap}
     * @throws NullPointerException if {@code data} is {@code null} or if any
     * of the rows are {@code null}
     * @throws InvalidArgumentException if the matrix isn't rectangular (given
     * that matrix implementations in Java are based on Iliffe vectors, it's
     * possible for the rows to have different sizes
     */
    public GPixMap(Color[][] data) throws NullPointerException,
                                          InvalidArgumentException
    {
        checkSize(data);

        this.data = new Color[data.length][data[0].length];
        for (int i = 0, n = data.length; i < n; i++) {
            System.arraycopy(data[i], 0, this.data[i], 0, data[0].length);
        }

        setPixelSize(8);
    }

    /**
     * Constructs a new {@code GPixMap} from a given {@link int} matrix
     *
     * @param data The data that will form the {@code GPixMap} all this data
     * will be converted to {@link Color} objects so they should have the form
     * {@code ARGB}
     * @param hasAlpha Tells if the ints represent a color with alpha
     * @throws NullPointerException if {@code data} is {@code null} or if any
     * of the rows are {@code null}
     * @throws InvalidArgumentException  if the matrix isn't rectangular (given
     * that matrix implementations in Java are based on Iliffe vectors, it's
     * possible for the rows to have different sizes
     */
    public GPixMap(
            int[][] data,
            boolean hasAlpha) throws NullPointerException,
                                     InvalidArgumentException
    {
        checkSize(data);

        this.data = new Color[data.length][data[0].length];
        for (int i = 0, n = data.length; i < n; i++) {
            for (int j = 0, m = data[0].length; j < m; j++) {
                this.data[i][j] = new Color(data[i][j], hasAlpha);
            }
        }

        setPixelSize(8);
    }

    /**
     * Set's the pixel size of the {@code GPixMap}
     *
     * @param size new pixel size
     * @throws InvalidArgumentException if the {@code size <= 0}
     */
    public void setPixelSize(int size) throws InvalidArgumentException {
        if (size <= 0) {
            throw new InvalidArgumentException("Size must be bigger than zero");
        }

        ps = size;

        final int xs = getXSize();
        final int ys = getYSize();

        bounds = new GRectangle(p.x(), p.y(), xs * ps, ys * ps);
    }

    /**
     * Retrieves the number of horizontal pixels in this {@code GPixMap}
     *
     * @return horizontal pixels
     */
    public int getXSize() {
        return data[0].length;
    }

    /**
     * Retrieves the number of vertical pixels in this {@code GPixMap}
     *
     * @return vertical pixels
     */
    public int getYSize() {
        return data.length;
    }

    /**
     * Retrieves the {@link Color} on a given position of the {@code GPixMap}
     * <br><i>Note: </i>The value of {@code j} is affected by the direction of
     * the {@code Y axis}
     *
     * @param i vertical coordinate
     * @param j horizontal coordinate
     * @return Color
     * @throws IntervalException if either {@code i} or {@code j} are out of
     * range
     */
    public Color colorAt(int i, int j) throws IntervalException {
        checkInterval(i, j);
        return data[i][j];
    }

    /**
     * Set's the {@link Color} at the specified coordinates
     * <br><i>Note: </i>The value of {@code j} is affected by the direction of
     * the {@code Y axis}
     *
     * @param i vertical coordinate
     * @param j horizontal coordinate
     * @param col the color to set
     * @throws IntervalException if either {@code i} or {@code j} are out of
     * range
     * @throws NullPointerException if {@code col} is {@code null}
     */
    public void setColorAt(
            int i,
            int j,
            Color col) throws IntervalException,
                              NullPointerException
    {
        checkInterval(i, j);
        if (col == null) {
            throw new NullPointerException("The color can't be null");
        }

        data[i][j] = col;
    }

    /**
     * Retrieves the {@link Color} on a given position of the {@code GPixMap}
     * <br><i>Note: </i>The value of {@code j} is affected by the direction of
     * the {@code Y axis}
     *
     * @param i vertical coordinate
     * @param j horizontal coordinate
     * @return numerical value of the color
     * @throws IntervalException if either {@code i} or {@code j} are out of
     * range
     */
    public int valueAt(int i, int j) throws IntervalException {
        checkInterval(i, j);
        //This actually returns ARGB
        return data[i][j].getRGB();
    }

    /**
     * Set's the {@link Color} at the specified coordinates which will be
     * created as {@code new Color(argb, hasAlpha)}
     * <br><i>Note: </i>The value of {@code j} is affected by the direction of
     * the {@code Y axis}
     *
     * @param i vertical coordinate
     * @param j horizontal coordinate
     * @param argb color data
     * @param hasAlpha {@code true} if the color has alpha and {@code false}
     * otherwise
     * @throws IntervalException if either {@code i} or {@code j} are out of
     * range
     */
    public void setValueAt(
            int i,
            int j,
            int argb,
            boolean hasAlpha) throws IntervalException
    {
        checkInterval(i, j);
        data[i][j] = new Color(argb, hasAlpha);
    }

    /**
     * This will retrieve the position of the lower left corner of the {@code
     * GPixMap}.<br>
     * <i>Note:</i> if the canvas is inverting the {@code Y axis} then it will
     * represent the upper left corner
     *
     * @return {@link GPoint} representing the lower left corner
     */
    public GPoint getPosition() {
        return new GPoint(p);
    }

    /**
     * Tells the {@GPixMap} to draw the inner/outer lines
     *
     * @param draw {@code true} to draw the lines and {@code false} otherwise
     */
    public void setDrawLines(boolean draw) {
        drawLine = draw;
    }

    /**
     * Tells if the {@code GPixMap} will draw the inner lines or not
     *
     * @return {@code true} if the {@code GPixMap} is drawing the inner lines
     * and {@code false} otherwise
     */
    public boolean drawLines() {
        return drawLine;
    }

    /**
     * Retrieves the pixel size
     *
     * @return pixel size
     */
    public int pixelSize() {
        return ps;
    }

    /**
     * Retrieves the bounds of this {@code GPixMap} at this particular moment,
     * if you plan on using this to check for collisions, it's better to use
     * the {@code contains(...)} methods of this class
     *
     * @return {@link GRectangle} object representing the bounds of this
     * {@code GPixMap}
     * @see GPixMap#contains(GLine)
     * @see GPixMap#contains(GPoint)
     * @see GPixMap#contains(GRectangle)
     */
    public GRectangle getBounds() {
        return new GRectangle(bounds);
    }

    /**
     * Tells if a given set of coordinates is contained in the bounds of this
     * {@code GPixMap}<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param x {@code x} coordinate of the point
     * @param y {@code y} coordinate of the point
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @see GPixMap#contains(GLine)
     * @see GPixMap#contains(GPoint)
     * @see GPixMap#contains(GRectangle)
     * @see GPixMap#setVisible(boolean)
     * @see GPixMap#isVisible()
     */
    public boolean contains(int x, int y) {
        return visible && bounds.contains(x, y);
    }

    /**
     * Tells if a {@link GPoint} is contained in the bounds of this
     * {@code GPixMap}<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param p {@link GPoint}
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @see GPixMap#contains(int, int)
     * @see GPixMap#contains(GLine)
     * @see GPixMap#contains(GRectangle)
     * @see GPixMap#setVisible(boolean)
     * @see GPixMap#isVisible()
     */
    public boolean contains(GPoint p) {
        return visible && bounds.contains(p);
    }

    /**
     * Tells if a {@link GLine} is contained in the bounds of this
     * {@code GPixMap}<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param l {@link GLine}
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @see GPixMap#contains(int, int)
     * @see GPixMap#contains(GPoint)
     * @see GPixMap#contains(GRectangle)
     * @see GPixMap#setVisible(boolean)
     * @see GPixMap#isVisible()
     */
    public boolean contains(GLine l) {
        return visible && bounds.contains(l);
    }

    /**
     * Tells this {@code GPixMap} intersects with a given {@link GRectangle}<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param r {@link GRectangle}
     * @return {@code true} if the maps intersect and {@code false} otherwise
     * @see GPixMap#setVisible(boolean)
     * @see GPixMap#isVisible()
     * @see GPixMap#touches(GPixMap)
     */
    public boolean intersects(GRectangle r) {
        return visible && bounds.intersects(r);
    }

    /**
     * Tells two {@code GPixMap}s intersect<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param map other {@code GPixMap}
     * @return {@code true} if the maps intersect and {@code false} otherwise
     * @see GPixMap#setVisible(boolean)
     * @see GPixMap#isVisible()
     * @see GPixMap#touches(GPixMap)
     */
    public boolean intersects(GPixMap map) {
        return visible && bounds.intersects(map.bounds);
    }

    /**
     * Tells if a {@link GRectangle} is contained in the bounds of this
     * {@code GPixMap}<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param r rectangle
     * @return {@code true} if the point is contained and {@code false}
     * otherwise
     * @see GPixMap#contains(int, int)
     * @see GPixMap#contains(GLine)
     * @see GPixMap#contains(GPoint)
     * @see GPixMap#setVisible(boolean)
     * @see GPixMap#isVisible()
     */
    public boolean contains(GRectangle r) {
        return visible && bounds.contains(r);
    }

    /**
     * As opposed to {@link GraphicE#traslate(int, int)} this method will
     * traslate the PixMap a certain amount of {@code pixels} (considering
     * {@code pixel} the size of each square drawn in the screen)
     *
     * @param h number of horizontal pixels
     * @param v number of vertical pixels
     * @see GraphicE#traslate(int, int)
     * @see GPixMap#setPixelSize(int)
     * @see GPixMap#pixelSize()
     */
    public void traslateUnits(int h, int v) {
        p.traslate(h * ps, v * ps);
        bounds.traslate(h * ps, v * ps);
    }

    /**
     * Tells if the {@code GPixMap} will be drawn<br>
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @return {@code true} if it's visible and {@code false} otherwise
     * @see GPixMap#setVisible(boolean)
     */
    public boolean isVisible() {
        return visible;
    }

    /**
     * Tells the {@code GPixMap} to draw itself in the canvas
     * <i>Note: </i> invisible {@code GPixMap}s don't intersect with anything
     *
     * @param v {@code true} if the {@code GPixMap} will be drawn and
     * {@code false} otherwise
     * @see GPixMap#isVisible()
     */
    public void setVisible(boolean v) {
        visible = v;
    }

    /**
     * Tells if the {@code GPixMap} has the same number of rows and columns
     *
     * @return {@code true} if the {@code GPixMap} is square and {@code false}
     * otherwise
     */
    public boolean isSquare() {
        return getXSize() == getYSize();
    }

    /**
     * Retrieves a new {@code GPixMap} which is the result of rotating this one
     * clockwise 90°.<pre>
     *                            +-----+
     *       +-------+            | *** |
     *       | ***** |            |   * |
     *       | *     |     ->     |   * |
     *       | *     |            |   * |
     *       +-------+            |   * |
     *                            +-----+</pre>
     *
     * @return Clockwise turn of this {@code GPixMap}
     */
    public GPixMap rotateCW() {
        final int sx = getXSize();
        final int sy = getYSize();
        final Color[][] foo = new Color[sx][sy];

        for (int i = 0, n = sy; i < n; i++) {
            for (int j = 0, m = sx; j < m; j++) {
                foo[j][sy - i - 1] = data[i][j];
            }
        }

        final GPixMap map = new GPixMap(foo);
        copy(map);

        return map;
    }

    /**
     * Retrieves a new {@code GPixMap} which is the result of rotating this one
     * clockwise 180°.<pre>
     *       +-------+            +-------+
     *       | ***** |            |     * |
     *       | *     |     ->     |     * |
     *       | *     |            | ***** |
     *       +-------+            +-------+</pre>
     *
     * @return Clockwise turn of this {@code GPixMap} twice
     */
    public GPixMap rotateCW2() {
        final int sx = getXSize();
        final int sy = getYSize();
        final Color[][] foo = new Color[sy][sx];

        for (int i = 0, n = sy; i < n; i++) {
            for (int j = 0, m = sx; j < m; j++) {
                foo[sy - i - 1][sx - j - 1] = data[i][j];
            }
        }

        final GPixMap map = new GPixMap(foo);
        copy(map);

        return map;
    }

    /**
     * Retrieves a new {@code GPixMap} which is the result of rotating this one
     * counter clockwise 90°.<pre>
     *                            +-----+
     *       +-------+            | *   |
     *       | ***** |            | *   |
     *       | *     |     ->     | *   |
     *       | *     |            | *   |
     *       +-------+            | *** |
     *                            +-----+</pre>
     *
     * @return Counter clockwise turn of this {@code GPixMap}
     */
    public GPixMap rotateCCW() {
        final int sx = getXSize();
        final int sy = getYSize();
        final Color[][] fooMat = new Color[sx][sy];

        for (int i = 0, n = sy; i < n; i++) {
            for (int j = 0, m = sx; j < m; j++) {
                fooMat[sx - j - 1][i] = data[i][j];
            }
        }

        final GPixMap map = new GPixMap(fooMat);
        copy(map);

        return map;
    }

    /**
     * Retrieves a new {@code GPixMap} which is the horizontally mirrored
     * version of this one.<pre>
     *       +------+            +------+
     *       | **** |            | **** |
     *       | *    |     ->     |    * |
     *       | **** |            | **** |
     *       +------+            +------+</pre>
     *
     * @return Horizontal mirror of this {@code GPixMap}
     */
    public GPixMap mirrorHorizontal() {
        final int sx = getXSize();
        final int sy = getYSize();
        final Color[][] fooMat = new Color[sy][sx];

        for (int i = 0; i < sy; i++) {
            for (int j = 0; j < sx; j++) {
                fooMat[i][sx - j - 1] = data[i][j];
            }
        }

        final GPixMap map = new GPixMap(fooMat);
        copy(map);

        return map;
    }

    /**
     * Retrieves a new {@code GPixMap} which is the vertically mirrored version
     * of this one.<pre>
     *       +------+            +------+
     *       | *  * |            | **** |
     *       | *  * |     ->     | *  * |
     *       | **** |            | *  * |
     *       +------+            +------+</pre>
     *
     * @return Vertical mirror of this {@code GPixMap}
     */
    public GPixMap mirrorVertical() {
        final int sx = getXSize();
        final int sy = getYSize();
        final Color[][] fooMat = new Color[sy][sx];

        for (int i = 0, n = sy; i < n; i++) {
            System.arraycopy(data[i], 0, fooMat[sy - i - 1], 0, sx);
        }

        final GPixMap map = new GPixMap(fooMat);
        copy(map);

        return map;
    }

    /**
     * Tells if two {@code GPixMap}s touch each other, this differs from the
     * {@link GPixMap#intersects(GPixMap)} because this checks if the images are
     * actually touching each other ignoring the transparent pixels.<br>
     * This method will only work on {@code GPixMap}s that have the same pixel
     * size.<br>
     * <i>Note:</i> This method will most likely fail miserably when combined
     * with {@link GTransform}s.<br>
     * <i>Note 2:</i> There are some issues when the traslation step is
     * different than the pixel size, but that should be a non issue, since it's
     * a <s>very rare</s> scenario, and that's why we have a
     * {@link GPixMap#traslateUnits(int, int)} method.
     * Oops... it appears to be a more common scenario than predicted... my bad.
     * If someone actually wants to take care of this, that would be great.
     *
     * @param map the {@code GPixMap} to check against
     * @return {@code true} if the two {@code GPixMap}s touch and {@code false}
     * otherwise
     * @throws NullPointerException if {@code map} is {@code null}
     * @throws InvalidArgumentException if the pixel sizes don't match
     * @see GPixMap#pixelSize()
     */
    public boolean touches(GPixMap map) throws NullPointerException,
                                               InvalidArgumentException {
        if (map == null) {
            throw new NullPointerException("The map can't be null");
        }

        if (pixelSize() != map.pixelSize()) {
            throw new InvalidArgumentException("The pixel sizes don't match");
        }

        if (!isVisible() || !map.isVisible()) {
            return false;
        }

        if (intersects(map)) {
            //This bounds in units
            final int x1 = p.x() / ps;
            final int y1 = p.y() / ps;
            final int x2 = x1 + getXSize();
            final int y2 = y1 + getYSize();
            //Map's bounds in units
            final int x3 = map.p.x() / ps;
            final int y3 = map.p.y() / ps;
            final int x4 = x3 + map.getXSize();
            final int y4 = y3 + map.getYSize();
            //Intersection bounds in units
            final int x5 = Math.max(x1, x3);
            final int y5 = Math.max(y1, y3);
            final int x6 = Math.min(x2, x4);
            final int y6 = Math.min(y2, y4);

            for (int i = x5; i < x6; i++) {
                for (int j = y5; j < y6; j++) {
                    final Color c1 =     data[j - y1][i - x1];
                    final Color c2 = map.data[j - y3][i - x3];

                    if (c1.getAlpha() != 0 & c2.getAlpha() != 0) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void traslate(int x, int y) {
        p.traslate(x, y);
        bounds.traslate(x, y);
    }

    @Override
    public void draw(Graphics2D g) {
        if (!visible) {
            return;
        }

        int xx = p.x();
        int yy = p.y();

        if (drawLine) {
            g.setStroke(getStroke());
            for (final Color[] row : data) {
                for (final Color e : row) {
                    g.setPaint(e);
                    g.fillRect(xx, yy, ps, ps);
                    g.setPaint(getPaint());
                    g.drawRect(xx, yy, ps, ps);

                    xx += ps;
                }

                xx  = p.x();
                yy += ps;
            }
        } else {
            for (final Color[] row : data) {
                for (final Color e : row) {
                    g.setColor(e);
                    g.fillRect(xx, yy, ps, ps);
                    xx += ps;
                }

                xx  = p.x();
                yy += ps;
            }
        }
    }

    /**
     * Retrieves a copy of the color data expressed as a {@link Color} matrix
     *
     * @return color data
     */
    public Color[][] getColorData() {
        final Color[][] nData = new Color[getYSize()][getXSize()];

        for (int i = 0, n = getYSize(); i < n; i++) {
            System.arraycopy(data[i], 0, nData[i], 0, data[i].length);
        }

        return nData;
    }

    /**
     * Retrieves a copy of the color data expressed as an {@code int} matrix
     *
     * @return color data
     */
    public int[][] getData() {
        final int[][] nData = new int[getYSize()][getXSize()];

        for (int i = 0, n = getYSize(); i < n; i++) {
            for (int j = 0, m = getXSize(); j < m; j++) {
                nData[i][j] = data[i][j].getRGB();
            }
        }

        return nData;
    }

    /**
     * Creates a {@code String} representation of the {@code GPixMap}, which
     * consists of space separated values representing the hex representation
     * of the colors of each pixel. This method does not include either pixel
     * size nor visibility of the grid.
     *
     * @return {@code String} representing the data of the {@code GPixMap}
     */
    public String toDataString() {
        final int size = getXSize() * 9 * getYSize() + getYSize();
        final StringBuilder builder = new StringBuilder(size);

        for (final Color[] row : data) {
            for (final Color col : row) {
                builder.append(Integer.toHexString(col.getRGB()));
                builder.append(" ");
            }
            builder.append("\n");
        }

        return builder.substring(0, builder.length() - 1);
    }

    /**
     * Saves a text representation of this {@code GPixMap} to a file
     *
     * @param map {@code GPixMap} to export
     * @param file file in which to export
     * @throws IOException if something goes wrong when writing the file
     * @throws NullPointerException if {@code file} or {@code map} are {@code
     * null}
     * @see GPixMap#importMap(java.io.File)
     * @see GPixMap#importMap(java.io.InputStream)
     */
    public static void exportMap(
            GPixMap map,
            File file) throws IOException,
                              NullPointerException {
        if (file == null) {
            throw new NullPointerException("The file can't be null");
        }
        if (map == null) {
            throw new NullPointerException("The map can't be null");
        }

        try (PrintStream fps = new PrintStream(file)) {
            fps.append(Integer.toString(map.getXSize()))
               .append(' ')
               .append(Integer.toString(map.getYSize()))
               .append(' ')
               .append(Integer.toString(map.pixelSize()))
               .append(' ')
               .append(Integer.toString(((Color)map.getPaint()).getRGB()))
               .append(' ')
               .append(Boolean.toString(map.drawLines()))
               .append('\n')
               .append(map.toDataString());
        }
    }

    /**
     * Imports a {@code GPixMap} from a given {@link InputStream}
     *
     * @param is {@link InputStream} of the file
     * @return a new {@code GPixMap} decoded from the {@link InputStream}
     * @throws IOException if something goes wrong when reading the file
     * @throws NullPointerException if {@code is} is {@code null}
     * @see GPixMap#exportMap(com.dkt.graphics.extras.GPixMap, java.io.File)
     * @see GPixMap#importMap(java.io.File)
     */
    public static GPixMap importMap(InputStream is) throws IOException,
                                                           NullPointerException {

        if (is == null) {
            throw new NullPointerException("The stream can't be null");
        }

        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader     br = new BufferedReader(isr)) {

            String line = br.readLine();
            final String[] elements = line.split(" ");
            final int cols = Integer.parseInt(elements[0]);
            final int rows = Integer.parseInt(elements[1]);
            final int pixs = Integer.parseInt(elements[2]);
            final int grid = Integer.parseInt(elements[3]);
            final boolean draw = Boolean.parseBoolean(elements[4]);

            final int[][] data = new int[rows][cols];

            int i = 0;
            while ((line = br.readLine()) != null) {
                int j = 0;
                for (final String dat : line.split(" ")) {
                    data[i][j] = (int)(Long.parseLong(dat, 16) & 0xFFFFFFFF);
                    j++;
                }
                i++;
            }

            final GPixMap map = new GPixMap(data, true);
            map.setPixelSize(pixs);
            map.setDrawLines(draw);
            map.setPaint(new Color(grid, true));

            return map;
        }
    }

    /**
     * Imports a {@code GPixMap} from a file
     *
     * @param file file from which to read the {@code GPixMap}
     * @return a new {@code GPixMap} decoded from the {@link File}
     * @throws FileNotFoundException if the file doesn't exist
     * @throws IOException if something goes wrong when reading the file
     * @throws NullPointerException if {@code is} is {@code null}
     * @see GPixMap#exportMap(com.dkt.graphics.extras.GPixMap, java.io.File)
     * @see GPixMap#importMap(java.io.InputStream)
     */
    public static GPixMap importMap(File file) throws FileNotFoundException,
                                                      IOException,
                                                      NullPointerException {
        if (file == null) {
            throw new NullPointerException("The file can't be null");
        }

        try (FileInputStream fis = new FileInputStream(file)) {
            return importMap(fis);
        }
    }

    private void checkInterval(int i, int j) throws IntervalException {
        if (i < 0 || i >= getYSize()) {
            String msg = "Y value must be contained in the interval";
            throw new IntervalException(msg, 0, getYSize(), i);
        }
        if (j < 0 || j >= getXSize()) {
            String msg = "X value must be contained in the interval";
            throw new IntervalException(msg, 0, getXSize(), j);
        }
    }

    private void checkSize(Object[] data) throws NullPointerException,
                                                 InvalidArgumentException {
        if (data == null) {
            throw new NullPointerException("Data can't be null");
        }

        if (data.length == 0) {
            throw new InvalidArgumentException("Data can't be empty");
        }

        final int size;
        if (data[0] instanceof int[]) {
            size = ((int[])data[0]).length;
        } else {
            size = ((Object[])data[0]).length;
        }

        for (final Object row : data) {
            if (row == null) {
                throw new NullPointerException("Data can't have null rows");
            }

            int ns;
            if (row instanceof int[]) {
                ns = ((int[])row).length;
            } else {
                ns = ((Object[])row).length;
            }

            if (ns != size) {
                String msg = "Data must be a rectangular matrix";
                throw new InvalidArgumentException(msg);
            }
        }
    }

    private void copy(GPixMap e){
        e.p  = new GPoint(p);
        e.ps = ps;
        e.drawLine = drawLine;
        e.visible  = visible;
        e.setPaint(getPaint());
        e.setStroke(getStroke());
    }

    @Override
    public GPixMap clone() {
        return new GPixMap(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 83 * hash + Objects.hashCode(p);
        hash = 83 * hash + Arrays.deepHashCode(data);
        hash = 83 * hash + Objects.hashCode(bounds);
        hash = 83 * hash + (drawLine ? 1 : 0);
        hash = 83 * hash + (visible ? 1 : 0);
        hash = 83 * hash + ps;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GPixMap other = (GPixMap) obj;
        if (drawLine != other.drawLine) {
            return false;
        }
        if (visible != other.visible) {
            return false;
        }
        if (ps != other.ps) {
            return false;
        }
        if (!Objects.equals(p, other.p)) {
            return false;
        }
        if (!Objects.equals(bounds, other.bounds)) {
            return false;
        }
        return Arrays.deepEquals(data, other.data);
    }
}
