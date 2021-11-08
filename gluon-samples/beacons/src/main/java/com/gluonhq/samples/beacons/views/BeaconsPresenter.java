/*
 * Copyright (c) 2016, 2020, Gluon
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *     * Redistributions of source code must retain the above copyright
 * notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 *     * Neither the name of Gluon, any associated website, nor the
 * names of its contributors may be used to endorse or promote products
 * derived from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 * ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 * WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED. IN NO EVENT SHALL GLUON BE LIABLE FOR ANY
 * DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 * (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES;
 * LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 * (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.gluonhq.samples.beacons.views;

import com.gluonhq.attach.ble.BleService;
import com.gluonhq.attach.ble.Configuration;
import com.gluonhq.attach.ble.ScanDetection;
import com.gluonhq.samples.beacons.Beacons;
import com.gluonhq.samples.beacons.settings.Settings;
import com.gluonhq.charm.glisten.afterburner.GluonPresenter;
import com.gluonhq.charm.glisten.control.AppBar;
import com.gluonhq.charm.glisten.control.CharmListCell;
import com.gluonhq.charm.glisten.control.CharmListView;
import com.gluonhq.charm.glisten.control.ListTile;
import com.gluonhq.charm.glisten.mvc.View;
import com.gluonhq.charm.glisten.visual.MaterialDesignIcon;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import org.jetbrains.annotations.NotNull;

import javax.inject.Inject;
import java.util.function.Consumer;

import static utils.FxUtils.*;

public class BeaconsPresenter extends GluonPresenter<Beacons> {

    @Inject
    private Settings settings;
	
    @FXML
    private View beacons;

    @FXML
    private CharmListView<ScanDetection, String> beaconsList;

    private static void setupScanBeaconIfPresent(ActionEvent e) {
        AppViewManager.SETTINGS_VIEW.switchView()
                .map(SettingsPresenter.class::cast)
                .ifPresent(SettingsPresenter::setupScanBeacon);
    }

    private static ObservableList<Button> buildScanBeaconButton() {
        final Button button = MaterialDesignIcon.SETTINGS.button(BeaconsPresenter::setupScanBeaconIfPresent);
        return FXCollections.singletonObservableList(button);
    }

    private static CharmListCell<ScanDetection> buildScanDetectionListCell(CharmListView<ScanDetection, String> charmListView) {

        class ScanDetectionListCell extends CharmListCell<ScanDetection> {

            private ScanDetection scan;
            private final ListTile tile;

            private ScanDetectionListCell() {
                tile = new ListTile();
                tile.setPrimaryGraphic(MaterialDesignIcon.BLUETOOTH.graphic());
                tile.setSecondaryGraphic(MaterialDesignIcon.CHEVRON_RIGHT.graphic());
                tile.setOnMouseClicked(e -> presenterSetBeaconIfPresent());
            }

            private void presenterSetBeaconIfPresent() {
                AppViewManager.BEACON_VIEW.switchView()
                                .filter(BeaconPresenter.class::isInstance)
                                .map(BeaconPresenter.class::cast)
                                .ifPresent(presenter -> presenter.setBeacon(scan));
            }

            @Override
            public void updateItem(ScanDetection item, boolean empty) {
                super.updateItem(item, empty);
                if (item != null && !empty) {
                    scan = item;
                    tile.setTextLine(0, "UUID: " + item.getUuid());
                    tile.setTextLine(1, "Major: " + item.getMajor());
                    tile.setTextLine(2, "Minor: " + item.getMinor());
                    setGraphic(tile);
                } else {
                    setGraphic(null);
                }
            }
        }
        return new ScanDetectionListCell();
    }

    public void initialize() {

        beaconsList.setPlaceholder(new Label("No beacons found"));
        beaconsList.setCellFactory(BeaconsPresenter::buildScanDetectionListCell);

        ObservableList<Button> actions = BleService.create()
                        .map(bleService -> createButtons(this::accept, bleService))
                        .orElseGet(BeaconsPresenter::buildScanBeaconButton);

        beacons.showingProperty().addListener(forNewValue(newValue -> scanBeacons(actions, newValue)));
    }

    private void scanBeacons(ObservableList<Button> actions, boolean newValue) {
        if (newValue) {
            AppBar appBar = getApp().getAppBar();
            appBar.setNavIcon(MaterialDesignIcon.CHEVRON_LEFT.button(e -> getApp().goHome()));
            appBar.setTitleText("Scan Beacons");
            appBar.getActionItems().setAll(actions);
        }
    }

    @NotNull
    private ObservableList<Button> createButtons(Consumer<ScanDetection> callback, BleService bleService) {
        final Button buttonScan = MaterialDesignIcon.BLUETOOTH_SEARCHING.button();
        final Button buttonStop = MaterialDesignIcon.STOP.button();
        buttonScan.setOnAction(e -> stopAndRestartScan(callback, bleService, buttonStop));
        buttonStop.setOnAction(e -> stopScanning(bleService, buttonStop));
        buttonScan.disableProperty().bind(buttonStop.disableProperty().not());
        buttonStop.setDisable(true);

        final Button button = MaterialDesignIcon.SETTINGS.button(BeaconsPresenter::setupScanBeaconIfPresent);

        return FXCollections.observableArrayList(button, buttonScan, buttonStop);
    }

    private void stopScanning(BleService bleService, Button buttonStop) {
        bleService.stopScanning();
        buttonStop.setDisable(true);
        beaconsList.itemsProperty().clear();
    }

    private void stopAndRestartScan(Consumer<ScanDetection> callback, BleService bleService, Button buttonStop) {
        bleService.stopScanning();
        Configuration conf = new Configuration(settings.getUuid());
        bleService.startScanning(conf, callback);
        buttonStop.setDisable(false);
    }

    private void accept(ScanDetection scanDetection) {
        // update beacon view
        AppViewManager.BEACON_VIEW.getPresenter()
                .filter(BeaconPresenter.class::isInstance)
                .map(BeaconPresenter.class::cast)
                .ifPresent(presenter -> setBeaconIfPresent(scanDetection, presenter));
        // update list
        final boolean noneMatchMajorAndMinorEqual = beaconsList.itemsProperty().stream()
                .noneMatch(detection -> majorAndMinorEqual(scanDetection, detection));

        if (scanDetection.getUuid().equalsIgnoreCase(settings.getUuid()) && noneMatchMajorAndMinorEqual) {
            Platform.runLater(() -> beaconsList.itemsProperty().add(scanDetection));
        }
    }

    private static boolean majorAndMinorEqual(ScanDetection scanDetection, ScanDetection currentBeacon) {
        return currentBeacon.getMajor() == scanDetection.getMajor() &&
                currentBeacon.getMinor() == scanDetection.getMinor();
    }

    private static void setBeaconIfPresent(ScanDetection scanDetection, BeaconPresenter presenter) {
        ScanDetection currentBeacon = presenter.getCurrentBeacon();
        if (currentBeacon != null && currentBeacon.getUuid().equals(scanDetection.getUuid()) &&
                majorAndMinorEqual(scanDetection, currentBeacon)) {
            presenter.setBeacon(scanDetection);
        }
    }
}
