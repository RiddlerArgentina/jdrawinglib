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
package com.dkt.graphics.utils;

import com.dkt.graphics.canvas.Canvas;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.metadata.IIOMetadataNode;
import javax.imageio.stream.FileImageOutputStream;
import javax.imageio.stream.ImageOutputStream;
import org.w3c.dom.Node;

/**
 * The purpose of this class is to create animated GIFs from a series of canvas
 * snapshots
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 * @since 0.1.10
 */
public class Gif {
    private final ArrayList<Wrapper> snapshots;
    private final Canvas canvas;
    private int delay = 10;

    private Exception exception;

    /**
     * Creates a new Gif object.
     *
     * @param canvas the canvas from which to create the images
     * @throws IllegalArgumentException if {@code canvas} is {@code null}
     * @see Gif#write(String, int, BufferedImage...)
     */
    public Gif (Canvas canvas) throws IllegalArgumentException {
        if (canvas == null) {
            throw new IllegalArgumentException("The canvas can't be null");
        }

        this.canvas = canvas;
        snapshots   = new ArrayList<>(10);
    }

    /**
     * Retrieves the number of snapshots taken
     *
     * @return number of snapshots
     */
    public int size() {
        return snapshots.size();
    }

    /**
     * Removes on of the snapshots
     *
     * @param idx index of the snapshot
     * @return the image that was removed or {@code null} if the index is out
     * of bounds.
     */
    public BufferedImage remove(int idx) {
        try {
            return snapshots.remove(idx).image;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Retrieves one of the snapshots
     *
     * @param idx index of th image
     * @return snapshot or {@code null} if the index is out of bounds
     */
    public BufferedImage get(int idx) {
        try {
            return snapshots.get(idx).image;
        } catch (IndexOutOfBoundsException ex) {
            return null;
        }
    }

    /**
     * Retrieves the delay between images in ms.<br>
     * The default value is 100ms.
     *
     * @return delay between images
     * @see Gif#setDelay(int)
     */
    public int getDelay() {
        return delay * 10;
    }

    /**
     * The delay between images in ms. Note that since the <a href=
     * "http://www.w3.org/Graphics/GIF/spec-gif89a.txt">GIF specification</a>
     * sets the delay in hundredths of a second the number of ms is actually
     * divided by ten.
     *
     * @param delay delay in ms
     * @see Gif#getDelay()
     */
    public void setDelay(int delay) {
        this.delay = delay / 10;
    }

    /**
     * This method should be called every time you want a snapshot of the canvas
     * to be taken. You must take at least 1 in order for the save method to
     * do something.
     *
     * @see Gif#snapshot(int)
     */
    public void snapshot() {
        snapshot(1);
    }

    /**
     * This method calls {@link Gif#snapshot()} a given number of times, which
     * results in (besides an increase in size) the image staying on the gif
     * {@code num} times {@code delay} seconds.
     *
     * @param num number of snapshots
     * @throws InvalidArgumentException if {@code num} is less than 1
     * @see Gif#snapshot()
     */
    public void snapshot(int num) {
        if (num < 1) {
            throw new InvalidArgumentException("num must be bigger than 1");
        }

        synchronized(canvas) {
            snapshots.add(new Wrapper(Utils.getImage(canvas, true), num));
        }
    }

    /**
     * Writes the image to a file, if this method fails it will return {@code
     * false} and the exception will be saved.
     *
     * @param path {@link String} representing the path and name of the file
     * @return {@code true} id the {@code gif} was correctly written and {@code
     * false} if something goes wrong.
     * @throws IllegalArgumentException if {@code path} is {@code null}
     * @see Gif#getLastException()
     * @see Gif#setDelay(int)
     */
    public boolean write(String path) throws IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException("The path can't be null");
        }

        exception = null;

        ArrayList<BufferedImage> imgs = new ArrayList<>(size() * 2);
        for (Wrapper wrap : snapshots) {
            for (int i = 0; i < wrap.num; i++) {
                imgs.ensureCapacity(imgs.size() + wrap.num);
                imgs.add(wrap.image);
            }
        }

        try {
            write(path, delay, imgs.toArray(new BufferedImage[imgs.size()]));
        } catch (IOException ex) {
            Logger.getLogger("Gif").log(Level.SEVERE, null, ex);
            exception = ex;
            return false;
        }

        return true;
    }

