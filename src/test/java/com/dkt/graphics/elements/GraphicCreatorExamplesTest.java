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
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
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
        test("arc_of_a_circle");
        test("arc_of_an_oval");
        test("blueprint");
        test("cartesian_line");
        test("cartesian_vector");
        test("chess_2");
        test("chess_board");
        test("circles_1");
        test("circles_2");
        test("circles_3");
        test("circuit");
        test("color_circles");
        test("gradient");
        test("line_path");
        test("line_path_closed");
        test("lines_1");
        test("lines_2");
        test("lines_3");
        test("lines_4");
        test("lines_5");
        test("lines_6");
        test("music_sheet");
        test("optical_1");
        test("optical_2");
        test("optical_3");
        test("optical_4");
        test("optical_5");
        test("optical_6");
        test("optical_7");
        test("oval");
        test("point");
        test("polar_line");
        test("polar_vector");
        test("polygons");
        test("rectangles");
        test("rectangles_2");
        test("spiral");
        test("strings");
        test("strings_2");
        test("technical");
        test("test");
        test("transformer");
    }

    private void test(String file) {
        String[] exp = readFile("/bin/" + file);
        String[] res =graph("/" + file);
        assertArrayEquals(exp, res);
        assertNotNull(exp);
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
                .setPrettyPrinting()
                .create();
        return gson.toJson(g).trim().split("\\n");
    }

}
