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

import com.dkt.graphics.elements.GArc;
import com.dkt.graphics.elements.GCircle;
import com.dkt.graphics.elements.GFillableE;
import com.dkt.graphics.elements.GLine;
import com.dkt.graphics.elements.GOval;
import com.dkt.graphics.elements.GPath;
import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GPoly;
import com.dkt.graphics.elements.GRectangle;
import com.dkt.graphics.elements.GRegPoly;
import com.dkt.graphics.elements.GString;
import com.dkt.graphics.elements.GVector;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Stroke;
import java.awt.geom.NoninvertibleTransformException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * This class came as an exercise to create {@link Graphic} objects from text
 * without modifying the classes themselves, it was later used on Graphic
 * Designer, and finally decided to add it to add it to the extras package.<br>
 * This class is pretty handy to create simple drawing based GUIs, since
 * manipulating strings is orders of magnitude easier than using Graphics. This
 * is not meant to be a programming language, it's simply a way of creating
 * simple (and not so simple) graphics.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GraphicCreator {
    private final Pattern linespl = Pattern.compile("\\s+");
    private final Pattern comment = Pattern.compile("(?=[#])(.*\\n?)(?=\\n)");
    private final Pattern initial = Pattern.compile("[\\n|;]+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

    private int errCount, lineCount;
    private GradientPaint paint = null;
    private Stroke stroke = null;
    private Color color   = null;
    private Color colorf  = null;
    private Font font     = null;

    private static final Class<?> GCC = GraphicCreator.class;
    private static final Class<?> SAC = String[].class;
    private GClip clip;
    private GTransform transf;

    public GraphicCreator() {

    }

    private GraphicCreator(GraphicCreator gc) {
        transf = gc.transf;
        stroke = gc.stroke;
        colorf = gc.colorf;
        paint  = gc.paint;
        color  = gc.color;
        font   = gc.font;
        clip   = gc.clip;
    }

    public void clear() {
        transf = null;
        stroke = null;
        colorf = null;
        color  = null;
        paint  = null;
        font   = null;
        clip   = null;
    }

    public Graphic parse(String line) {
        //Remove comments
        String data    = comment.matcher(line).replaceAll("");
        //Split avoiding what's between " "
        String[] lines = initial.split(data);
        return parse(lines);
    }

    /**
     * Parses a {@code String} (that respects the documentation) into a
     * {@link Graphic}
     *
     * @param lines String representation of the graphic
     * @return {@code Graphic} object
     */
    public Graphic parse(String... lines) {
        final Graphic graphic = new Graphic(lines.length + 10);
        errCount = 0;
        lineCount = lines.length;

        for (final String line : lines) {
            final Object out = parseOne(line);

            if (out instanceof GraphicE) {
                final GraphicE ge = (GraphicE)out;
                graphic.add(ge);

                if (stroke != null) {
                    ge.setStroke(stroke);
                }

                if (color != null) {
                    ge.setPaint(color);
                }

                if (out instanceof GFillableE) {
                    final GFillableE gf = (GFillableE)out;

                    if (paint != null) {
                        gf.setFillPaint(paint);
                        gf.setFill(true);
                        paint = null;
                    } else if (colorf != null) {
                        gf.setFillPaint(colorf);
                        gf.setFill(true);
                    }
                } else {
                    if (paint != null) {
                        ge.setPaint(paint);
                        paint = null;
                    }
                }
            } else if (out instanceof Color) {
                if (line.contains("colorf")) {
                    colorf = (Color)out;
                } else {
                    color  = (Color)out;
                }
            } else if (out instanceof Stroke) {
                stroke = (Stroke)out;
            } else if (out instanceof Font) {
                font = (Font)out;
            } else if (out instanceof GradientPaint) {
                paint = (GradientPaint)out;
            }
        }

        return graphic;
    }

    private Object parseOne(String line) {
        if (line == null) {
            return null;
        }

        String lline = line.trim();

        try {
            if (lline.isEmpty() ||
                lline.charAt(0) == '#' ||
                lline.charAt(0) == '!') {
                return null;
            }

            final String[] foo  = linespl.split(lline);
            final Method method = GCC.getMethod(foo[0], SAC);
            final Object out    = method.invoke(this, (Object)foo);

            return out;

        } catch (SecurityException |
                 NoSuchMethodException |
                 IllegalAccessException |
                 InvalidArgumentException |
                 InvocationTargetException ex) {
            errCount++;
        }

        return null;
    }

    /**
     * Retrieves the number of errors encountered during the parsing of a
     * given text.
     *
     * @return error count
     */
    public int getErrorCount() {
        return errCount;
    }

    public int getTotalLineCount() {
        return lineCount;
    }

    public Stroke stroke(String[] args) {
        final float w = Float.parseFloat(args[1]);

        int cap  = BasicStroke.CAP_SQUARE;
        int join = BasicStroke.JOIN_BEVEL;

        if (args.length <= 3) {
            return new BasicStroke(w, cap, join);
        }

        float offset = 0;
        boolean offsetSet = false;
        final ArrayList<Float> dashes = new ArrayList<>(args.length - 1);
        for (int i = 2; i < args.length; i++) {
            String arg = args[i].toLowerCase();
            switch (arg) {
                case "cap_butt"   : cap  = BasicStroke.CAP_BUTT  ; break;
                case "cap_round"  : cap  = BasicStroke.CAP_ROUND ; break;
                case "cap_square" : cap  = BasicStroke.CAP_SQUARE; break;
                case "join_bevel" : join = BasicStroke.JOIN_BEVEL; break;
                case "join_mitter": join = BasicStroke.JOIN_MITER; break;
                case "join_round" : join = BasicStroke.JOIN_ROUND; break;
                default:
                    if (!offsetSet){
                        offset = Float.parseFloat(arg);
                        offsetSet = true;
                    } else {
                        dashes.add(Float.parseFloat(arg));
                    }
            }
        }

        final float[] arr = new float[dashes.size()];
        for (int i = 0; i < arr.length; i++) {
            arr[i] = dashes.get(i);
        }

        return new BasicStroke(w, cap, join, 0, arr, offset);
    }

    /**
     * Clears the global configurations {@code color}, {@code colorf}, {@code
     * stroke}, {@code paint} and {@code font}
     *
     * @param args these are ignored
     * @return {@code null}
     */
    public Object reset(String[] args) {
        checkArgs(args, 0);
        clear();
        return null;
    }

    /**
     * Sets a global font, if the argument is {@code no} then it will reset the
     * global font
     *
     * @param args the arguments will be used in {@link Font#decode(String)},
     * so for further documentation, please refer to it
     * @return Font
     */
    public Font font(String[] args) {
        if (args.length == 2 && args[1] != null && args[1].equals("no")){
            font = null;
            return null;
        }

        String name = concatenate(args, 1);

        return Font.decode(name);
    }

    /**
     * Set's the global fill color. If the argument is {@code no} then it will
     * reset the fill color. If this color is set all {@link GFillableE} will
     * be set to fill.<br>This color is also used as the secondary color on
     * gradients.<br><ul>
     * <li>If 3 arguments are passed there should be: {@code R[0-255] G[0-255]
     * B[0-255]},
     * <li>if 4 arguments are passed the format should be: {@code A[0-255]
     * R[0-255] G[0-255] B[0-255]}</ul>
     *
     * @param args color info
     * @return Fill color
     * @see GraphicCreator#color(String[])
     * @see GraphicCreator#gradient(String[])
     * @see GraphicCreator#gradient2(String[])
     * @see GFillableE#setFill(boolean)
     */
    public Color colorf(String[] args) {
        if (args.length == 2 &&
            args[1] != null &&
            args[1].equals("no")) {
            colorf = null;
            return null;
        }
        return color(args);
    }

    /**
     * Set's the global color.
     * This color is also used as the primary color on gradients.<br><ul>
     * <li>If 3 arguments are passed there should be: {@code R[0-255] G[0-255]
     * B[0-255]},
     * <li>if 4 arguments are passed the format should be: {@code A[0-255]
     * R[0-255] G[0-255] B[0-255]}</ul>
     *
     * @param args color info
     * @return Fill color
     * @see GraphicCreator#colorf(String[])
     * @see GraphicCreator#gradient(String[])
     * @see GraphicCreator#gradient2(String[])
     * @see GFillableE#setFill(boolean)
     */
    public Color color(String[] args) {
        checkArgs(args, 1, 3, 4);
        switch(args.length -1) {
            case 1: //[argb]
                return new Color(getInt(args[1]), true);
            case 3: //[r, g, b]
            {
                final int r = getInt(args[1], 0, 255);
                final int g = getInt(args[2], 0, 255);
                final int b = getInt(args[3], 0, 255);
                return new Color(r, g, b);
            }
            case 4: //[a, r, g, b]
            {
                final int a = getInt(args[1], 0, 255);
                final int r = getInt(args[2], 0, 255);
                final int g = getInt(args[3], 0, 255);
                final int b = getInt(args[4], 0, 255);
                return new Color(r, g, b, a);
            }
        }
        return null;
    }

    /**
     * Creates a linear gradient between {@code color} and {@code colorf}, so
     * they need to be set before calling this method.<br>
     * The arguments should be {@code x1, y1, x2, y2}, that is, the coordinates
     * of the first point of the gradient vector and the coordinates of the last
     * point
     *
     * @param args vector arguments
     * @return Linear gradient
     * @see GraphicCreator#color(String[])
     * @see GraphicCreator#colorf(String[])
     * @see GraphicCreator#gradient2(String[])
     */
    public GradientPaint gradient(String[] args) {
        return grad(args, false);
    }

    /**
     * Creates a linear cyclic gradient between {@code color} and
     * {@code colorf}, so they need to be set before calling this method.<br>
     * The arguments should be {@code x1, y1, x2, y2}, that is, the coordinates
     * of the first point of the gradient vector and the coordinates of the last
     * point
     *
     * @param args vector arguments
     * @return Linear cyclic gradient
     * @see GraphicCreator#color(String[])
     * @see GraphicCreator#colorf(String[])
     * @see GraphicCreator#gradient(String[])
     */
    public GradientPaint gradient2(String[] args) {
        return grad(args, true);
    }

    private GradientPaint grad(String[] args, boolean cyclic) {
        checkArgs(args, 4);
        return new GradientPaint(
                getInt(args[1]),
                getInt(args[2]),
                color,
                getInt(args[3]),
                getInt(args[4]),
                colorf,
                cyclic
        );
    }

    public Graphic for1(String[] args) {
        final int i = getInt(args[1]);
        final int s = getInt(args[2]);
        final int f = getInt(args[3]);

        if (s == 0) {
            return new Graphic();
        }

        String txt = concatenate(args, 4);
        //Check the format before creating the arrays
        String.format(txt, 0, 0, 0);
        txt = txt.replace("\"", "");

        StringBuilder foo = new StringBuilder(txt.length() * (f - i) / s * 2);
        final int[] inter = getInterval(i, s, f);

        for (int ii : inter) {
           foo.append(String.format(txt, ii)).append("\n");
        }

        String[] lines = foo.toString().split("\\n|;");
        lineCount+= lines.length;

        return new GraphicCreator(this).parse(lines);
    }

    public Graphic for2(String[] args) {
        final int i1 = getInt(args[1]);
        final int s1 = getInt(args[2]);
        final int f1 = getInt(args[3]);
        final int i2 = getInt(args[4]);
        final int s2 = getInt(args[5]);
        final int f2 = getInt(args[6]);

        if (s1 == 0 | s2 == 0) {
            return new Graphic();
        }

        String txt = concatenate(args, 7);
        //Check the format before creating the arrays
        String.format(txt, 0, 0, 0);
        txt = txt.replace("\"", "");

        final int[] int1 = getInterval(i1, s1, f1);
        final int[] int2 = getInterval(i2, s2, f2);

        StringBuilder foo = new StringBuilder(txt.length() * (f1 - i1) / s1 * 2);

        final int n = Math.min(int1.length, int2.length);
        for (int i = 0; i < n; i++) {
           foo.append(String.format(txt, int1[i], int2[i])).append("\n");
        }

        String[] lines = foo.toString().split("\\n|;");
        lineCount+= lines.length;

        return new GraphicCreator(this).parse(lines);
    }

    public Graphic for3(String[] args) {
        final int i1 = getInt(args[1]);
        final int s1 = getInt(args[2]);
        final int f1 = getInt(args[3]);
        final int i2 = getInt(args[4]);
        final int s2 = getInt(args[5]);
        final int f2 = getInt(args[6]);

        if (s1 == 0 | s2 == 0) {
            return new Graphic();
        }

        String txt = concatenate(args, 7).replace("\"", "");
        //Check the format before creating the arrays
        String.format(txt, 0, 0, 0);

        StringBuilder foo = new StringBuilder(txt.length() * (f1 - i1) / s1 * 2);

        final int[] int1 = getInterval(i1, s1, f1);
        final int[] int2 = getInterval(i2, s2, f2);

        for (int i : int1) {
            for (int j : int2) {
                foo.append(String.format(txt, i, j)).append("\n");
            }
        }

        String[] lines = foo.toString().split("\\n|;");
        lineCount+= lines.length;

        return new GraphicCreator(this).parse(lines);
    }

    public Graphic for4(String[] args) {
        final int i1 = getInt(args[1]);
        final int s1 = getInt(args[2]);
        final int f1 = getInt(args[3]);
        final int i2 = getInt(args[4]);
        final int s2 = getInt(args[5]);
        final int f2 = getInt(args[6]);
        final int i3 = getInt(args[7]);
        final int s3 = getInt(args[8]);
        final int f3 = getInt(args[9]);

        if (s1 == 0 | s2 == 0) {
            return new Graphic();
        }

        String txt = concatenate(args, 10).replace("\"", "");
        //Check the format before creating the arrays
        String.format(txt, 0, 0, 0);

        StringBuilder foo = new StringBuilder(txt.length() * (f1 - i1) / s1 * 2);


        final int[] int1 = getInterval(i1, s1, f1);
        final int[] int2 = getInterval(i2, s2, f2);
        final int[] int3 = getInterval(i3, s3, f3);

        for (int i : int1) {
            for (int j : int2) {
                for (int k : int3) {
                    foo.append(String.format(txt, i, j, k)).append("\n");
                }
            }
        }

        String[] lines = foo.toString().split("\\n|;");
        lineCount+= lines.length;

        return new GraphicCreator(this).parse(lines);
    }

    public GClip clipadd(String[] args) {
        if (clip == null) {
            clip = new GClip();
            clip.add((GFillableE)parseOne(concatenate(args, 1)));
            return clip;
        }
        clip.add((GFillableE)parseOne(concatenate(args, 1)));
        return null;
    }

    public GClip clipsub(String[] args) {
        if (clip == null) {
            clip = new GClip();
            clip.subtract((GFillableE)parseOne(concatenate(args, 1)));
            return clip;
        }
        clip.subtract((GFillableE)parseOne(concatenate(args, 1)));
        return null;
    }

    public GClip clipoff(String[] args) {
        checkArgs(args, 0);
        clip = null;
        return new GClip();
    }

    public GTransform scale(String[] args) {
        checkArgs(args, 2);
        if (transf == null) {
            transf = new GTransform(
                getDouble(args[1]),
                getDouble(args[2])
            );
            return transf;
        }
        transf.scale(
            getDouble(args[1]),
            getDouble(args[2])
        );
        return null;
    }

    public GTransform rotate(String[] args) {
        checkArgs(args, 3);
        if (transf == null) {
            transf = new GTransform(
                getInt(args[1]),
                getInt(args[2]),
                getDouble(args[3])
            );
            return transf;
        }
        transf.rotate(
            getInt(args[1]),
            getInt(args[2]),
            getDouble(args[3])
        );
        return null;
    }

    public GTransform traslate(String[] args) {
        checkArgs(args, 2);
        if (transf == null) {
            transf = new GTransform(1, 1);
            transf.traslate(
                getInt(args[1]),
                getInt(args[2])
            );
            return transf;
        }
        transf.traslate(
            getInt(args[1]),
            getInt(args[2])
        );
        return null;
    }

    public GTransform transoff(String[] args) throws NoninvertibleTransformException {
        checkArgs(args, 0);
        GTransform foo = transf.invert();
        transf = null;
        return foo;
    }

    /**
     * Creates a new point.<br>
     * The first two arguments should be {@code x} and {@code y} coordinates of
     * the point. The third optional argument is the cross size used to
     * represent the point
     *
     * @param args point arguments
     * @return {@code point}
     */
    public GPoint point(String[] args) {
        checkArgs(args, 2, 3);
        switch (args.length - 1) {
            case 2://[x, y]
            {
                final int x = getInt(args[1]);
                final int y = getInt(args[2]);
                return new GPoint(x, y);
            }
            case 3://[x, y, cs]
            {
                final int x = getInt(args[1]);
                final int y = getInt(args[2]);
                final int c = getInt(args[3]);
                return new GPoint(x, y, c);
            }

        }
        return null;
    }

    /**
     * Creates a line based on cartesian coordinates.<br>
     * The arguments are {@code x1, y1, x2, y2}.
     *
     * @param args coordinates of the line
     * @return Line
     */
    public GLine linec(String[] args) {
        checkArgs(args, 4);

        final int x1 = getInt(args[1]);
        final int y1 = getInt(args[2]);
        final int x2 = getInt(args[3]);
        final int y2 = getInt(args[4]);

        return new GLine(x1, y1, x2, y2);
    }

    /**
     * Creates a line based on polar coordinates.<br>
     * The arguments are {@code x, y, l, a}. With {@code x, y} being the initial
     * point, {@code l} the length of the vector and {@code a} the angle in
     * degrees.
     *
     * @param args coordinates of the line
     * @return Line
     */
    public GLine linep(String[] args) {
        checkArgs(args, 4);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final double l = getDouble(args[3]);
        final double a = getDouble(args[4]);

        return new GLine(x, y, l, a);
    }

//* lpath  [x1, y1, x2, y2, ..., xn, yn]
    public GPath lpath(String[] args) {
        GPath path = new GPath(args.length / 2);

        for (int i = 1; i < args.length; i += 2) {
            final int x = getInt(args[i]);
            final int y = getInt(args[i + 1]);

            path.append(x, y);
        }

        return path;
    }

//* rectf  [x1, y1, x2, y2]
    public GRectangle rectf(String[] args) {
        checkArgs(args, 4);

        final int x1 = getInt(args[1]);
        final int y1 = getInt(args[2]);
        final int x2 = getInt(args[3]);
        final int y2 = getInt(args[4]);

        final int w = Math.abs(x1 - x2);
        final int h = Math.abs(y1 - y2);
        final int x = Math.min(x1, x2) + w / 2;
        final int y = Math.min(y1, y2) + h / 2;

        return new GRectangle(x, y, w, h);
    }

//* rectm  [x, y, w, h]
    public GRectangle rectc(String[] args) {
        checkArgs(args, 4);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final int w = getInt(args[3]);
        final int h = getInt(args[4]);

        return new GRectangle(x, y, w, h);
    }

//* circle  [x, y, r]
    public GCircle circle(String[] args) {
        checkArgs(args, 3);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final int r = getInt(args[3]);

        return new GCircle(x, y, r);
    }

//* oval  [x, y, w, h]
    public GOval oval(String[] args) {
        checkArgs(args, 4);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final int w = getInt(args[3]);
        final int h = getInt(args[4]);

        return new GOval(x, y, w, h);
    }

//* polyp  [x1, y1, x2, y2, ..., xn, yn]
    public GPoly polyp(String[] args) {
        final GPoly poly = new GPoly(args.length / 2);
        final int n = args.length;

        for (int i = 1; i < n; i += 2) {
            final int x = getInt(args[i]);
            final int y = getInt(args[i + 1]);

            poly.append(x, y);
        }

        return poly;
    }

//* polyn  [x, y, r, n] [x, y, r, n, a]
    public GRegPoly polyn(String[] args) {
        checkArgs(args, 4, 5);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final int r = getInt(args[3]);
        final int n = getInt(args[4]);
        int a = 0;

        if (args.length == 6) {
            a = getInt(args[5]);
        }

        return new GRegPoly(x, y, r, n, a);
    }

//* vectc  [x1, y1, x2, y2]
    public GVector vectc(String[] args) {
        checkArgs(args, 4);

        final int x1 = getInt(args[1]);
        final int y1 = getInt(args[2]);
        final int x2 = getInt(args[3]);
        final int y2 = getInt(args[4]);

        return new GVector(x1, y1, x2, y2);
    }

//* vectp  [x, y, l, a]
    public GVector vectp(String[] args) {
        checkArgs(args, 4);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final double l = getDouble(args[3]);
        final double a = getDouble(args[4]);

        return new GVector(x, y, l, a);
    }

//* arc    [x, y, r, s, o] [x, y, w, h, s, a]
    public GArc arc(String[] args) {
        checkArgs(args, 6);

        final int x  = getInt(args[1]);
        final int y  = getInt(args[2]);
        final int w  = getInt(args[3]);
        final int h  = getInt(args[4]);
        final int sa = getInt(args[5]);
        final int so = getInt(args[6]);

        return new GArc(x, y, w, h, sa, so);
    }

    public GArc arcc(String[] args) {
        checkArgs(args, 5);

        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final int r = getInt(args[3]);
        final int s = getInt(args[4]);
        final int o = getInt(args[5]);

        return new GArc(x, y, r, s, o);
    }

    public GString string(String[] args) {
        final int x = getInt(args[1]);
        final int y = getInt(args[2]);

        final GString str = new GString(x, y, concatenate(args, 3));
        if (font != null) {
            str.setFont(font);
        }

        return str;
    }

    public GString string2(String[] args) {
        final int x = getInt(args[1]);
        final int y = getInt(args[2]);
        final double a = getDouble(args[3]);

        final GString str = new GString(x, y, a,concatenate(args, 4));
        if (font != null) {
            str.setFont(font);
        }

        return str;
    }

    public GGrid grid(String[] args) {
        checkArgs(args, 4, 6);

        if (args.length - 1 == 4) {
            return new GGrid(
                getInt(args[1]),
                getInt(args[2]),
                getInt(args[3]),
                getInt(args[4])
            );
        } else {
            return new GGrid(
                getInt(args[1]),
                getInt(args[2]),
                getInt(args[3]),
                getInt(args[4]),
                getInt(args[5]),
                getInt(args[6])
            );
        }
    }

    private String concatenate(String[] args, int init) {
        final StringBuilder sb = new StringBuilder(64);

        for (int i = init; i < args.length; i++) {
            sb.append(args[i]);
            sb.append(" ");
        }

        return sb.substring(0, sb.length() - 1);
    }

    private static double getDouble(String ar) {
        return Double.parseDouble(ar);
    }

    private static int getInt(String ar) {
        return Integer.parseInt(ar);
    }

    private static int getInt(String ar, int min, int max) {
        int val = Integer.parseInt(ar);
        val = Math.min(val, max);
        val = Math.max(val, min);
        return val;
    }

    private static void checkArgs(String[] args, int ... lens) {
        final int len = args.length - 1;
        boolean status = false;

        for (int l : lens) {
            status |= len == l;
        }

        if (!status) {
            String msg = "'%s' must have one of the following argument lenghts %s";
            String lengths = Arrays.toString(lens);
            throw new InvalidArgumentException(String.format(msg, args[0], lengths));
        }
    }

    private int[] getInterval(final int i, final int s, final int f) {
        if (s == 0 | i == f) {
            return new int[0];
        }

        int ii = i;
        int ss = Math.abs(s);
        int n;

        if (ii > f) {
            n = (ii - f) / ss + 1;
            ss = -ss;
        } else {
            n = (f - ii) / ss + 1;
        }

        int[] arr = new int[n];

        for (int j = 0; j < n; j++) {
            arr[j] = ii;
            ii += ss;
        }

        return arr;
    }

}
