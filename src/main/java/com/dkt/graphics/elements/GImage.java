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
package com.dkt.graphics.elements;

import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.PaintContext;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.io.File;
import java.io.IOException;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * Wraps a {@link BufferedImage} into a {@link GraphicE} object
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GImage extends GraphicE implements Paint {
    private final BufferedImage original;
    private BufferedImage image;
    private int x, y;

    /**
     * Copy constructor
     *
     * @param e {@code GImage} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GImage (GImage e) {
        super(e);

        x = e.x;
        y = e.y;
        image = e.image;
        original = e.original;
    }

    /**
     * Create a new {@code GImage} from a given path
     *
     * @param path path to the image
     * @throws IOException if something goes wrong when opening or reading the
     * image file.
     */
    public GImage (final String path) throws IOException {
        this(new File(path));
    }

    /**
     * Create a new {@code GImage} from a given file
     *
     * @param file file of the image
     * @throws IOException if something goes wrong when opening or reading the
     * image file.
     */
    public GImage (final File file) throws IOException {
        this(0, 0, file);
    }

    /**
     * Create a new {@code GImage} from a given file
     *
     * @param x X coordinate of the center of the image
     * @param y Y coordinate of the center of the image
     * @param file file of the image
     * @throws IOException if something goes wrong when opening or reading the
     * image file.
     */
    public GImage (
            final int x,
            final int y,
            final File file) throws IOException
    {
        this(x, y, ImageIO.read(file));
    }

    /**
     * Create a new {@code GImage} from a given {@link BufferedImage}
     *
     * @param x X coordinate of the center of the image
     * @param y Y coordinate of the center of the image
     * @param img image to wrap
     */
    public GImage (
            final int x,
            final int y,
            final BufferedImage img)
    {
        this.original = img;
        this.image    = img;
        this.x = x - getWidth () / 2;
        this.y = y - getHeight() / 2;
    }

    /**
     * Retrieves the current width of the {@code GImage} in pixels
     *
     * @return width in px
     */
    public final int getWidth() {
        return image.getWidth(null);
    }

    /**
     * Retrieves the current height of the {@code GImage} in pixels
     *
     * @return height in px
     */
    public final int getHeight() {
        return image.getHeight(null);
    }

    /**
     * Retrieves the original width of the {@code GImage} in pixels
     *
     * @return width in px
     */
    public final int getOriginalWidth() {
        return original.getWidth(null);
    }

    /**
     * Retrieves the original height of the {@code GImage} in pixels
     *
     * @return height in px
     */
    public final int getOriginalHeight() {
        return original.getHeight(null);
    }

    /**
     * Symmetrically scales an image.<br>
     * <ul>
     * <li>If {@code scale} is less than 1 then the image will be reduced.
     * <li>If {@code scale} is 1, then nothing noticeable should happen to
     * the image.
     * <li>If {@code scale} is more than 1 then the image will be bigger.
     * </ul>
     *
     * @param scale scale quotient
     */
    public void scale(final double scale) {
        scale(scale, scale);
    }

    /**
     * Scales an image. <br>
     * <ul>
     * <li>If {@code scaleX/Y} is less than 1 then the image will be reduced.
     * <li>If {@code scaleX/Y} is 1, then nothing noticeable should happen to
     * the image.
     * <li>If {@code scaleX/Y} is more than 1 then the image will be bigger.
     * </ul>
     * <i>Note:</i> Scaling is not accumulative, it always refer to the original
     * image.
     *
     * @param sx Horizontal scale
     * @param sy Vertical scale
     * @see GImage#getOriginalHeight()
     * @see GImage#getOriginalWidth ()
     */
    public void scale(
            final double sx,
            final double sy)
    {
        final int newX = (int)(original.getHeight() * sx);
        final int newY = (int)(original.getWidth () * sy);

        image = new BufferedImage(newX, newY, BufferedImage.TYPE_INT_ARGB);
        final AffineTransform at = AffineTransform.getScaleInstance(sx, sy);

        final AffineTransformOp scaleOp = new AffineTransformOp(at,
                AffineTransformOp.TYPE_BILINEAR
        );

        image = scaleOp.filter(original, image);
    }

    @Override
    public void traslate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Moves the center of the image to the given coordinates
     *
     * @param x new X coordinate of the center of the image
     * @param y new Y coordinate of the center of the image
     */
    public void move(final int x, final int y) {
        this.x = x - getWidth () / 2;
        this.y = y - getHeight() / 2;
    }

    @Override
    public void draw(final Graphics2D g) {
        g.drawImage(image, x, y, null);
    }

    @Override
    public GImage clone() {
        return new GImage(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 71 * hash + Objects.hashCode(original);
        hash = 71 * hash + Objects.hashCode(image);
        hash = 71 * hash + x;
        hash = 71 * hash + y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GImage other = (GImage) obj;
        if (!Objects.equals(original, other.original)) {
            return false;
        }

        if (!Objects.equals(image, other.image)) {
            return false;
        }

        return !(
            x != other.x |
            y != other.y
        );
    }

    @Override
    public PaintContext createContext(
            final ColorModel cm,
            final Rectangle deviceBounds,
            final Rectangle2D userBounds,
            final AffineTransform xform,
            final RenderingHints hints) {
        final Rectangle anchor = new Rectangle(getWidth(), getHeight());
        final TexturePaint paint = new TexturePaint(image, anchor);
        return paint.createContext(cm, deviceBounds, userBounds, xform, hints);
    }

    @Override
    public int getTransparency() {
        return image.getColorModel().getTransparency();
    }
}
