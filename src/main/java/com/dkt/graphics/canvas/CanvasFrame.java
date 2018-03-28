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

import java.awt.BorderLayout;
import javax.swing.JFrame;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class CanvasFrame extends JFrame {
    private final Canvas canvas = new Canvas();
    public CanvasFrame(String title){
        super(title);
        init();
    }

    public CanvasFrame() {
        init();
    }

    private void init(){
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 600);
        BorderLayout layout = new BorderLayout();
        setLayout(layout);
        add(canvas, BorderLayout.CENTER);
        canvas.setVisible(true);
        canvas.setUseFullArea(true);
    }

    public Canvas getCanvas(){
        return canvas;
    }
}
