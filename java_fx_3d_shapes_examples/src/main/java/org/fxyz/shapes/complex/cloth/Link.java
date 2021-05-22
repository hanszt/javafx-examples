/*
 * Copyright (C) 2014 F(Y)zx :
 * Authored by : Jason Pollastrini aka jdub1581, 
 * All rights reserved.
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.fxyz.shapes.complex.cloth;

import org.fxyz.geometry.Point3D;
import org.fxyz.utils.Constraint;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class Link implements Constraint {

    private final double distance;
    private final double stiffness;
    private final WeightedPoint p1;
    private final WeightedPoint p2;

    public Link(WeightedPoint p1, WeightedPoint p2, double distance, double stiffness) {
        this.p1 = p1;
        this.p2 = p2;
        this.distance = distance;
        this.stiffness = stiffness;
    }

    @Override
    public void solve() {
        
        //calculate the distance between the two PointMasses
        Point3D diff = p1.subtract(p2);
        double d = diff.magnitude();
                
        double difference =(distance - d) / d;        
        
        double im1 = 1 / p1.getMass();
        double im2 = 1 / p2.getMass();
        float scalarP1 = (float) ((im1 / (im1 + im2)) * stiffness);
        float scalarP2 = (float) (stiffness - scalarP1);

        synchronized (this) {
            p1.x += ((float)(diff.x * scalarP1 * difference));
            p1.y += ((float)(diff.y * scalarP1 * difference));
            p1.z += ((float)(diff.z * scalarP1 * difference));

            p2.x -= ((float)(diff.x * scalarP2 * difference));
            p2.y -= ((float)(diff.y * scalarP2 * difference));
            p2.z -= ((float)(diff.z * scalarP2 * difference));
        }
            
    }

    @Override
    public String toString() {
        return "PointLink{" + "distance=" + distance + ", stiffness=" + stiffness + ", p1=" + p1 + ", p2=" + p2 + '}';
    }

}
