/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.fxyz.utils;

import java.util.Arrays;

/**
 * Collector to generate a float[] array from a DoubleStream of floats
 * <p>
 * General use:
 * .collect(FloatCollector::new, FloatCollector::add, FloatCollector::join)
 * Known initial size:
 * .collect(()->new FloatCollector(size), FloatCollector::add, FloatCollector::join)
 * <p>
 * Generates a FloatCollector, use FloatCollector.toArray() to get the float[]
 *
 * @author José Pereda Llamas
 * Created on 19-nov-2014 - 18:51:11
 */
public final class FloatCollector {

    private float[] floats = new float[64];
    private int size;

    public FloatCollector(int initialSize) {
        if (floats.length < initialSize) {
            floats = Arrays.copyOf(floats, initialSize);
        }
    }

    public void add(double d) {
        if (floats.length == size) {
            floats = Arrays.copyOf(floats, size * 2);
        }
        floats[size++] = (float) d;
    }

    public void join(FloatCollector other) {
        if (size + other.size > floats.length) {
            floats = Arrays.copyOf(floats, size + other.size);
        }
        System.arraycopy(other.floats, 0, floats, size, other.size);
        size += other.size;
    }

    public float[] toArray() {
        return size == floats.length ? floats : Arrays.copyOf(floats, size);
    }
}
