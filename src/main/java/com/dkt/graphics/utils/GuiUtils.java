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
package com.dkt.graphics.utils;

import java.awt.Dimension;
import java.awt.Toolkit;
import javax.swing.JDialog;
import javax.swing.JFrame;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public class GuiUtils {
    private GuiUtils() {

    }

    /**
     * Centers a frame on the screen
     *
     * @param frame the frame to center
     * @throws IllegalArgumentException if {@code frame} is {@code null}
     */
    public static void centerFrame(JFrame frame) throws IllegalArgumentException {
        if (frame == null) {
            throw new IllegalArgumentException("The frame can't be null");
        }

        final Dimension s = Toolkit.getDefaultToolkit().getScreenSize();
        final Dimension f = frame.getSize();

        frame.setLocation(
            (s.width  - f.width ) / 2,
            (s.height - f.height) / 2
        );
    }

    /**
     * Centers a dialog in a frame
     *
     * @param dialog the dialog to center
     * @param frame the frame
     * @throws IllegalArgumentException if either {@code dialog} or {@code frame}
     * are {@code null}
     */
    public static void centerDialog(
            JDialog dialog,
            JFrame frame) throws IllegalArgumentException
    {
        if (dialog == null) {
            throw new IllegalArgumentException("The dialog can't be null");
        }

        if (frame == null) {
            throw new IllegalArgumentException("The frame can't be null");
        }

        final Dimension d = dialog.getSize();
//        frame.setLocationByPlatform(true);
        dialog.setLocationRelativeTo(frame);

        dialog.setLocation(
            (frame.getWidth () - d.width ) / 2,
            (frame.getHeight() - d.height) / 2
        );
    }
}
