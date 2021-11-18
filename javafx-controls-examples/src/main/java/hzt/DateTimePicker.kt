package hzt

import fxutils.onNewValue
import javafx.beans.property.ObjectProperty
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.control.DatePicker
import javafx.scene.input.KeyCode
import javafx.scene.input.KeyEvent
import javafx.util.StringConverter
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.time.format.DateTimeFormatter

/**
 * A DateTimePicker with configurable datetime format where both date and time can be changed
 * via the text field and the date can additionally be changed via the JavaFX default date picker.
 */
class DateTimePicker : DatePicker() {

    private var formatter: DateTimeFormatter? = null
    private val dateTimeValue: ObjectProperty<LocalDateTime> = SimpleObjectProperty(LocalDateTime.now())
    private val format: ObjectProperty<String> = object : SimpleObjectProperty<String>() {
        override fun set(newValue: String) {
            super.set(newValue)
            formatter = DateTimeFormatter.ofPattern(newValue)
        }
    }

    fun getDateTimeValue(): LocalDateTime {
        return dateTimeValue.get()
    }

    private fun simulateEnterPressed(newValue: Boolean) {
        if (!newValue) {
            editor.fireEvent(
                KeyEvent(
                    editor, editor, KeyEvent.KEY_PRESSED,
                    null, null, KeyCode.ENTER, false, false, false, false
                )
            )
        }
    }

    private fun updateDateTimeValue(newValue: LocalDate?) {
        if (newValue == null) {
            dateTimeValue.set(null)
        } else {
            if (dateTimeValue.get() == null) {
                dateTimeValue.set(LocalDateTime.of(newValue, LocalTime.now()))
            } else {
                val time = dateTimeValue.get()!!.toLocalTime()
                dateTimeValue.set(LocalDateTime.of(newValue, time))
            }
        }
    }

    internal inner class InternalConverter : StringConverter<LocalDate?>() {
        override fun toString(`object`: LocalDate?): String = getDateTimeValue().format(formatter)

        override fun fromString(value: String): LocalDate? {
            dateTimeValue.set(LocalDateTime.parse(value, formatter))
            return dateTimeValue.get()!!.toLocalDate()
        }
    }

    companion object {
        const val DEFAULT_FORMAT = "yyyy-MM-dd HH:mm"
    }

    init {
        styleClass.add("datetime-picker")
        format.set(DEFAULT_FORMAT)
        converter = InternalConverter()

        // Synchronize changes to the underlying date value back to the dateTimeValue
        super.valueProperty().onNewValue(::updateDateTimeValue)

        // Synchronize changes to dateTimeValue back to the underlying date value
        dateTimeValue.onNewValue { value = it?.toLocalDate() }

        // Persist changes onblur
        editor.focusedProperty().onNewValue(::simulateEnterPressed)
    }
}
