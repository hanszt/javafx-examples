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

import java.util.HashMap;
import java.util.Map;

import org.fxyz.geometry.Point3D;
import org.fxyz.utils.Constraint;

/**
 *
 * @author Jason Pollastrini aka jdub1581
 */
public class WeightedPoint extends Point3D {

    private final double mass;
    private final Point3D oldPosition;
    private Point3D anchorPosition;
    private Point3D force;

    private final Map<WeightedPoint, Constraint> constraints = new HashMap<>();

    private boolean anchored;
    private boolean forceAffected = true;

        /*==========================================================================
         Constructors
         */

        public WeightedPoint() {
            this(0.1, 0, 0, 0, false);
        }

        public WeightedPoint(double mass) {
            this(mass, 0, 0, 0, false);
        }

        public WeightedPoint(double mass, double x, double y, double z) {
            this(mass, x, y, z, false);
        }

        public WeightedPoint(double mass, double x, double y, double z, boolean anchored) {
            super((float) x, (float) y, (float) z);
            this.oldPosition = new Point3D((float) x, (float) y, (float) z);
            this.anchorPosition = new Point3D(0, 0, 0);
            this.force = new Point3D(0, 0, 0);

            this.mass = mass;
            this.anchored = anchored;

        }
        /*==========================================================================
         Constraints
         */

        public final void attatchTo(WeightedPoint other, double linkDistance, double stiffness) {
            attatchTo(this, other, linkDistance, stiffness);
        }

        public final void attatchTo(WeightedPoint self, WeightedPoint other, double linkDistance, double stiffness) {
            Link pl = new Link(self, other, linkDistance, stiffness);
            addConstraint(other, pl);
        }

        //==========================================================================
        public Map<WeightedPoint, Constraint> getConstraints() {
            return constraints;
        }

        public void addConstraint(WeightedPoint other, Constraint constraint) {
            this.constraints.put(other, constraint);
        }

        public void addConstraint(Constraint c){
            this.constraints.put(this, c);
        }

    public void clearConstraints() {
            constraints.clear();
        }
        /*==========================================================================
         Updating
         */

        public void solveConstraints() {
            constraints.values().parallelStream().forEach(Constraint::solve);
        }

        public void updatePhysics(double dt) {
            synchronized (this) {
                if (isAnchored()) {
                    setPosition(getAnchorPosition());
                    return;
                }
                Point3D vel = new Point3D(
                        (getX() - oldPosition.x),
                        (getY() - oldPosition.y),
                        (getZ() - oldPosition.z)
                );
                float dtSq = (float) (dt * dt);

                // calculate the next position using Verlet Integration
                Point3D next = new Point3D(
                        getX() + (vel.x + (((force.x / (float) (mass)) * 0.5f) * dtSq)),
                        getY() + (vel.y + (((force.y / (float) (mass)) * 0.5f) * dtSq)),
                        getZ() + (vel.z + (((force.z / (float) (mass)) * 0.5f) * dtSq))
                );

                // reset variables
                setOldPosition(this);
                setPosition(next);
                clearForces();
            }
        }
        /*==========================================================================
         Variable's Getters / setters
         */

        public double getMass() {
            return mass;
        }

        /*==========================================================================
         Positions
         */
        public final Point3D getPosition() {
            return new Point3D(getX(), getY(), getZ());
        }

        public void setPosition(Point3D pos) {
            x = pos.x;
            y = pos.y;
            z = pos.z;
        }

        //==========================================================================
        public Point3D getOldPosition() {
            return oldPosition;
        }

        public void setOldPosition(Point3D oldPosition) {
            this.oldPosition.x = oldPosition.x;
            this.oldPosition.y = oldPosition.y;
            this.oldPosition.z = oldPosition.z;
        }

        public void setOldPosition(float x, float y, float z) {
            this.oldPosition.x = x;
            this.oldPosition.y = y;
            this.oldPosition.z = z;
        }

        public void setOldPosition(double x, double y, double z) {
            this.oldPosition.x = (float) x;
            this.oldPosition.y = (float) y;
            this.oldPosition.z = (float) z;
        }

        //==========================================================================
        public boolean isAnchored() {
            return anchored;
        }

        public void setAnchored(boolean anchored) {
            this.anchored = anchored;
            if (anchored) {
                setAnchorPosition(new Point3D(getX(), getY(), getZ()));
            } else {
                setAnchorPosition(null);
            }
        }

        public Point3D getAnchorPosition() {
            return anchorPosition;
        }

        public void setAnchorPosition(Point3D anchorPosition) {
            this.anchorPosition = anchorPosition;
        }
        /*==========================================================================
         Forces
         */

        public Point3D getForce() {
            return force;
        }

        private void setForce(Point3D p) {
            this.force = p;
        }

        public void applyForce(Point3D force) {
            if (isForceAffected()) {
                this.force.x += force.x;
                this.force.y += force.y;
                this.force.z += force.z;
            }
        }

        public void clearForces() {
            setForce(new Point3D(0, 0, 0));
        }

        public boolean isForceAffected() {
            return forceAffected;
        }

        public void setForceAffected(boolean forceAffected) {
            this.forceAffected = forceAffected;
        }

        /*==========================================================================
    
         */
        @Override
        public String toString() {
            return "WeightedPoint: ".concat(toString());
        }

    }//End WeightedPoint========================================================
