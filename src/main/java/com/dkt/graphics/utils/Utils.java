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
package com.dkt.graphics.utils;

import com.dkt.graphics.canvas.Canvas;
import com.dkt.graphics.elements.GraphicE;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.TexturePaint;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Utils {
    /**
     * Don't let anyone initialize this class
     */
    private Utils(){}

    /**
     * Generates a new {@link Color} based on the color but with the given
     * alpha
     *
     * @param color the original color
     * @param alpha the alpha value if the value is bigger than 255 it will be set
     * to 255, and if the value is smaller than 0 then it will be set to 0
     * @return A new color with the same RGB value as the argument but with the
     * specified alpha
     * @throws IllegalArgumentException if color is {@code null}
     */
    public static Color getColorWithAlpha(Color color, int alpha){
        if (color == null){
            throw new IllegalArgumentException("Color can't be null!");
        }

        //Ensure the value is within an interval
        int a = Math.max(alpha,  0 );
            a = Math.min(a, 255);

        final int r = color.getRed  ();
        final int g = color.getGreen();
        final int b = color.getBlue ();

        return new Color(r, g, b, a);
    }

    /**
     * Saves a 1:1 screenshot of the canvas<br>
     * The image is saved using the {@code png} format, so if the file doesn't
     * end with {@code .png} the extension will be appended
     *
     * @param canvas The {@link Canvas} to print
     * @param path The path of the file
     * @param back {@code true} if you want to paint the background and {@code
     * false} otherwise ({@code false} is needed when painting with transparent
     * components.
     * @throws IOException If the image can't be written
     * @throws IllegalArgumentException If either the canvas or the path are
     * {@code null}
     */
    public static void saveScreenshot(
            Canvas canvas,
            String path,
            boolean back) throws IOException
    {
        if (canvas == null){
            throw new IllegalArgumentException("Canvas can't be null");
        }

        if (path == null){
            throw new IllegalArgumentException("Path can't be null");
        }

        final File file = new File(path.endsWith(".png") ? path : path + ".png");

        final int width  = canvas.getXSize();
        final int height = canvas.getYSize();

        final BufferedImage img = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
        );

        final Graphics2D g2d = img.createGraphics();

        canvas.paintDrawableArea(g2d, back);

        ImageIO.write(img, "png", file);
    }

    /**
     * Retrieves a 1:1 image of the canvas
     *
     * @param canvas The {@link Canvas} to print
     * @param back {@code true} if you want to paint the background and {@code
     * false} otherwise ({@code false} is needed when painting with transparent
     * components.
     * @return a {@link BufferedImage} the same size of the canvas
     * @throws IllegalArgumentException If the canvas is {@code null}
     */
    public static BufferedImage getImage(Canvas canvas, boolean back) {
        if (canvas == null){
            throw new IllegalArgumentException("Canvas can't be null");
        }

        final int width  = canvas.getXSize();
        final int height = canvas.getYSize();

        final BufferedImage img = new BufferedImage(
                width,
                height,
                BufferedImage.TYPE_INT_ARGB
        );

        final Graphics2D g2d = img.createGraphics();

        canvas.paintDrawableArea(g2d, back);

        return img;
    }

    /**
     * Trims a given image to its minimum size without loosing any part of the
     * drawing.
     *
     * @param img The image to trim
     * @param omit The {@code Color} to omit when trimming (what to consider as
     * not drawn). Usually is {@link Color#WHITE} or
     * {@code new Color(0, true)}
     * @return a {@link BufferedImage} result of trimming the original image
     * @throws IllegalArgumentException If {@code img} is {@code null}
     */
    public static BufferedImage trimImage(BufferedImage img, Color omit) {
        if (img == null){
            throw new IllegalArgumentException("The image can't be null");
        }
        
        final int w = img.getWidth ();
        final int h = img.getHeight();
        final int c = omit == null ? 0 : omit.getRGB();

        final int[] data = img.getRGB(0, 0, w, h, null, 0, w);

        //Search for the image bounds
        int l = w;
        int r = 0;
        int u = h;
        int d = 0;

        for (int i = 0; i < h; i++) {
            for (int j = 0; j < w; j++) {
                if (data[i * w + j] != c) {
                    //Using the unbrached versions of min and max gives a
                    //significant performance improvement in this case
                    l = MathUtils.min(l, j);
                    r = MathUtils.max(r, j);
                    u = MathUtils.min(u, i);
                    d = MathUtils.max(d, i);
                }
            }
        }

        return img.getSubimage(l, u, r - l, d - u);
    }

    /**
     * Generates a texture from a given file
     *
     * @param path the path of the image file
     * @return A {@link TexturePaint} for this file
     * @throws IOException If the image can't be read or if the file isn't a
     * valid image
     * @throws IllegalArgumentException If the path is {@code null}
     */
    public static Paint getTexture(String path) throws IOException {
        if (path == null){
            throw new IllegalArgumentException("Path can't be null");
        }

        final File file = new File(path);
        final BufferedImage bi = ImageIO.read(file);

        final int width  = bi.getWidth ();
        final int height = bi.getHeight();

        final Rectangle2D r2d = new Rectangle(width, height);

        return new TexturePaint(bi, r2d);
    }

    /**
     * Draws a {@code GraphicE} into a {@code BufferedImage}
     *
     * @param e element to draw
     * @param w width of the image
     * @param h height of the image
     * @return a {@code BufferedImage} that represents the {@code GraphicE}
     */
    public static BufferedImage draw(GraphicE e, int w, int h) {
        BufferedImage image = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = (Graphics2D)image.getGraphics();

        g2d.setRenderingHint(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON
        );

        e.draw(g2d);
        g2d.dispose();

        return image;
    }
}
