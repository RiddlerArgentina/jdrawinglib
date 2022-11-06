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
 * These events are triggered from the {@link Config} class each time something
 * is added, updated or removed.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public interface ConfigEvent {
    /**
     * A new value was added to the {@link Config} object
     */
    int VALUE_ADDED   = 1;
    /**
     * An existing value was updated
     */
    int VALUE_UPDATED = 2;
    /**
     * A value as been removed from the {@link Config} object
     */
    int VALUE_REMOVED = 4;

    /**
     * Retrieves the type of change that has happened
     *
     * @return type of change
     * @see ConfigEvent#VALUE_ADDED
     * @see ConfigEvent#VALUE_REMOVED
     * @see ConfigEvent#VALUE_UPDATED
     */
    int getChangeType();

    /**
     * Tells if the value affects a given key, basically:<pre>
     * if (evt.isKey("my.key")){
     *     //...
     * }
     *
     * is equivalent to:
     *
     * if (evt.getChangedKey().equals("my.key") {
     *     //...
     * }
     * </pre>
     *
     * @param key the {@code key} to compare
     * @return {@code true} if {@code key} is the key that triggered the event
     * and {@code false} otherwise
     * @see ConfigEvent#getChangedKey()
     */
    boolean isKey(String key);

    /**
     * Retrieves the {@code key} that triggered the event
     *
     * @return key
     */
    String getChangedKey();

    /**
     * Retrieves the old value associated with this key
     *
     * @return old value
     */
    Object getOldValue();

    /**
     * Retrieves the new value associated with this key
     *
     * @return new value
     */
    Object getNewValue();
}
