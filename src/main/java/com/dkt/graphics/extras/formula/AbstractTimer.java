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
package com.dkt.graphics.extras.formula;

import com.dkt.graphics.elements.GPoint;
import com.dkt.graphics.elements.GPointArray;
import com.dkt.graphics.elements.Graphic;
import com.dkt.graphics.elements.GraphicE;
import com.dkt.graphics.exceptions.AlreadyRunningException;
import com.dkt.graphics.exceptions.InvalidArgumentException;
import com.dkt.graphics.utils.PThread;
import java.util.ArrayList;

/**
 * This class implements an abstract timer mainly used to draw functions on
 * the canvas.<br>
 * Please note that there are several ways to improve the speed of this class,
 * for instance using a {@link Graphic} object for each thread, using
 * {@link GPointArray} instead of an array of {@link GPoint}s, etc. Those
 * things will improve the speed and memory quite a bit and the change is almost
 * trivial... <i>Why don't we improve it? </i> well it's quite simple actually,
 * we use this classes to draw the functions as a <i>real time</i> drawing, and
 * in that case, the perceived speed of the application is somewhat more
 * important than the real speed.
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 * @param <T> The {@link AbstractCalculable} instance that will be used
 */
public abstract class AbstractTimer<T extends AbstractCalculable> extends GraphicE {
    /**
     * This interface contains all the methods that will be executed
     * after starting, pausing, resuming and stopping a timer.
     */
    public static interface Action {
        /**
         * This method will be executed immediately after the timer starts and
         * all threads are created and running.
         */
        void start();
        /**
         * This method will be executed immediately after all the timer threads
         * are paused.
         */
        void pause();
        /**
         * This method will be executed immediately after all the timer threads
         * were resumed.
         */
        void resume();
        /**
         * This method will be executed immediately after all the timer threads
         * were stopped.<br>
         * <i>Note: </i> is the responsibility of the coder to call
         * {@link AbstractTimer#stop()} at the end of the execution of the
         * timer's instance
         */
        void stop();
    }

    private final ArrayList<PThread> threads = new ArrayList<>(1);
    private final GPointArray pointArray = new GPointArray();
    private final T calculable;
    private volatile boolean isRunning;
    private volatile boolean isPaused;
    private int numberOfThreads = 1;
    private boolean drawAsPath;
    private boolean drawPen;
    private Action action;

    /**
     * @param calculable object that will be used on the calculations
     * @throws NullPointerException if {@code calculable} is {@code null}
     */
    protected AbstractTimer(T calculable){
        if (calculable == null){
            throw new NullPointerException("You must pass a formula!");
        }

        this.calculable = calculable;
    }

    /**
     * Retrieves the calculable object used for the calculations.
     *
     * @return {@link AbstractCalculable} object used to calculate
     */
    public T getCalculable(){
        return calculable;
    }

    /**
     * Sets the number of threads that will be used for this calculations
     *
     * @param n number of threads
     * @throws InvalidArgumentException if {@code n} is less than one
     * @throws AlreadyRunningException if the Timer was already started
     */
    public void setNumberOfThreads(int n){
        checkRunning();

        if (n <= 0){
            String msg = "You must have at least one thread";
            throw new InvalidArgumentException(msg);
        }

        numberOfThreads = n;
    }

    /**
     * Tells if all the threads have finished their execution
     *
     * @return {@code true} if there's no running or paused thread and
     * {@code false} otherwise
     */
    protected boolean threadsEnded() {
        synchronized (threads){
            return threads.isEmpty();
        }
    }

    /**
     * Tells the timer to draw the pen on the last drawn point
     *
     * @param drawPen {@code true} if the pen must be drawn, and {@code false}
     * otherwise
     * @throws AlreadyRunningException if the Timer was already started
     */
    public void setDrawPen(boolean drawPen){
        checkRunning();

        this.drawPen = drawPen;
    }

    /**
     * Tells the timer to draw the equation as a path o points
     *
     * @param drawAsPath {@code true} if the equation should be drawn as a path,
     * and {@code false} in order to draw only the points
     * @throws AlreadyRunningException if the Timer was already started
     */
    public void setDrawAsPath(boolean drawAsPath){
        checkRunning();

        this.drawAsPath = drawAsPath;
    }

    /**
     * Tells if the equation will be drawn as a path
     *
     * @return {@code true} if the equation is drawn as a path and {@code false}
     * if it's drawn as points
     */
    public boolean drawAsPath(){
        return drawAsPath;
    }

    /**
     * Tells if the timer is paused
     *
     * @return {@code true} if the thread is paused and {@code false} otherwise
     */
    public boolean isPaused(){
        return isPaused;
    }

    /**
     * Tells if the timer is running
     *
     * @return {@code true} if the thread is running and {@code false} otherwise
     */
    public boolean isRunning(){
        return isRunning;
    }

    /**
     * Starts the timer with all of it's threads.<br>
     * <i>Note: </i> This method will remove all of the {@link GraphicE} of the
     * {@link Graphic}
     *
     * @throws AlreadyRunningException if the Timer was already started
     * @see Action#start()
     */
    public void start(){
        checkRunning();

        pointArray.clear();

        isRunning = true;

        synchronized(threads){
            for (int i = 0; i < numberOfThreads; i++){
                PThread thread = getThread(calculable, i, numberOfThreads, drawPen);
                threads.add(thread);
                thread.start();
            }

            if (action != null){
                action.start();
            }
        }
    }

    /**
     * Pauses the timer with all of it's threads
     *
     * @see Action#pause()
     */
    public void pause() {
        if (!isPaused){
            synchronized (threads){
                isPaused = true;

                for (PThread thread : threads){
                    thread.pause();
                }

                if (action != null){
                    action.pause();
                }
            }
        }
    }

    /**
     * Resumes the timer and all of it's threads
     *
     * @see Action#resume()
     */
    public void resume(){
        if (isPaused){
            synchronized (threads){
                isPaused = false;

                for (PThread thread : threads){
                    thread.unpause();
                }

                if (action != null){
                    action.resume();
                }
            }
        }
    }

    /**
     * Stops the timer and all of it's running threads
     *
     * @see Action#stop()
     */
    public void stop(){
        if (isRunning){
            isRunning = true;

            synchronized(threads){
                for (Thread thread : threads){
                    thread.interrupt();
                }

                threads.clear();
            }

            if (action != null){
                action.stop();
            }
        }

    }

    /**
     * Sets the actions to be executed at start, stop, pause, resume.
     *
     * @param action The actions to be executed
     * @see Action
     */
    public void setActions(Action action){
        checkRunning();
        this.action = action;
    }

    private void checkRunning() {
        if (isRunning){
            String msg = "The thread was already running";
            throw new AlreadyRunningException(msg);
        }
    }

    protected void removeThread(PThread thread){
        synchronized(threads){
            threads.remove(thread);
            if (threadsEnded()){
                stop();
            }
        }
    }

    protected abstract PThread getThread(T calculable,
                                         int threadNumber,
                                         int threadsTotal,
                                         boolean drawPen);
}
