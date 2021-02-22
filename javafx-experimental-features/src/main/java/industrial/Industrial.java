/*
 * Copyright (c) 2014, 2015, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
package industrial;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.beans.value.ObservableValue;
import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.geometry.Orientation;
import javafx.geometry.Rectangle2D;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.paint.Color;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.transform.Scale;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.util.Duration;

import static java.lang.System.*;

public class Industrial extends Application {

    public static final String FLOW_RATE = "Flow Rate:";
    Model model = new Model();
    Controller controller;
    Timeline controllerTimeline;

    Pump pump;
    Tank tank;

    Pipe pipeIn;
    Pipe pipeAIn;
    Pipe pipeAOut;
    Pipe pipeBIn;
    Pipe pipeBOut;

    Button chartFlowButton;
    Button chartFillButton;

    Group chartFillGroup;
    Group chartFlowGroup;
    Group helpGroup;

    static final int LAYOUT_SIZE_X = 1280;
    static final int LAYOUT_SIZE_Y = 720;

    static final int CHART_WIDTH = 800;
    static final int CHART_HEIGHT = 500;

    static final double HISTORY_POINTS_TO_KEEP = 60;
    static final int HISTORY_INTERVAL = 1;

    Scale rootTransform = new Scale(1.0, 1.0, 0.0, 0.0);

    @Override
    public void start(Stage primaryStage) {

        Label title = getLabel("Primary Holding Tank", 40, 430, 20);

        //****************************
        pipeIn = new Pipe(new Path(
                new MoveTo(0, 550),
                new LineTo(440, 550)));

        pump = new Pump(model);
        Group pumpGroup = getPumpGroup();

        Label nameP = getLabel("Feed Pump", 22, 120, 450);
        Label flowRateP = getLabel(75, 635);
        Slider ratePSlider = getRateSlider(10.0, 150);

        //****************************
        tank = new Tank(400, 580);
        Group tankGroup = tank.getGroup();
        tankGroup.setTranslateX(440);
        tankGroup.setTranslateY(100);

        //****************************
        configureBPipes();

        Group valveAGroup = getValveGroup(520);
        Label nameALabel = getLabel("Discharge A", 22, 1030, 500);
        Label flowRateALabel = getLabel(950, 635);
        Slider rateASlider = getRateSlider(5.0, 1025);

        //****************************
        configurePipes();

        Group valveBGroup = getValveGroup(320);
        Label nameB = getLabel("Discharge B", 22, 1030, 300);
        Label flowRateB = getLabel(950, 435);
        Slider rateBSlider = getRateBSlider();

        //****************************
        Slider lowWaterSlider = getWaterSlider(0, 90, 390, 150);
        Slider highWaterSlider = getWaterSlider(10, 100, 875, 95);

        //****************************

        Rectangle chartFlowBG = buildChartFlowBackGround();

        final NumberAxis xAxisFlow = new NumberAxis();
        final NumberAxis yAxisFlow = new NumberAxis();

        yAxisFlow.setLabel("Flow Rate");
        yAxisFlow.setAutoRanging(false);
        yAxisFlow.setLowerBound(0);
        yAxisFlow.setUpperBound(10);

        xAxisFlow.setLabel("Time (sec)");
        xAxisFlow.setAutoRanging(false);
        xAxisFlow.setLowerBound(0);
        xAxisFlow.setUpperBound(HISTORY_POINTS_TO_KEEP * HISTORY_INTERVAL);

        chartFlowGroup = new Group(chartFlowBG, getFlowLineChart(xAxisFlow, yAxisFlow));

        chartFlowGroup.setVisible(false);
        chartFlowGroup.setTranslateX(200);
        chartFlowGroup.setTranslateY(80);

        chartFlowButton = new Button("Flow History");
        chartFlowButton.setFont(new Font(18));
        chartFlowButton.setTranslateX(30.0);
        chartFlowButton.setTranslateY(80.0);
        chartFlowButton.setOnAction(this::charFlowButtonAction);

        //****************************

        Rectangle chartFillBG = buildChartFlowBackGround();

        final NumberAxis xAxisFill = new NumberAxis();
        final NumberAxis yAxisFill = new NumberAxis();

        yAxisFill.setLabel("Fill Percent");
        yAxisFill.setAutoRanging(false);
        yAxisFill.setLowerBound(0);
        yAxisFill.setUpperBound(100);

        xAxisFill.setLabel("Time (sec)");
        xAxisFill.setAutoRanging(false);
        xAxisFill.setLowerBound(0);
        xAxisFill.setUpperBound(HISTORY_POINTS_TO_KEEP * HISTORY_INTERVAL);

        //noinspection unchecked,rawtypes
        model.getFillPercentHistory().getData()
                .addListener((ListChangeListener.Change c) -> onFillPercentHistoryChange(xAxisFlow, xAxisFill));

        chartFillGroup = new Group(chartFillBG, getFillLineChart(xAxisFill, yAxisFill));

        chartFillGroup.setVisible(false);
        chartFillGroup.setTranslateX(200);
        chartFillGroup.setTranslateY(80);

        chartFillButton = new Button("Fill History");
        chartFillButton.setFont(new Font(18));
        chartFillButton.setTranslateX(30.0);
        chartFillButton.setTranslateY(120.0);
        chartFillButton.setOnAction(this::chartFillButtonAction);

        //****************************

        helpGroup = Help.createHelp(CHART_WIDTH, CHART_HEIGHT);
        helpGroup.setVisible(false);
        helpGroup.setTranslateX(200);
        helpGroup.setTranslateY(80);

        Button helpButton = new Button("Help");
        helpButton.setFont(new Font(18));
        helpButton.setTranslateX(LAYOUT_SIZE_X - 100.);
        helpButton.setTranslateY(80.0);
        helpButton.setOnAction((ActionEvent event) -> {
            boolean visible = !helpGroup.isVisible();
            helpGroup.setVisible(visible);
        });

        //****************************

        Rectangle backdrop = new Rectangle(0, 0, LAYOUT_SIZE_X, LAYOUT_SIZE_Y);
        backdrop.setFill(Color.OLDLACE);

        //****************************
        Group root = new Group(
                backdrop,
                pipeIn.getGroup(),
                pumpGroup,
                nameP, flowRateP, ratePSlider,
                pipeAIn.getGroup(),
                pipeBIn.getGroup(),
                tankGroup,
                pipeAOut.getGroup(),
                valveAGroup,
                nameALabel, flowRateALabel, rateASlider,
                pipeBOut.getGroup(),
                valveBGroup,
                nameB, flowRateB, rateBSlider,
                lowWaterSlider, highWaterSlider,
                chartFlowButton, chartFlowGroup,
                chartFillButton, chartFillGroup,
                helpButton, helpGroup,
                title
        );

        root.getTransforms().add(rootTransform);

        Rectangle2D primaryScreenBounds = Screen.getPrimary().getVisualBounds();
        out.println("Screen size is " + primaryScreenBounds);
        Scene scene = new Scene(root, LAYOUT_SIZE_X, LAYOUT_SIZE_Y);
        controller = new Controller(model /*, tracker */);

        ratePSlider.valueProperty().bindBidirectional(model.getValveFlowRateProperty(Model.PUMP));
        rateASlider.valueProperty().bindBidirectional(model.getValveFlowRateProperty(Model.VALVE_A));
        rateBSlider.valueProperty().bindBidirectional(model.getValveFlowRateProperty(Model.VALVE_B));

        tank.getFillPercentageProperty().bind(model.getTankFillPercentProperty());
        tank.getLowWaterPercentageProperty().bind(model.getTankLowWaterPercentProperty());
        tank.getHighWaterPercentageProperty().bind(model.getTankHighWaterPercentProperty());

        model.getValveFlowRateProperty(Model.PUMP).addListener(this::flowRateChangeAction);

        model.getValveFlowRateProperty(Model.VALVE_A).addListener((ObservableValue<? extends Number> observable, Number oldValue, Number newValue) -> {
            double lowThreshold = 0.5;
            double oldv = oldValue.doubleValue();
            double newv = newValue.doubleValue();
            if (oldv < lowThreshold && newv > lowThreshold) {
                pipeAOut.setFilled(true);
                pipeAIn.setFlow(true);
            } else if (newv <= lowThreshold && oldv > lowThreshold) {
                pipeAOut.setFilled(false);
                pipeAIn.setFlow(false);
            }
        });

        model.getValveFlowRateProperty(Model.VALVE_B).addListener(this::valveFlowRatePropertyChnageAction);

        lowWaterSlider.valueProperty().bindBidirectional(model.getTankLowWaterPercentProperty());
        highWaterSlider.valueProperty().bindBidirectional(model.getTankHighWaterPercentProperty());

        scene.widthProperty().addListener(this::adaptedScaleXOnWidthChanged);
        scene.heightProperty().addListener(this::adaptedScaleYOnHeightChanged);

        primaryStage.setTitle("Industrial Demo");
        primaryStage.setScene(scene);
        primaryStage.show();

        //this drives the Controller polling.
        final KeyFrame kf = new KeyFrame(Duration.millis(50), controller);
        controllerTimeline = new Timeline(kf);
        controllerTimeline.setCycleCount(Animation.INDEFINITE);
        controllerTimeline.play();
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private LineChart getFillLineChart(NumberAxis xAxisFill, NumberAxis yAxisFill) {
        LineChart lineChartFill = new LineChart(xAxisFill, yAxisFill);
        lineChartFill.setTitle("Fill History");
        lineChartFill.setPrefSize(CHART_WIDTH, CHART_HEIGHT);
        lineChartFill.setAnimated(false);
        lineChartFill.getData().add(model.getFillPercentHistory());
        lineChartFill.setCreateSymbols(false);
        return lineChartFill;
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    private LineChart getFlowLineChart(NumberAxis xAxisFlow, NumberAxis yAxisFlow) {
        LineChart lineChartFlow = new LineChart(xAxisFlow, yAxisFlow);
        lineChartFlow.setTitle("Flow History");
        lineChartFlow.setPrefSize(CHART_WIDTH, CHART_HEIGHT);
        lineChartFlow.setAnimated(false);
        lineChartFlow.getData().add(model.getFlowRateHistory(Model.PUMP));
        lineChartFlow.getData().add(model.getFlowRateHistory(Model.VALVE_A));
        lineChartFlow.getData().add(model.getFlowRateHistory(Model.VALVE_B));
        lineChartFlow.setCreateSymbols(false);
        return lineChartFlow;
    }

    @SuppressWarnings("rawtypes")
    private void onFillPercentHistoryChange(NumberAxis xAxisFlow, NumberAxis xAxisFill) {
        XYChart.Data get = (XYChart.Data) model.getFillPercentHistory().getData().get(0);
        if (get != null) {

            long low = (long) get.getXValue();
            double high = low + HISTORY_POINTS_TO_KEEP * HISTORY_INTERVAL;
            xAxisFill.setLowerBound(low);
            xAxisFill.setUpperBound(high);

            // a bit of a cheat, but these are in lock step
            // and it saves us a listener
            xAxisFlow.setLowerBound(low);
            xAxisFlow.setUpperBound(high);
        }
    }

    private Label getLabel(String s, int i, int i2, int i3) {
        Label nameP = new Label(s);
        nameP.setFont(new Font(i));
        nameP.setTranslateX(i2);
        nameP.setTranslateY(i3);
        return nameP;
    }

    private Group getPumpGroup() {
        Group pumpGroup = pump.getGroup();
        pumpGroup.setTranslateX(110);
        pumpGroup.setTranslateY(440);
        pumpGroup.setScaleX(.5);
        pumpGroup.setScaleY(.5);
        return pumpGroup;
    }

    private Group getValveGroup(int translateY) {
        Valve valve = new Valve();
        Group valveAGroup = valve.getGroup();
        valveAGroup.setScaleX(.5);
        valveAGroup.setScaleY(.5);
        valveAGroup.setTranslateX(1010);
        valveAGroup.setTranslateY(translateY);
        return valveAGroup;
    }

    private void configureBPipes() {
        pipeAIn = new Pipe(new Path(
                new MoveTo(730, 590),
                new LineTo(1040, 590)));

        pipeAOut = new Pipe(new Path(
                new MoveTo(1135, 590),
                new LineTo(LAYOUT_SIZE_X, 590)));
    }

    private void configurePipes() {
        pipeBIn = new Pipe(new Path(
                new MoveTo(730, 390),
                new LineTo(1040, 390)));

        pipeBOut = new Pipe(new Path(
                new MoveTo(1135, 390),
                new LineTo(LAYOUT_SIZE_X, 390)));
    }

    private Slider getRateBSlider() {
        Slider rateBSlider = new Slider(0.0, 5.0, 3);
        rateBSlider.setShowTickLabels(true);
        rateBSlider.setShowTickMarks(true);
        rateBSlider.setMajorTickUnit(1.0);
        rateBSlider.setBlockIncrement(1.0f);
        rateBSlider.setMinorTickCount(4);
        rateBSlider.setSnapToTicks(true);
        rateBSlider.setTranslateX(1025);
        rateBSlider.setTranslateY(430);
        return rateBSlider;
    }

    private Rectangle buildChartFlowBackGround() {
        Rectangle rectangle = new Rectangle();
        rectangle.setX(0);
        rectangle.setY(0);
        rectangle.setWidth(CHART_WIDTH);
        rectangle.setHeight(CHART_HEIGHT);
        rectangle.setFill(Color.ANTIQUEWHITE);
        rectangle.setOpacity(0.80);
        return rectangle;
    }

    private Label getLabel(int translateX, int translateY) {
        Label label = new Label(FLOW_RATE);
        label.setTranslateX(translateX);
        label.setTranslateY(translateY);
        return label;
    }

    private Slider getRateSlider(double upperBound, int translateX) {
        Slider rateSlider = new Slider(0.0, upperBound, 3);
        rateSlider.setShowTickLabels(true);
        rateSlider.setShowTickMarks(true);
        rateSlider.setMajorTickUnit(1.0);
        rateSlider.setBlockIncrement(1.0f);
        rateSlider.setSnapToTicks(true);
        rateSlider.setTranslateX(translateX);
        rateSlider.setTranslateY(630);
        return rateSlider;
    }

    private Slider getWaterSlider(int lowerBound, int upperBound, int translateX, int translateY) {
        Slider lowWaterSlider = new Slider(lowerBound, upperBound, 10);
        lowWaterSlider.setShowTickLabels(true);
        lowWaterSlider.setShowTickMarks(true);
        lowWaterSlider.setMajorTickUnit(10.0);
        lowWaterSlider.setBlockIncrement(1.0f);
        lowWaterSlider.setMinorTickCount(0);
        lowWaterSlider.setSnapToTicks(false);
        lowWaterSlider.setOrientation(Orientation.VERTICAL);
        lowWaterSlider.setTranslateX(translateX);
        lowWaterSlider.setTranslateY(translateY);
        lowWaterSlider.setPrefHeight(535);
        return lowWaterSlider;
    }

    private void flowRateChangeAction(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double lowThreshold = 0.5;
        double newv = newValue.doubleValue();
        boolean active = pump.getPumpActive();
        if (newv < lowThreshold && active) {
            pump.setPumpActive(false);
            pump.setPumpRatePercent(0.0);
            pipeIn.setFlow(false);
        } else {
            double ratePercent = newv / model.getValveFlowRateProperty(Model.PUMP).getMax();
            if (!active) {
                pump.setPumpActive(true);
                pipeIn.setFlow(true);
            }
            pump.setPumpRatePercent(ratePercent);
        }
    }

    private void charFlowButtonAction(ActionEvent event) {
        boolean visible = !chartFlowGroup.isVisible();
        chartFlowGroup.setVisible(visible);
        if (visible) {
            chartFillGroup.setVisible(false);
        }
    }

    private void chartFillButtonAction(ActionEvent event) {
        boolean visible = !chartFillGroup.isVisible();
        chartFillGroup.setVisible(visible);
        if (visible) {
            chartFlowGroup.setVisible(false);
        }
    }

    private void adaptedScaleYOnHeightChanged(ObservableValue<? extends Number> o, Number curVal, Number newVal) {
        double scale = (double) newVal / LAYOUT_SIZE_Y;
        rootTransform.setY(scale);
    }

    private void adaptedScaleXOnWidthChanged(ObservableValue<? extends Number> o, Number curVal, Number newVal) {
        double scale = (double) newVal / LAYOUT_SIZE_X;
        rootTransform.setX(scale);
    }

    private void valveFlowRatePropertyChnageAction(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
        double lowThreshold = 0.5;
        double oldv = oldValue.doubleValue();
        double newv = newValue.doubleValue();
        if (oldv < lowThreshold && newv > lowThreshold) {
            pipeBOut.setFilled(true);
            pipeBIn.setFlow(true);
        } else if (newv <= lowThreshold && oldv > lowThreshold) {
            pipeBOut.setFilled(false);
            pipeBIn.setFlow(false);
        }
    }
}
