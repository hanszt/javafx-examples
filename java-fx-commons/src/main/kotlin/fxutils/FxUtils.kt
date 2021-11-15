package fxutils

import javafx.beans.value.ObservableValue
import javafx.scene.control.ColorPicker
import javafx.scene.control.Slider
import javafx.scene.control.ToggleButton
import javafx.scene.image.Image
import javafx.scene.paint.Color
import javafx.stage.Stage
import java.io.InputStream
import java.util.*
import java.util.function.Consumer
import java.util.function.Function
import java.util.stream.Collector
import java.util.stream.Collectors
import java.util.stream.Stream

fun <T, U, R> Stream<out T>.collectAndThen(downstream: Collector<in T, *, U>, finisher: Function<U, out R>):
        R = collect(Collectors.collectingAndThen(downstream, finisher))

fun <T> ObservableValue<T>.onNewValue(consumer: Consumer<T>) = addListener { _, _, new -> consumer.accept(new) }

fun Slider.onChange(consumer: Consumer<Number>) = valueProperty().onNewValue(consumer::accept)

fun ToggleButton.onPressed(consumer: Consumer<Boolean>) = selectedProperty().onNewValue(consumer::accept)

fun ColorPicker.onChange(consumer: Consumer<Color>) = valueProperty().onNewValue(consumer::accept)

fun Stage.inverseFullScreen() {
    isFullScreen = isFullScreen.not()
}

fun InputStream?.ifImagePresent(imageConsumer: Consumer<Image>) = Optional.ofNullable(this)
    .map(::Image)
    .ifPresent(imageConsumer)

fun String.firstCharUpperCase() = lowercase().replaceFirstChar(Char::uppercaseChar)
