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
package com.dkt.graphics.utils.config;

/**
 * This interface must be implemented on all objects that should be notified
 * of changes on the {@link Config} class.<br>
 * Since configs are individual objects for each name, this objects must be
 * registered against every configuration that should be controlled
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public interface ConfigListener {
    /**
     * What to do when something changes on the {@link Config} for a given name
     *
     * @param event event that triggered the action
     * @see Config
     * @see ConfigEvent
     */
    void somethingChange(ConfigEvent event);
}
