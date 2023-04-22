/*
 *                      ..::jDrawingLib::..
 *
 * Copyright (C) Federico Vera 2012 - 2023 <fede@riddler.com.ar>
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

import java.util.concurrent.Semaphore;

/**
 * This class basically implements a <i>pausable</i> thread. Please note that
 * the {@link PThread#pause()} and {@link PThread#unpause()} methods won't
 * release any of the locks the thread might hold, it will simply pause the
 * thread.
 *
 * @author Federico Vera {@literal<fede@riddler.com.ar>}
 */
public abstract class PThread extends Thread {
    private final Semaphore lock = new Semaphore(1);

    /**
     * Pauses the current thread
     *
     * @return {@code true} if the thread was successfully paused and
     * {@code false} if the thread was already paused
     */
    public final boolean pause(){
        try {
            lock.acquire();
            return true;
        } catch (InterruptedException ex) {
            return false;
        }
    }

    /**
     * Resumes the current thread
     */
    public final void unpause(){
        lock.release();
    }

    /**
     * This method should be called within the main run loop. If this isn't
     * called then the thread will not be paused. An example of an
     * implementation could be:
     * <pre>
     *      class MyThread extends PThread {
     *          &#64;Override
     *          public void run() {
     *              //Init code
     *              while(!interrupted() &amp;&amp; condition){
     *                   checkPause();
     *                   //Loop code
     *              }
     *              //End code
     *          }
     *      }
     * </pre>
     */
    protected final void checkPause(){
        try {
            lock.acquire();
        } catch (InterruptedException __) {
        } finally {
            lock.release();
        }
    }

    @Override abstract public void run();
}