    /**
     * Retrieves the last exception in case such exception exists. <br>
     * This method will always return {@code null} before
     * {@link Gif#write(String)} is called for the first time.
     *
     * @return the exception or {@code null} if none exists.
     * @see Gif#write(String)
     */
    public Exception getLastException() {
        return exception;
    }

    /**
     * Creates a new {@code gif} from an array of {@link BufferedImage}.
     *
     * @param path The path in which to save the {@code gif}
     * @param delay The delay between frames in hundredths of a second
     * @param imgs Array of images
     * @throws IllegalArgumentException if {@code path} is {@code null}
     * @throws IOException if something goes wrong when saving the image
     */
    public static void write(
            String path,
            int delay,
            BufferedImage... imgs) throws IllegalArgumentException, IOException
    {
        if (path == null) {
            throw new IllegalArgumentException("The path can't be null");
        }

        final File file = new File(path);

        try (ImageOutputStream fos = new FileImageOutputStream(file);
             Writer gsw = new Writer(fos, delay)) {

            for (BufferedImage img : imgs) {
                gsw.add(img);
            }

        }
    }

    private static class Wrapper {
        private final BufferedImage image;
        private final int num;

        public Wrapper (BufferedImage image, int num) {
            this.image = image;
            this.num   = num;
        }
    }

    /**
     * This class is based on the one created by Elliot Kroo. The original code
     * can be found <a href="http://elliot.kroo.net/software/java/">here</a>.
     */
    private static class Writer implements Closeable {
        private final ImageWriter writer;
        private final ImageWriteParam iwparam;
        private final IIOMetadata mdata;

        private Writer(ImageOutputStream os, int delay) throws IOException {
            final int type = BufferedImage.TYPE_INT_ARGB;

            //Get a writer for the GIF
            writer  = ImageIO.getImageWritersByMIMEType("image/gif").next();
            iwparam = writer.getDefaultWriteParam();

            //Generate the necessary metadata
            final ImageTypeSpecifier typeSpec;
            typeSpec = ImageTypeSpecifier.createFromBufferedImageType(type);
            mdata = writer.getDefaultImageMetadata(typeSpec, iwparam);

            //Set the necessary attributes
            final String fname = mdata.getNativeMetadataFormatName();
            final Node tree = mdata.getAsTree(fname);
            setGCEAttributes(getNode(tree, "GraphicControlExtension"), delay);
            getNode(tree, "ApplicationExtensions").appendChild(new LoopNode());

            mdata.setFromTree(fname, tree);
            writer.setOutput(os);
            writer.prepareWriteSequence(null);
        }

        private void add(RenderedImage img) throws IOException {
            writer.writeToSequence(new IIOImage(img, null, mdata), iwparam);
        }

        @Override
        public void close() throws IOException {
            writer.endWriteSequence();
        }

        private static IIOMetadataNode getNode(Node root, String name) {
            Node node = root.getFirstChild();

            for (;node != null; node = node.getNextSibling()) {
                if (node.getNodeName().equalsIgnoreCase(name)) {
                    return (IIOMetadataNode) node;
                }
            }

            final IIOMetadataNode nnode = new IIOMetadataNode(name);
            root.appendChild(nnode);

            return nnode;
        }

        private static void setGCEAttributes(IIOMetadataNode gcen, int delay) {
            gcen.setAttribute("disposalMethod", "none");
            gcen.setAttribute("userInputFlag", "FALSE");
            gcen.setAttribute("transparentColorFlag", "TRUE");
            gcen.setAttribute("delayTime", Integer.toString(delay));
            gcen.setAttribute("transparentColorIndex", "0");
        }
    }

    private static class LoopNode extends IIOMetadataNode {
        public LoopNode () {
            super("ApplicationExtension");
            setAttribute("applicationID", "NETSCAPE");
            setAttribute("authenticationCode", "2.0");
            setUserObject(new byte[]{1,0,0,0});
        }
    }
}
