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
package com.dkt.graphics.extras.formula;

/**
 *
 * @author Federico Vera {@literal<dktcoding [at] gmail>}
 */
public abstract class AbstractCalculable {
    private static final String NO_NAME = "NO_NAME";
    private String name = NO_NAME;
    private double start;
    private double end;
    private double scaleX = 1;
    private double scaleY = 1;
    private double step = 0.1;

    /**
     * Retrieves the getName for the formula
     *
     * @return getName of the formula
     */
    public String getName(){
        return name;
    }

    /**
     * Set's a new getName for this formula
     *
     * @param name getName of the formula
     */
    public void setName(String name){
        if (name == null){
            name = NO_NAME;
        }
        this.name = name;
    }

    /**
     * Retrieves the start point of the interval
     *
     * @return start point
     */
    public double startPoint(){
        return start;
    }

    /**
     * Retrieves the end point of the interval
     *
     * @return end point
     */
    public double endPoint(){
        return end;
    }

    /**
     * Retrieves the X setScale for this formula
     *
     * @return X setScale
     */
    public double scaleX(){
        return scaleX;
    }

    /**
     * Retrieves the Y setScale for this formula, basically every Y value is
     * multiplied by this value
     *
     * @return Y setScale
     */
    public double scaleY(){
        return scaleY;
    }

    /**
     * Retrieves the step used to calculate the formula in the interval.
     *
     * @return step of the interval
     */
    public double step(){
        return step;
    }

    /**
     * The start point of the interval used to calculate this formula<br>
     * <i>Note:</i> this method doesn't control the values of the interval, nor
     * the value used for the step, it's merely a container.
     *
     * @param start start point of the interval
     */
    public void startPoint(double start){
        this.start = start;
    }

    /**
     * The end point of the interval used to calculate this formula<br>
     * <i>Note:</i> this method doesn't control the values of the interval, nor
     * the value used for the step, it's merely a container.
     *
     * @param end end point of the interval
     */
    public void endPoint(double end){
        this.end = end;
    }

    /**
     * Sets a getScale for both x and y axis.
     *
     * @param scale new getScale
     * @deprecated this method was deprecated because now we have individual
     * scales for both x and y. This method is still used in several projects,
     * but it will be removed in future versions.
     * @see AbstractCalculable#setScaleX(double)
     * @see AbstractCalculable#setScaleY(double)
     */
    @Deprecated
    public void setScale(double scale){
        this.scaleX = this.scaleY = scale;
    }

    /**
     * Retrieves the x getScale of the formula.
     *
     * @return the x getScale of the formula
     * @deprecated this method was deprecated because now we have individual
     * scales for both x and y. This method is still used in several projects,
     * but it will be removed in future versions.
     * @see AbstractCalculable#scaleX()
     * @see AbstractCalculable#scaleY()
     */
    @Deprecated
    public double getScale(){
        return scaleX;
    }

    /**
     * Sets the new setScale for the X formula, basically every X value is
     * multiplied by this value
     *
     * @param scale setScale value
     */
    public void setScaleX(double scale){
        this.scaleX = scale;
    }

    /**
     * Sets the new setScale for the Y formula, basically every Y value is
     * multiplied by this value
     *
     * @param scale setScale value
     */
    public void setScaleY(double scale){
        this.scaleY = scale;
    }

    /**
     * Sets the step used to evaluate the formula<br>
     * <i>Note:</i> this method doesn't control the values of the interval, nor
     * the value used for the step, it's merely a container.
     *
     * @param step new step
     */
    public void step(double step){
        this.step = step;
    }
}
