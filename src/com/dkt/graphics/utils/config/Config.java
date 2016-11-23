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
package com.dkt.graphics.utils.config;

import java.awt.Color;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Set;
import javax.swing.ImageIcon;

/**
 * This class represents a generic Configuration class, with change listeners. 
 * This class supports a {@link ConfigListener} that is fired when key-values 
 * are updated, added or removed on a <i>per config</i> basis.
 *
 * @author Federico Vera {@literal <dktcoding [at] gmail>}
 */
public class Config {
    private static final HashMap<String, Config> CONFIGS = new HashMap<>(8);
    private final        HashMap<String, Object> data    = new HashMap<>(16);
    
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
    public static synchronized Config from(String name) {
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
    public static synchronized Config on(String name) {
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
     * @param name config name
     */
    public static void remove(String name) {
        final Config conf;
        
        synchronized(CONFIGS) {
            conf = CONFIGS.remove(name);
        }

        if (conf != null) {
            conf.listeners.clear();
            conf.data.clear();
        }
    }
    
    /**
     * Retrieves a {@link Set} with all the available {@link Config} objects
     * @return Set of configs
     */
    public static Set<String> configSet() {
        return CONFIGS.keySet();
    }
    
    /**
     * Retrieves the number of created {@link Config} objects.
     * @return number of configs
     */
    public static int size() {
        return CONFIGS.size();
    }

    /**
     * A wrapper for {@link Config#put(String,String)}
     * 
     * @param key {@code key} 
     * @param value {@code value} 
     * @see Config#put(String,String)
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
     * @param key {@code key} 
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

    private final LinkedList<WeakReference<ConfigListener>> listeners = new 
LinkedList<>();

    /**
     * Adds a new {@link ConfigListener} to this {@code config}, this listeners
     * will be notified of all the changes that happen to the <b>
     * {@code non-volatile}</b> field of this config.
     *
     * @param listener {@link ConfigListener}
     */
    public void addListener(ConfigListener listener) {
        synchronized (listeners) {
            listeners.add(new WeakReference<>(listener));
        }
    }

    /**
     * Removes a previously registered {@link ConfigListener}
     *
     * @param listener {@link ConfigListener}
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
