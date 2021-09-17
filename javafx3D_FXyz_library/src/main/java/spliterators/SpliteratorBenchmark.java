/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package spliterators;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.stream.Stream;

import static java.util.concurrent.TimeUnit.SECONDS;
import static spliterators.FixedBatchSpliterator.withBatchSize;

public class SpliteratorBenchmark {

    private static final Logger LOGGER = LogManager.getLogger(SpliteratorBenchmark.class);
    private static double sink;

    public static void main(String[] args) {
        try {
            final Path inputPath = createInput();
            for (int i = 0; i < 3; i++) {
                LOGGER.info("Start processing JDK stream");
                measureProcessing(Files.lines(inputPath));
                LOGGER.info("Start processing fixed-batch stream");
                measureProcessing(withBatchSize(Files.lines(inputPath), 10));
            }
        } catch (IOException e) {
            LOGGER.error("Could not load input", e);
        }
    }

    private static void measureProcessing(Stream<String> input) {
        final long start = System.nanoTime();
        try (Stream<String> lines = input) {
            final double cpuTime = lines.parallel()
                    .mapToLong(SpliteratorBenchmark::processLine)
                    .sum();

            final double realTime = System.nanoTime() - (double) start;
            final int cores = Runtime.getRuntime().availableProcessors();
            LOGGER.info(" Cores: {}", cores);
            LOGGER.info(" CPU time: {} s", cpuTime / SECONDS.toNanos(1));
            LOGGER.info(" Real time: {} s", realTime / SECONDS.toNanos(1));
            LOGGER.info("CPU utilization: {} %", 100.0 * cpuTime / realTime / cores);
            LOGGER.info("Sink: {}", sink);
            LOGGER.info("");
        }
    }

    private static long processLine(String line) {
        final long localStart = System.nanoTime();
        double d = 0;
        for (int i = 0; i < line.length(); i++) {
            for (int j = 0; j < line.length(); j++) {
                d += Math.pow(line.charAt(i), line.charAt(j) / 32.0);
            }
        }
        sink += d;
        return System.nanoTime() - localStart;
    }

    private static Path createInput() throws IOException {
        final Path inputPath = Paths.get("input.txt");
        try (PrintWriter w = new PrintWriter(Files.newBufferedWriter(inputPath))) {
            for (int i = 0; i < 6_000; i++) {
                final String text = String.valueOf(System.nanoTime());
                for (int j = 0; j < 15; j++) {
                    w.print(text);
                }
                w.println();
            }
        }
        return inputPath;
    }
}
