/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2014 <dktcoding [at] gmail>
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

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This class represents a generic Configuration class, with both notifiable
 * and temporary configs (the latter being more of a bucket to store objects
 * during the program's execution). This class supports a {@link ConfigListener}
 * that is fired when (non-temp) key-values are updates, added or removed.
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class Config {
    private static final HashMap<String, Config> CONFIGS = new HashMap<>(5);
    /**
     * Don't let anyone else initialize this class
     */
    private Config () {

    }

    /**
     * Retrieves a {@code Config} instance associated with a given {@code name},
     * this is usually the program's name, or module/plugin name.<br>
     * <pre>
     *
     * Config myApp = Config.getConfig("my.application");
     * myApp.set("key.1", "value.1");
     * ...
     * </pre>
     *
     * @param name programs name
     * @return {@code Config} object
     */
    public static synchronized Config getConfig(String name) {
        if (!CONFIGS.containsKey(name)) {
            CONFIGS.put(name, new Config());
        }

        return CONFIGS.get(name);
    }

    /**
     * Destroys an instance of {@code Config} for a given {@code name}, this
     * is useful when you have more than one application using this Class, and
     * they don't start and stop at the same time.
     *
     * @param name program's name
     */
    public static synchronized void flushConfig(String name) {
        final Config conf = CONFIGS.remove(name);

        if (conf != null) {
            conf.data.clear();
            conf.temp.clear();
            conf.listeners.clear();
        }
    }

    private final HashMap<String, Object> data = new HashMap<>(32);
    private final HashMap<String, Object> temp = new HashMap<>(32);

    /**
     * Set's a temporary {@code key,value} pair in the config
     *
     * @param key
     * @param value
     */
    public void setTemp(String key, Object value) {
        temp.put(key, value);
    }

    /**
     * Retrieves a temporary {@code value} for a given key
     *
     * @param key
     * @return value
     */
    public Object getTemp(String key) {
        return temp.get(key);
    }

    /**
     * Set's a config {@code key,value} pair. This values can be exported and
     * imported.<br>
     * <i>Note: </i> every <b>change</b> in this values will trigger a {@link
     * ConfigEvent} on all the registered {@link ConfigListener}s.
     *
     * @param key
     * @param value
     * @see Config#addListener(ConfigListener)
     */
    public void set(String key, Object value) {
        final Object old;

        synchronized (data) {
            old = data.get(key);

            if (value == null){
                data.remove(key);
            } else {
                data.put(key, value);
            }
        }

        fireEvent(key, old, value);
    }

    /**
     * Retrieves a raw {@code value} for a given {@code key}
     *
     * @param key
     * @return value
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@link Color}
     *
     * @param key
     * @return value as {@link Color}
     * @throws ClassCastException if the {@code value} isn't a {@link Color}
     */
    public Color getColor(String key) throws ClassCastException {
        return (Color)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@code boolean}
     *
     * @param key
     * @return value as {@link Boolean}
     * @throws ClassCastException if the {@code value} isn't a {@link Boolean}
     */
    public boolean getBoolean(String key) throws ClassCastException {
        return (Boolean)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as an {@code int}
     *
     * @param key
     * @return value as {@link Integer}
     * @throws ClassCastException if the {@code value} isn't a {@link Integer}
     */
    public int getInteger(String key) throws ClassCastException {
        return (Integer)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@code String}
     *
     * @param key
     * @return value as {@link String}
     * @throws ClassCastException if the {@code value} isn't a {@link String}
     */
    public String getString(String key) throws ClassCastException {
        return (String)data.get(key);
    }

    private final LinkedList<WeakReference<ConfigListener>> listeners = new LinkedList<>();

    /**
     * Adds a new {@link ConfigListener} to this {@code config}, this listeners
     * will be notified of all the changes that happen to the <b>
     * {@code non-volatile}</b> field of this config.
     *
     * @param listener
     */
    public void addListener(ConfigListener listener) {
        synchronized (listeners) {
            listeners.add(new WeakReference<>(listener));
        }
    }

    /**
     * Removes a previously registered {@link ConfigListener}
     *
     * @param listener
     */
    public void removeListener(ConfigListener listener) {
        synchronized (listeners) {
            for (WeakReference<ConfigListener> element : listeners) {
                if (element.get() == listener) {
                    element.clear();
                    break;
                }
            }
        }
    }

    private void fireEvent(
            final String key,
            final Object oval,
            final Object nval)
    {
        if (listeners.isEmpty()){
            return;
        }

        if (key  == null || key.isEmpty() ||
            oval == nval || (oval != null && oval.equals(nval))){
            return;
        }

        final int type;

        if (oval == null && nval != null) {
            type = ConfigEvent.VALUE_ADDED;
        } else if (nval == null) {
            type = ConfigEvent.VALUE_REMOVED;
        } else {
            type = ConfigEvent.VALUE_UPDATED;
        }

        final ConfigEvent evt = new ConfigEvent() {
            @Override public int     getChangeType() {return type;}
            @Override public String  getChangedKey() {return key ;}
            @Override public Object  getOldValue  () {return oval;}
            @Override public Object  getNewValue  () {return nval;}
            @Override public boolean isKey(String k) {return key.equals(k);}
        };

        int nullCount = 0;

        for (final WeakReference<ConfigListener> ref : listeners) {
            final ConfigListener lr = ref.get();
            if (lr != null) {
                lr.somethingChange(evt);
            } else {
                nullCount++;
            }
        }

        if (nullCount != 0){
            cleanListeners();
        }
    }

    private void cleanListeners() {
        final ArrayList<WeakReference<ConfigListener>> foo;
        foo = new ArrayList<>(listeners.size());

        for (final WeakReference<ConfigListener> ref : listeners) {
            if (ref.get() != null) {
                foo.add(ref);
            }
        }

        listeners.clear();
        listeners.addAll(foo);
        foo.clear();
    }
}
