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
package com.dkt.graphics.canvas;

import com.dkt.graphics.elements.GString;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import com.dkt.graphics.utils.TPS;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Paint;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.LinkedList;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * This class represents a basic canvas, it has some features that may seem
 * useless... they are...<br><br>
 *
 * Within the canvas there are 2 types of {@link GraphicE}, fixed and mobile.
 * fixed elements are only redrawn when the canvas is resized or when 
 * specifically told to.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Canvas extends JPanel implements ActionListener {
    private final LinkedList<GraphicE> elements = new LinkedList<>();
    private final LinkedList<GraphicE> fixed    = new LinkedList<>();

    private boolean centerBounds, fullArea, invert;
    private int xSize = 100;
    private int ySize = 100;
    private int xO;
    private int yO;
    private Paint drawableAreaPaint   = Color.WHITE;
    private Paint drawableBorderPaint = Color.BLACK;
    private boolean useAntiAliasing   = true;
    private boolean autoRepaint;
    private int repaintDelay = 50;
    private final Timer repaintTimer = new Timer(500, this);
    private boolean showFPS;
    private final GString fps = new GString(10, 20, "");
    private final TPS     tps = new TPS();
    private final DecimalFormat formatter = new DecimalFormat("#.00");
    private final AffineTransform emptyTran = new AffineTransform();
    private static GraphicsConfiguration GFX_CFG;
    private AffineTransform transform = new AffineTransform();

    public Canvas(){
        //Init timer config
        repaintTimer.setRepeats(true);
        repaintTimer.setCoalesce(true);
        repaintTimer.setDelay(repaintDelay);
        
        setBackground(Color.LIGHT_GRAY);
        setIgnoreRepaint(true);
        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                if (fullArea) {
                    final Dimension dim = getSize();
                    xSize = dim.width;
                    ySize = dim.height;
                    setCenterOrigin(centerOrigin);
                } else {
                    redraw(true);
                }
            }
        });
    }

    /**
     * Sets the canvas size.<br>
     * This value doesn't affect the panel's size, just the drawable area, this
     * means that the canvas area will be of the given size.
     *
     * <pre>
     *       +------+----+
     *       |      |    |
     *       |canvas|    |
     *       |      |    |
     *       +------+    |
     *       |   panel   |
     *       +-----------+</pre>
     *
     * @param xSize horizontal size
     * @param ySize vertical size
     * @throws InvalidArgumentException if either size is less than 1
     */
    public void setDrawableSize(int xSize, int ySize) {
        if (xSize <= 0 | ySize <= 0){
            String msg = "The size can't be less than 1";
            throw new InvalidArgumentException(msg);
        }

        this.xSize = xSize;
        this.ySize = ySize;

        if (centerOrigin){
            setCenterOrigin(true);
        }

        redraw(true);
    }

    /**
     * Tells the canvas to center (or not) the drawable area in the panel.
     *
     * <pre>
     *       +--------------+
     *       |              |
     *       |  +--------+  |
     *       |  |        |  |
     *       |  | canvas |  |
     *       |  |        |  |
     *       |  +--------+  |
     *       |     panel    |
     *       +--------------+</pre>
     * @param centerBounds {@code true} to center the bounds an {@code false}
     * otherwise
     */
    public void setCenterBounds(boolean centerBounds){
        this.centerBounds = centerBounds;
        redraw(true);
    }

    /**
     * Tells if the canvas is being centered.<br>
     * <i>Note:</i> the default value is {@code false}
     *
     * @return {@code true} if the canvas is being centered and {@code false}
     * otherwise
     */
    public boolean centerBounds() {
        return centerBounds;
    }

    /**
     * Tells if the drawable area will be automatically set to the canvas size.
     * <br><i>Note:</i> the default value is {@code false}
     * @return {@code true} if the drawable area is the canvas size, and {@code
     * false} otherwise.
     */
    public boolean useFullArea() {
        return fullArea;
    }

    /**
     * Tells the canvas to set the drawable area size to the size of the canvas.
     *
     * @param fullArea {@code true} for the canvas and drawable area to have the
     * same size, and {@code false} to set it manually.
     */
    public void setUseFullArea(boolean fullArea) {
        this.fullArea = fullArea;
        
        if (fullArea) {
            final Dimension dim = getSize();
            xSize = dim.width;
            ySize = dim.height;
        }

        setCenterOrigin(centerOrigin);
    }

    /**
     * Retrieves the horizontal size of the drawable area
     *
     * @return horizontal size in px
     */
    public int getXSize() {
        return xSize;
    }

    /**
     * Retrieves the vertical size of the drawable area
     *
     * @return vertical size in px
     */
    public int getYSize() {
        return ySize;
    }

    /**
     * Sends the selected {@link GraphicE} to the bottom of the canvas, so other
     * elements will be painted on top
     *
     * @param element {@link GraphicE} to be send to the bottom
     */
    public void sendToBottom(GraphicE element){
        synchronized (elements){
            if (elements.contains(element)){
                elements.remove  (element);
                elements.addFirst(element);
                redraw(false);
            }
        }
    }

    /**
     * Sends the selected {@link GraphicE} to the top of the canvas, so it will
     * be painted on top of all the other elements.<br>
     * <i>Note:</i> if a new element is added it will land on top of this one.
     *
     * @param element {@link GraphicE} to be send to the front
     */
    public void sendToFront(GraphicE element){
        synchronized (elements){
            if (elements.contains(element)){
                elements.remove(element);
                elements.addLast(element);
                redraw(false);
            }
        }
    }

    /**
     * Adds a {@link GraphicE} to the canvas.<br>
     * This method doesn't check if the element is already contained on the
     * canvas, this mean, that you can add elements twice (With no particular
     * gain).
     *
     * @param element element that will be added
     * @see Canvas#contains(com.dkt.graphics.elements.GraphicE)
     * @see Graphic#add(com.dkt.graphics.elements.GraphicE)
     */
    public void add(GraphicE element){
        if (element == null) {
            return;
        }

        synchronized (elements){
            elements.add(element);
            redraw(false);
        }
    }

    /**
     * Tells if a {@link GraphicE} is already being painted on the canvas.
     *
     * @param element element to test
     * @return {@code true} if the element is contained and {@code false}
     * otherwise
     */
    public boolean contains(GraphicE element){
        if (element == null) {
            return false;
        }

        synchronized (elements){
            return elements.contains(element);
        }
    }

    /**
     * Removes a given {@link GraphicE} from the canvas
     *
     * @param element element to remove
     * @return {@code true} if the element was contained and {@code false}
     * otherwise
     */
    public boolean remove(GraphicE element){
        if (element == null) {
            return false;
        }

        synchronized (elements){
            boolean stat = elements.remove(element);
            redraw(false);
            return stat;
        }
    }

    @Override
    public void removeAll(){
        super.removeAll();

        synchronized (elements){
            elements.clear();
        }

        synchronized (fixed){
            fixed.clear();
        }
    }

    /**
     * Adds a new fixed {@link GraphicE} to the canvas
     *
     * @param element element that you want to add
     */
    public void addFixed(GraphicE element){
        if (element == null) {
            return;
        }

        synchronized (fixed){
            fixed.add(element);
            redraw(true);
        }
    }

    /**
     * Removes a given fixed {@link GraphicE} from the canvas
     *
     * @param element element to remove
     * @return {@code true} if the element was contained and {@code false}
     * otherwise
     */
    public boolean removeFixed(GraphicE element){
        if (element == null) {
            return true;
        }

        synchronized (fixed){
            boolean stat = fixed.remove(element);
            redraw(stat);
            return stat;
        }
    }

    @Override
    public void setBackground(Color bg) {
        if (bg == null){
            throw new IllegalArgumentException("The background color can't be null");
        }

        super.setBackground(bg);

        //Since this method is called by the super class constructor so the
        //class might not be completly initialized when this method is called
        //the first time
        if (fixed != null & elements != null){
            redraw(true);
        }
    }

    /**
     * Retrieves the {@link Paint} used as background on the drawable area of
     * the canvas
     *
     * @return drawable area background paint
     */
    public Paint getDrawableAreaPaint() {
        return drawableAreaPaint;
    }

    /**
     * Sets the background {@link Paint} of the drawable area of the canvas
     *
     * @param paint new paint
     * @throws IllegalArgumentException if the color is {@code null}
     */
    public void setDrawableAreaPaint(Paint paint) {
        if (paint == null){
            throw new IllegalArgumentException("The paint can't be null");
        }

        drawableAreaPaint = paint;
        redraw(true);
    }

    /**
     * Retrieves the {@link Paint} of the border of the drawable area
     *
     * @return drawable area border paint
     */
    public Paint getDrawableBorderPaint() {
        return drawableBorderPaint;
    }

    /**
     * Sets the border {@link Paint} of the drawable area of the canvas
     *
     * @param paint new paint
     * @throws IllegalArgumentException if the paint is {@code null}
     */
    public void setDrawableBorderPaint(Paint paint) {
        if (paint == null){
            throw new IllegalArgumentException("The paint can't be null");
        }

        drawableBorderPaint = paint;
        redraw(true);
    }

    /**
     * Tells the canvas to invert the Y axis.<br>
     * This method comes in handy when plotting functions or working with
     * physics since it gives a more <i>natural</i> way of drawing. But should
     * be ignored most of the time, since it might break other transforms.
     * <br><br>
     * <i><b>WARNING: </b></i> this method does it's magic using an
     * {@link AffineTransform}, so this basically means that it draws in the
     * conventional way and then invert the image, so text and images will
     * appear inverted.
     *
     * @param invert {@code true} if you wish to invert the Y axis and
     * {@code false} otherwise
     */
    public void setInvertYAxis(boolean invert){
        this.invert = invert;
        redraw(true);
    }

    /**
     * Tells if the canvas is inverting the Y axis
     *
     * @return {@code true} if the canvas is inverting the Y axis and
     * {@code false} otherwise
     */
    public boolean invertYAxis() {
        return invert;
    }

    /**
     * Moves the origin of coordinates to the given position, this method is
     * very useful when working with mathematical functions since it gives a
     * more natural way of drawing things.
     *
     * @param x new X coordinate of the origin
     * @param y new Y coordinate of the origin
     */
    public void moveOrigin(int x, int y){
        xO = x;
        yO = y;
        redraw(true);
    }

    /**
     * This method redraws the background and then repaints the canvas
     */
    public void repaintWBackground(){
        redraw(true);
        repaint();
    }

    private void calcTransform() {
        transform = AffineTransform.getTranslateInstance(xO, yO);

        if (invert){
            transform.concatenate(AffineTransform.getScaleInstance(1, -1));
        }
    }

    private boolean centerOrigin;
    /**
     * Tells the canvas to center the origin of coordinates within the drawable
     * area.
     *
     * @param center {@code true} if you want to center the origin and
     * {@code false} otherwise
     */
    public void setCenterOrigin(boolean center) {
        centerOrigin = center;

        if (center){
            moveOrigin(getXSize() / 2, getYSize() / 2);
        } else {
            moveOrigin(0, 0);
        }
    }

    /**
     * Tells if the canvas is rendering using anti-aliasing.<br>
     * <i>Note:</i> the default value is {@code true}
     *
     * @return {@code true} if anti-alising is on and {@code false} otherwise
     */
    public boolean useAntiAliasing() {
        return useAntiAliasing;
    }

    /**
     * Turns anti-aliasing <tt>on</tt> or <tt>off</tt>
     * 
     * @param useAntiAliasing {@code true} to turn anti-aliasing on, and {@code
     * false} to turn it off.
     */
    public void setUseAntiAliasing(boolean useAntiAliasing) {
        this.useAntiAliasing = useAntiAliasing;
        redraw(true);
    }

    /**
     * Tells the canvas to repaint itself automatically
     *
     * @param repaint {@code true} if the canvas should repaint itself and
     * {@code false} otherwise
     * @see Canvas#setRepaintDelay(int)
     */
    public void setAutoRepaint(boolean repaint){
        autoRepaint = repaint;
        if (repaint){
            repaintTimer.start();
        } else {
            repaintTimer.stop();
        }
    }

    /**
     * Tells if the canvas is repainting itself automatically
     *
     * @return repaint {@code true} if the canvas is repainting itself and
     * {@code false} otherwise
     * @see Canvas#setRepaintDelay(int)
     */
    public boolean autoRepaint(){
        return autoRepaint;
    }

    /**
     * The time in ms of the repaint interval.<br>
     * This will not guaranty that each is repaint is done every n ms, it will
     * only call the repaint method every n ms.
     *
     * @param delay time in ms
     * @throws InvalidArgumentException if the delay is less than 1
     * @see Canvas#setAutoRepaint(boolean)
     */
    public void setRepaintDelay(int delay){
        if (delay < 1){
            throw new InvalidArgumentException("Delay MUST be a positive real");
        }

        repaintDelay = delay;
        repaintTimer.setDelay(repaintDelay);
    }

    /**
     * Retrieves the time in ms for the canvas to repaint itself
     *
     * @return time in ms
     * @see Canvas#setAutoRepaint(boolean)
     */
    public long repaintDelay(){
        return repaintDelay;
    }

    /**
     * Tells if the canvas is centering automatically the origin of coordinates.
     * <pre>
     *        not centered           centered
     *       +------------+       +------------+
     *       |*(0,0)      |       |            |
     *       |            |       |            |
     *       |            |       |     *(0,0) |
     *       |            |       |            |
     *       |   canvas   |       |   canvas   |
     *       +------------+       +------------+</pre>
     *
     * @return {@code true} if the origin is centered and {@code false}
     * otherwise
     */
    public boolean centerOrigin(){
        return centerOrigin;
    }

    /**
     * This method tells the canvas to print the current FPS value on the screen
     * (it will be painted on the upper left corner above all other elements).
     * <br><i>Note:</i> if {@code showFPS} is set to {@code true} it will most
     * likely have a small impact on performance (usually we draw hundreds of
     * thousands of {@link GraphicE}, but in small applications with very high
     * FPS the impact is quite noticeable).
     *
     * @param show {@code true} if you want to show the FPS and {@code false}
     * otherwise
     */
    public void setShowFPS(boolean show){
        if (show){
            tps.reset();
        }
        this.showFPS = show;
    }

    private transient BufferedImage background;
    private void redraw(boolean with_background){
        calcTransform();

        if (with_background){
            createBackground();
            if (getParent() != null){
                getParent().repaint();
            }
        }
    }

    private void createBackground() {
        //Release resourses
        if (background != null){
            background.flush();
        }

        //When a resize event happened is possible that the screen configuration
        //has changed
        GFX_CFG = GraphicsEnvironment
                        .getLocalGraphicsEnvironment()
                        .getDefaultScreenDevice()
                        .getDefaultConfiguration();

        //New background image
        background = GFX_CFG.createCompatibleImage(
                getWidth () + 2,
                getHeight() + 2
        );

        Graphics2D g2d = background.createGraphics();

        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                useAntiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON
                                : RenderingHints.VALUE_ANTIALIAS_OFF
        );

        //Paint background color
        g2d.setBackground(getBackground());
        g2d.clearRect(0, 0, getWidth(), getHeight());

        //Calculate affine transform for the background
        final AffineTransform btransform;
        if (centerBounds){
            final int txoff = xO + (getWidth () - xSize) / 2;
            final int tyoff = yO + (getHeight() - ySize) / 2;

            btransform = AffineTransform.getTranslateInstance(txoff, tyoff);
        } else {
            btransform = AffineTransform.getTranslateInstance(xO, yO);
        }

        if (invert){
            btransform.concatenate(AffineTransform.getScaleInstance(1, -1));
        }

        //Calculate background offsets
        final int xOff;
        final int yOff;
        if (centerBounds){
            xOff = (getWidth () - xSize) / 2;
            yOff = (getHeight() - ySize) / 2;
        } else {
            xOff = 0;
            yOff = 0;
        }

        //Draw 'drawing' area
        g2d.setPaint(drawableAreaPaint);
        g2d.fillRect(xOff, yOff, xSize, ySize);
        g2d.setPaint(drawableBorderPaint);

        //The extra px is for the border (looks nicer)
        g2d.drawRect(xOff - 1, yOff - 1, xSize + 1, ySize + 1);
        g2d.clipRect(xOff    , yOff    , xSize    , ySize    );

        g2d.setTransform(btransform);

        //Draw all fixed elements
        synchronized (fixed){
            for (GraphicE element : fixed){
                element.draw(g2d);
            }
        }

        g2d.dispose();
    }

    /**
     * Paints the canvas drawable area on the given graphics
     *
     * @param g2d Where to paint
     * @param back {@code true} if you want to paint the background and {@code
     * false} otherwise ({@code false} is needed when painting with transparent
     * components.
     * @throws IllegalArgumentException if the g2d is {@code null}
     */
    public void paintDrawableArea(Graphics2D g2d, boolean back) {
        if (g2d == null){
            throw new IllegalArgumentException("Graphics can't be null");
        }

        //Paint the background
        if (back) {
            g2d.setPaint(drawableAreaPaint);
            g2d.fillRect(0, 0, xSize, ySize);
        }

        //Set the coordinate transform
        g2d.setTransform(transform);

        //Set anti-aliasing
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                useAntiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON
                                : RenderingHints.VALUE_ANTIALIAS_OFF
        );

        //Draw all fixed components
        synchronized (fixed){
            for (GraphicE element : fixed){
                element.draw(g2d);
            }
        }

        //Draw all volatile components
        synchronized (elements){
            for (GraphicE element : elements){
                element.draw(g2d);
            }
        }
    }

    @Override
    public void paintComponent (Graphics g){
        super.paintComponent(g);

        final BufferedImage content = GFX_CFG.createCompatibleImage(
                xSize,
                ySize,
                Transparency.TRANSLUCENT
        );

        //Try to accelerate the image
        content.setAccelerationPriority(1);

        Graphics2D g2d = content.createGraphics();
        g2d.setRenderingHint(
                RenderingHints.KEY_ANTIALIASING,
                useAntiAliasing ? RenderingHints.VALUE_ANTIALIAS_ON
                                : RenderingHints.VALUE_ANTIALIAS_OFF
        );

        //Don't allow drawing outside the drawable area
        g2d.clipRect(0, 0, xSize, ySize);

        //Draw all elements
        g2d.setTransform(transform);
        synchronized (elements){
            for (GraphicE element : elements){
                element.draw(g2d);
            }
        }

        //Paint the FPS number on the screen
        if (showFPS){
            tps.action();
            g2d.setTransform(emptyTran);
            fps.setString(formatter.format(tps.ctps()));
            fps.draw(g2d);
        }

        //Dispose of new image graphics
        g2d.dispose();

        //Image bounds
        final int xOff, yOff;
        if (centerBounds){
            xOff = (getWidth () - xSize) / 2;
            yOff = (getHeight() - ySize) / 2;
        } else {
            xOff = 0;
            yOff = 0;
        }

        //Draw back
        g.drawImage(background, 0, 0, null);
        //Draw front
        g.drawImage(content, xOff, yOff, null);
    }

    @Override
    public void actionPerformed(ActionEvent ae) {
        repaint();
    }

    /**
     * Tells if a given point is contained in the drawing area.
     *
     * @param x X coordinate of the point
     * @param y Y coordinate of the point
     * @return {@code true} if the point is contained in the drawing area, and
     * {@code false} otherwise
     */
    public boolean inDrawingArea(int x, int y) {
        final int xOff, yOff;
        if (centerBounds){
            xOff = (getWidth () - xSize) / 2;
            yOff = (getHeight() - ySize) / 2;
        } else {
            xOff = 0;
            yOff = 0;
        }

        return xOff <= x & x <= xOff + xSize &
               yOff <= y & y <= yOff + ySize;
    }

    /**
     * Retrieves the horizontal offset of the drawing area in the canvas.
     *
     * @return horizontal offset in px
     */
    public int getXOff() {
        return centerBounds ? (getWidth () - xSize) / 2 : 0;
    }

    /**
     * Retrieves the vertical offset of the drawing area in the canvas.
     *
     * @return vertical offset in px
     */
    public int getYOff() {
        return centerBounds ? (getHeight() - ySize) / 2 : 0;
    }
}
