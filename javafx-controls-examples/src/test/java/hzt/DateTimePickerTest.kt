package hzt

import javafx.scene.Scene
import javafx.stage.Stage
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.testfx.framework.junit5.ApplicationExtension
import org.testfx.framework.junit5.Start
import java.time.LocalDateTime
import kotlin.test.assertTrue

@ExtendWith(ApplicationExtension::class)
internal class DateTimePickerTest {

    private lateinit var dateTimePicker: DateTimePicker

    @Start
    fun onStartup(stage: Stage) {
        dateTimePicker = DateTimePicker()
        stage.scene = Scene(dateTimePicker)
        stage.show()
    }

    @Test
    fun `when date time picker initialized, then date time value initialized with now`() {
        val dateTimeValue = dateTimePicker.getDateTimeValue()
        val now = LocalDateTime.now()
        assertTrue { dateTimeValue in now.minusSeconds(1)..now }
    }

}
