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
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class ConfigTest {
    private boolean statusNew = false;
    private boolean statusRem = false;
    private boolean statusUpd = false;

    @Test
    @DisplayName("Test config")
    public void testConfig() {
        Config.remove("conf_1");
        Config.remove("conf_2");
        Config.on("conf_1").put("k1", "v1");
        Config.on("conf_2").put("k2", "v2");
        assertNotNull(Config.on("conf_1"));
        assertNotNull(Config.on("conf_2"));
        assertEquals("v1", Config.from("conf_1").get("k1"));
        assertEquals("v2", Config.from("conf_2").get("k2"));
        assertEquals("v1", Config.from("conf_1").getString("k1"));
        assertEquals("v2", Config.from("conf_2").getString("k2"));
        assertNull(Config.from("conf_1").get("k2"));
        assertNull(Config.from("conf_2").get("k1"));
        assertEquals(2, Config.size());
    }

    @Test
    @DisplayName("Test listener new")
    public void testListenerNew() {
        Config.remove("conf_1");
        Config.remove("conf_2");
        Config.on("conf_1").put("k4", "v4");
        Config.on("conf_1").put("k3", "v5");
        Config.on("conf_2").put("k1", "v1");
        Config.on("conf_2").put("k3", "v5");
        Config.on("conf_1").addListener(new ConfigListener() {
            @Override
            public void somethingChange(ConfigEvent event) {
                if (event.getChangeType() == ConfigEvent.VALUE_ADDED) {
                    statusNew = true;
                    assertTrue(event.isKey("k2"));
                    assertEquals("k2", event.getChangedKey());
                    assertEquals(null, event.getOldValue());
                    assertEquals("v2", event.getNewValue());
                }
            }
        });
        assertFalse(statusNew);
        Config.on("conf_2").put("k2", "v2");
        assertFalse(statusNew);
        Config.on("conf_1").put("k2", "v2");
        assertTrue(statusNew);
    }

    @Test
    @DisplayName("Test listener removed")
    public void testListenerRem() {
        Config.remove("conf_1");
        Config.on("conf_1").put("k2", "v4");
        Config.on("conf_1").put("k3", "v5");
        Config.on("conf_1").put("k1", "v1");
        Config.on("conf_1").put("k2", "v4");
        Config.on("conf_1").put("k3", "v5");
        Config.on("conf_1").addListener(new ConfigListener() {
            @Override
            public void somethingChange(ConfigEvent event) {
                if (event.getChangeType() == ConfigEvent.VALUE_REMOVED) {
                    statusRem = true;
                    assertTrue(event.isKey("k1"));
                    assertEquals("k1", event.getChangedKey());
                    assertEquals("v1", event.getOldValue());
                    assertEquals(null, event.getNewValue());
                }
            }
        });
        assertFalse(statusRem);
        Config.on("conf_1").set("k1", null);
        assertTrue(statusRem);

    }

    @Test
    @DisplayName("Test listener updated")
    public void testListenerUpd() {
        Config.remove("conf_1");
        Config.on("conf_1").put("k2", "v4");
        Config.on("conf_1").put("k3", "v5");
        Config.on("conf_1").put("k1", "v1");
        Config.on("conf_1").put("k2", "v4");
        Config.on("conf_1").put("k3", "v5");
        ConfigListener listener = new ConfigListener() {
            @Override
            public void somethingChange(ConfigEvent event) {
                if (event.getChangeType() == ConfigEvent.VALUE_UPDATED) {
                    statusUpd = true;
                    assertTrue(event.isKey("k1"));
                    assertEquals("k1", event.getChangedKey());
                    assertEquals("v1", event.getOldValue());
                    assertEquals("v2", event.getNewValue());
                }
            }
        };
        Config.on("conf_1").addListener(listener);
        assertFalse(statusUpd);
        Config.on("conf_1").set("k1", "v2");
        assertTrue(statusUpd);
        statusUpd = false;
        Config.on("conf_1").removeListener(listener);
        Config.on("conf_1").set("k1", "v3");
        assertFalse(statusUpd);
    }

    @Test
    @DisplayName("Test write read single conf")
    public void testWriteSingle() {
        Config.remove("conf_1");
        Config.remove("conf_2");
        Config.on("conf_1").put("k1", Color.RED);
        Config.on("conf_1").put("k2", 123.3);
        Config.on("conf_1").put("k3", 100);
        Config.on("conf_1").put("k4", "Hello World");
        try (FileOutputStream fos = new FileOutputStream("foo1")) {
            Config.from("conf_1").save(fos);
        } catch (Exception e) {
            fail("Unable to write file foo1");
        }
        Config.remove("conf_1");
        try (FileInputStream fis = new FileInputStream("foo1")) {
            Config.read(fis, "conf_2");
        } catch (Exception e) {
            fail("Unable to read file foo1");
        }
        assertEquals(Color.RED, Config.from("conf_2").getColor("k1"));
        assertEquals(123.3, Config.from("conf_2").getDouble("k2"));
        assertEquals(100, Config.from("conf_2").getInt("k3"));
        assertEquals("Hello World", Config.from("conf_2").getString("k4"));
    }

    @Test
    @DisplayName("Test write all")
    public void testWriteAll() {
        Config.remove("conf_1");
        Config.remove("conf_2");
        Config.on("conf_1").put("k1", Color.RED);
        Config.on("conf_1").put("k2", 123.3);
        Config.on("conf_1").put("k3", true);
        Config.on("conf_2").put("k3", 100);
        Config.on("conf_2").put("k4", "Hello World");
        try (FileOutputStream fos = new FileOutputStream("foo2")) {
            Config.saveAll(fos);
        } catch (Exception e) {
            fail("Unable to write file foo2");
        }
        Config.remove("conf_1");
        try (FileInputStream fis = new FileInputStream("foo2")) {
            Config.read(fis, null);
        } catch (Exception e) {
            fail("Unable to read file foo2");
        }
        assertEquals(Color.RED, Config.from("conf_1").getColor("k1"));
        assertEquals(123.3, Config.from("conf_1").getDouble("k2"));
        assertEquals(true, Config.from("conf_1").getBool("k3"));
        assertEquals(100, Config.from("conf_2").getInt("k3"));
        assertEquals("Hello World", Config.from("conf_2").getString("k4"));

        try (FileOutputStream fos = new FileOutputStream("foo3");
             ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject("This is not a config file");
        } catch (Exception e) {
            fail("Unable to write file foo3");
        }
        assertTrue(Config.configSet().contains("conf_1"));
        assertTrue(Config.configSet().contains("conf_2"));
        assertThrows(IllegalArgumentException.class, () -> {
                try (FileInputStream fis = new FileInputStream("foo3")) {
                    Config.read(fis, null);
                }
            }
        );
        assertThrows(ClassCastException.class, () -> {
                try (FileInputStream fis = new FileInputStream("foo3")) {
                    Config.from("conf_2").getIcon("k4");
                }
            }
        );
    }
}
