package utils.stream

import java.util.function.Function
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.Stream

fun <T, K> Stream<out T>.groupBy(
    classifier: Function<in T, out K>
): Map<K, List<T>> = collect(Collectors.groupingBy(classifier))

fun <T, K, A, D> Stream<out T>.groupBy(
    classifier: Function<in T, out K>,
    downStream: Collector<in T, A, D>
): Map<K, D> = collect(Collectors.groupingBy(classifier, downStream))
