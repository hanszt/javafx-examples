package fxutils

import javafx.application.Application

/**
 * Launches a JavaFX app. It allows for Kotlin idiomatic syntax.
 */
inline fun <reified T : Application> launchFXApp(vararg args: String) = Application.launch(T::class.java, *args)