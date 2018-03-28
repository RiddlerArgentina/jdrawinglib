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
package com.dkt.graphics.utils;

/**
 * This class assists in calculating how many times <i>something</i> happens
 * on a second or to calculate the mean time between calls.<br>
 * Please note that this class was designed to be used for debugging or counting
 * small things, when the action is executed more than 1e15 times the error in
 * the total average {@link TPS#tps()} is very big. Also, this class uses
 * {@link System#nanoTime()} so if the elapsed time between the calls to
 * {@link TPS#action()} is very big, it might yield wrong results
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public class TPS {
    private final int SIZE = 50;
    private final long[] times = new long[SIZE];
    private double average;
    private long lastTime;
    private int i = -1;

    public TPS() {

    }

    /**
     * This method is the one that tells the {@code TPS} instance that an action
     * has been performed. It should be called every time the action is executed
     * in order for it to compute the times.
     */
    public void action(){
        final long curr = System.nanoTime();

        if (lastTime == 0){
            lastTime = System.nanoTime();
            return;
        }

        final long delta = curr - lastTime;
        final int idx = ++i;

        times[(idx % SIZE)] = delta;

        if (i == 0){
            average = delta;
        } else {
            average += (delta - average) / idx;
        }

        lastTime = System.nanoTime();
    }

    /**
     * Retrieves the number of times per second that the action method has
     * been called
     *
     * @return times per second
     * @see TPS#ctps()
     */
    public double tps(){
        return 1e9 / average;
    }

    /**
     * Retrieves the number of times per second that the action method has
     * been called, considering ONLY the last 50 times
     *
     * @return times per second (last 50 times)
     * @see TPS#tps()
     */
    public double ctps(){
        return 1e9 / cAvgNanos();
    }

    /**
     * Retrieves the average time in ms between calls to {@link TPS#action()}
     *
     * @return average time in ms
     */
    public double avgMilis(){
        return average / 1e6;
    }

    /**
     * Retrieves the average time in µs between calls to {@link TPS#action()}
     *
     * @return average time in µs
     */
    public double avgMicros(){
        return average / 1e3;
    }

    /**
     * Retrieves the average time in ns between calls to {@link TPS#action()}
     *
     * @return average time in ns
     */
    public double avgNanos(){
        return average;
    }

    /**
     * Retrieves the average time in ms between calls to {@link TPS#action()},
     * considering only the last 50 calls
     *
     * @return average time in ms
     */
    public double cAvgMilis(){
        return cAvgNanos() / 1e6;
    }

    /**
     * Retrieves the average time in µs between calls to {@link TPS#action()},
     * considering only the last 50 calls
     *
     * @return average time in µs
     */
    public double cAvgMicros(){
        return cAvgNanos() / 1e3;
    }

    /**
     * Retrieves the average time in ns between calls to {@link TPS#action()},
     * considering only the last 50 calls
     *
     * @return average time in ns
     */
    public long cAvgNanos(){
        return avg(times, i < SIZE ? i : SIZE);
    }

    /**
     * Resets the current object to it's original state
     */
    public void reset(){
        lastTime = 0;
        average = 0;
        i = -1;
    }

    private static long avg(long[] ary, int n) {
        long avg = 0;
        int t = 1;

        for (int i = 0; i < n; i++) {
            avg += (ary[i] - avg) / t;
            ++t;
        }

        return avg;
    }

    @Override
    public String toString(){
        return String.format("%d actions avg ~%6.3fms", i, avgMilis());
    }
}

