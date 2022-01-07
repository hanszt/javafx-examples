package hzt.fxml.controller;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.AreaChart;
import javafx.scene.chart.LineChart;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.DoubleUnaryOperator;

public final class MainAppController implements Initializable {

	@FXML
	private LineChart<Double, Double> lineGraph;

	@FXML
	private AreaChart<Double, Double> areaGraph;

	private FunctionGraph mathsGraph;
	private FunctionGraph areaMathsGraph;

	public MainAppController() {
		super();
	}

	@Override
	public void initialize(final URL url, final ResourceBundle rb) {
		mathsGraph = new FunctionGraph(lineGraph, 10);
		areaMathsGraph = new FunctionGraph(areaGraph, 10);
	}

	@FXML
	private void handleLineGraphButtonAction() {
		lineGraph.setVisible(true);
		areaGraph.setVisible(false);
	}

	@FXML
	private void handleAreaGraphButtonAction() {
		areaGraph.setVisible(true);
		lineGraph.setVisible(false);
	}

	@FXML
	private void handleXYButtonAction() {
		plotFunction(x -> x);
	}

	private void plotFunction(DoubleUnaryOperator mathFunction) {
		if (lineGraph.isVisible()) {
			mathsGraph.plotExplicitFunction(mathFunction);
		} else {
			areaMathsGraph.plotExplicitFunction(mathFunction);
		}
	}

	@FXML
	private void handleXYButton2Action() {
		plotFunction(x -> x - 3);
	}

	@FXML
	private void handleSquaredButtonAction() {
		plotFunction(x -> Math.pow(x, 2));
	}

	@FXML
	private void handleSquaredButton2Action() {
		plotFunction(x -> Math.pow(x, 2) + 2);
	}

	@FXML
	private void handleCubedButtonAction() {
		plotFunction(x -> Math.pow(x, 3));
	}

	@FXML
	private void handleCubedButton2Action() {
		plotFunction(x -> Math.pow(x - 3, 3) - 1);
	}

	@FXML
	private void handleSineButtonAction() {
		plotFunction(x -> 4 * Math.sin(x));
	}

	@FXML
	private void handleExponentButtonAction() {
		plotFunction(x -> Math.exp(x / 2));
	}

	@FXML
	private void handleLogButtonAction() {
		plotFunction(x -> Math.log(x / 2));
	}

	@FXML
	private void handleSemiCircleButtonAction() {
		plotFunction(x -> Math.sqrt(100 - x * x));
	}

	@FXML
	private void handleClearButtonAction() {
		clear();
	}

	private void clear() {
		if (lineGraph.isVisible()) {
			mathsGraph.clear();
		} else {
			areaMathsGraph.clear();
		}
	}
}
