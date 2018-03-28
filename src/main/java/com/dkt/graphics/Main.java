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
package com.dkt.graphics;

import com.dkt.graphics.extras.examples.IExample;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class Main {

    public static void main(String[] args) {
        boolean needHelp = true;

        for (String arg : args) {
            try {
                SwingUtilities.invokeLater(contruct(arg));
                needHelp = false;
                break;
            } catch (ClassNotFoundException |
                     InstantiationException |
                     IllegalAccessException ex) {
                Logger.getLogger("Main").log(Level.SEVERE, null, ex);
                System.out.format("Unrecognized option '%s'%n", arg);
            }
        }

        if (needHelp) {
            help();
        }
    }

    private static void help() {
        System.out.println("Usage: java -jar JDrawingLib.jar [args]");
        System.out.println("       java -jar JDrawingLib.jar Example01");
        System.out.println("\nExample list:");
        for (int i = 1; i < 18; i++) {
            try {
                String name = String.format("Example%02d", i);
                String desc = contruct(name).getName();
                System.out.format("\t %s -> %s%n", name, desc);
            } catch (ClassNotFoundException |
                     InstantiationException |
                     IllegalAccessException ex) {
                Logger.getLogger("Main").log(Level.SEVERE, null, ex);
            }
        }
        System.out.println();
    }

    public static IExample contruct(String name) throws ClassNotFoundException,
                                                        InstantiationException,
                                                        IllegalAccessException {
        Class<?> c = Class.forName("com.dkt.graphics.extras.examples." + name);
        return (IExample) c.newInstance();
    }
}
