/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2018 <fede@riddler.com.ar>
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
package com.dkt.graphics.elements;

import com.dkt.graphics.extras.GraphicCreator;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Since the graphic creator uses almost all of the other classes, this is the
 * closest thing to integration tests.<br>
 * Since all of this examples show the correct output on GraphicCreator (it's
 * visually comparable), the easiest way to test that the examples are correct
 * after modifications is checking that the same objects were created.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class GraphicCreatorExamplesTest {

    @Test
    @DisplayName("Examples")
    public void testExamples() {
        assertTrue(test("arc_of_a_circle"));
        assertTrue(test("arc_of_an_oval"));
        assertTrue(test("blueprint"));
        assertTrue(test("cartesian_line"));
        assertTrue(test("cartesian_vector"));
        assertTrue(test("chess_2"));
        assertTrue(test("chess_board"));
        assertTrue(test("circles_1"));
        assertTrue(test("circles_2"));
        assertTrue(test("circles_3"));
        assertTrue(test("circuit"));
        assertTrue(test("color_circles"));
        assertTrue(test("gradient"));
        assertTrue(test("line_path"));
        assertTrue(test("line_path_closed"));
        assertTrue(test("lines_1"));
        assertTrue(test("lines_2"));
        assertTrue(test("lines_3"));
        assertTrue(test("lines_4"));
        assertTrue(test("lines_5"));
        assertTrue(test("lines_6"));
        assertTrue(test("music_sheet"));
        assertTrue(test("optical_1"));
        assertTrue(test("optical_2"));
        assertTrue(test("optical_3"));
        assertTrue(test("optical_4"));
        assertTrue(test("optical_5"));
        assertTrue(test("optical_6"));
        assertTrue(test("optical_7"));
        assertTrue(test("oval"));
        assertTrue(test("point"));
        assertTrue(test("polar_line"));
        assertTrue(test("polar_vector"));
        assertTrue(test("polygons"));
        assertTrue(test("rectangles"));
        assertTrue(test("rectangles_2"));
        assertTrue(test("spiral"));
        assertTrue(test("strings"));
        assertTrue(test("strings_2"));
        assertTrue(test("technical"));
        assertTrue(test("test"));
        assertTrue(test("transformer"));
    }

    private boolean test(String file) {
        String[] exp = readFile("/bin/" + file);
        String[] res =graph("/" + file);
        assertArrayEquals(exp, res);
        assertNotNull(exp);
        return true;
    }

    private String[] readFile(String name) {
        ArrayList<String> content = new ArrayList<>();
        try (InputStream is = GraphicCreatorExamplesTest.class.getResourceAsStream(name);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader fr = new BufferedReader(isr)){
            String foo;
            while ((foo = fr.readLine()) != null) {
                content.add(foo);
            }
        } catch (Exception e) {
            fail("Failed to read file: " + name);
        }
        return content.toArray(new String[content.size()]);
    }

    private String[] graph(String name) {
        String content = "";
        try (InputStream is = GraphicCreatorExamplesTest.class.getResourceAsStream(name);
             InputStreamReader isr = new InputStreamReader(is, StandardCharsets.UTF_8);
             BufferedReader fr = new BufferedReader(isr)){
            String foo;
            while ((foo = fr.readLine()) != null) {
                content += foo + "\n";
            }
        } catch (Exception e) {
            fail("Failed to read file: " + name);
        }
        GraphicCreator gc = new GraphicCreator();
        Graphic g = gc.parse(content);
        g.flatten();
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(Double.class, (JsonSerializer<Double>)
                        (Double s, Type t, JsonSerializationContext c)
                                -> new JsonPrimitive(String.format("%8.6f", s)))
                .setPrettyPrinting()
                .create();
        return gson.toJson(g).trim().split("\\n");
    }

}
