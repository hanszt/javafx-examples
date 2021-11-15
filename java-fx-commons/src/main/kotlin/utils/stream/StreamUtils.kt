//source: https://dev.to/h3xstream/how-to-solve-symbol-is-declared-in-module-x-which-does-not-export-package-y-303g
@file:Suppress("JAVA_MODULE_DOES_NOT_EXPORT_PACKAGE")

package utils.stream

import java.util.stream.DoubleStream
import java.util.stream.IntStream
import java.util.stream.Stream
import java.util.stream.StreamSupport

fun <T> Iterable<T>.stream():
        Stream<T> = StreamSupport.stream(spliterator(), false)

fun <T> Iterable<T>.parallelStream():
        Stream<T> = StreamSupport.stream(spliterator(), true)

fun <T> Array<out T>.stream(): Stream<out T> = Stream.of(*this)

fun IntArray.intStream(): Stream<Int> = IntStream.of(*this).mapToObj { it }

fun DoubleArray.stream(): Stream<Double> = DoubleStream.of(*this).mapToObj { it }

