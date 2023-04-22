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

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class LibUtils {
    /**
     * Don't let anyone initialize this class
     */
    private LibUtils(){}

    /**
     * Retrieves a String representing the library's version
     *
     * @return version string
     */
    public static String versionString(){
        return "0.4.2";
    }

    /**
     * Retrieves the library's URL
     *
     * @return URL
     */
    public static String url(){
        return "https://github.com/dktcoding/jdrawinglib";
    }

    /**
     * Retrieves the list of authors
     *
     * @return authors
     */
    public static String[] authors(){
        return new String[]{
            "Federico Vera <fede@riddler.com.ar>",
        };
    }

    /**
     * Retrieves the list of collaborators
     *
     * @return collaborators
     */
    public static String[] collaborators(){
        return new String[]{
            "Eduardo Nieva <edu@riddler.com.ar>",
        };
    }

    /**
     * Retrieves the license name
     *
     * @return license name
     */
    public static String license(){
        return "GPL v3";
    }

    /**
     * Retrieves the license URL
     *
     * @return license URL
     */
    public static String licenseUrl(){
        return "http://www.gnu.org/licenses/gpl-3.0.html";
    }
}
