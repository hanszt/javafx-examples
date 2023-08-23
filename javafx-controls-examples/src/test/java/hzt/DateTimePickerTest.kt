package hzt

import java.time.Clock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneOffset
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.Scene
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import kotlin.test.assertTrue

@ExtendWith(ApplicationExtension::class)
internal class DateTimePickerTest {

    private val dateTimePicker = SimpleObjectProperty<DateTimePicker>().apply {
        addListener { _, _, new -> println("Changed value to $new") }
    }

    private val clock = Clock.fixed(Instant.parse("2023-08-07T12:34:43Z"), ZoneOffset.UTC)

    @Start
    fun onStartup(stage: Stage) {
        dateTimePicker.value = DateTimePicker(clock)
        stage.scene = Scene(dateTimePicker.value)
        stage.show()
    }

    @Test
    fun `when date time picker initialized, then date time value initialized with now`() {
        val dateTimeValue = dateTimePicker.value.getDateTimeValue()
        val now = LocalDateTime.now(clock)
        assertTrue { dateTimeValue in now.minusSeconds(1)..now }
    }

}
