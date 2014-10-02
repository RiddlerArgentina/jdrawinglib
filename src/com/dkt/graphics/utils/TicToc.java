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
package com.dkt.graphics.utils;

/**
 * Implements a Tic-Toc mechanism.<br>
 * This class is very basic, but at least it's easy...
 *
 * @author Federico Vera <dktcoding [at] gmail>
 */
public class TicToc {
    private long iter;
    private long start;
    private long total;

    {
        //Calling the methods before hand helps to get a better results
        //http://www.artima.com/insidejvm/ed2/jvmP.html
        tic();
        toc();
        total = 0;
        start = 0;
        iter  = 0;
    }

    /**
     * Sets the start time
     */
    public void tic(){
        start = System.nanoTime();
    }

    /**
     * Sets the end time
     */
    public void toc(){
        long end = System.nanoTime();
        total += end - start;
        iter++;
    }

    /**
     * @return elapsed time in ns
     */
    public double getNanoTime(){
        return total / iter;
    }

    /**
     * @return elapsed time in µs
     */
    public double getMicroTime(){
        return getNanoTime() / 1000;
    }

    /**
     * @return elapsed time in ms
     */
    public double getMilisTime(){
        return getNanoTime() / 1000000;
    }

    /**
     * @return elapsed time in s
     */
    public double getSecsTime(){
        return getNanoTime() / 1000000000;
    }

    @Override
    public String toString(){
        double time = getSecsTime();
        if (time > 1){
            return String.format("%5.3fs", time);
        }

        time = getMilisTime();
        if (time > 1){
            return String.format("%5.3fms", time);
        }

        time = getMicroTime();
        if (time > 1){
            return String.format("%5.3fµs", time);
        }

        return String.format("%dns", (long)getNanoTime());
    }

    /**
     * Counts the number of {@link TicToc#toc()} calls
     *
     * @return number of calls
     */
    public int getIterations() {
        return (int)iter;
    }
}
