package hzt.fxml.controller;

import javafx.scene.chart.XYChart;

import java.util.function.DoubleUnaryOperator;

public class FunctionGraph {

	private final XYChart<Double, Double> graph;
	private final double range;

	public FunctionGraph(final XYChart<Double, Double> graph, final double range) {
		this.graph = graph;
		this.range = range;
	}

	public void plotExplicitFunction(final DoubleUnaryOperator function) {
		final XYChart.Series<Double, Double> series = new XYChart.Series<>();
		for (double x = -range; x <= range; x = x + 0.01) {
			double y = function.applyAsDouble(x);
			series.getData().add(chartPoint(x, y));
		}
		graph.getData().add(series);
	}

	private XYChart.Data<Double, Double> chartPoint(final double x, final double y) {
		return new XYChart.Data<>(x, y);
	}

	public void clear() {
		graph.getData().clear();
	}
}
