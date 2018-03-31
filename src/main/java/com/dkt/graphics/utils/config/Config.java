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

import java.awt.Color;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 * This class represents a generic Configuration class, with change listeners.
 * This class supports a {@link ConfigListener} that is fired when key-values
 * are updated, added or removed on a <i>per config</i> basis.
 *
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class Config implements Serializable {
    private static final long serialVersionUID = 6450068842696620740L;

    private static final HashMap<String, Config> CONFIGS = new HashMap<>(8);
    private final        HashMap<String, Object> data    = new HashMap<>(16);

    private final ArrayList<ConfigListener> listeners = new
ArrayList<>(4);

    /**
     * Don't let anyone else initialize this class
     */
    private Config () {

    }

    /**
     * A wrapper for {@link Config#on(String)}
     *
     * @param name config name
     * @return {@code Config} object
     * @see Config#on(String)
     */
    public static Config from(String name) {
        return on(name);
    }

    /**
     * Retrieves a {@code Config} instance associated with a given {@code name},
     * this is usually the module/plugin name.<br>
     * <pre>
     *
     * Config myApp = Config.on("my.config");
     * myApp.set("key.1", "value.1");
     * ...
     * </pre>
     *
     * @param name config name
     * @return {@code Config} object
     */
    public static Config on(String name) {
        synchronized(CONFIGS) {
            if (!CONFIGS.containsKey(name)) {
                CONFIGS.put(name, new Config());
            }

            return CONFIGS.get(name);
        }
    }

    /**
     * Destroys an instance of {@code Config} for a given {@code name}, this
     * is useful when you have more than one application using this Class, and
     * they don't start and stop at the same time.
     *
     * @param name config name
     */
    public static void remove(String name) {
        synchronized(CONFIGS) {
            final Config conf = CONFIGS.remove(name);

            if (conf != null) {
                conf.listeners.clear();
                conf.data.clear();
            }
        }
    }

    /**
     * Retrieves a {@link Set} with all the available {@link Config} objects
     * @return Set of configs
     */
    public static Set<String> configSet() {
        synchronized(CONFIGS) {
            return CONFIGS.keySet();
        }
    }

    /**
     * Retrieves the number of created {@link Config} objects.
     * @return number of configs
     */
    public static int size() {
        return CONFIGS.size();
    }

    /**
     * A wrapper for {@code Config.put(String,Object)}
     *
     * @param key {@code key}
     * @param value {@code value}
     * @see Config#put(String,Object)
     * @see Config#addListener(ConfigListener)
     */
    public void set(String key, Object value) {
        put(key, value);
    }

    /**
     * Set's a config {@code key,value} pair. This values can be exported and
     * imported.<br>
     * <i>Note: </i> every <b>change</b> in this values will trigger a {@link
     * ConfigEvent} on all the registered {@link ConfigListener}s.
     *
     * @param key {@code key}
     * @param value {@code value}
     * @see Config#addListener(ConfigListener)
     */
    public void put(String key, Object value) {
        synchronized (data) {
            final Object old = data.get(key);

            if (value == null){
                data.remove(key);
            } else {
                data.put(key, value);
            }

            fireEvent(key, old, value);
        }
    }

    /**
     * Retrieves a raw {@code value} for a given {@code key}
     *
     * @param key {@code key}
     * @return {@code value}
     */
    public Object get(String key) {
        return data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@link Color}
     *
     * @param key {@code key}
     * @return value as {@link Color}
     * @throws ClassCastException if the {@code value} isn't a {@link Color}
     */
    public Color getColor(String key) throws ClassCastException {
        return (Color)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@code boolean}
     *
     * @param key {@code key}
     * @return value as {@link Boolean}
     * @throws ClassCastException if the {@code value} isn't a {@link Boolean}
     */
    public boolean getBool(String key) throws ClassCastException {
        return (Boolean)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@code double}
     *
     * @param key {@code key}
     * @return value as {@link Double}
     * @throws ClassCastException if the {@code value} isn't a {@link Double}
     */
    public double getDouble(String key) throws ClassCastException {
        return (Double)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as an
     * {@code ImageIcon}
     *
     * @param key {@code key}
     * @return value as {@link ImageIcon}
     * @throws ClassCastException if the {@code value} isn't a {@link ImageIcon}
     */
    public ImageIcon getIcon(String key) throws ClassCastException {
        return (ImageIcon)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as an {@code int}
     *
     * @param key {@code key}
     * @return value as {@link Integer}
     * @throws ClassCastException if the {@code value} isn't a {@link Integer}
     */
    public int getInt(String key) throws ClassCastException {
        return (Integer)data.get(key);
    }

    /**
     * Retrieves the {@code value} for a given {@code key} as a {@code String}
     *
     * @param key {@code key}
     * @return value as {@link String}
     * @throws ClassCastException if the {@code value} isn't a {@link String}
     */
    public String getString(String key) throws ClassCastException {
        return (String)data.get(key);
    }

    /**
     * Adds a new {@link ConfigListener} to this {@code config}, this listeners
     * will be notified of all the changes that happen to the <b>
     * {@code non-volatile}</b> field of this config.
     *
     * @param listener {@link ConfigListener}
     */
    public void addListener(ConfigListener listener) {
        synchronized (listeners) {
            listeners.add(listener);
        }
    }

    /**
     * Removes a previously registered {@link ConfigListener}
     *
     * @param listener {@link ConfigListener}
     */
    public void removeListener(ConfigListener listener) {
        synchronized (listeners) {
            listeners.remove(listener);
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

        if (key  == null || key.isEmpty() || Objects.equals(oval, nval)){
            return;
        }

        final int type;

        if (oval == null && nval != null) {
            type = ConfigEvent.VALUE_ADDED;
        } else if (oval != null && nval == null) {
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

        synchronized(listeners) {
            for (ConfigListener lr : listeners) {
                lr.somethingChange(evt);
            }
        }
    }

    /**
     * Saves all the available Configs in the given {@code OutputStream}.
     * <p><i>Note:</i> all of the values of the {@code Config} object must be {@link Serializable}
     * for this to work</p>
     * @param os {@code OutputStream} on which to write
     * @throws IOException in case an I/O error occurs
     * @see Config#save(java.io.OutputStream)
     * @see Config#read(java.io.InputStream, java.lang.String)
     */
    public static void saveAll(OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(CONFIGS);
        }
    }

    /**
     * Saves {@code this} {@code Config} in the given {@code OutputStream}.
     * <p><i>Note:</i> all of the values of the {@code Config} object must be {@link Serializable}
     * for this to work</p>
     * @param os {@code OutputStream} on which to write
     * @throws IOException in case an I/O error occurs
     * @see Config#saveAll(java.io.OutputStream)
     * @see Config#read(java.io.InputStream, java.lang.String)
     */
    public void save(OutputStream os) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(os)) {
            oos.writeObject(this);
        }
    }

    /**
     * Read a {@code Config} or {@code Map} of configs from the given {@link InputStream}.
     *
     * @param is {@code InputStream} from which to read
     * @param name If reading a single {@code Config} this is the name it will have when calling
     * {@link Config#from(java.lang.String)}, it should be {@code null} when reading a set of
     * configs.
     * @throws IOException in case an I/O error occurs
     * @throws ClassNotFoundException If the {@code InputStream} doesn't point to an appropriate
     * {@code Config}
     * @see Config#save(java.io.OutputStream)
     * @see Config#saveAll(java.io.OutputStream)
     */
    @SuppressWarnings("unchecked")
    public static void read(InputStream is, String name) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(is)) {
            Object obj = ois.readObject();
            if (obj instanceof Config) {
                CONFIGS.put(name, (Config)obj);
            } else if (obj instanceof HashMap) {
                CONFIGS.putAll((HashMap<String, Config>)obj);
            } else {
                String msg = "This Input Stream doesn't contain a valid Config object";
                throw new IllegalArgumentException(msg);
            }
        }
    }
}
