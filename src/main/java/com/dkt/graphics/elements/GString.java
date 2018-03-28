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

import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.util.Objects;

/**
 * This class wraps a {@link String} in a {@link GraphicE}
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GString extends GraphicE {
    private static final Font DEFAULT = new Font(Font.DIALOG, Font.PLAIN, 14);
    private String string;
    private Font font;
    private int x, y;
    private double a;

    /**
     * Copy constructor
     *
     * @param e {@code GString} to copy
     * @throws IllegalArgumentException if {@code e} is {@code null}
     */
    public GString(GString e){
        super(e);

        string = e.string;
        font = e.font;
        a = e.a;
        x = e.x;
        y = e.y;
    }

    /**
     * GString constructor
     *
     * @param x X coordinate of the lower left corner of the {@code String}
     * @param y Y coordinate of the lower left corner of the {@code String}
     * @param str String to show
     */
    public GString(
            final int x,
            final int y,
            final String str)
    {
        this(x, y, str, null);
    }

    /**
     * GString constructor
     *
     * @param x X coordinate of the lower left corner of the {@code String}
     * @param y Y coordinate of the lower left corner of the {@code String}
     * @param str String to show
     * @param font {@link Font}
     */
    public GString(
            final int x,
            final int y,
            final String str,
            final Font font)
    {
        this(x, y, 0, str, font);
    }

    /**
     * GString constructor
     *
     * @param x X coordinate of the lower left corner of the {@code String}
     * @param y Y coordinate of the lower left corner of the {@code String}
     * @param a The angle in degrees of the {@code String}
     * @param str String to show
     */
    public GString(
            final int x,
            final int y,
            final double a,
            final String str)
    {
        this(x, y, a, str, null);
    }

    /**
     * Complete GString constructor
     *
     * @param x X coordinate of the lower left corner of the {@code String}
     * @param y Y coordinate of the lower left corner of the {@code String}
     * @param a The angle in degrees of the {@code String}
     * @param str String to show
     * @param font {@link Font}
     */
    public GString(
            final int x,
            final int y,
            final double a,
            final String str,
            final Font font)
    {
        this.x = x;
        this.y = y;
        this.a = Math.toRadians(a);
        this.string = str;
        this.font = font;

        if (this.font == null){
            this.font = DEFAULT;
        }
    }

    /**
     * Set's the {@code String} to print in the screen
     *
     * @param string {@code String} to be printed
     * @throws IllegalArgumentException if {@code string} is {@code null}
     */
    public void setString(final String string) throws IllegalArgumentException {
        if (string == null){
            throw new IllegalArgumentException("The string can't be null");
        }

        this.string = string;
    }

    /**
     * Retrieves the {@code String}
     *
     * @return string
     */
    public String getString() {
        return string;
    }

    /**
     * Set's the font used to render this {@code String}
     *
     * @param font font
     * @throws IllegalArgumentException if {@code font} is {@code null}
     */
    public void setFont(final Font font) throws IllegalArgumentException {
        if (font == null){
            throw new IllegalArgumentException("The font can't be null");
        }

        this.font = font;
    }

    /**
     * Retrieves the font used to print this {@code String}
     *
     * @return font
     */
    public Font getFont(){
        return font;
    }

    /**
     * Set's the angle of the String
     *
     * @param angle angle in degrees
     */
    public void setAngle(final double angle) {
        a = Math.toRadians(angle % 360);
    }

    /**
     * Retrieves the angle in degrees of the {@code String}
     *
     * @return angle
     */
    public double getAngle() {
        return Math.toDegrees(a);
    }

    @Override
    public void draw(final Graphics2D g) {
        g.setPaint(getPaint());
        g.setFont(font);
        final AffineTransform t = g.getTransform();
        final boolean isInverting = t.getScaleY() < 0;

        if (isInverting){
            final double tx = t.getTranslateX();
            final double ty = t.getTranslateY();
            final AffineTransform t2;
            t2 = AffineTransform.getTranslateInstance(tx, ty);
            t2.rotate(a, x, -y);
            g.setTransform(t2);
            g.drawString(string, x, -y);
            g.setTransform(t);
        } else {
            g.rotate(-a, x, y);
            g.drawString(string, x, y);
            g.setTransform(t);
        }
    }

    /**
     * Retrieves the {@link FontMetrics} associated with this {@code GString}
     * for a given {@link Graphics2D}.
     *
     * @param g The {@code Graphics2D} that will be used to render this
     * {@code GString}
     * @return {@code FontMetrics} object.
     */
    public FontMetrics getFontMetrics(Graphics2D g) {
        g.setPaint(getPaint());
        g.setFont(font);
        return g.getFontMetrics();
    }

    @Override
    public void traslate(final int x, final int y) {
        this.x += x;
        this.y += y;
    }

    /**
     * Moves this element to the given coordinates
     *
     * @param x new x coordinate
     * @param y new y coordinate
     */
    public void move(final int x, final int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public GString clone() {
        return new GString(this);
    }

    @Override
    public int hashCode() {
        int hash = super.hashCode();
        hash = 23 * hash + Objects.hashCode(string);
        hash = 23 * hash + Objects.hashCode(font);
        hash = 23 * hash + x;
        hash = 23 * hash + y;
        hash = 23 * hash + (int) (Double.doubleToLongBits(a)
                               ^ (Double.doubleToLongBits(a) >>> 32));
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (!super.equals(obj)) {
            return false;
        }

        final GString other = (GString) obj;
        if (!Objects.equals(string, other.string)) {
            return false;
        }
        if (!Objects.equals(font, other.font)) {
            return false;
        }

        return !(
            x != other.x |
            y != other.y |
            a != other.a
        );
    }
    
    /**
     * Retrieves the {@code x} coordinate of the string
     * 
     * @return x coordinate
     */
    public int getX() {
        return x;
    }

    /**
     * Retrieves the {@code y} coordinate of the string
     * 
     * @return y coordinate
     */
    public int getY() {
        return y;
    }

}
